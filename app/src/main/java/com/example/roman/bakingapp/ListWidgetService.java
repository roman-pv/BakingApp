package com.example.roman.bakingapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.roman.bakingapp.data.DataRepository;
import com.example.roman.bakingapp.data.model.Ingredient;
import com.example.roman.bakingapp.ui.main.MainActivity;

import java.util.List;

import javax.inject.Inject;

import dagger.android.AndroidInjection;

public class ListWidgetService extends RemoteViewsService {
    @Inject
    Context mContext;
    @Inject
    DataRepository mRepository;

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        AndroidInjection.inject(this);
        Log.d("Widget_log", "RemoteViewsFactory launched");
        if (intent != null) {
            int recipeId = intent.getIntExtra(MainActivity.EXTRA_RECIPE_ID, 0);
            Log.d("Widget_log", "recieved intent to show ingredients for recipe No " + recipeId);
            return new ListRemoteViewsFactory(mContext, mRepository, recipeId);
        } else return null;
    }
}

class ListRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    Context mContext;
    DataRepository mRepository;
    int mRecipeId;
    List<Ingredient> mIngredients;

    public ListRemoteViewsFactory(Context context, DataRepository repository, int recipeId) {
        mContext = context;
        mRepository = repository;
        mRecipeId = recipeId;

    }

    @Override
    public void onCreate() {
    }

    //called on start and when notifyAppWidgetViewDataChanged is called
    @Override
    public void onDataSetChanged() {
        // Get all plant info ordered by creation time
        //TODO: transfer call to ViewModel or Repository?
        mIngredients = mRepository.getRecipeByIdOffline(mRecipeId).getIngredients();
    }

    @Override
    public void onDestroy() {
    }

    @Override
    public int getCount() {
        if (mIngredients == null) return 0;
        return mIngredients.size();
    }

    /**
     * This method acts like the onBindViewHolder method in an Adapter
     *
     * @param position The current position of the item in the GridView to be displayed
     * @return The RemoteViews object to display for the provided postion
     */
    @Override
    public RemoteViews getViewAt(int position) {
        if (mIngredients == null || mIngredients.size() == 0) return null;
        Ingredient currentIngredient = mIngredients.get(position);

                String ingredientString = mContext.getResources().getString(R.string.widget_ingredient,
                currentIngredient.getIngredient(),
                Double.toString(currentIngredient.getQuantity()),
                currentIngredient.getMeasure().toLowerCase());

        RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.ingredients_widget);

        views.setTextViewText(R.id.widget_ingredient, ingredientString);

        Bundle extras = new Bundle();
        extras.putInt(MainActivity.EXTRA_RECIPE_ID, mRecipeId);
        Intent fillInIntent = new Intent();
        fillInIntent.putExtras(extras);
        views.setOnClickFillInIntent(R.id.widget_list_item_layout, fillInIntent);


        return views;

    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1; // Treat all items in the GridView the same
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}

