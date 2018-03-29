package org.lm

import com.intellij.openapi.application.runWriteAction
import com.intellij.openapi.fileEditor.FileDocumentManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.text.StringUtil.convertLineSeparators
import com.intellij.openapi.vfs.VfsUtil
import com.intellij.openapi.vfs.VirtualFile
import org.intellij.lang.annotations.Language
import kotlin.text.Charsets.UTF_8

fun fileTreeFromText(@Language("LM") text: String): FileTree {
    val fileSeparator = """^\s* #- (\S+)\s*$""".toRegex(RegexOption.MULTILINE)
    val fileNames = fileSeparator.findAll(text).map { it.groupValues[1] }.toList()
    val fileTexts = fileSeparator.split(text).filter(String::isNotBlank).map { it.trimIndent() }

    check(fileNames.size == fileTexts.size) {
        "Have you placed `#- filename.lm` markers?"
    }

    fun fill(dir: Entry.Directory, path: List<String>, contents: String) {
        val name = path.first()
        if (path.size == 1) {
            dir.children[name] = Entry.File(contents)
        } else {
            val childDir = dir.children.getOrPut(name, { Entry.Directory(mutableMapOf()) }) as Entry.Directory
            fill(childDir, path.drop(1), contents)
        }
    }

    return FileTree(Entry.Directory(mutableMapOf()).apply {
        for ((path, contents) in fileNames.map { it.split("/") }.zip(fileTexts)) {
            fill(this, path, contents)
        }
    })
}

class FileTree(private val rootDirectory: Entry.Directory) {
    fun create(project: Project, directory: VirtualFile): TestProject {
        val filesWithCaret: MutableList<String> = mutableListOf()

        fun go(dir: Entry.Directory, root: VirtualFile, parentComponents: List<String> = emptyList()) {
            for ((name, entry) in dir.children) {
                val components = parentComponents + name
                when (entry) {
                    is Entry.File -> {
                        val vFile = root.findChild(name) ?: root.createChildData(root, name)
                        VfsUtil.saveText(vFile, entry.text)
                        if (hasCaretMarker(entry.text) || "#^" in entry.text) {
                            filesWithCaret += components.joinToString(separator = "/")
                        }
                    }
                    is Entry.Directory -> {
                        go(entry, root.createChildDirectory(root, name), components)
                    }
                }
            }
        }

        runWriteAction {
            go(rootDirectory, directory)
            fullyRefreshDirectory(directory)
        }

        return TestProject(project, directory, filesWithCaret)
    }

    fun assertEquals(baseDir: VirtualFile) {
        fun go(expected: Entry.Directory, actual: VirtualFile) {
            val actualChildren = actual.children.associateBy { it.name }
            check(expected.children.keys == actualChildren.keys) {
                "Mismatch in directory ${actual.path}\n" +
                        "Expected: ${expected.children.keys}\n" +
                        "Actual  : ${actualChildren.keys}"
            }

            for ((name, entry) in expected.children) {
                val a = actualChildren[name]!!
                when (entry) {
                    is Entry.File -> {
                        check(!a.isDirectory)
                        val actualText = convertLineSeparators(String(a.contentsToByteArray(), UTF_8))
                        check(entry.text.trimEnd() == actualText.trimEnd()) {
                            "Expected:\n${entry.text}\nGot:\n$actualText"
                        }
                    }
                    is Entry.Directory -> go(entry, a)
                }
            }
        }

        FileDocumentManager.getInstance().saveAllDocuments()
        go(rootDirectory, baseDir)
    }
}

class TestProject(
        private val project: Project,
        val root: VirtualFile,
        private val filesWithCaret: List<String>
) {

    val fileWithCaret: String
        get() = filesWithCaret.singleOrNull()!!
}

sealed class Entry {
    class File(val text: String) : Entry()
    class Directory(val children: MutableMap<String, Entry>) : Entry()
}

fun hasCaretMarker(text: String): Boolean = text.contains("<caret>")
