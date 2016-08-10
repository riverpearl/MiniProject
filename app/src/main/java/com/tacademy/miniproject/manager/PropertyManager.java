package com.tacademy.miniproject.manager;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.tacademy.miniproject.MyApplication;

/**
 * Created by Tacademy on 2016-08-10.
 */
public class PropertyManager {

    private static PropertyManager instance;

    public static PropertyManager getInstance() {
        if (instance == null)
            instance = new PropertyManager();

        return instance;
    }

    SharedPreferences sPrefs;
    SharedPreferences.Editor sEditor;

    private PropertyManager() {
        Context context = MyApplication.getContext();
        sPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        sEditor = sPrefs.edit();
    }

    private static final String KEY_EMAIL = "email";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_REGID = "registrationId";

    public void setEmail(String email) {
        sEditor.putString(KEY_EMAIL, email);
        sEditor.commit();
    }

    public String getEmail() {
        return sPrefs.getString(KEY_EMAIL, "");
    }

    public void setPassword(String password) {
        sEditor.putString(KEY_PASSWORD, password);
        sEditor.commit();
    }

    public String getPassword() {
        return sPrefs.getString(KEY_PASSWORD, "");
    }

    public void setRegId(String regId) {
        sEditor.putString(KEY_REGID, regId);
        sEditor.commit();
    }

    public String getRegId() {
        return sPrefs.getString(KEY_REGID, "");
    }
}
