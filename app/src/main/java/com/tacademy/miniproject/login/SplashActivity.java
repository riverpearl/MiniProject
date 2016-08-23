package com.tacademy.miniproject.login;

import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.tacademy.miniproject.MainActivity;
import com.tacademy.miniproject.MyApplication;
import com.tacademy.miniproject.R;
import com.tacademy.miniproject.autodata.User;
import com.tacademy.miniproject.autodata.UserResult;
import com.tacademy.miniproject.gcm.RegistrationIntentService;
import com.tacademy.miniproject.manager.NetworkManager;
import com.tacademy.miniproject.manager.NetworkRequest;
import com.tacademy.miniproject.manager.PropertyManager;
import com.tacademy.miniproject.request.FacebookLoginRequest;
import com.tacademy.miniproject.request.ProfileRequest;
import com.tacademy.miniproject.request.SignInRequest;

public class SplashActivity extends AppCompatActivity {

    LoginManager loginManager;
    CallbackManager callbackManager;

    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private BroadcastReceiver mRegistrationBroadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        loginManager = LoginManager.getInstance();
        callbackManager = CallbackManager.Factory.create();

        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                doRealStart();
            }
        };

        setUpIfNeeded();
    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(RegistrationIntentService.REGISTRATION_COMPLETE));
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }

    private void setUpIfNeeded() {
        if (checkPlayServices()) {
            String regId = PropertyManager.getInstance().getRegId();
            if (!regId.equals("")) {
                doRealStart();
            } else {
                Intent intent = new Intent(this, RegistrationIntentService.class);
                startService(intent);
            }
        }
    }

    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                Dialog dialog = apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST);
                dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {

                    @Override
                    public void onCancel(DialogInterface dialog) {
                        finish();
                    }
                });
                dialog.show();
            } else {
                finish();
            }
            return false;
        }
        return true;
    }

    private void doRealStart() {
        ProfileRequest request = new ProfileRequest(this);
        Log.d("Splash : ", "request");
        NetworkManager.getInstance().getNetworkData(request, new NetworkManager.OnResultListener<UserResult<User>>() {
            @Override
            public void onSuccess(NetworkRequest<UserResult<User>> request, UserResult<User> result) {
                Log.d("Splash : ", "profile request");
                moveMainActivity();
            }

            @Override
            public void onFail(NetworkRequest<UserResult<User>> request, int errorCode, String errorMessage, Throwable e) {
                Log.d("Splash : ", errorMessage.toString());
                if (errorCode == -1) {
                    if (errorMessage.equals("not login")) {
                        loginSharedPreference();
                        return;
                    }
                }
                moveLoginActivity();
            }
        });
    }

    private void loginSharedPreference() {
        if (isFacebookLoggedIn()) processFacebookLogin();
        else if (isLoggedIn()) processAutoLogin();
        else moveLoginActivity();
    }

    private boolean isFacebookLoggedIn() {
        return (!TextUtils.isEmpty((PropertyManager.getInstance().getFacebookId())));
    }

    private boolean isLoggedIn() {
        String result = PropertyManager.getInstance().getEmail();
        return !TextUtils.isEmpty(result);
    }

    private void processFacebookLogin() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();

        if (!accessToken.getUserId().equals(PropertyManager.getInstance().getFacebookId())) {
            resetFacebookAndMoveLoginActivity();
            return;
        }

        if (accessToken != null) {
            String token = accessToken.getToken();
            String regId = PropertyManager.getInstance().getRegId();

            FacebookLoginRequest request = new FacebookLoginRequest(this, token, regId);
            NetworkManager.getInstance().getNetworkData(request, new NetworkManager.OnResultListener<UserResult<Object>>() {
                @Override
                public void onSuccess(NetworkRequest<UserResult<Object>> request, UserResult<Object> result) {
                    if (result.getCode() == 1)
                        moveMainActivity();
                    else {
                        resetFacebookAndMoveLoginActivity();
                    }
                }

                @Override
                public void onFail(NetworkRequest<UserResult<Object>> request, int errorCode, String errorMessage, Throwable e) {
                    loginManager.logOut();
                    facebookLogin();
                }
            });
        } else facebookLogin();
    }

    private void facebookLogin() {
        loginManager.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                AccessToken accessToken = AccessToken.getCurrentAccessToken();

                if (!accessToken.getUserId().equals(PropertyManager.getInstance().getFacebookId())) {
                    resetFacebookAndMoveLoginActivity();
                    return;
                }

                FacebookLoginRequest request = new FacebookLoginRequest(SplashActivity.this, accessToken.getToken(),
                        PropertyManager.getInstance().getRegId());

                NetworkManager.getInstance().getNetworkData(request, new NetworkManager.OnResultListener<UserResult<Object>>() {
                    @Override
                    public void onSuccess(NetworkRequest<UserResult<Object>> request, UserResult<Object> result) {
                        if (result.getCode() == 1)
                            moveMainActivity();
                        else {
                            resetFacebookAndMoveLoginActivity();
                        }
                    }

                    @Override
                    public void onFail(NetworkRequest<UserResult<Object>> request, int errorCode, String errorMessage, Throwable e) {
                        resetFacebookAndMoveLoginActivity();
                    }
                });
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });

        loginManager.logInWithReadPermissions(this, null);
    }

    private void resetFacebookAndMoveLoginActivity() {
        loginManager.logOut();
        PropertyManager.getInstance().setFacebookId("");
        moveLoginActivity();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PLAY_SERVICES_RESOLUTION_REQUEST &&
                resultCode == Activity.RESULT_OK) {
            setUpIfNeeded();
            return;
        }

        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void processAutoLogin() {
        String email = PropertyManager.getInstance().getEmail();
        String password = PropertyManager.getInstance().getPassword();
        String regId = PropertyManager.getInstance().getRegId();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(regId))
            return;

        SignInRequest request = new SignInRequest(this, email, password, regId);
        NetworkManager.getInstance().getNetworkData(request, new NetworkManager.OnResultListener<UserResult<User>>() {
            @Override
            public void onSuccess(NetworkRequest<UserResult<User>> request, UserResult<User> result) {
                moveMainActivity();
            }

            @Override
            public void onFail(NetworkRequest<UserResult<User>> request, int errorCode, String errorMessage, Throwable e) {
                moveLoginActivity();
            }
        });
    }

    Handler mHandler = new Handler(Looper.getMainLooper());

    private void moveLoginActivity() {

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        }, 1000);

    }

    private void moveMainActivity() {
        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
