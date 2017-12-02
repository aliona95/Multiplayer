package com.aeappss.multiplayer;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class MyCurrentAzimuth implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor sensor;
    private int azimuthFrom = 0 ;
    private int azimuthTo = 0 ;
    private OnAzimuthChangedListener mAzimuthListener;
    Context mContext ;
    //в конструкторе передаем интерфейс OnAzimuthChangedListener и контекст
    public MyCurrentAzimuth(OnAzimuthChangedListener azimuthListener, Context context) {
        mAzimuthListener = azimuthListener;
        mContext = context;
    }
//подключаемся к сенсору и регистрируем слушатель для данного датчика с заданной периодичностью
    //SENSOR_DELAY_UI - частота обновления пользовательского интерфейса.
    //TYPE_ROTATION_VECTOR - Возвращает положение устройства в пространстве в виде угла
//относительно оси Z, указывающей на север.
// Виртуальный датчик, берущий показания от акселерометра, гироскопа и датчика магнитного поля.

    public void start(){
        sensorManager = (SensorManager) mContext .getSystemService( mContext. SENSOR_SERVICE);
        sensor = sensorManager .getDefaultSensor(Sensor. TYPE_ROTATION_VECTOR);
        sensorManager.registerListener( this, sensor ,
                SensorManager. SENSOR_DELAY_UI);
    }
    //Отменяет регистрацию слушателя для всех датчиков.
    public void stop(){
        sensorManager.unregisterListener( this );
    }

    //вызывается при новом событии датчика
    //получаем матрицу вращения устройства
    // в переменную azimuthTo сохраняем градусную меру угла поворота в радианах
    @Override
    public void onSensorChanged(SensorEvent event) {
        azimuthFrom = azimuthTo;

        float[] orientation = new float[ 3];
        float[] rMat = new float[ 9];
        SensorManager.getRotationMatrixFromVector (rMat, event. values);
        azimuthTo = ( int) ( Math. toDegrees( SensorManager.getOrientation( rMat, orientation )[ 0] ) + 360 ) % 360 ;

        mAzimuthListener.onAzimuthChanged( azimuthFrom , azimuthTo );
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}