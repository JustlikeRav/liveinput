package com.example.rav.liveinput;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.view.View;

public class FullscreenUtils {

    private Activity activity;

    public FullscreenUtils(Activity activity){
        this.activity = activity;
    }

    public void setFullscreen(){
        int flags = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_FULLSCREEN;

        if (isImmersiveAvailable()) {
            flags |= View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        }

        activity.getWindow().getDecorView().setSystemUiVisibility(flags);
    }

    private static boolean isImmersiveAvailable() {
        return Build.VERSION.SDK_INT >= 19;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void registerSystemUiVisibility() {
        final View decorView = this.activity.getWindow().getDecorView();
        decorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {

            @Override
            public void onSystemUiVisibilityChange(int visibility) {
                if ((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0) {
                    setFullscreen();
                }
            }
        });
    }
}
