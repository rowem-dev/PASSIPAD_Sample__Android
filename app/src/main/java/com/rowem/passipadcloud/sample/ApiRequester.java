package com.rowem.passipadcloud.sample;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.RetryPolicy;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.rowem.oneshotpadlib.util.MLog;
import com.rowem.passipadcloud.BuildConfig;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ApiRequester {

    private static ApiRequester INSTANCE;

    private final RequestQueue mQueue;

    public static synchronized ApiRequester getInstance(Context ctx){
        if(INSTANCE == null){
            INSTANCE = new ApiRequester(ctx);
        }

        return INSTANCE;
    }

    private ApiRequester(Context ctx) {
        mQueue = Volley.newRequestQueue(ctx);
    }

    public void getPartnerInfo(String partnerCode, ApiListener<PartnerInfoResponse> listener){

        String url = BuildConfig.BASE_URL + "//spin/getPartnerInfo";

        try {
            JSONObject jsonObj = new JSONObject();
            jsonObj.put("app_type", partnerCode);

            MLog.d("getPartnerInfo : url -" + url);
            JsonObjectRequest jor = new JsonObjectRequest(Request.Method.POST, url, jsonObj, response -> {
                PartnerInfoResponse resp = new PartnerInfoResponse();
                try {
                    resp.parseResponse(response);
                } catch (Exception e) {
                    if (MLog.PRINT_LOG) {
                        e.printStackTrace();
                        MLog.e(e.getMessage());
                    }
                    resp = null;
                }

                if(listener!=null) listener.onResult(resp);
            }, error ->{
                MLog.e("onErrorResponse(" + error + ")");

                if(listener!=null) listener.onResult(null);
            }){
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headerMap = new HashMap<String, String>();
                    // json 형식 데이터 요청
                    headerMap.put("Accept", "application/json");
                    return headerMap;
                }

                @Override
                public byte[] getBody() {
                    byte[] body = super.getBody();
                    // Log 출력을 위해 Override..
                    if (body != null) {
                        MLog.d(new String(body));
                    } else {
                        MLog.e("body is null.");
                    }
                    return body;
                }

                @Override
                public Request<?> setRetryPolicy(RetryPolicy retryPolicy) {
                    return super.setRetryPolicy(new DefaultRetryPolicy(10 * 1000, 3, 1.0f));
                }
            };

            mQueue.add(jor);
        } catch (JSONException e) {
            e.printStackTrace();

            if(listener!=null) listener.onResult(null);
        }
    }
}
