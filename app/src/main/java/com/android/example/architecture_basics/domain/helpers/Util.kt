package com.android.example.architecture_basics.domain.helpers

import android.content.Context
import android.net.Uri
import android.provider.Settings
import android.util.Log
import androidx.core.content.FileProvider
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.BufferedWriter
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.FileWriter
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject


class Util @Inject constructor(@ApplicationContext private val context: Context) {

    fun backupDatabase(): Uri {

        // Se obtiene el archivo de base de datos
        val dbFile = context.getDatabasePath(GlobalConst.DATABASE_NAME)

        /*
        * Se copia el archivo a otro temporal en la carpeta files/databases_temp.
        * Se copian tambien los archivos "example.db-shm" y "example.db-wal" porque son necesarios
        * para analizar la base de datos.
        * */
        val parent = File(context.filesDir, GlobalConst.DB_BACKUP_DIRECTORY)
        val bckpFile = File(parent, GlobalConst.DB_BACKUP_FILENAME)

        if (!parent.exists()) {
            parent.mkdirs()
        }

        copiarData(dbFile.absolutePath, bckpFile.absolutePath)
        copiarData(dbFile.absolutePath + "-shm", bckpFile.absolutePath + "-shm")
        copiarData(dbFile.absolutePath + "-wal", bckpFile.absolutePath + "-wal")

        val archivosAComprimir = arrayOf(
            bckpFile.absolutePath,
            bckpFile.absolutePath + "-shm",
            bckpFile.absolutePath + "-wal"
        )

        /*
        * Creo el zip y se lo paso al zip
        * */
        val zipFile = File(parent, GlobalConst.DB_BACKUP_FILENAME_ZIP)
        ZipManager().zip(archivosAComprimir, zipFile.absolutePath)

        // Obtengo la URI del archivo zipeado y lo retorno.
        return getUri(zipFile)
    }

    private fun copiarData(fromPath: String, toPath: String) {
        val inStream = File(fromPath).inputStream()
        val outStream = FileOutputStream(toPath)
        inStream.use { input ->
            outStream.use { output ->
                input.copyTo(output)
            }
        }
    }

    private fun getUri(file: File): Uri {
        // Using FileProvider for API >= 24
        return FileProvider.getUriForFile(
            context,
            context.packageName,
            file
        )
    }

    /*
    * Funcion que elimina la carpeta "database_temp".
    *
    private fun deleteTempFiles(): Boolean {
        val tempDirectory = File(context.filesDir, GlobalConst.DB_BACKUP_DIRECTORY)
        return tempDirectory.delete()
    }*/


    // Guardar Logs

    fun guardaLog(mensaje: String): Int {
        val fecha = SimpleDateFormat("dd/MM/yy HH:mm:ss", Locale.getDefault()).format(Date())

        try {
            //Obtenemos el directorio donde se almacenan los Logs. Si no existe lo creamos.
            val dir = File(context.filesDir, GlobalConst.LOG_BACKUP_DIRECTORY)
            if (!dir.exists()) dir.mkdirs()

            //Obtenemos el archivo de Logs. Si no existe lo creamos.
            val f = File((dir.absolutePath + File.separator + GlobalConst.LOG_BACKUP_FILENAME)
                        + "_" + obtenerImei() + "_" + GlobalConst.ARCHIVO_LOGERROR)
            if (!f.exists()) f.createNewFile()

            val fwriter = FileWriter(f, true)
            val writer = BufferedWriter(fwriter)
            writer.append("$fecha | $mensaje \n")
            writer.flush()
            writer.close()

        } catch (e: FileNotFoundException) {
            e.printStackTrace()
            Log.i(
                GlobalConst.DEBUG_TAG, "******* Archivo no encontrado. Verificar permiso "
                        + "WRITE_EXTERNAL_STORAGE en Manifest.")
            return -3
        }
        return 1
    }


    private fun obtenerImei(): String {
        var imei = ""

        try {
            imei = Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)

        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        return imei
    }
}
