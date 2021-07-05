package com.rowem.passipadcloud.ui;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.rowem.oneshotpadlib.net.res.BaseResponse;
import com.rowem.oneshotpadlib.net.res.SimpleResponse;
import com.rowem.passipadcloud.R;
import com.rowem.passipadcloud.pin.SPinManager;
import com.rowem.passipadcloud.pref.Pref;
import com.rowem.passipadcloud.sample.ApiListener;
import com.rowem.passipadcloud.sample.SpEncKey;
import com.rowem.passipadcloud.sample.SpEncKeyResponse;

public class SignUpActivity extends AppCompatActivity {

    private Pref pref;

    private EditText etPartnerCode, etCusId;
    private TextView tvError;
    private Button btRegister;
    private ImageButton ibBack;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_signup);

        pref = Pref.INSTANCE.load(this);

        initView();
    }

    private void initView(){
        etPartnerCode = findViewById(R.id.edit_partnercode);
        etCusId = findViewById(R.id.edit_userid);
        tvError = findViewById(R.id.tv_error);
        btRegister = findViewById(R.id.btn_ok);
        ibBack = findViewById(R.id.appbar_back);

        //-  RP47638872
        //-  RP50712813
        //-  RP55248120
        etPartnerCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkInput();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        etCusId.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkInput();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        ibBack.setOnClickListener(v -> onBackPressed());

        btRegister.setOnClickListener(v -> signUp());
    }

    /**
     * 입력 체크
     */
    private void checkInput(){
        boolean is = etPartnerCode.getText().length() > 0 && etCusId.getText().length() > 0;

        if(is != btRegister.isEnabled()){
            btRegister.setEnabled(is);
        }
    }

    private void setErrorMessage(String message){
        tvError.setVisibility(View.VISIBLE);
        tvError.setText(message);
    }

    /**
     * 가입
     */
    private void signUp() {
        final String cusId = etCusId.getText().toString().trim(); //mEtCusId.getText().toString();
        final String partnerCode = etPartnerCode.getText().toString().trim();

        if (TextUtils.isEmpty(cusId)) {
            setErrorMessage("고객 ID를 입력해주세요..");
            return;
        }

        reqSpEncKey(cusId, partnerCode);
    }

    /**
     * SP 암호키 및 인증토큰 요청 (향후 사이트 서버에서 받아와야 하는 부분..)
     */
    private void reqSpEncKey(String cusId, String partnerCode) {
        ApiListener<SpEncKeyResponse> l = res -> {
             if(BaseResponse.CD_OK.equals(res.code)){
                 reqSignUp(cusId, res.auth_token, res.sp_enc_key, partnerCode);
             }else{
                 setErrorMessage(res.message);
//                 Toast.makeText(SignUpActivity.this, res.code+"_"+res.message, Toast.LENGTH_SHORT).show();
             }
        };

        SpEncKey.getInstance(this).reqSpEncKey(null, cusId, String.valueOf(1), partnerCode, l);
    }

    /**
     * 가입요청
     */
    private void reqSignUp(String cusId, String authToken, String spEncKey, String partnerCode){
        String pushToken = pref.getPushToken();

        SPinManager.getInstance().setPartnerCode(partnerCode);

        SPinManager.getInstance().reqJoin(this, authToken, pushToken, cusId, cusId, spEncKey, res -> {
            if(SimpleResponse.CD_OK.equals(res.code)){
                pref.setCusId(cusId);
                pref.setPartnerCode(partnerCode);

                new AlertDialog.Builder(SignUpActivity.this)
                        .setMessage("가입완료")
                        .setCancelable(false)
                        .setPositiveButton("확인", (dialog, which) -> {
                            setResult(RESULT_OK);
                            finish();
                        })
                        .show();
            }else{
                setErrorMessage(res.message);
            }
        });
    }
}
