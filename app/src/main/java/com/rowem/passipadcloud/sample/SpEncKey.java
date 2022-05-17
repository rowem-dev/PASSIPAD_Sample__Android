package com.rowem.passipadcloud.sample;

import android.content.Context;
import android.text.TextUtils;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.RetryPolicy;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.rowem.oneshotpadlib.util.MLog;
import com.rowem.passipadcloud.BuildConfig;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SpEncKey {

    private RequestQueue mQueue;

    private static SpEncKey mInstance;

    public SpEncKey(Context ctx) {
        mQueue = Volley.newRequestQueue(ctx);
    }

    public static SpEncKey getInstance(Context ctx) {
        if (mInstance == null) {
            mInstance = new SpEncKey(ctx);
        }
        return mInstance;
    }

    public void reqSpEncKey(String base_url, String cus_no, String used_type, String partner_code, String secret_key, final ApiListener<SpEncKeyResponse> l) {
        if (TextUtils.isEmpty(base_url)) {
            base_url = BuildConfig.BASE_URL;
        }
        String url = base_url + "/spin/spmng/reqSpEncKey";

        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.putOpt("cus_no", cus_no);
            jsonObj.putOpt("used_type", used_type);
            jsonObj.putOpt("app_type", partner_code);

            // 시크릿키는 단말에서 사용 금지. 서버간의 통신에서만 사용바랍니다.
            jsonObj.putOpt("secret_key", secret_key);
        } catch (Exception e) {
            if (MLog.PRINT_LOG) {
                e.printStackTrace();
                MLog.w(e.getMessage());
            }
        }
        if (jsonObj != null) {
            MLog.d("jsonObj length : " + jsonObj.length());
            /*
             * jsonObj length 가 0 이하면 null 로 초기화하여 보내도록 하자. 값이 없는데 굳이 빈 jsonObj
             * 를 보낼 필요가 없다.
             */
            if (jsonObj.length() <= 0) {
                jsonObj = null;
            }
        }

        MLog.d("reqSpEncKey : url -" + url);
        JsonObjectRequest jor = new JsonObjectRequest(Request.Method.POST, url, jsonObj, response -> {
            MLog.i("onResponse(" + response + ")");
            SpEncKeyResponse res = new SpEncKeyResponse();
            try {
                res.parseResponse(response);
            } catch (Exception e) {
                if (MLog.PRINT_LOG) {
                    e.printStackTrace();
                    MLog.e(e.getMessage());
                }
            }
            l.onResult(res);
        }, error -> MLog.e("onErrorResponse(" + error + ")")) {
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
    }
}
