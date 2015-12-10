package com.example.avk.screenoff;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Vibrator;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;


/**
 * Implementation of App Widget functionality.
 */
public class ScreenOffWidget extends AppWidgetProvider {
    public static String ACTION_WIDGET_RECEIVER = "ActionReceiverWidget";
    public static final String APP_PREFERENCES_VIBRATE = "vibrate";
    private static final String LOG_TAG = "ScreenOffWidget";
    private Boolean IsVibrate;
    private Boolean IsShowCaption;
    private SharedPreferences mSettings;

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        final int N = appWidgetIds.length;
        for (int i = 0; i < N; i++) {
            updateAppWidget(context, appWidgetManager, appWidgetIds[i]);
        }
    }


    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created

    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        CharSequence widgetText = context.getString(R.string.appwidget_text);
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.screen_off_widget);
      //  views.setTextViewText(R.id.appwidget_text, widgetText);

        //Подготавливаем Intent для Broadcast
        Intent active = new Intent(context, ScreenOffWidget.class);
        active.setAction(ACTION_WIDGET_RECEIVER);

        //создаем событие
        PendingIntent actionPendingIntent = PendingIntent.getBroadcast(context, 0, active, 0);

        views.setOnClickPendingIntent(R.id.widget_button, actionPendingIntent);
      //  views.setOnClickFillInIntent(appWidgetId, active);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);


    }


    @Override
    public void onReceive(Context context, Intent intent) {

        //Ловим наш Broadcast, проверяем и выводим сообщение
        final String action = intent.getAction();
        /// Toast.makeText(context, "action = " + action, Toast.LENGTH_SHORT).show();
        if (ACTION_WIDGET_RECEIVER.equals(action)) {
            turnScreenOff(context);

           Log.e("onReceive", "screenOff");
          // Toast.makeText(context, "onReceive", Toast.LENGTH_SHORT).show();
        }
        super.onReceive(context, intent);
   }



    public void turnScreenOff(final Context context) {
        // Получить настройки

        mSettings = context.getSharedPreferences(MainActivity.APP_PREFERENCES, Context.MODE_PRIVATE);
        if (mSettings.contains(MainActivity.APP_PREFERENCES_VIBRATE)) {

            // Если установлен параметр Вибрация
          IsVibrate = mSettings.getBoolean(MainActivity.APP_PREFERENCES_VIBRATE, Boolean.FALSE);
        }
        if (mSettings.contains(MainActivity.APP_PREFERENCES_SHOWCAPTION)) {
           IsShowCaption = mSettings.getBoolean(MainActivity.APP_PREFERENCES_SHOWCAPTION, Boolean.FALSE);
        }

        if (IsVibrate) {
            ((Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE)).vibrate(50);
            Log.i(LOG_TAG, "Вибрация");
        }


        MainActivity.screenOff(context);



    }


}


