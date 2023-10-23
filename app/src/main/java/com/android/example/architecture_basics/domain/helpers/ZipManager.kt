package com.android.example.architecture_basics.domain.helpers

import java.io.BufferedInputStream
import java.io.BufferedOutputStream
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

class ZipManager {
    private val bufferSize = 6 * 1024

    @Throws(IOException::class)
    fun zip(files: Array<String>, zipFile: String?) {
        var origin: BufferedInputStream
        ZipOutputStream(BufferedOutputStream(FileOutputStream(zipFile))).use { out ->
            val data = ByteArray(bufferSize)
            for (file in files) {
                val fi = FileInputStream(file)
                origin = BufferedInputStream(fi, bufferSize)
                try {
                    val entry = ZipEntry(file.substring(file.lastIndexOf("/") + 1))
                    out.putNextEntry(entry)
                    var count: Int
                    while (origin.read(data, 0, bufferSize).also { count = it } != -1) {
                        out.write(data, 0, count)
                    }
                } finally {
                    origin.close()
                }
            }
        }
    }
}