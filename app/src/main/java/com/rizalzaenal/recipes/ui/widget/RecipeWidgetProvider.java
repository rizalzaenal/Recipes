package com.rizalzaenal.recipes.ui.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.RemoteViews;
import com.rizalzaenal.recipes.R;
import com.rizalzaenal.recipes.ui.main.MainActivity;
import com.rizalzaenal.recipes.utils.PrefsStatic;

public class RecipeWidgetProvider extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
      int appWidgetId) {

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.item_widget);
        SharedPreferences
          sharedPreferences = context.getSharedPreferences(PrefsStatic.PREFERENCE_ID, Context.MODE_PRIVATE);
        views.setTextViewText(R.id.tv_widget_title, sharedPreferences.getString(PrefsStatic.PREFERENCE_RECIPE_TITLE, ""));
        views.setTextViewText(R.id.tv_widget_ingredients, sharedPreferences.getString(PrefsStatic.PREFERENCE_RECIPE_INGREDIENT, ""));

        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        views.setOnClickPendingIntent(R.id.tv_widget_ingredients, pendingIntent);
        views.setOnClickPendingIntent(R.id.tv_widget_title, pendingIntent);

        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}
