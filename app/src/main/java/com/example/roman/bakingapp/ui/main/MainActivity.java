package com.example.roman.bakingapp.ui.main;

import android.appwidget.AppWidgetManager;
import android.arch.lifecycle.ViewModelProviders;
import android.content.ComponentName;
import android.content.Intent;
import android.content.res.Configuration;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.widget.Toast;

import com.example.roman.bakingapp.IngredientsWidgetProvider;
import com.example.roman.bakingapp.IngredientsWidgetService;
import com.example.roman.bakingapp.R;
import com.example.roman.bakingapp.databinding.ActivityMainBinding;
import com.example.roman.bakingapp.ui.ViewModelFactory;
import com.example.roman.bakingapp.ui.detail.StepsActivity;
import com.squareup.picasso.Picasso;

import javax.inject.Inject;

import dagger.android.AndroidInjection;

public class MainActivity extends AppCompatActivity
        implements RecipesAdapter.RecipesAdapterOnItemClickHandler {

    public final static String EXTRA_RECIPE_ID = "com.example.roman.bakingapp.extra.recipe_id";

    private MainActivityViewModel mViewModel;
    private ActivityMainBinding mBinding;


    private RecipesAdapter mAdapter;
    private GridLayoutManager mLayoutManager;

    @Inject
    ViewModelFactory mFactory;

    @Inject
    Picasso mPicasso;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        mViewModel = ViewModelProviders.of(this, mFactory)
                .get(MainActivityViewModel.class);

        setupRecipesAdapter();

        mViewModel.getRecipes().observe(this, recipes -> {
            if (recipes != null && recipes.size() > 0) {
                mAdapter.swapRecipesList(recipes);
            }
        });

    }

    @Override
    public void onItemClick(int id) {
        Intent recipesDetailsIntent = new Intent(this, StepsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt(EXTRA_RECIPE_ID, id);
        recipesDetailsIntent.putExtras(bundle);
        startActivity(recipesDetailsIntent);
    }

    @Override
    public void onWidgetButtonClick(int id) {
        mViewModel.setRecipeIdPreference(id);
        IngredientsWidgetService.startActionUpdateIngredientsWidgets(this);
        Toast.makeText(this, "List of ingredients is added to a widget", Toast.LENGTH_LONG).show();
    }

    private void setupRecipesAdapter() {
        boolean isTablet = getResources().getBoolean(R.bool.isTablet);
        if (isTablet) {
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                mLayoutManager = new GridLayoutManager(this, 2);
            } else {
                mLayoutManager = new GridLayoutManager(this, 3);
            }
        } else {
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                mLayoutManager = new GridLayoutManager(this, 1);
            } else {
                mLayoutManager = new GridLayoutManager(this, 2);
            }
        }
        mBinding.recyclerViewRecipes.setLayoutManager(mLayoutManager);

        mAdapter = new RecipesAdapter(this, mPicasso);
        mAdapter.setOnItemClickHandler(this);

        mBinding.recyclerViewRecipes.setAdapter(mAdapter);

    }
}
