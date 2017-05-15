package com.safeandsound.app.safeandsound.controller.firebase;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by louisapabst on 30.04.17.
 */

public class MyFirebaseInstanceService extends FirebaseInstanceIdService {
    private static final String TAG = MyFirebaseInstanceService.class.getSimpleName();
    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);

        // TODO: Implement this method to send any registration to your app's servers.
        sendRegistrationToServer(refreshedToken);

    }

    private void sendRegistrationToServer(String token){

    }
}
