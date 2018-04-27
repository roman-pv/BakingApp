package com.example.roman.bakingapp.ui.detail;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.roman.bakingapp.R;
import com.example.roman.bakingapp.dagger.Injectable;
import com.example.roman.bakingapp.data.model.Ingredient;
import com.example.roman.bakingapp.data.model.Step;
import com.example.roman.bakingapp.databinding.FragmentStepsBinding;
import com.example.roman.bakingapp.ui.main.MainActivity;

import java.util.List;

import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;

/**
 *
 */
public class StepsFragment extends Fragment
        implements Injectable, StepsAdapter.StepsAdapterOnItemClickHandler {

    private StepsAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;

    private FragmentStepsBinding mBinding;

    private int mRecipeId;

    private static final String NESTED_SCROLL_POSITION_KEY = "scroll_position";
    private int mNestedScrollPosition;

    @Inject
    public ViewModelProvider.Factory mFactory;

    StepsViewModel mViewModel;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        mBinding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_steps, container, false);
        View view = mBinding.getRoot();

        setupStepsAdapter();

        if (savedInstanceState != null) {
            mNestedScrollPosition = savedInstanceState.getInt(NESTED_SCROLL_POSITION_KEY);
        }

        if (getArguments() != null) {
            mRecipeId = getArguments().getInt(MainActivity.EXTRA_RECIPE_ID);
            mViewModel = ViewModelProviders.of(this, mFactory)
                    .get(StepsViewModel.class);

            mViewModel.getRecipe(mRecipeId).observe(this, recipe -> {
                mAdapter.swapStepsList(recipe.getSteps());

                ((AppCompatActivity)getActivity()).getSupportActionBar()
                        .setTitle(recipe.getName());

                List<Ingredient> ingredients = recipe.getIngredients();
                String ingredientsString = "";
                for (Ingredient ingredient : ingredients) {
                    ingredientsString += "\u25BA " + ingredient.getIngredient() + ": " +
                            ingredient.getQuantity() + " " +
                            ingredient.getMeasure().toLowerCase() + "\n";
                }
                mBinding.ingredientsTitleTextView.setText(ingredientsString);

                mBinding.nestedScrollView.post(() -> {
                    mBinding.nestedScrollView.scrollTo(0, mNestedScrollPosition);
                });

            });
        }

        mBinding.ingredientsTitleTextView.setOnClickListener((View v) -> {
            Bundle bundle = new Bundle();
            bundle.putInt("step_num", RecipeDetailsFragment.INGREDIENTS_VIEW);
            bundle.putInt(MainActivity.EXTRA_RECIPE_ID, mRecipeId);

            if (getResources().getBoolean(R.bool.isTablet)) {
                RecipeDetailsFragment newFragment = new RecipeDetailsFragment();
                newFragment.setArguments(bundle);
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.steps_details_fragment, newFragment)
                        .commit();
            } else {
                Intent intent = new Intent(getActivity(), RecipeDetailsActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        return view;

    }

    private void setupStepsAdapter() {

        mLayoutManager = new LinearLayoutManager(getContext());
        mBinding.recyclerViewSteps.setLayoutManager(mLayoutManager);

        mAdapter = new StepsAdapter(getContext());
        mAdapter.setOnItemClickHandler(this);

        mBinding.recyclerViewSteps.setAdapter(mAdapter);

        mBinding.recyclerViewSteps.setFocusable(false);

    }

    @Override
    public void onItemClick(Step step) {
        Bundle bundle = new Bundle();
        bundle.putInt("step_num", step.getId());
        bundle.putInt(MainActivity.EXTRA_RECIPE_ID, mRecipeId);

        if (getResources().getBoolean(R.bool.isTablet)) {
            RecipeDetailsFragment newFragment = new RecipeDetailsFragment();
            newFragment.setArguments(bundle);
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.steps_details_fragment, newFragment)
                    .commit();
        } else {
            Intent intent = new Intent(getActivity(), RecipeDetailsActivity.class);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(NESTED_SCROLL_POSITION_KEY, mBinding.nestedScrollView.getScrollY());


    }
}
