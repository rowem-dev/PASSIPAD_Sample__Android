package com.rowem.passipadcloud.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Switch;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.rowem.oneshotpadlib.crypto.RSACryptor;
import com.rowem.oneshotpadlib.manager.OneShotPadManager;
import com.rowem.oneshotpadlib.net.res.SimpleResponse;
import com.rowem.oneshotpadlib.util.MLog;
import com.rowem.passipadcloud.BuildConfig;
import com.rowem.passipadcloud.C;
import com.rowem.passipadcloud.R;
import com.rowem.passipadcloud.pin.SPinManager;
import com.rowem.passipadcloud.pref.Pref;
import com.rowem.passipadcloud.sample.ApiRequester;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class HomeActivity extends AppCompatActivity {

    private Pref pref;

    private boolean useBio = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EventBus.getDefault().register(this);

        setContentView(R.layout.activity_home);

        MLog.PRINT_LOG = true;

        pref = Pref.INSTANCE.load(this);

        OneShotPadManager.getInstance().setBaseUrl(BuildConfig.BASE_URL);
        SPinManager.getInstance().initPushToken(this, false);
        // hyune - 10/20/21 삭제
//        RSACryptor.getInstance().init(this);

        initView();

        load();
    }

    private void initView(){
        findViewById(R.id.ib_login).setOnClickListener(v -> {
            String cusId = pref.getCusId();

            if(TextUtils.isEmpty(cusId)) return;

            login(cusId);
        });
        findViewById(R.id.ib_change_pw).setOnClickListener(v -> {
            String cusId = pref.getCusId();

            if(TextUtils.isEmpty(cusId)) return;

            changePassword(cusId);
        });
        findViewById(R.id.ib_signup).setOnClickListener(v -> startSignUp());

        findViewById(R.id.ll_use_bio).setOnClickListener(v -> {
            boolean isChecked = ((Switch)findViewById(R.id.sw_bio)).isChecked();

            String cusId = pref.getCusId();

            if(TextUtils.isEmpty(cusId)) return;

            setBioAvailable(cusId, !isChecked);
        });
    }

    @Subscribe
    public void onMessage(C.BIO bio){
        setUseBio(bio == C.BIO.USE);
    }

    private void setUseBio(boolean useBio){
        this.useBio = useBio;

        ((Switch)findViewById(R.id.sw_bio)).setChecked(useBio);
        if(useBio) {
            findViewById(R.id.ll_use_bio).setBackground(getDrawable(R.drawable.line_5dc1ff));
        } else {
            findViewById(R.id.ll_use_bio).setBackground(getDrawable(R.drawable.line_cbcbcb));
        }
    }

    private void load(){
        String cusId = pref.getCusId();
        String partnerCode = pref.getPartnerCode();

        // 가입 여부 체크
        if(TextUtils.isEmpty(cusId)){
            startSignUp();
        }else{
            getPartnerInfo(partnerCode, cusId);
        }
    }

    /**
     * 로그인 요청
     */
    private void login(String cusId){
        SPinManager.getInstance().reqPush(this, useBio ? OneShotPadManager.USED_TYPE_BIO_AUTH : OneShotPadManager.USED_TYPE_LOGIN, cusId,  null, "passipad USED_TYPE_LOGIN");
    }

    /**
     * 가입 여부 체크
     */
    private void checkJoin(String partnerCode, String cusId){
        // 파트너사 코드 설정 (app_type)
        SPinManager.getInstance().setPartnerCode(partnerCode);

        SPinManager.getInstance().reqCheckJoin(this, cusId, res -> {
            if(SimpleResponse.CD_NORMAL_USER.equals(res.code) ){
            }else if(SimpleResponse.REGISTERED_SUCCESS_AND_USED_BIO.equals(res.code)){
                setUseBio(true);
            }else{
                startSignUp();
            }
        });
    }

    /**
     * 파트너사 정보조회
     */
    private void getPartnerInfo(String partnerCode, String cusId){
        ApiRequester.getInstance(this).getPartnerInfo(partnerCode, res -> {
            // 프리미엄 요금제(생체인증 지원) 체크
            boolean useBio = res!=null && "2".equals(res.bill_cd);

            setLayoutUseBio(useBio);

            checkJoin(partnerCode, cusId);
        });
    }

    /**
     * 생체 인증 UI 노출
     */
    private void setLayoutUseBio(boolean use){
//        findViewById(R.id.sw_bio).setVisibility(use ? View.VISIBLE : View.GONE);
    }

    /**
     * 비밀번호 변경 요청
     */
    private void changePassword(String cusId){
        SPinManager.getInstance().reqPush(this, OneShotPadManager.USED_TYPE_CHANGE_PWD, cusId, null, "passipad USED_TYPE_CHANGE_PWD");
    }

    /**
     * 생체인증 활성화/비활성화 요청
     */
    private void setBioAvailable(String cusId, boolean available){
        if(available) {
            SPinManager.getInstance().reqPush(this, OneShotPadManager.USED_TYPE_JOIN_BIO_AUTH, cusId, null, "passipad USED_TYPE_JOIN_BIO_AUTH");
        }else{
            SPinManager.getInstance().reqPush(this, OneShotPadManager.USED_TYPE_BIO_CLOSE, cusId, null, "passipad USED_TYPE_BIO_CLOSE");
        }
    }

    /**
     * 회원 가입 화면 이동
     */
    private void startSignUp(){
        startActivityForResult(new Intent(this, SignUpActivity.class), 1111);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1111) {
            if (resultCode == RESULT_OK) {
                // UI 초기화
                setLayoutUseBio(false);
                setUseBio(false);

                // 신규 회원 정보
                String cusId = pref.getCusId();
                String partnerCode = pref.getPartnerCode();

                // 파트너사 정보 조회
                getPartnerInfo(partnerCode, cusId);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
