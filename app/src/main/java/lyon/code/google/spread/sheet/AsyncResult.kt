package lyon.code.google.spread.sheet

import org.json.JSONObject

interface AsyncResult {
    fun onResult(`object`: JSONObject?)
}