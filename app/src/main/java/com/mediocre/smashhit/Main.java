package com.mediocre.smashhit;

import android.annotation.SuppressLint;
import android.app.NativeActivity;
import android.content.Context;
import android.os.Bundle;
import android.os.Build;
import android.os.PowerManager;
import android.util.DisplayMetrics;
import android.view.View;
import android.util.Log;

import java.util.List;
import java.util.Locale;

public class Main extends NativeActivity {
    public static final String TAG = "smashhit";
    public static final String gAppName = "smashhit";
    private PowerManager.WakeLock wakeLock;

    void complain(String message) {
        Log.e(TAG, "**** Error: " + message);
    }

    @SuppressLint("InvalidWakeLockTag")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Smash Hit uses "Movie" so I use it too :P
        this.wakeLock = ((PowerManager) getSystemService(Context.POWER_SERVICE)).newWakeLock(10, "Movie");

        if (Build.VERSION.SDK_INT >= 19) {
            setSystemUiFlags();
            getWindow().getDecorView().setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
                @Override
                public void onSystemUiVisibilityChange(int visibility) {
                    Main.this.setSystemUiFlags();
                }
            });
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            setSystemUiFlags();
        }
    }

    public void setSystemUiFlags() {
        if (Build.VERSION.SDK_INT >= 19) {
            try {
                // Should be 5894
                getWindow().getDecorView().setSystemUiVisibility(
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN |View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                        View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }

    public String command_(String cmd) {
        String[] words = new String[0];

        try {
            words = cmd.split(" ");
        } catch (Exception e) {
            System.out.println(e);
        }

        if (words.length == 0) {
            return "";
        }

        switch (words[0]) {
            case "setalwayson":
                break;
            case "getosname":
                return Build.VERSION.RELEASE;
            case "getmodelname":
                return Build.MODEL;
            case "getdensity":
                DisplayMetrics metrics = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(metrics);
                return Integer.toString(metrics.densityDpi);
            case "visiturl":
                // todo
                break;
            case "getlanguage":
                return Locale.getDefault().getLanguage();
            case "quit":
                // todo
                break;
            case "istv":
                // todo
                return "false";
            case "storeenabled":
                // todo (but probably not)
                return "false";
            case "storepurchase":
                // todo
                break;
            case "storegetstatus":
                // todo
                return "0";
            case "storerestore":
                // todo
                break;
            case "storeisrestored":
                // todo
                return "false";
            case "storedetails":
                // todo
                break;
            case "storegetprice":
                // todo
                return "unknown";
            case "signin":
                // todo
                break;
            case "signout":
                // todo
                break;
            case "updateleaderboard":
                // todo
                break;
            case "incrementachievement":
                // todo
                break;
            case "showleaderboards":
                // todo
                break;
            case "showachievements":
                // todo
                break;
            case "issignedin":
                // todo
                return "false";
            case "cloudsave":
                // todo
                break;
            case "cloudload":
                // todo
                break;
            case "cloudget":
                // todo
                break;
            case "gpcp":
                return getPackageCodePath();
            default:
                complain("Don't know what cmd '" + cmd + "' means.");
                break;
        }

        return "";
    }

    public String command(String cmd) {
        String result = command_(cmd);
        Log.i(TAG, "Got command '" + cmd + "' with result '" + result + "'");
        return result;
    }
}
