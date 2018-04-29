package com.example.roman.bakingapp.ui.main;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.res.Configuration;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.roman.bakingapp.R;
import com.example.roman.bakingapp.dagger.Injectable;
import com.example.roman.bakingapp.databinding.FragmentRecipesBinding;
import com.example.roman.bakingapp.ui.detail.StepsActivity;
import com.example.roman.bakingapp.widget.IngredientsWidgetService;
import com.squareup.picasso.Picasso;

import javax.inject.Inject;

import static com.example.roman.bakingapp.RecipeUtilities.EXTRA_RECIPE_ID;

public class RecipesFragment extends Fragment
        implements Injectable, RecipesAdapter.RecipesAdapterOnItemClickHandler {

    private static final String RECYCLER_STATE_KEY = "grid_state";
    private static final int SPAN_COUNT_TABLET_LANDSCAPE = 3;
    private static final int SPAN_COUNT_TABLET_PORTRAIT = 2;
    private static final int SPAN_COUNT_PHONE_LANDSCAPE = 2;
    private static final int SPAN_COUNT_PHONE_PORTRAIT = 1;

    @Inject
    public ViewModelProvider.Factory mFactory;
    @Inject
    Picasso mPicasso;
    private RecipesViewModel mViewModel;
    private FragmentRecipesBinding mBinding;
    private RecipesAdapter mAdapter;
    private GridLayoutManager mLayoutManager;
    private Parcelable mRecyclerState;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_recipes, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this, mFactory)
                .get(RecipesViewModel.class);

        setupRecipesAdapter();

        mViewModel.getRecipes().observe(this, recipes -> {
            if (recipes != null && recipes.size() > 0) {
                mBinding.loadingIndicator.setVisibility(View.GONE);
                mBinding.recyclerViewRecipes.setVisibility(View.VISIBLE);
                mAdapter.swapRecipesList(recipes);
                if (savedInstanceState != null) {
                    mRecyclerState = savedInstanceState.getParcelable(RECYCLER_STATE_KEY);
                    mLayoutManager.onRestoreInstanceState(mRecyclerState);
                }
            }
        });

    }

    @Override
    public void onItemClick(int id) {
        Intent recipesDetailsIntent = new Intent(getActivity(), StepsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt(EXTRA_RECIPE_ID, id);
        recipesDetailsIntent.putExtras(bundle);
        startActivity(recipesDetailsIntent);
    }

    @Override
    public void onWidgetButtonClick(int id) {
        mViewModel.setRecipeIdPreference(id);
        IngredientsWidgetService.startActionUpdateIngredientsWidgets(getContext());
        Toast.makeText(getContext(), "List of ingredients is added to a widget", Toast.LENGTH_LONG).show();
    }

    private void setupRecipesAdapter() {

        int spanCount = getSpanCountForGridLayoutManager();

        mLayoutManager = new GridLayoutManager(getContext(), spanCount);

        mBinding.recyclerViewRecipes.setLayoutManager(mLayoutManager);

        mAdapter = new RecipesAdapter(getContext(), mPicasso);
        mAdapter.setOnItemClickHandler(this);

        mBinding.recyclerViewRecipes.setAdapter(mAdapter);
    }

    private int getSpanCountForGridLayoutManager() {
        boolean isTablet = getResources().getBoolean(R.bool.isTablet);
        if (isTablet) {
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                return SPAN_COUNT_TABLET_PORTRAIT;
            } else {
                return SPAN_COUNT_TABLET_LANDSCAPE;
            }
        } else {
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                return SPAN_COUNT_PHONE_PORTRAIT;
            } else {
                return SPAN_COUNT_PHONE_LANDSCAPE;
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mRecyclerState = mLayoutManager.onSaveInstanceState();
        outState.putParcelable(RECYCLER_STATE_KEY, mRecyclerState);
    }

}
