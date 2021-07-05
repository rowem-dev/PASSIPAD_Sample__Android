package com.rowem.passipadcloud.push;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.rowem.oneshotpadlib.data.PushInfo;
import com.rowem.passipadcloud.pin.SPinManager;
import com.rowem.passipadcloud.pref.Pref;

public class FCMService extends FirebaseMessagingService {

    public interface FCMRegisterListener {
        void onRegister(String token);
    }

    private static FCMRegisterListener registerListener = null;

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.i(getClass().getSimpleName(), "onMessageReceived(" + remoteMessage + ")");

        SPinManager.getInstance().setPushInfo(getApplicationContext(), PushInfo.parsePushInfo(remoteMessage));
    }

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);

        if(registerListener != null){
            registerListener.onRegister(s);
        }
    }

    public static void getFcmToken(final Context ctx, FCMService.FCMRegisterListener listener){
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {

                        // Get new FCM registration token
                        String token = task.getResult();

//                        Pref.INSTANCE.load(ctx).setPushToken(token);

                        listener.onRegister(token);
                        return;
                    }

                    FCMService.registerListener = listener;
                });
    }
}
