package com.example.project1;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;
import android.view.TextureView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.project1.util.CameraHelper;
import com.example.project1.util.CameraHelperImpl;
import com.example.project1.util.CaptureImageListener;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;


public class MainActivity extends AppCompatActivity implements CaptureImageListener {

    private final int CAPTURE_FRONT = 0;
    private final int DISPLAY_FRONT = 1;
    private final int CAPTURE_BACKSIDE = 2;
    private final int DISPLAY_BACKSIDE = 3;
    private final int NEXT = 4;

    private static MutableLiveData<Integer> state;
    private IdentificationViewModel viewModel;

    private LinearLayout captureLayout;
    private LinearLayout displayLayout;
    private TextView tvTitleCapture;
    private TextView tvTitleDisplay;
    private TextureView textureView;
    private ImageView imageView;
    private Button btnCapture;
    private Button btnCaptureAgain;
    private Button btnNext;
    private CameraHelper cameraHelper;
    private HandlerThread backgroundThread;
    private Handler backgroundHandler;
    private Identification identification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewModel = ViewModelProviders.of(this).get(IdentificationViewModel.class);

        state = new MutableLiveData<>();

        identification = new Identification();
        initView();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            initAction();
        }
        state.setValue(CAPTURE_FRONT);
    }

    void initView() {
        captureLayout = findViewById(R.id.capture_layout);
        displayLayout = findViewById(R.id.display_layout);
        tvTitleCapture = findViewById(R.id.tv_title_capture);
        tvTitleDisplay = findViewById(R.id.tv_title_display);
        textureView = findViewById(R.id.textureView);
        imageView = findViewById(R.id.imv_identification);
        btnCapture = findViewById(R.id.btn_capture);
        btnCaptureAgain = findViewById(R.id.btn_capture_again);
        btnNext = findViewById(R.id.btn_next);

    }

    void initAction() {
        cameraHelper = new CameraHelperImpl(this, textureView);

        state.observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer state) {
                Log.d("state", state.toString());
                switch (state){
                    case CAPTURE_FRONT:
                        tvTitleCapture.setText(R.string.front);
                        captureLayout.setVisibility(View.VISIBLE);
                        displayLayout.setVisibility(View.GONE);
                        break;
                    case DISPLAY_FRONT:
                        cameraHelper.closeCamera();
                        tvTitleDisplay.setText(R.string.front_identity_card);
                        btnNext.setText(R.string.capture_back_side);
                        captureLayout.setVisibility(View.GONE);
                        displayLayout.setVisibility(View.VISIBLE);
                        break;
                    case CAPTURE_BACKSIDE:
                        if (textureView.isAvailable()) {
                            cameraHelper.setupCamera(textureView.getWidth(), textureView.getHeight());
                            cameraHelper.openCamera();
                        } else {
                            cameraHelper.setTextureViewListener();
                        }
                        tvTitleCapture.setText(R.string.back_side);
                        captureLayout.setVisibility(View.VISIBLE);
                        displayLayout.setVisibility(View.GONE);
                        break;
                    case DISPLAY_BACKSIDE:
                        cameraHelper.closeCamera();
                        tvTitleDisplay.setText(R.string.back_side_identity_card);
                        btnNext.setText(R.string.next);
                        captureLayout.setVisibility(View.GONE);
                        displayLayout.setVisibility(View.VISIBLE);
                        break;
                    case NEXT:
                        cameraHelper.closeCamera();
                        String random = String.valueOf(new Random().nextInt(200));
                        String name = "Tran Van Dinh " + random;
                        identification.setGTCNnumber(random);
                        identification.setFullname(name);
                        viewModel.insert(identification);
                        startActivity(new Intent(MainActivity.this, ListActivity.class));
                        finish();
                        overridePendingTransition(0, 0);
                }
            }
        });

        btnCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cameraHelper.takeImage();
            }
        });
        btnCaptureAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(state.getValue() == DISPLAY_FRONT){
                    if (textureView.isAvailable()) {
                        cameraHelper.setupCamera(textureView.getWidth(), textureView.getHeight());
                        cameraHelper.openCamera();
                    } else {
                        cameraHelper.setTextureViewListener();
                    }
                }
                state.setValue(state.getValue() - 1);
            }
        });
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                state.setValue(state.getValue() + 1);
            }
        });
    }

    @Override
    public void captureImageListener(String path) {
        displayImage(path);
    }

    void displayImage(final String path) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(state.getValue() == CAPTURE_FRONT) {
                    identification.setFrontImage(path);
                } else identification.setBackSideImage(path);
                File file = new File(path);
                if (file.exists()) {
                    Bitmap myBitmap = null;
                    try {
                        myBitmap = handleSamplingAndRotationBitmap(getApplicationContext() , Uri.fromFile(file));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    imageView.setImageBitmap(myBitmap);
                    state.setValue(state.getValue() + 1);
                }
            }
        });
    }

    private void startBackgroundThread() {
        backgroundThread = new HandlerThread("CameraBackground");
        backgroundThread.start();
        backgroundHandler = new Handler(backgroundThread.getLooper());
        cameraHelper.setHandler(backgroundHandler);
    }

    private void stopBackgroundThread() {
        backgroundThread.quitSafely();
        try {
            backgroundThread.join();
            backgroundThread = null;
            backgroundHandler = null;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        startBackgroundThread();
        super.onResume();
        if (textureView.isAvailable()) {
            cameraHelper.setupCamera(textureView.getWidth(), textureView.getHeight());
            cameraHelper.openCamera();
        } else {
            cameraHelper.setTextureViewListener();
        }
        if(state.getValue() == NEXT) state.setValue(CAPTURE_FRONT);
    }

    @Override
    protected void onPause() {
        cameraHelper.closeCamera();
        stopBackgroundThread();
        super.onPause();
    }

    //handle rotated image
    public static Bitmap handleSamplingAndRotationBitmap(Context context, Uri selectedImage) throws IOException {
        int MAX_HEIGHT = 1024;
        int MAX_WIDTH = 1024;

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        InputStream imageStream = context.getContentResolver().openInputStream(selectedImage);
        BitmapFactory.decodeStream(imageStream, null, options);
        imageStream.close();

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, MAX_WIDTH, MAX_HEIGHT);


        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        imageStream = context.getContentResolver().openInputStream(selectedImage);
        Bitmap img = BitmapFactory.decodeStream(imageStream, null, options);

        img = rotateImageIfRequired(context, img, selectedImage);
        return img;
    }

    private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            // Calculate ratios of height and width to requested height and width
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);

            // Choose the smallest ratio as inSampleSize value, this will guarantee a final image
            // with both dimensions larger than or equal to the requested height and width.
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;

            // This offers some additional logic in case the image has a strange
            // aspect ratio. For example, a panorama may have a much larger
            // width than height. In these cases the total pixels might still
            // end up being too large to fit comfortably in memory, so we should
            // be more aggressive with sample down the image (=larger inSampleSize).

            final float totalPixels = width * height;

            // Anything more than 2x the requested pixels we'll sample down further
            final float totalReqPixelsCap = reqWidth * reqHeight * 2;

            while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
                inSampleSize++;
            }
        }
        return inSampleSize;
    }

    private static Bitmap rotateImageIfRequired(Context context, Bitmap img, Uri selectedImage) throws IOException {

        InputStream input = context.getContentResolver().openInputStream(selectedImage);
        ExifInterface ei;
        if (Build.VERSION.SDK_INT > 23)
            ei = new ExifInterface(input);
        else
            ei = new ExifInterface(selectedImage.getPath());

        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                return rotateImage(img, 90);
            case ExifInterface.ORIENTATION_ROTATE_180:
                return rotateImage(img, 180);
            case ExifInterface.ORIENTATION_ROTATE_270:
                return rotateImage(img, 270);
            default:
                return img;
        }
    }

    private static Bitmap rotateImage(Bitmap img, int degree) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        Bitmap rotatedImg = Bitmap.createBitmap(img, 0, 0, img.getWidth(), img.getHeight(), matrix, true);
        img.recycle();
        return rotatedImg;
    }
}
