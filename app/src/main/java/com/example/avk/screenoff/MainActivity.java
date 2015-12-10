package com.example.avk.screenoff;

import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Vibrator;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity {

    private static final String LOG_TAG = "MainActivity";
    public static final String APP_PREFERENCES = "mysettings";
    public static final String APP_PREFERENCES_VIBRATE = "vibrate";
    public static final String APP_PREFERENCES_SHOWCAPTION = "showcaption";

    private SharedPreferences mSettings;
    private Boolean IsVibrate;
    private Boolean IsShowCaption;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*Button btn = (Button) findViewById(R.id.btnSetting);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewSetting();
            }
        });*/
        //screenOffAndExit();
        mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
    }

   /*
    public void screenOffAndExit() {
        // first lock screen
        screenOff(getApplicationContext());

        // Если установлен параметр Вибрация
        if (IsVibrate) {
            ((Vibrator) getSystemService(Context.VIBRATOR_SERVICE)).vibrate(50);
            Log.i(LOG_TAG, "Вибрация");
        }

        // schedule end of activity
        final Activity activity = this;
        Thread t = new Thread() {
            public void run() {
                try {
                    sleep(200);
                } catch (InterruptedException e) {

                }
                activity.finish();
            }
        };
        t.start();
    }
     */

    public static void screenOff(final Context context) {
        DevicePolicyManager policyMng = (DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);
        ComponentName adminReceiver = new ComponentName(context, AdminReceiver.class);
        boolean admin = policyMng.isAdminActive(adminReceiver);
        if (admin) {
            Log.i(LOG_TAG, "Перед блокировкой экрана");
            policyMng.lockNow();
        } else {
            Log.i(LOG_TAG, "Программа не является админитратором");
            Toast.makeText(context, R.string.device_admin_not_enabled,
                    Toast.LENGTH_LONG).show();
        }


    }


    public void viewSetting(){
        //Intent intent = new Intent(this, SettingsActivity.class);
        //startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (mSettings.contains(APP_PREFERENCES_VIBRATE)) {
            // Получить настройки
            IsVibrate = mSettings.getBoolean(APP_PREFERENCES_VIBRATE, Boolean.FALSE);
            IsShowCaption = mSettings.getBoolean(APP_PREFERENCES_SHOWCAPTION, Boolean.FALSE);
            // Установить контролы
            CheckBox chB = (CheckBox) findViewById(R.id.chVibr);
            chB.setChecked(IsVibrate);

            chB = (CheckBox) findViewById(R.id.chShowCaption);
            chB.setChecked(IsShowCaption);


        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences.Editor editor = mSettings.edit();

        // Сохранить настройки
        CheckBox chB = (CheckBox) findViewById(R.id.chVibr);
        IsVibrate = chB.isChecked();
        chB = (CheckBox) findViewById(R.id.chShowCaption);
        IsShowCaption= chB.isChecked();
        editor.putBoolean(APP_PREFERENCES_VIBRATE, IsVibrate);
        editor.putBoolean(APP_PREFERENCES_SHOWCAPTION, IsShowCaption);
        editor.apply();
    }
}
