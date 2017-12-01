package org.lm

import com.intellij.openapi.fileTypes.FileTypeConsumer
import com.intellij.openapi.fileTypes.FileTypeFactory

class LmFileTypeFactory : FileTypeFactory() {
    override fun createFileTypes(fileTypeConsumer: FileTypeConsumer) =
            fileTypeConsumer.consume(LmFileType, LmFileType.defaultExtension)
}
