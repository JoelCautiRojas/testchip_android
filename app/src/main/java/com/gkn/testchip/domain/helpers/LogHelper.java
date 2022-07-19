package com.gkn.testchip.domain.helpers;

import android.util.Log;

import com.gkn.testchip.BuildConfig;

public class LogHelper {

    private static final String TAG_SESSION_SINGLE = "LOG SINGLE SESSION";
    private static final String TAG_SESSION_LONG = "LOG LONG SESSION";
    private static final String TAG_SINGLE = "LOG SINGLE MESSAGE";
    private static final String TAG_LONG = "LOG LONG MESSAGE";
    private static final Integer MAXLENGTH = 300;

    /*public static void printSession(Context context){
        if(BuildConfig.DEBUG){
            Integer l = MAXLENGTH;
            PreferencesHelper preferencesHelper = new PreferencesHelper(context);
            String m = preferencesHelper.getSession();
            if(m != null){
                if(m.length() > l){
                    Log.e(TAG_SESSION_LONG,"Longitud de cadena "+ m.length());
                    int chunk = m.length()/l;
                    for (int i = 0; i <= chunk; i++){
                        int max = l * (i + 1);
                        if (max >= m.length()) {
                            Log.e(TAG_SESSION_LONG, "cadena " + i + " of " + chunk + ":" + m.substring(l * i));
                        } else {
                            Log.e(TAG_SESSION_LONG, "cadena " + i + " of " + chunk + ":" + m.substring(l * i, max));
                        }
                    }
                }else{
                    Log.e(TAG_SESSION_SINGLE, m);
                }
            }
        }
    }*/

    public static void printLog(String tag,String m){
        if(BuildConfig.DEBUG){
            Integer l = MAXLENGTH;
            if (m.length() > l) {
                Log.e(tag,"Longitud de cadena "+ m.length());
                int chunk = m.length()/l;
                for (int i = 0; i <= chunk; i++){
                    int max = l * (i + 1);
                    if (max >= m.length()) {
                        Log.e(tag,"cadena " + i + " of " + chunk + ":" + m.substring(l * i));
                    } else {
                        Log.e(tag,"cadena " + i + " of " + chunk + ":" + m.substring(l * i, max));
                    }
                }
            }else{
                Log.e(tag,m);
            }
        }
    }

    public static void printLog(String m){
        if(BuildConfig.DEBUG){
            Integer l = MAXLENGTH;
            if (m.length() > l) {
                Log.e(TAG_LONG,"Longitud de cadena "+ m.length());
                int chunk = m.length()/l;
                for (int i = 0; i <= chunk; i++){
                    int max = l * (i + 1);
                    if (max >= m.length()) {
                        Log.e(TAG_LONG,"cadena " + i + " of " + chunk + ":" + m.substring(l * i));
                    } else {
                        Log.e(TAG_LONG,"cadena " + i + " of " + chunk + ":" + m.substring(l * i, max));
                    }
                }
            }else{
                Log.e(TAG_SINGLE,m);
            }
        }
    }
}
