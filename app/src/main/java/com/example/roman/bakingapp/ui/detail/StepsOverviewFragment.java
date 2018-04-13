package com.example.roman.bakingapp.ui.detail;

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
import com.example.roman.bakingapp.data.model.Recipe;
import com.example.roman.bakingapp.data.model.Step;
import com.example.roman.bakingapp.databinding.FragmentStepsOverviewBinding;
import com.example.roman.bakingapp.ui.main.MainActivity;

import java.util.ArrayList;

import dagger.android.support.AndroidSupportInjection;

/**
 *
 */
public class StepsOverviewFragment extends Fragment
        implements StepsAdapter.StepsAdapterOnItemClickHandler {

    private StepsAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;

    private FragmentStepsOverviewBinding mBinding;

    private Recipe mRecipe;


    @Override
    public void onAttach(Context context) {
        AndroidSupportInjection.inject(this);
        super.onAttach(context);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        mBinding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_steps_overview, container, false);
        View view = mBinding.getRoot();

        setupStepsAdapter();

        if (getArguments() != null) {
            mRecipe = getArguments().getParcelable(MainActivity.EXTRA_RECIPE);
            mAdapter.swapStepsList(mRecipe.getSteps());

            mBinding.recipeTitleTextView.setText(mRecipe.getName());
        }

        mBinding.ingredientsTitleTextView.setOnClickListener((View v) -> {
            Intent intent = new Intent(getActivity(), StepDetailActivity.class);
            Bundle bundle = new Bundle();
            bundle.putInt("step_num", StepsDetailsFragment.INGREDIENTS_VIEW);
            bundle.putParcelable(MainActivity.EXTRA_RECIPE, mRecipe);
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

        Intent intent = new Intent(getActivity(), StepDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("step_num", step.getId());
        bundle.putParcelable(MainActivity.EXTRA_RECIPE, mRecipe);
        intent.putExtras(bundle);
        startActivity(intent);

    }
}
