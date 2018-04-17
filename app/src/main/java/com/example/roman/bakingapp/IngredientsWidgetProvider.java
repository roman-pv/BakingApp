package com.example.roman.bakingapp;

import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

import com.example.roman.bakingapp.data.model.RecipeWithStepsAndIngredients;
import com.example.roman.bakingapp.ui.detail.RecipeDetailsFragment;
import com.example.roman.bakingapp.ui.detail.StepsActivity;
import com.example.roman.bakingapp.ui.main.MainActivity;

public class IngredientsWidgetProvider extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                RecipeWithStepsAndIngredients recipe, int appWidgetId) {

        Log.d("Widget_log", "updateAppWidget" + recipe.getId());
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_list_view);
        // Set the GridWidgetService intent to act as the adapter for the GridView
        Intent intent = new Intent(context, ListWidgetService.class);
        intent.putExtra(MainActivity.EXTRA_RECIPE_ID, recipe.getId());
        views.setRemoteAdapter(R.id.widget_list_view, intent);
        // Set the PlantDetailActivity intent to launch when clicked
        Intent startActivityIntent = new Intent(context, StepsActivity.class);
        PendingIntent appPendingIntent = PendingIntent.getActivity(context, 0,
                startActivityIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setPendingIntentTemplate(R.id.widget_list_view, appPendingIntent);

//        Intent openRecipeIntent = new Intent(context, StepsActivity.class);
//        Bundle extras = new Bundle();
//        extras.putInt(MainActivity.EXTRA_RECIPE_ID, recipe.getId());
//        openRecipeIntent.putExtra(MainActivity.EXTRA_RECIPE_ID, extras);
//        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
//        views.setOnClickPendingIntent(R.id.widget_view, pendingIntent);
        // Handle empty gardens
        //views.setEmptyView(R.id.widget_list_view, R.id.empty_view);
        views.setTextViewText(R.id.widget_recipe_title_text_view, recipe.getName());
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        //Start the intent service update widget action, the service takes care of updating the widgets UI
        IngredientsWidgetService.startActionUpdateIngredientsWidgets(context);
    }

    /**
     * Updates all widget instances given the widget Ids and display information
     *
     * @param context          The calling context
     * @param appWidgetManager The widget manager
     * @param appWidgetIds     Array of widget Ids to be updated
     */
    public static void updateIngredientsWidgets(Context context, AppWidgetManager appWidgetManager,
                                                RecipeWithStepsAndIngredients recipe, int[] appWidgetIds) {
        Log.d("Widget_log", "updateIngredientsWidgets" + recipe.getId());
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, recipe, appWidgetId);
        }
    }

    @Override
    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager,
                                          int appWidgetId, Bundle newOptions) {
        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions);
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        // Perform any action when one or more AppWidget instances have been deleted
    }

    @Override
    public void onEnabled(Context context) {
        // Perform any action when an AppWidget for this provider is instantiated
    }

    @Override
    public void onDisabled(Context context) {
        // Perform any action when the last AppWidget instance for this provider is deleted
    }

}