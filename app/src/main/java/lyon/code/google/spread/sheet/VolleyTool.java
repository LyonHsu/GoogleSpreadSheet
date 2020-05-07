package lyon.code.google.spread.sheet;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;


/*
 * https://bng86.gitbooks.io/android-third-party-/content/volley.html
 */
public class VolleyTool {

    String TAG = VolleyTool.class.getSimpleName();
    private RequestQueue mQueue;
    Context context;
    OnCallPathBack onCallPathBack;
    public VolleyTool(Context context){
        this.context = context;

        mQueue = Volley.newRequestQueue(context);

    }

    public void CallPath(String url){
        if(mQueue==null)
            mQueue = Volley.newRequestQueue(context);
        Log.d(TAG,"url:"+ url);
        StringRequest mStringRequest = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // response
                Log.d(TAG,"Response:"+ response);
                if(onCallPathBack!=null)
                {
                    onCallPathBack.Next(response);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // error
                Log.d(TAG,"Error.Response:"+ error.toString());
                if(onCallPathBack!=null)
                {
                    onCallPathBack.Next(error.toString());
                }
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("username", "mohammad_username");
                params.put("password", "mohammad_password");

                return params;
            }

        };

        mQueue.add(mStringRequest);
    }
    public static interface OnCallPathBack{
        void Next(String response);
    }

    public void setOnCallPathBackListener(OnCallPathBack onCallPathBack){
        this.onCallPathBack=onCallPathBack;
    }
}
