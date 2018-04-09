package com.example.roman.bakingapp.ui.detail;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.roman.bakingapp.R;
import com.example.roman.bakingapp.data.model.Recipe;
import com.example.roman.bakingapp.data.model.Step;
import com.example.roman.bakingapp.databinding.FragmentStepsDetailsBinding;
import com.example.roman.bakingapp.ui.main.MainActivity;

import dagger.android.support.AndroidSupportInjection;

/**
 *
 */
public class StepsDetailsFragment extends Fragment {

    private FragmentStepsDetailsBinding mBinding;

    private int mCurrentStepNumber;


    @Override
    public void onAttach(Context context) {
        AndroidSupportInjection.inject(this);
        super.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mBinding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_steps_details, container, false);
        View view = mBinding.getRoot();

        if (getArguments() != null) {
            Recipe recipe = getArguments().getParcelable(MainActivity.EXTRA_RECIPE);
            mCurrentStepNumber = getArguments().getInt("step_num");


            if (mCurrentStepNumber > recipe.getSteps().size()) {
                mCurrentStepNumber = 0;
            } else if (mCurrentStepNumber < 0) {
                mCurrentStepNumber = recipe.getSteps().size();
            }

            Step step = recipe.getSteps().get(mCurrentStepNumber);
            mBinding.stepDetailedDescription.setText(step.getDescription());
        }

        return view;
    }
}
