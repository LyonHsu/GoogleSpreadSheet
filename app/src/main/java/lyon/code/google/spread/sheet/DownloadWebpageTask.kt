package lyon.code.google.spread.sheet

import android.os.AsyncTask
import org.json.JSONException
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL


class DownloadWebpageTask() : AsyncTask<String, Void, String>(){

    lateinit var callback:AsyncResult
    override fun doInBackground(vararg urls: String?): String {
        return try {
            urls.get(0)?.let { downloadUrl(it).toString() }.toString()
        } catch (e: IOException) {
            "Unable to download the requested page."
        }
    }

    override fun onPostExecute(result: String?) {
        val start = result!!.indexOf("{", result.indexOf("{") + 1)
        val end = result.lastIndexOf("}")

        val jsonResponse = result.substring(start, end)

        try {
            val table = JSONObject(jsonResponse)
            callback.onResult(table)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }

    @Throws(IOException::class)
    private fun downloadUrl(urlString: String): String? {
        var `is`: InputStream? = null
        return try {
            val url = URL(urlString)
            val conn: HttpURLConnection = url.openConnection() as HttpURLConnection
            conn.setReadTimeout(10000 /* milliseconds */)
            conn.setConnectTimeout(15000 /* milliseconds */)
            conn.setRequestMethod("GET")
            conn.setDoInput(true)
            // Starts the query            conn.connect();
            val responseCode: Int = conn.getResponseCode()
            `is` = conn.getInputStream()
            convertStreamToString(`is`)
        } finally {
            if (`is` != null) {
                `is`.close()
            }
        }
    }

    private fun convertStreamToString(`is`: InputStream): String? {
        val reader = BufferedReader(InputStreamReader(`is`))
        val sb = StringBuilder()
        var line: String? = null
        try {
            while (reader.readLine().also({ line = it }) != null) {
                sb.append(line + "\n")
            }
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            try {
                `is`.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        return sb.toString()
    }

}