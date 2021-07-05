package com.rowem.passipadcloud.pref;

import android.content.Context;
import android.content.SharedPreferences;

public class Pref {
    private static final String FILE_NAME = "passipad_cloud";

    private final String KEY_CUS_ID = "CUS_ID";
    private final String KEY_PARTNER_CODE = "PARTNER_CODE";
    private final String KEY_PUSH_TOKEN = "PUSH_TOKEN";
    private final String KEY_USE_BIO = "USE_BIO";

    public static Pref INSTANCE = new Pref();

    private SharedPreferences mPreferences;

    public Pref load(Context context) {
        mPreferences = context.getApplicationContext().getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);

        return this;
    }

    private SharedPreferences.Editor getEditor() {
        return mPreferences.edit();
    }

    public void clear() {
        getEditor().clear().commit();
    }

    /** CUS_ID */

    public void setCusId(String cusId) {
        getEditor().putString(KEY_CUS_ID, cusId).apply();
    }

    public String getCusId() {
        return mPreferences.getString(KEY_CUS_ID, null);
    }

    /** PUSH_TOKEN */

    public void setPushToken(String pushToken) {
        getEditor().putString(KEY_PUSH_TOKEN, pushToken).apply();
    }

    public String getPushToken() {
        return mPreferences.getString(KEY_PUSH_TOKEN, null);
    }

    /** PARTNER_CODE */

    public void setPartnerCode(String partnerCode){
        getEditor().putString(KEY_PARTNER_CODE, partnerCode).apply();
    }

    public String getPartnerCode() {
        return mPreferences.getString(KEY_PARTNER_CODE, null);
    }

    /** USE_BIO */

//    public void setUseBio(boolean useBio){
//        getEditor().putBoolean(KEY_USE_BIO, useBio).apply();
//    }
//
//    public boolean getUseBio() {
//        return mPreferences.getBoolean(KEY_USE_BIO, false);
//    }
}
