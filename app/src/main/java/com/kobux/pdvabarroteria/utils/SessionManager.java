package com.kobux.pdvabarroteria.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {

    private static final String PREF_NAME = "sesion_usuario";
    private static final String KEY_USER_ID = "userId";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_HORA = "horaLogin";
    public static final String KEY_USER_NAME = "userName";

    SharedPreferences prefs;
    SharedPreferences.Editor editor;

    public SessionManager(Context ctx) {
        prefs = ctx.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = prefs.edit();
    }

    public void guardarSesion(long id, String email,String userName) {
        editor.putLong(KEY_USER_ID, id);
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_USER_NAME,userName);
        editor.putLong(KEY_HORA, System.currentTimeMillis());
        editor.apply();
    }

    public boolean haySesionActiva() {
        return prefs.contains(KEY_USER_ID) && prefs.contains(KEY_EMAIL);
    }

    public void cerrarSesion() {
        editor.clear();
        editor.apply();
    }

    public long getUserId() {
        return prefs.getLong(KEY_USER_ID, -1);
    }

    public String getEmail() {
        return prefs.getString(KEY_EMAIL, null);
    }
    public String getUserName() {
        return prefs.getString(KEY_USER_NAME, null);
    }
}