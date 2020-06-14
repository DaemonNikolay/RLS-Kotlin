package ru.nikulux.rlstestlibrary

import android.util.Log
import com.google.gson.GsonBuilder
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets

class RLS {
    private val _url = "https://localhost:5001/api/log/new"


    public fun sendLog(content: String, logType: LogType, appToken: String) {
        val urlSendSmsToServer = URL(_url)

        val connection = urlSendSmsToServer.openConnection() as HttpURLConnection
        connection.requestMethod = "POST"
        connection.doOutput = true

        val log = RLSLog(content, logType, appToken)

        val gson = GsonBuilder().setPrettyPrinting().create()
        val json = gson.toJson(log)

        Log.d("Test json", json)

        val smsByteArray: ByteArray = json.toByteArray(StandardCharsets.UTF_8)

        connection.setRequestProperty("charset", "utf-8")
        connection.setRequestProperty("Content-length", smsByteArray.size.toString())
        connection.setRequestProperty("Content-Type", "application/json")
        connection.doOutput = true

        val os = connection.outputStream
        val input = json.toByteArray(Charset.defaultCharset())
        os.write(input, 0, input.size)

        try {
            InputStreamReader(connection.inputStream, "utf-8")
        } catch (e: Exception) {
            Log.d("EXCEPTION", e.message.toString())
        }

        connection.disconnect()
    }
}

class RLSLog(val content: String, val type: LogType, val appToken: String) {
}

enum class LogType(s: String) {
    info("info"),
    warning("warning"),
    error("error"),
}
