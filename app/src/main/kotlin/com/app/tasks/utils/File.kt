package com.app.tasks.utils

import java.io.File
import java.io.FileInputStream
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

object FileUtils {
    fun addFileToZip(file: File, fileName: String, zipOut: ZipOutputStream) {
        if (file.isHidden) {
            return
        }
        if (file.isDirectory) {
            val children = file.listFiles()
            if (children != null) {
                for (childFile in children) {
                    addFileToZip(childFile, "$fileName/${childFile.name}", zipOut)
                }
            }
        } else {
            FileInputStream(file).use { fis ->
                val zipEntry = ZipEntry(fileName)
                zipOut.putNextEntry(zipEntry)
                fis.copyTo(zipOut)
            }
        }
    }
}