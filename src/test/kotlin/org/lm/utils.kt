package org.lm

import com.intellij.openapi.vfs.VfsUtil
import com.intellij.openapi.vfs.VirtualFile

fun fullyRefreshDirectory(directory: VirtualFile) {
    VfsUtil.markDirtyAndRefresh(false, true, true, directory)
}
