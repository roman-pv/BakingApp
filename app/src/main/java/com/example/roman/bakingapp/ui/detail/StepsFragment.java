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
import com.example.roman.bakingapp.RecipeUtilities;
import com.example.roman.bakingapp.dagger.Injectable;
import com.example.roman.bakingapp.data.model.Ingredient;
import com.example.roman.bakingapp.data.model.RecipeWithStepsAndIngredients;
import com.example.roman.bakingapp.data.model.Step;
import com.example.roman.bakingapp.databinding.FragmentStepsBinding;
import com.example.roman.bakingapp.ui.main.MainActivity;
import com.squareup.picasso.Picasso;

import java.util.List;

import javax.inject.Inject;

/**
 *
 */
public class StepsFragment extends Fragment
        implements Injectable, StepsAdapter.StepsAdapterOnItemClickHandler {

    private static final String NESTED_SCROLL_POSITION_KEY = "scroll_position";
    @Inject
    public ViewModelProvider.Factory mFactory;
    @Inject
    Picasso mPicasso;
    StepsViewModel mViewModel;
    private StepsAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    private FragmentStepsBinding mBinding;
    private int mRecipeId;
    private int mNestedScrollPosition;

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
            mRecipeId = getArguments().getInt(RecipeUtilities.EXTRA_RECIPE_ID);
            mViewModel = ViewModelProviders.of(this, mFactory)
                    .get(StepsViewModel.class);

            mViewModel.getRecipe(mRecipeId).observe(this, this::handleResult);
        }

        mBinding.ingredientsTitleTextView.setOnClickListener((View v) -> {
            Bundle bundle = new Bundle();
            bundle.putInt(RecipeUtilities.EXTRA_STEP_NUMBER, RecipeDetailsFragment.INGREDIENTS_VIEW);
            bundle.putInt(RecipeUtilities.EXTRA_RECIPE_ID, mRecipeId);

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

        mAdapter = new StepsAdapter(getContext(), mPicasso);
        mAdapter.setOnItemClickHandler(this);

        mBinding.recyclerViewSteps.setAdapter(mAdapter);

        mBinding.recyclerViewSteps.setFocusable(false);

    }

    private void handleResult(RecipeWithStepsAndIngredients recipe) {

        mAdapter.swapStepsList(recipe.getSteps());

        ((AppCompatActivity) getActivity()).getSupportActionBar()
                .setTitle(recipe.getName());

        List<Ingredient> ingredients = recipe.getIngredients();

        String ingredientsString =
                RecipeUtilities.ingredientsListToString(ingredients);

        mBinding.ingredientsTitleTextView.setText(ingredientsString);

        mBinding.nestedScrollView.post(() -> {
            mBinding.nestedScrollView.scrollTo(0, mNestedScrollPosition);
        });
    }

    @Override
    public void onItemClick(Step step) {
        Bundle bundle = new Bundle();
        bundle.putInt(RecipeUtilities.EXTRA_STEP_NUMBER, step.getId());
        bundle.putInt(RecipeUtilities.EXTRA_RECIPE_ID, mRecipeId);

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
