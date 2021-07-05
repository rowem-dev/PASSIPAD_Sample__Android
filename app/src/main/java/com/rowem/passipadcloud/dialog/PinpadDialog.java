package com.rowem.passipadcloud.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricPrompt;

import com.rowem.oneshotpadlib.interfaces.IOnInputListener;
import com.rowem.oneshotpadlib.util.OneShotPadUtil;
import com.rowem.passipadcloud.InterfaceCodes;
import com.rowem.passipadcloud.R;

import java.util.ArrayList;
import java.util.Collections;

public class PinpadDialog extends Dialog implements View.OnClickListener, OnCancelListener {

	private static final int MODE_JOIN = 1;
	private static final int MODE_JOIN_2 = 2;
	private static final int MODE_LOGIN = 3;

	private View mTitleView;
	private String mSubTitle;
	private String mLabel1;
	private String mLabel2;

	private String mPassword;
	private String mPrevPw;

	private ImageView[] mPwInputImgViews;
	private TextView[] mDigitViews;
	private ArrayList<Integer> mUsedDigit;
	private OnInputListener mListener;

	private Dialog mProgressDlg;

	private int mMode = MODE_LOGIN;

	private Context context;
	private boolean useBio = false;

	PinpadDialog(Context context, int theme) {
		super(context, theme);
	}

	public static PinpadDialog getSetDialog(Context ctx, OnInputListener listener) {
		PinpadDialog dlg = new PinpadDialog(ctx, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
		dlg.context = ctx;
		dlg.mListener = listener;
		dlg.mMode = MODE_JOIN;
		return dlg;
	}

	public static PinpadDialog getCertDialog(Context ctx, OnInputListener listener, boolean useBio) {
		PinpadDialog dlg = new PinpadDialog(ctx, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
		dlg.context = ctx;
		dlg.mListener = listener;
		dlg.mMode = MODE_LOGIN;
		dlg.useBio = useBio;
		return dlg;
	}

	///////////////////////////////////
	// Functions
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.layout_digit_pad_dlg);
		setOnCancelListener(this);

		initViews();

		if(useBio && mMode == MODE_LOGIN){
			showBiometricPrompt();
		}
	}

	private void initViews() {
		// 타이틀 처리
		TextView tv = (TextView) findViewById(R.id.tv_title);
		if (TextUtils.isEmpty(mLabel1) == false) {
			tv.setText(mLabel1);
		} else {
			tv.setVisibility(View.GONE);
		}

		// 라벨 처리.
		tv = (TextView) findViewById(R.id.tv_label);
		if (TextUtils.isEmpty(mLabel2) == false) {
			tv.setText(mLabel2);
		} else {
			tv.setVisibility(View.GONE);
		}

		// 입력된 패스워드 창.
		mPwInputImgViews = new ImageView[4];
		mPwInputImgViews[0] = (ImageView) findViewById(R.id.iv_input1);
		mPwInputImgViews[1] = (ImageView) findViewById(R.id.iv_input2);
		mPwInputImgViews[2] = (ImageView) findViewById(R.id.iv_input3);
		mPwInputImgViews[3] = (ImageView) findViewById(R.id.iv_input4);

		mPassword = "";
		updatePwView();

		// 닫기 버튼 처리
		findViewById(R.id.btn_close).setOnClickListener(this);
		
		// 숫자키 처리.
		mDigitViews = new TextView[10];
		Resources res = context.getResources();
		String pn = context.getPackageName();
		for (int i = 0; i < 10; i++) {
			mDigitViews[i] = (TextView) findViewById(res.getIdentifier("tv_num" + i, "id", pn));
			mDigitViews[i].setOnClickListener(this);
		}

		// del 키 처리.
		findViewById(R.id.tv_num_back).setOnClickListener(this);

		// 숫자패드 섞기.
		shuffleDigit();
	}

	private void shuffleDigit() {
		if (mUsedDigit == null) {
			mUsedDigit = new ArrayList<Integer>();
			for (int i = 0; i < 10; i++)
				mUsedDigit.add(i);
		}
		Collections.shuffle(mUsedDigit);

		for (int i = 0; i < 10; i++) {
			Integer it = mUsedDigit.get(i);
			mDigitViews[i].setText(it.toString());
			mDigitViews[i].setTag(it);
		}
	}

	private void updatePwView() {
		int pwLen = mPassword.length();

		for (int i = 0; i < mPwInputImgViews.length; i++) {
			if (i < pwLen)
				mPwInputImgViews[i].setImageDrawable(context.getDrawable(R.drawable.key_input_on));//mPwInputImgViews[i].setImageResource(null);
			else
				mPwInputImgViews[i].setImageDrawable(context.getDrawable(R.drawable.key_input_off));
		}
	}

	public PinpadDialog setPadTitle(String label) {
		mLabel1 = label;

		// 다이얼로그가 보여지고 있는 상태라면 바로 화면에 적용하자.
		if (isShowing() == true) {
			TextView tv = (TextView) findViewById(R.id.tv_title);
			if (TextUtils.isEmpty(mLabel1) == false) {
				tv.setText(mLabel1);
			} else {
				tv.setVisibility(View.GONE);
			}
		}

		return this;
	}

	public PinpadDialog setPadTitle(int label) {
		return setPadTitle(context.getString(label));
	}

	public PinpadDialog setLabel(String label) {
		mLabel2 = label;

		if (isShowing() == true) {
			TextView tv = (TextView) findViewById(R.id.tv_label);
			if (TextUtils.isEmpty(mLabel2) == false) {
				tv.setText(mLabel2);
			} else {
				tv.setVisibility(View.GONE);
			}
		}

		return this;
	}

	public PinpadDialog setLabel(int label) {
		return setLabel(context.getString(label));
	}

	public PinpadDialog setError(String error) {
		if(isShowing() == true) {
			TextView tv = (TextView) findViewById(R.id.tv_error);
			if(TextUtils.isEmpty(error) == false) {
				tv.setVisibility(View.VISIBLE);
				tv.setText(error);
			} else {
				tv.setVisibility(View.GONE);
			}
		}

		return this;
	}

	public PinpadDialog setError(int error) {
		return setError(context.getString(error));
	}

	@Override
	public void onClick(View v) {
		int vId = v.getId();
		if (vId == R.id.btn_close) {
			dismiss();
		} else if (vId == R.id.tv_num_back) {
			procDelKey();
		} else {
			if (v.getTag() instanceof Integer)
				addDigit((Integer) v.getTag());
		}

	}

	private void performInput() {
		int chkValidRtn = checkInputPw();

		// 일단 입력체크~
		if (chkValidRtn != InterfaceCodes.ERR_OK) {
			if (mListener != null)
				mListener.onInvalidInput(this, chkValidRtn);
			return;
		}

		switch (mMode) {
		case MODE_JOIN:
			mMode = MODE_JOIN_2;
			mPrevPw = mPassword;
			mPassword = "";
			updatePwView();
			shuffleDigit();

			if (mListener != null)
				mListener.onStep1Finished(this);
			break;

		case MODE_JOIN_2:
		case MODE_LOGIN:
			if (mListener != null)
				mListener.onInputPw(this, mPassword, false);
			break;
		}

	}

	public void clearInputPw() {
		mPassword = "";
		updatePwView();
	}

	private int checkInputPw() {
		if (TextUtils.isEmpty(mPassword) == true)
			return InterfaceCodes.ERR_TOO_SHORT;
		if (mPassword.length() != 4)
			return InterfaceCodes.ERR_TOO_SHORT;

		if (mMode == MODE_JOIN_2) {
			if (mPassword.equals(mPrevPw) == false)
				return InterfaceCodes.ERR_NOT_MATCH;
		}

		return InterfaceCodes.ERR_OK;
	}

	private void procDelKey() {
		if (TextUtils.isEmpty(mPassword) == true)
			return;

		mPassword = mPassword.substring(0, mPassword.length() - 1);
		// MLog.d("최종 조합 : " + mPassword);
		updatePwView();
	}

	private void addDigit(int d) {
		// MLog.d("누른 숫자 : " + d);
		if (mPassword != null && mPassword.length() >= 4)
			return;

		if (mPassword == null)
			mPassword = "" + d;
		else
			mPassword += d;

		// MLog.d("최종 조합 : " + mPassword);
		updatePwView();

		if(mPassword.length() == 4){
			performInput();
		}
	}

	@Override
	public void onCancel(DialogInterface dialog) {
		if (mListener != null)
			mListener.onCanceled(this);
	}

	public void showProgress() {
		if (mProgressDlg == null) {
			mProgressDlg = new Dialog(context);
			mProgressDlg.setCancelable(false);
			mProgressDlg.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
			ProgressBar pb = new ProgressBar(context);
			mProgressDlg.setContentView(pb);
		}
		if (mProgressDlg.isShowing() == true) {
			return;
		}

		mProgressDlg.show();
	}

	public void hideProgress() {
		if (mProgressDlg == null || mProgressDlg.isShowing() == false) {
			return;
		}

		mProgressDlg.dismiss();
	}

	private void showBiometricPrompt(){
		BiometricPrompt biometricPrompt = OneShotPadUtil.getBioListener(((AppCompatActivity) context), new IOnInputListener() {
			@Override
			public void onSucesss(String s) {
				mListener.onInputPw(PinpadDialog.this, "true", true);
			}

			@Override
			public void onError(String s) {

			}

			@Override
			public void onFaild(String s) {

			}
		});
		BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder()
				.setTitle("지문 인증")
				.setSubtitle("기기에 등록된 지문을 이용하여 지문을 인증해주세요.")
				.setNegativeButtonText("취소")
				.setDeviceCredentialAllowed(false)
				.build();

		//  사용자가 다른 인증을 이용하길 원할 때 추가하기
		biometricPrompt.authenticate(promptInfo);
	}

	public interface OnInputListener {
		void onStep1Finished(PinpadDialog dlg);

		void onInputPw(PinpadDialog dlg, String pw, boolean useBio);

		void onCanceled(PinpadDialog dlg);

		void onInvalidInput(PinpadDialog dlg, int code);
	}

}
