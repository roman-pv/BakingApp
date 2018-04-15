package com.example.roman.bakingapp.ui.detail;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.roman.bakingapp.R;
import com.example.roman.bakingapp.data.model.Step;
import com.example.roman.bakingapp.databinding.FragmentStepsBinding;
import com.example.roman.bakingapp.ui.ViewModelFactory;
import com.example.roman.bakingapp.ui.main.MainActivity;

import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;

/**
 *
 */
public class StepsFragment extends Fragment
        implements StepsAdapter.StepsAdapterOnItemClickHandler {

    private StepsAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;

    private FragmentStepsBinding mBinding;

    private int mRecipeId;

    @Inject
    ViewModelFactory mFactory;

    StepsViewModel mViewModel;


    @Override
    public void onAttach(Context context) {
        AndroidSupportInjection.inject(this);
        super.onAttach(context);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        mBinding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_steps, container, false);
        View view = mBinding.getRoot();

        setupStepsAdapter();

        if (getArguments() != null) {
            mRecipeId = getArguments().getInt(MainActivity.EXTRA_RECIPE_ID);
            mViewModel = ViewModelProviders.of(this, mFactory)
                    .get(StepsViewModel.class);
            mViewModel.getRecipe(mRecipeId).observe(this, recipe -> {
                mAdapter.swapStepsList(recipe.getSteps());
                mBinding.recipeTitleTextView.setText(recipe.getName());
                });
        }

        mBinding.ingredientsTitleTextView.setOnClickListener((View v) -> {
            Intent intent = new Intent(getActivity(), RecipeDetailsActivity.class);
            Bundle bundle = new Bundle();
            bundle.putInt("step_num", RecipeDetailsFragment.INGREDIENTS_VIEW);
            bundle.putInt(MainActivity.EXTRA_RECIPE_ID, mRecipeId);
            intent.putExtras(bundle);
            startActivity(intent);
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

        Intent intent = new Intent(getActivity(), RecipeDetailsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("step_num", step.getId());
        bundle.putInt(MainActivity.EXTRA_RECIPE_ID, mRecipeId);
        intent.putExtras(bundle);
        startActivity(intent);

    }
}
