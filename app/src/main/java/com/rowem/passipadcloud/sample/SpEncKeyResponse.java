package com.rowem.passipadcloud.sample;

import android.util.Log;

import org.json.JSONObject;

public class SpEncKeyResponse implements ApiResponse {

	public String code;
	public String message;
	public String cus_no;
	public String auth_token;
	public String sp_enc_key;
	
	private final String TAG = SpEncKeyResponse.class.getSimpleName();
	
	/**
	 * JSONObject 데이터 Parsing
	 * @param jsonObj 데이터
	 */
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
			cus_no = result.optString("cus_no");
			auth_token = result.optString("auth_token");
			sp_enc_key = result.optString("sp_enc_key");
		}
	}
}
