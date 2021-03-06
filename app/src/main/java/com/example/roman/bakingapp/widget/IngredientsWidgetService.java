package com.example.roman.bakingapp.widget;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

import com.example.roman.bakingapp.R;
import com.example.roman.bakingapp.data.DataRepository;
import com.example.roman.bakingapp.data.model.RecipeWithStepsAndIngredients;

import javax.inject.Inject;

import dagger.android.AndroidInjection;

public class IngredientsWidgetService extends IntentService {

    @Inject
    DataRepository mRepository;

    @Override
    public void onCreate() {
        super.onCreate();
        AndroidInjection.inject(this);
    }

    public IngredientsWidgetService() {
        super("IngredientsWidgetService");
    }

    /**
     * Starts this service to perform UpdateIngredientsWidgets action with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    public static void startActionUpdateIngredientsWidgets(Context context) {
        Intent intent = new Intent(context, IngredientsWidgetService.class);
        context.startService(intent);
    }

    /**
     * @param intent
     */
    @Override
    protected void onHandleIntent(Intent intent) {
        handleActionUpdateIngredientsWidgets();
    }


    /**
     * Handle action UpdateIngredientsWidgets in the provided background thread
     */
    private void handleActionUpdateIngredientsWidgets() {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(
                new ComponentName(this, IngredientsWidgetProvider.class));
        //Trigger data update to handle the ListView widgets and force a data refresh
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_list_view);
        //Now update all widgets
        int recipeId = mRepository.getRecipeIdPreference();
        RecipeWithStepsAndIngredients recipe = mRepository.getRecipeByIdOffline(recipeId);
        if (recipe != null) {
            IngredientsWidgetProvider.updateIngredientsWidgets(this, appWidgetManager, recipe, appWidgetIds);
        }
    }
}
