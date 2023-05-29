package com.fypvpreventor.VpreventorFYP;

import android.Manifest;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.widget.RemoteViews;

/**
 * Implementation of App Widget functionality.
 */
public class emergency_call extends AppWidgetProvider {

    private static final String PHONE_NUMBER = "+601120066491";
    private static final int CALL_PHONE_REQUEST_CODE = 123;

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.emergency_call);
        PendingIntent pendingIntent = getPendingIntent(context);
        views.setOnClickPendingIntent(R.id.imageView3, pendingIntent);
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    private PendingIntent getPendingIntent(Context context) {
        Intent intent = new Intent(Intent.ACTION_CALL); //ACTION_CALL TO CALL DIRECTLY
        intent.setData(Uri.parse("tel:" + PHONE_NUMBER));
        return PendingIntent.getActivity(context, 0, intent, 0);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        if (AppWidgetManager.ACTION_APPWIDGET_UPDATE.equals(intent.getAction())) {
            if (context != null) {
                AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
                int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(context, emergency_call.class));
                for (int appWidgetId : appWidgetIds) {
                    updateAppWidget(context, appWidgetManager, appWidgetId);
                }
            }
        }
    }

    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
        if (context.checkSelfPermission(Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.emergency_call);
            Intent permissionIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("package:" + context.getPackageName()));
            PendingIntent pendingPermissionIntent = PendingIntent.getActivity(
                    context,
                    CALL_PHONE_REQUEST_CODE,
                    permissionIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT
            );
            views.setOnClickPendingIntent(R.id.imageView3, pendingPermissionIntent);
            AppWidgetManager.getInstance(context).updateAppWidget(
                    new ComponentName(context, emergency_call.class),
                    views
            );
        }
    }
}