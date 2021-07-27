package com.lollipop.sidatabangda.session;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.lollipop.sidatabangda.activity.LoginActivity;

import java.util.HashMap;

public class Config {

    SharedPreferences pref;

    SharedPreferences.Editor editor;

    Context _context;

    int PRIVATE_MODE = 0;

//    public static final String LOGIN_URL = "http://dioraska.my.id/android/nadia/log_in.php";
//    public static final String LOGIN_SUCCESS = "success";
    public static final String SHARED_PREF_NAME = "myloginapp";
    public static final String LOGGEDIN = "loggedin";
//    public static final String PIN_SHARED_PREF = "id";
//    public static final String USER_URL = "http://lineitopkal.com/android/api.php?case=user&id=";
    public static final String KEY_NAME = "myname";
    public static final String KEY_ID = "id_rt";
    public static final String KEY_NO_RT = "nomor_rt";
    public static final String KEY_USER = "user";
//    public static final String KEY_ID_KELURAHAN = "id_kelurahan";
    public static final String KEY_KEC_KEL = "kec_kel";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_NO_TELP = "no_telp_user";
    public static final String KEY_TOKEN = "jwt";
    public static final String KEY_VERSION = "version";
    public static final String KEY_PASS = "pass";
//    public static final String KEY_LUAS_M2 = "luas_m2";
//    public static final String KEY_KLL_M2 = "keliling_m2";
//    public static final String KEY_PJ_JLN_M = "panjang_jalan_meter";
//    public static final String KEY_PJ_JLN_B = "panjang_jalan_baik";
//    public static final String KEY_PJ_JLN_R = "panjang_jalan_rusak";
//    public static final String KEY_PJ_DRAINASE= "panjang_drainase";
//    public static final String KEY_KELURAHAN = "nama_kelurahan";
//    public static final String KEY_KAB_KOTA = "nama_kab_kota";
//    public static final String KEY_ADDRESS = "long";
    public static final String KEY_IMAGE = "images";
//    public static final String JSON_ARRAY = "result";
    public static final String KEY_LOKASI = "lokasi";

    public Config(Context context){
        this._context = context;
        pref = _context.getSharedPreferences(SHARED_PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void createLoginSession(String token,String id, String no_rt, String nama, String user, String pass, String kec_kel, String email, String no_telp, String version){
        editor.putBoolean(LOGGEDIN, true);
        editor.putString(KEY_TOKEN,token);
        editor.putString(KEY_ID, id);
        editor.putString(KEY_NO_RT, no_rt);
        editor.putString(KEY_NAME, nama);
        editor.putString(KEY_USER, user);
        editor.putString(KEY_PASS,pass);
        editor.putString(KEY_KEC_KEL,kec_kel);
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_NO_TELP, no_telp);
        editor.putString(KEY_VERSION,version);
        editor.commit();
    }

    public void checkLogin(){
        if(!this.isLoggedIn()){
            Intent i = new Intent(_context, LoginActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            _context.startActivity(i);
        }
    }

    public HashMap<String, String> getUserDetails(){
        HashMap<String, String> user = new HashMap<String, String>();
        user.put(KEY_TOKEN, pref.getString(KEY_TOKEN, null));
        user.put(KEY_ID, pref.getString(KEY_ID, null));
        user.put(KEY_NO_RT, pref.getString(KEY_NO_RT, null));
        user.put(KEY_NAME, pref.getString(KEY_NAME, null));
        user.put(KEY_USER, pref.getString(KEY_USER, null));
        user.put(KEY_PASS, pref.getString(KEY_PASS,null));
        user.put(KEY_KEC_KEL, pref.getString(KEY_KEC_KEL, null));
        user.put(KEY_EMAIL, pref.getString(KEY_EMAIL, null));
        user.put(KEY_NO_TELP, pref.getString(KEY_NO_TELP, null));
        user.put(KEY_VERSION, pref.getString(KEY_VERSION,null));

        return user;
    }

    public void logoutUser(){
        editor.clear();
        editor.commit();

        Intent i = new Intent(_context, LoginActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        _context.startActivity(i);
    }

    public boolean isLoggedIn(){
        return pref.getBoolean(LOGGEDIN, false);
    }
}
