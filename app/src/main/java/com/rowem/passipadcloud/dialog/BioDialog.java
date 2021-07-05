package com.rowem.passipadcloud.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricPrompt;

import com.rowem.oneshotpadlib.interfaces.IOnInputListener;
import com.rowem.oneshotpadlib.util.OneShotPadUtil;
import com.rowem.passipadcloud.R;

import java.util.concurrent.Executor;

@Deprecated
public class BioDialog extends Dialog implements View.OnClickListener, OnCancelListener {


	private BioDialog.OnInputListener mListener;
	private Executor executor;
	private BiometricPrompt biometricPrompt;
	private BiometricPrompt.PromptInfo promptInfo;
	private Context mContext;


	BioDialog(Context context) {
		super(context);
	}

	BioDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
		super(context, cancelable, cancelListener);
	}

	BioDialog(Context context, int theme) {
		super(context, theme);
	}

	public static BioDialog getDialog(Context ctx, BioDialog.OnInputListener listener) {
		BioDialog dlg = new BioDialog(ctx, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
		dlg.mListener = listener;
		dlg.mContext = ctx;
		return dlg;
	}


	///////////////////////////////////
	// Functions
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.layout_bio_dlg);
		setOnCancelListener(this);

		initViews();

		reqBioMettric();

	}



	private void reqBioMettric(){
		final BioDialog b = this;
		/*
		executor = ContextCompat.getMainExecutor(mContext);
		biometricPrompt = new BiometricPrompt((AppCompatActivity) mContext,
				executor, new BiometricPrompt.AuthenticationCallback() {
			@Override
			public void onAuthenticationError(int errorCode,
											  @NonNull CharSequence errString) {
				super.onAuthenticationError(errorCode, errString);
				Toast.makeText(mContext,
						"error : " + errString, Toast.LENGTH_SHORT)
						.show();
			}

			@Override
			public void onAuthenticationSucceeded(
					@NonNull BiometricPrompt.AuthenticationResult result) {
				super.onAuthenticationSucceeded(result);
				Toast.makeText(mContext,
						"succeed", Toast.LENGTH_SHORT).show();
				mListener.onInputPw(b, "true");
			}

			@Override
			public void onAuthenticationFailed() {
				super.onAuthenticationFailed();
				Toast.makeText(mContext, "onAuthenticationFailed",
						Toast.LENGTH_SHORT)
						.show();
			}
		});
		*/
		IOnInputListener l  = new IOnInputListener() {
			@Override
			public void onSucesss(String s) {
				//Toast.makeText(mContext, s,	Toast.LENGTH_SHORT).show();
				mListener.onInputPw(b, s);
			}

			@Override
			public void onError(String s) {

			}

			@Override
			public void onFaild(String s) {

			}


		};

		biometricPrompt = OneShotPadUtil.getBioListener( ((AppCompatActivity)mContext), l);
		promptInfo = new BiometricPrompt.PromptInfo.Builder()
				.setTitle("지문 인증")
				.setSubtitle("기기에 등록된 지문을 이용하여 지문을 인증해주세요.")
				.setNegativeButtonText("취소")
				.setDeviceCredentialAllowed(false)
				.build();

		//  사용자가 다른 인증을 이용하길 원할 때 추가하기

		biometricPrompt.authenticate(promptInfo);
	}

	private void initViews() {
		TextView tv;
	}


	@Override
	public void onClick(View v) {


	}


	@Override
	public void onCancel(DialogInterface dialog) {
		if (mListener != null)
			mListener.onCanceled(this);
	}

	public interface OnInputListener {
		void onStep1Finished(BioDialog dlg);

		void onInputPw(BioDialog dlg, String pw);

		void onCanceled(BioDialog dlg);

		void onInvalidInput(BioDialog dlg, int code);
	}
}
