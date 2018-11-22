package com.fuel4media.carrythistoo.utils

import android.content.Context

import java.io.File

class FileCache private constructor(context: Context) {
    private val rootDir: File

    init {

        // Find the dir at SDCARD to save cached images
        if (android.os.Environment.getExternalStorageState() == android.os.Environment.MEDIA_MOUNTED) {

            rootDir = File(
                    android.os.Environment.getExternalStorageDirectory(),
                    FOLDER_NAME)
        } else {
            // if checking on simulator the create cache dir in your application
            // context
            rootDir = context.cacheDir
        }

        if (!rootDir.exists()) {
            // create cache dir in your application context
            rootDir.mkdirs()
        }
    }

    fun getFile(url: String, isHres: Boolean): File {
        //Identify images by hashcode or encode by URLEncoder.encode.
        var filename = getFileName(url)
        if (!isHres) {
            filename = "lres_" + System.currentTimeMillis() + filename
        }
        return File(rootDir, filename)
    }

    fun getFileName(url: String): String {
        var name = url.substring(url.lastIndexOf('/') + 1)
        val index = name.indexOf('?')
        if (index >= 0) {
            name = name.substring(0, index)
        }
        return name
    }


    fun createFile(filename: String): File {
        return File(rootDir, filename)
    }

    fun clear() {
        // list all files inside cache directory
        val files = rootDir.listFiles() ?: return
//delete all cache directory files
        for (f in files)
            f.delete()
    }

    /**
     * Delete file from cache
     * @param filename
     * @return whether file deleted or not
     */
    fun delete(filename: String): Boolean {
        return File(rootDir, getFileName(filename)).delete()
    }

    companion object {

        private val FOLDER_NAME = ".Wedoshoes"
        private var fileCache: FileCache? = null
        fun getInstance(context: Context): FileCache {
            if (fileCache == null)
                fileCache = FileCache(context)
            return fileCache as FileCache

        }
    }
}