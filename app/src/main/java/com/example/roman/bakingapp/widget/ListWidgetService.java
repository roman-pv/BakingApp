package com.example.roman.bakingapp.widget;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.roman.bakingapp.R;
import com.example.roman.bakingapp.RecipeUtilities;
import com.example.roman.bakingapp.data.DataRepository;
import com.example.roman.bakingapp.data.model.Ingredient;
import com.example.roman.bakingapp.ui.main.MainActivity;

import java.util.List;

import javax.inject.Inject;

import dagger.android.AndroidInjection;

/**
 * Handles listView for a widget.
 */
public class ListWidgetService extends RemoteViewsService {
    @Inject
    Context mContext;
    @Inject
    DataRepository mRepository;

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        AndroidInjection.inject(this);
        if (intent != null) {
            int recipeId = intent.getIntExtra(RecipeUtilities.EXTRA_RECIPE_ID, 0);
            return new ListRemoteViewsFactory(mContext, mRepository, recipeId);
        } else return null;
    }
}

/**
 * List Adapter for a widget listView.
 *
 */
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
     * @param position The current position of the item in the ListView to be displayed
     * @return The RemoteViews object to display for the provided position
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
        extras.putInt(RecipeUtilities.EXTRA_RECIPE_ID, mRecipeId);
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
        return 1; // Treat all items the same
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

