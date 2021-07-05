package com.rowem.passipadcloud.sample;

import org.json.JSONObject;

public interface ApiResponse {

    void parseResponse(JSONObject obj) throws Exception;
}
