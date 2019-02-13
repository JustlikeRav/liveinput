package com.example.rav.liveinput;

import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    Camera meraCamera;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FullscreenUtils mFullscreenUtils = new FullscreenUtils(this);
        mFullscreenUtils.setFullscreen();
        mFullscreenUtils.registerSystemUiVisibility();

        if(checkCameraHardware()){
            meraCamera = getCameraInstance();
            Toast.makeText(this, meraCamera.toString(), Toast.LENGTH_SHORT).show();

            Camera.Parameters parameters = meraCamera.getParameters();
            Camera.Size size = getOptimalPreviewSize(parameters.getSupportedPictureSizes(),1920, 1080);
            int w = size.width;
            int h = size.height;
            parameters.setPreviewSize(w, h);
            meraCamera.setParameters(parameters);

            // Create our Preview view and set it as the content of our activity.
            CameraPreview mPreview = new CameraPreview(this, meraCamera);
            FrameLayout preview = findViewById(R.id.camera_preview);
            preview.addView(mPreview);
        }

    }

    private Camera.Size getOptimalPreviewSize(List<Camera.Size> sizes, int w, int h) {
        final double ASPECT_TOLERANCE = 0.1;
        double targetRatio = (double) h / w;
        if (sizes == null) {
            return null;
        }
        Camera.Size optimalSize = null;
        double minDiff = Double.MAX_VALUE;
        int targetHeight = h;
        for (Camera.Size size : sizes) {
            double ratio = (double) size.height / size.width;
            if (Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE) {
                continue;
            }
            if (Math.abs(size.height - targetHeight) < minDiff) {
                optimalSize = size;
                minDiff = Math.abs(size.height - targetHeight);
            }
        }
        if (optimalSize == null) {
            minDiff = Double.MAX_VALUE;
            for (Camera.Size size : sizes) {
                if (Math.abs(size.height - targetHeight) < minDiff) {
                    optimalSize = size;
                    minDiff = Math.abs(size.height - targetHeight);
                }
            }
        }
        return optimalSize;
    }

    /** Check if this device has a camera */
    private boolean checkCameraHardware() {
        int numCameras = Camera.getNumberOfCameras();
        return (numCameras > 0);
    }

    /** A safe way to get an instance of the Camera object. */
    public static Camera getCameraInstance(){
        Camera c = null;
        try {
            c = Camera.open(); // attempt to get a Camera instance
        }
        catch (Exception e){
            // Camera is not available (in use or does not exist)
        }
        return c; // returns null if camera is unavailable
    }
}
