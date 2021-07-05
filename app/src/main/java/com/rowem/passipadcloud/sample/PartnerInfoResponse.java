package com.rowem.passipadcloud.sample;

import android.util.Log;

import org.json.JSONObject;

public class PartnerInfoResponse implements ApiResponse{
    private final String TAG = PartnerInfoResponse.class.getSimpleName();

    /**
     * 요금제 타입
     * 1:	일반	지원안함
     * 2:	프리미엄	지원
     */
    public String bill_cd;
    /**
     * 사용타입 Y: 사용중
     */
    public String used_type;
    /**
     * 응답코드
     */
    public String code;
    /**
     * 	응답 메세지
     */
    public String message;

    @Override
    public void parseResponse(JSONObject jsonObj) throws Exception {
        // JSONObject 로그 출력
        if (jsonObj != null) {
            Log.d(TAG, jsonObj.toString(4));
        }
        JSONObject result = jsonObj.getJSONObject("result");
        if (result != null) {
            code = result.optString("code");
            message = result.optString("message");
            bill_cd = result.optString("bill_cd");
            used_type = result.optString("used_type");
        }
    }
}
