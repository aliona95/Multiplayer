package com.aeappss.multiplayer;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Size;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;

import java.util.Arrays;
import java.util.Comparator;

import android.content.Context;
import android.content.Intent;
import android.graphics.SurfaceTexture;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Size;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;

import java.util.Arrays;
import java.util.Comparator;

import static java.lang.Math.abs;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class Camera2Activity extends AppCompatActivity implements SensorEventListener {

    private CameraDevice mCameraDevice = null;
    private CaptureRequest.Builder mCaptureRequestBuilder = null;
    private CameraCaptureSession mCameraCaptureSession  = null;
    private TextureView mTextureView = null;
    private Size mPreviewSize = null;

    private int maxThrowingForce = 0;

    private SensorManager senSensorManager;
    private ProgressBar firstBar = null;
    private Sensor senAccelerometer;

    private TextView textViewThrowing;
    private Button throwingButton;

    private long lastUpdate = 0;
    private float last_x = 0, last_y = 0, last_z = 0;
    private static final int SHAKE_THRESHOLD = 600;
    private boolean pressedThrow = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera2);


        senSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        senAccelerometer = senSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        senSensorManager.registerListener(this, senAccelerometer , SensorManager.SENSOR_DELAY_NORMAL);
        //final RelativeLayout rl = (RelativeLayout)findViewById(R.id.RelativeLayout02);
        //rl.setBackgroundColor(Color.rgb(190, 238, 233));

        firstBar = (ProgressBar)findViewById(R.id.firstBar);


        textViewThrowing = (TextView) findViewById(R.id.textViewThrowing);
        throwingButton = (Button) findViewById(R.id.throwingButton);
        throwingButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                firstBar.setVisibility(View.VISIBLE);
                firstBar.setMax(35);
                firstBar.setProgress(0);
                //firstBar.setProgress(3);
                throwingButton.setVisibility(View.INVISIBLE);
                textViewThrowing.setVisibility(View.INVISIBLE);
                pressedThrow = true;
            }
        });



        //Toast.makeText(Camera2Activity.this, "Nėra duomenų", Toast.LENGTH_LONG).show();

        mTextureView = (TextureView) findViewById(R.id.textureView);
        mTextureView.setSurfaceTextureListener(mSurfaceTextureListener);

        final FloatingActionButton mapAction = (FloatingActionButton) findViewById(R.id.action_map);
        mapAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /// MAP INTENT
                //Toast.makeText(Camera2Activity.this, "AS CIA", Toast.LENGTH_LONG).show();
                Intent homeIntent = new Intent(Camera2Activity.this, MapsActivity.class);
                startActivity(homeIntent);

            }
        });


       /* View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.action_map:
                        Intent homeIntent = new Intent(Camera2Activity.this, MapsActivity.class);
                        startActivity(homeIntent);
                        //finish();
                        Toast.makeText(getApplicationContext(), "TEXT", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        } ;

        mapAction.setOnClickListener(onClickListener);
*/

    }

    static class CompareSizesByArea implements Comparator<Size> {
        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public int compare(Size lhs, Size rhs) {
            return Long.signum((long) lhs.getWidth() * lhs.getHeight() - (long) rhs.getWidth() * rhs.getHeight());
        }
    }

    private CameraDevice.StateCallback mStateCallback = new CameraDevice.StateCallback() {
        @Override
        public void onOpened(CameraDevice camera) {
            mCameraDevice = camera;
            SurfaceTexture texture = mTextureView.getSurfaceTexture();
            if (texture == null) {
                return;
            }
            texture.setDefaultBufferSize(mPreviewSize.getWidth(), mPreviewSize.getHeight());
            Surface surface = new Surface(texture);
            try {
                mCaptureRequestBuilder = mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
            } catch (CameraAccessException e){
                e.printStackTrace();
            }
            mCaptureRequestBuilder.addTarget(surface);
            try {
                mCameraDevice.createCaptureSession(Arrays.asList(surface), mPreviewStateCallback, null);
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
        }
        @Override
        public void onError(CameraDevice camera, int error) {}
        @Override
        public void onDisconnected(CameraDevice camera) {

        }
    };

    private TextureView.SurfaceTextureListener mSurfaceTextureListener = new TextureView.SurfaceTextureListener() {
        @Override
        public void onSurfaceTextureUpdated(SurfaceTexture surface) {

        }
        @Override
        public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

        }
        @Override
        public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
            return false;
        }
        @Override
        public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
            openCamera();
        }
    };

    private CameraCaptureSession.StateCallback mPreviewStateCallback = new CameraCaptureSession.StateCallback() {
        @Override
        public void onConfigured(CameraCaptureSession session) {
            startPreview(session);
        }
        @Override
        public void onConfigureFailed(CameraCaptureSession session) {

        }
    };

    @Override
    protected void onPause() {
        super.onPause();
        if (mCameraDevice != null) {
            mCameraDevice.close();
            mCameraDevice = null;
        }

        //senSensorManager.unregisterListener(this);

    }
    @Override
    public void onResume() {
        super.onResume();
        if (mTextureView.isAvailable()) {
            openCamera();
        } else {
            mTextureView.setSurfaceTextureListener(
                    mSurfaceTextureListener);
        }

        //senSensorManager.registerListener(this, senAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);

    }

    private void openCamera() {
        CameraManager manager = (CameraManager) getSystemService(CAMERA_SERVICE);
        try{
            String cameraId = manager.getCameraIdList()[0];
            CameraCharacteristics characteristics = manager.getCameraCharacteristics(cameraId);
            StreamConfigurationMap map = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
            mPreviewSize = map.getOutputSizes(SurfaceTexture.class) [0];
            manager.openCamera(cameraId, mStateCallback, null);
        } catch(CameraAccessException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    private void startPreview(CameraCaptureSession session) {
        mCameraCaptureSession = session;
        mCaptureRequestBuilder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO);
        HandlerThread backgroundThread = new HandlerThread("CameraPreview");
        backgroundThread.start();
        Handler backgroundHandler = new Handler(backgroundThread. getLooper());
        try {
            mCameraCaptureSession.setRepeatingRequest(mCaptureRequestBuilder.build(), null, backgroundHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    float maxDown = 0;
    boolean doneDown = false;
    boolean doneUp = false;
    boolean was = false;
    boolean was1 = false;
    float downX = 0;
    float upX = 0;
    long curTimeThrow;
    long allThrowingTime;
    float accelerometerDistance;
    float accelerometerSpeed;
    boolean throwUp = false;
    boolean wasUp = false;
    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        Sensor mySensor = sensorEvent.sensor;
        //final RelativeLayout rl = (RelativeLayout)findViewById(R.id.RelativeLayout02);
        if (mySensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float x = sensorEvent.values[0];
            float y = sensorEvent.values[1];
            float z = sensorEvent.values[2];

            //Log.i("PASUKIMAS ", "IESKOMA " + String.valueOf(x));

            long curTime = System.currentTimeMillis();

            /*////////////////////// 2 budas
            if(!was && last_x == 0 && last_x < (x + 5) && pressedThrow && !throwUp && !(last_x > x && (last_x <= 0 && x <= 0 && last_x - x > 0.5) && !doneDown && pressedThrow && !throwUp)){
                throwUp = true;
                wasUp = true;
            }
            if(!was && throwUp && last_x < x && pressedThrow){
                last_x = x;
            }else if(!was && throwUp && last_x > (x + 2) && pressedThrow && x > -5){// metimas ivyko
                accelerometerSpeed = (abs(x) + abs(y) + abs(z));  // ar nereikejo last_x vetoj x?
                Toast.makeText(this, "GREITIS111 " + accelerometerSpeed, Toast.LENGTH_SHORT).show();
                throwUp = false;
                last_x = 0;
                pressedThrow = false;
                firstBar.setVisibility(View.INVISIBLE);
                //firstBar.setMax(35);
                throwingButton.setVisibility(View.VISIBLE);
                wasUp = false;
            }*/



            // 1 budas
            if(/*(curTime/1000 - lastUpdate/1000 > 0.001) && */last_x > x && (last_x <= 0 && x <= 0 && last_x - x > 0.5) && !doneDown && pressedThrow && !throwUp && !wasUp){ // atgal
                    //Log.i("PASUKIMAS ", "IESKOMA " + String.valueOf(x));
                    firstBar.setProgress(Math.abs(Math.round(Math.abs(x))));
                    lastUpdate = curTime;
                    was = true;
                    last_x = x;
                    last_y = y;
                    last_z = z;
            } // priesingu atveju jei padaugeja paklaida einant i kita puse, nustojama, issaugomi duomenys ir vykdoma i prieki
            else if(/*(curTime/1000 - lastUpdate/1000 > 0.001) && */last_x < x && (last_x <= 0 && x <= 0 && (abs(last_x) - abs(x) > 2) || abs(last_x) - abs(x) > 0.5 && (curTime/1000 - lastUpdate/1000 > 0.75)) && !doneDown && was && pressedThrow && !throwUp && !wasUp){   // laika iki 0.5 gal sumazint???
                //Log.i("PASUKIMAS", "GREITIS kitas = " + (abs(x) + abs(y) + abs(z)) + "m/s^2");
                //accelerometerSpeed = (abs(x) + abs(y) + abs(z));  // ar nereikejo last_x vetoj x ir kitu likusiu?
                accelerometerSpeed = (abs(last_x) + abs(last_y) + abs(last_z));  // ar nereikejo last_x vetoj x ir kitu likusiu?
                //Log.i("PASUKIMAS ", "GAUTAS " + last_x);
                lastUpdate = curTime;
                doneDown = true;
                downX = last_x;
                curTimeThrow = System.currentTimeMillis();   // nustatome laika, kai pasiekiame galines koord
            }

            if(/*(curTime/1000 - lastUpdate/1000 > 0.001) && */doneDown && last_x < x && abs(last_x) - abs(x) > 0.5 && abs(last_x) - abs(x) < 2 && !doneUp && pressedThrow && !throwUp && !wasUp){
                //Log.i("PASUKIMAS ", "IESKOMA KITO " + String.valueOf(x));
                lastUpdate = curTime;
                was1 = true;
                last_x = x;
            }else if(/*(curTime/1000 - lastUpdate/1000 > 0.001) && */abs(x) - abs(last_x) > 5 && !doneUp && was1 && pressedThrow && !throwUp && !wasUp){
                //Log.i("PASUKIMAS ", "GAUTAS KITO ");
                lastUpdate = curTime;
                doneUp = true;
                upX = last_x;
                allThrowingTime = System.currentTimeMillis() - curTimeThrow;
                //Log.i("PASUKIMAS ", "GAUTAS LAIKAS " + allThrowingTime/1000 + "s");
            }else if((curTime/1000 - lastUpdate/1000 > 0.75) && !doneUp && was1 && pressedThrow && !throwUp && !wasUp){ // laukiama 0.75 sek
                //Log.i("PASUKIMAS ", "GAUTAS KITO ");
                lastUpdate = curTime;
                doneUp = true;
                upX = last_x;
                allThrowingTime = System.currentTimeMillis() - curTimeThrow;
                //Log.i("PASUKIMAS ", "GAUTAS LAIKAS " + allThrowingTime/1000 + "s");
            }

            if(doneDown && doneUp && !wasUp){
                accelerometerDistance = abs(downX) + abs(upX);
                //Log.i("PASUKIMAS ", "ATSTUMAS " + accelerometerDistance);
                //accelerometerSpeed = accelerometerDistance*(allThrowingTime/1000);
                Log.i("PASUKIMAS ", "GREITIS " + accelerometerSpeed + "m/s");
                Toast.makeText(this, "GREITIS " + accelerometerSpeed, Toast.LENGTH_LONG).show();
                pressedThrow = false;
                doneDown = false;
                doneUp = false;
                firstBar.setVisibility(View.INVISIBLE);
                //firstBar.setMax(35);
                throwingButton.setVisibility(View.VISIBLE);
                //textViewThrowing.setVisibility(View.VISIBLE);
                //firstBar.setProgress(0);
                last_x = 0;
            }




























            /*if ((curTime - lastUpdate) > 200) {
                long diffTime = (curTime - lastUpdate);
                Log.i("TAG", "LAIKAAAAAAS " + diffTime);

                lastUpdate = curTime;

                float speed = Math.abs(x + y + z - last_x - last_y - last_z);/// diffTime * 10000/2;
                Log.i("TAG", "SPEED " + speed);
                if (speed > SHAKE_THRESHOLD) {
                    Log.i("RESOLUTION", String.valueOf(mySensor.getResolution()) + " DELAY " + mySensor.getMaximumRange());
                    Toast toast = Toast.makeText(getApplicationContext(), "Įrenginys pajudintas. Greitis: " + speed, Toast.LENGTH_SHORT);
                    toast.show();
                }

                last_x = x;
                last_y = y;
                last_z = z;


                if (last_x < 0){
                    firstBar.setProgress(Math.abs(Math.round(last_x)));
                    if (maxThrowingForce < Math.abs(Math.round(last_x))){
                        maxThrowingForce = Math.abs(Math.round(last_x));
                        textViewThrowing.setVisibility(View.INVISIBLE);
                    } else if (maxThrowingForce - Math.abs(Math.round(last_x)) > 2){
                        // METYMAS, kelias sekundes pastabdyti, tada isjungti, ir kad vel galima butu metyti
                        maxThrowingForce = 0;
                        //textViewThrowing.setVisibility(View.VISIBLE);

                        if(throwingButton.getVisibility() == View.INVISIBLE) {
                            textViewThrowing.setVisibility(View.VISIBLE);
                        }

                        firstBar.setVisibility(View.INVISIBLE);
                        //firstBar.setProgress(0);
                        throwingButton.setVisibility(View.VISIBLE);

                    }
                }
            }*/
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}


