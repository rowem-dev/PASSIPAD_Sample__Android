package com.rowem.passipadcloud.sample;

import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONObject;

public class SpVerifyResponse implements ApiResponse{

	public ResultData resultData = null;
	
	private final String TAG = SpVerifyResponse.class.getSimpleName();
	
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
			resultData = new Gson().fromJson(result.toString(), ResultData.class);
		}
	}

	public class ResultData {
		private String code;
		private String message;
		private String reg_dt;
		private String reg_dt_fmt;
		private String cus_no;
		private String cus_id;
		private String app_type;
		private String auth_token;
		private String used_type;
		private String used_type_fmt;
		private String signature;
		private String sign_text;
		private String auth_result;
		private String auth_end_dt_fmt;
		private String auth_result_fmt;
		private String auth_end_dt;

		public String getCode() {
			return code;
		}

		public void setCode(String code) {
			this.code = code;
		}

		public String getMessage() {
			return message;
		}

		public void setMessage(String message) {
			this.message = message;
		}

		public String getReg_dt() {
			return reg_dt;
		}

		public void setReg_dt(String reg_dt) {
			this.reg_dt = reg_dt;
		}

		public String getReg_dt_fmt() {
			return reg_dt_fmt;
		}

		public void setReg_dt_fmt(String reg_dt_fmt) {
			this.reg_dt_fmt = reg_dt_fmt;
		}

		public String getCus_no() {
			return cus_no;
		}

		public void setCus_no(String cus_no) {
			this.cus_no = cus_no;
		}

		public String getCus_id() {
			return cus_id;
		}

		public void setCus_id(String cus_id) {
			this.cus_id = cus_id;
		}

		public String getApp_type() {
			return app_type;
		}

		public void setApp_type(String app_type) {
			this.app_type = app_type;
		}

		public String getAuth_token() {
			return auth_token;
		}

		public void setAuth_token(String auth_token) {
			this.auth_token = auth_token;
		}

		public String getUsed_type() {
			return used_type;
		}

		public void setUsed_type(String used_type) {
			this.used_type = used_type;
		}

		public String getUsed_type_fmt() {
			return used_type_fmt;
		}

		public void setUsed_type_fmt(String used_type_fmt) {
			this.used_type_fmt = used_type_fmt;
		}

		public String getSignature() {
			return signature;
		}

		public void setSignature(String signature) {
			this.signature = signature;
		}

		public String getSign_text() {
			return sign_text;
		}

		public void setSign_text(String sign_text) {
			this.sign_text = sign_text;
		}

		public String getAuth_result() {
			return auth_result;
		}

		public void setAuth_result(String auth_result) {
			this.auth_result = auth_result;
		}

		public String getAuth_end_dt_fmt() {
			return auth_end_dt_fmt;
		}

		public void setAuth_end_dt_fmt(String auth_end_dt_fmt) {
			this.auth_end_dt_fmt = auth_end_dt_fmt;
		}

		public String getAuth_result_fmt() {
			return auth_result_fmt;
		}

		public void setAuth_result_fmt(String auth_result_fmt) {
			this.auth_result_fmt = auth_result_fmt;
		}

		public String getAuth_end_dt() {
			return auth_end_dt;
		}

		public void setAuth_end_dt(String auth_end_dt) {
			this.auth_end_dt = auth_end_dt;
		}
	}
}
