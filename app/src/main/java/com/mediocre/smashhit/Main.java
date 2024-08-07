package com.mediocre.smashhit;

import android.annotation.SuppressLint;
import android.app.NativeActivity;
import android.app.UiModeManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Build;
import android.os.Process;
import android.os.PowerManager;
import android.util.DisplayMetrics;
import android.view.View;
import android.util.Log;
import android.view.WindowManager;

import java.util.List;
import java.util.Locale;

public class Main extends NativeActivity {
    public static final String TAG = "smashhit";
    public static final String gAppName = "smashhit";
    private PowerManager.WakeLock wakeLock;
    public BackgroundThread mThread;

    void complain(String message) {
        Log.e(TAG, "**** Error: " + message);
    }

    class BackgroundThread extends AsyncTask<Object, Integer, Long> {
        public Main mMain;
        public boolean mCurrentWakeLock;
        public boolean mWantedWakeLock;
        public boolean mQuitFlag;

        BackgroundThread(Main main) {
            this.mMain = main;
        }

        @Override
        public Long doInBackground(Object... _unused) {
            while (true) {
                publishProgress(0);
                try {
                    Thread.sleep(50L);
                }
                catch (Exception e) {}
            }
        }

        @Override
        public void onProgressUpdate(Integer... _unused) {
            try {
                // Quit on a request to quit the game
                if (this.mQuitFlag) {
                    Process.killProcess(Process.myPid());
                }

                // Makes the window take the full screen regardless of UI elements
                if (this.mCurrentWakeLock != this.mWantedWakeLock) {
                    this.mCurrentWakeLock = this.mWantedWakeLock;
                    if (this.mCurrentWakeLock) {
                        this.mMain.getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
                    } else {
                        this.mMain.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
                    }
                }
            }
            catch (Exception e) {
                this.mMain.complain(e.toString());
            }
        }
    }

    @SuppressLint("InvalidWakeLockTag")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Smash Hit uses "Movie" so I use it too :P
        this.wakeLock = ((PowerManager) getSystemService(Context.POWER_SERVICE)).newWakeLock(10, "Movie");

        // Create the background thread (handles some stuff)
        this.mThread = new BackgroundThread(this);
        this.mThread.execute(1);

        // Some type of hack, probably
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
                if (words.length > 1) {
                    if (words[1].equals("true")) {
                        this.mThread.mWantedWakeLock = true;
                    } else {
                        this.mThread.mWantedWakeLock = false;
                    }
                }
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
                if (words.length > 1) {
                    try {
                        Intent myIntent = new Intent("android.intent.action.VIEW", Uri.parse(words[1]));
                        startActivity(myIntent);
                    }
                    catch (Exception e) {
                        System.out.println(e);
                    }
                }
                break;
            case "getlanguage":
                return Locale.getDefault().getLanguage();
            case "quit":
                this.mThread.mQuitFlag = true;
                break;
            case "istv":
                // todo
                UiModeManager uiModeManager = (UiModeManager) getSystemService(Context.UI_MODE_SERVICE);
                if (uiModeManager.getCurrentModeType() == Configuration.UI_MODE_TYPE_TELEVISION) {
                    return "true";
                }
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
        if (!cmd.equals("issignedin")) {
            Log.i(TAG, "Got command '" + cmd + "' with result '" + result + "'");
        }
        return result;
    }
}
