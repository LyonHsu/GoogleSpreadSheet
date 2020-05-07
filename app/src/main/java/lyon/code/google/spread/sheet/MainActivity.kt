package lyon.code.google.spread.sheet


import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONException
import org.json.JSONObject

/**
 * Ref.:https://farmercd.blogspot.com/2017/03/android-google-spreadsheet-google.html
 */
class MainActivity : AppCompatActivity() {
    val TAG = "MainActivity"
    var btnDownload: Button? = null
    var NameView: TextView? = null
    var CountryView:TextView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnDownload = findViewById(R.id.btnDownload) as Button
        NameView = findViewById(R.id.textView) as TextView
        CountryView = findViewById(R.id.textView2) as TextView

        val connMgr = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connMgr.activeNetworkInfo

        if (networkInfo != null && networkInfo.isConnected) {
            btnDownload!!.isEnabled = true
        } else {
            btnDownload!!.isEnabled = false
        }
    }

    fun buttonClickHandler(view: View?) {
        NameView!!.text =""
        CountryView!!.text =""
        val ID ="1mT-u5IGrQdoTuNMPvFwOQlIxXEPV-_pWY-B0xsaBtd0"
        val volleyTool=VolleyTool(this)
        volleyTool.CallPath("https://spreadsheets.google.com/tq?key="+ID)
        volleyTool.setOnCallPathBackListener(object :VolleyTool.OnCallPathBack{
            override fun Next(result: String?) {
                try {
                    val start = result!!.indexOf("({")+1
                    val end = result.lastIndexOf("})")+1
                    val jsonResponse = result.substring(start, end)
                    Log.e(TAG,"jsonResponse:"+jsonResponse)
                    val jsonObject = JSONObject(jsonResponse)
                    processJson(jsonObject)
                }catch (e:JSONException){
                    Log.e(TAG,"JSONException:"+e)
                    NameView!!.text = e.toString()
                    CountryView!!.text = result
                }
            }
        })
    }

    private fun processJson(jsonObject: JSONObject) {
        var Name = ""
        var Country = ""
        try {
            /**
            {
                "version": "0.6",
                "reqId": "0",
                "status": "ok",
                "sig": "572817344",
                "table": {
                    "cols": [],
                    "rows": ["c":[{"v":"20190613"},{"v":"Momo 電磁爐退款"}]],
                    "parsedNumHeaders": 1
                }
            }
             */
            val version = jsonObject.optString("version")
            val reqId = jsonObject.optString("reqId")
            val status = jsonObject.optString("status")
            val sig = jsonObject.optString("sig")
            val table = jsonObject.optJSONObject("table")
            val tableLen = table.length()
            val cols = table.getJSONArray("cols")
            val colsLen = cols.length()
            val rows = table.getJSONArray("rows")
            val rowsLen = rows.length()
            val parsedNumHeaders = table.optString("parsedNumHeaders")
            Log.d(TAG,"version:"+version+", reqId:"+reqId+", status:"+status+", sig:"+sig+" ")
            Log.d(TAG,"tableLen:"+tableLen)
            Log.d(TAG,"colsLen:"+colsLen)
            Log.d(TAG,"rowsLen:"+rowsLen)
            Log.d(TAG,"parsedNumHeaders:"+parsedNumHeaders)
            Log.d(TAG,"cols:"+cols)
            for(i in 0..colsLen){
                val json =cols.optJSONObject(i)
                if(json!=null) {
                    val id =  json.getString("id")
                    val v = "["+id+"]"+json.getString("label") + ",\t"
                    Name += v
                }
            }
            val v = "\\n \n"
            Name += v
            for (r in 0 until rowsLen) {
                val row = rows.getJSONObject(r)
                Log.d(TAG,"row["+r+"]:"+row)
                val columns = row.getJSONArray("c")
                val columnsLen = columns.length()
                Log.d(TAG,"columns["+r+"]:"+columns)
                Log.d(TAG,"columnsLen:"+columnsLen)
                for( k in 0..columnsLen){
                    try{
                        val data = columns.optJSONObject(k)
                        if(data!=null) {
                            val v = "["+k+"]"+data.getString("v") + ",\t"
                            Name += v
                        }else{
                            val v =  "["+k+"]  ,\t"
                            Name += v
                        }
                    }catch (e: JSONException) {
                        Log.e(TAG,"JSONException:"+e)
                    }catch (e:NullPointerException){
                        Log.e(TAG,"NullPointerException:"+e)
                    }

                }
                val v = "\\n \n"
                Name += v
            }
           NameView!!.text = Name
            CountryView!!.text = Country
        } catch (e: JSONException) {
            Log.e(TAG,"JSONException:"+e)
        }
    }
}
