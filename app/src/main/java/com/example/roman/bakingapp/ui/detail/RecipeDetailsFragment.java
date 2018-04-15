package com.example.roman.bakingapp.ui.detail;

import android.annotation.SuppressLint;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.res.Configuration;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.roman.bakingapp.R;
import com.example.roman.bakingapp.data.model.Ingredient;
import com.example.roman.bakingapp.data.model.RecipeWithStepsAndIngredients;
import com.example.roman.bakingapp.data.model.Step;
import com.example.roman.bakingapp.databinding.FragmentRecipeDetailsBinding;
import com.example.roman.bakingapp.ui.ViewModelFactory;
import com.example.roman.bakingapp.ui.main.MainActivity;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;

import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;

/**
 * Some lines of code, related to ExoPlayer, were taken from this lesson:
 * https://codelabs.developers.google.com/codelabs/exoplayer-intro
 */
public class RecipeDetailsFragment extends Fragment {

    private FragmentRecipeDetailsBinding mBinding;

    public static final int INGREDIENTS_VIEW = -1;
    private static final String PLAYBACK_POSITION_KEY =
            "com.example.roman.bakingapp.ui.detail.playback_position";
    private static final String PLAY_WHEN_READY_KEY =
            "com.example.roman.bakingapp.ui.detail.play_when_ready";

    private int mCurrentStepNumber;
    private RecipeWithStepsAndIngredients mRecipe;
    private int mRecipeId;

    private boolean mIsVideo;

    private ExoPlayer mPlayer;
    private long mPlaybackPosition;
    private int mCurrentWindow;
    private boolean mPlayWhenReady = true;

    @Inject
    ViewModelFactory mFactory;

    StepsViewModel mViewModel;



    @Override
    public void onAttach(Context context) {
        AndroidSupportInjection.inject(this);
        super.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mBinding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_recipe_details, container, false);
        View view = mBinding.getRoot();

        mViewModel = ViewModelProviders.of(this, mFactory)
                .get(StepsViewModel.class);

        if (getArguments() != null) {
            mCurrentStepNumber = getArguments().getInt("step_num");
            mRecipeId = getArguments().getInt(MainActivity.EXTRA_RECIPE_ID);
        }

        if (savedInstanceState != null) {
            mPlaybackPosition = savedInstanceState.getLong(PLAYBACK_POSITION_KEY);
            mPlayWhenReady = savedInstanceState.getBoolean(PLAY_WHEN_READY_KEY);
        }



        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        mViewModel.getRecipe(mRecipeId).observe(this, this :: handleRecipe);
    }


    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    public void onStop() {
        super.onStop();
        if (mPlayer != null) {
            releasePlayer();
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        if (mPlayer != null) {
            mPlaybackPosition = mPlayer.getCurrentPosition();
            mPlayWhenReady = mPlayer.getPlayWhenReady();
        }
        outState.putLong(PLAYBACK_POSITION_KEY, mPlaybackPosition);
        outState.putBoolean(PLAY_WHEN_READY_KEY, mPlayWhenReady);

    }

    private void handleRecipe(RecipeWithStepsAndIngredients recipe) {
        mRecipe = recipe;
        checkAndCorrectStepNumber();
        displayCurrentStepInfo();
        if (mIsVideo) {
            mBinding.stepVideoView.setVisibility(View.VISIBLE);
            mBinding.stepVideoPlaceholder.setVisibility(View.GONE);
            if (!getResources().getBoolean(R.bool.isTablet) &&
                    getResources().getConfiguration().orientation
                    == Configuration.ORIENTATION_LANDSCAPE) {
                hideSystemUi();
            }
            initializePlayer();
        }
    }


    private void checkAndCorrectStepNumber() {
        if (mCurrentStepNumber >= mRecipe.getSteps().size()) {
            mCurrentStepNumber = INGREDIENTS_VIEW;
        } else if (mCurrentStepNumber < 0 && mCurrentStepNumber != INGREDIENTS_VIEW) {
            mCurrentStepNumber = mRecipe.getSteps().size() - 1;
        }
    }

    private void displayCurrentStepInfo() {
        if (mCurrentStepNumber == INGREDIENTS_VIEW) {
            mIsVideo = false;
            mBinding.stepVideoView.setVisibility(View.GONE);
            for (Ingredient ingredient : mRecipe.getIngredients()) {
                String line = ingredient.getQuantity() + ingredient.getMeasure() +
                        ": " + ingredient.getIngredient() + "\n\n";
                mBinding.stepDetailedDescription.append(line);
            }
        } else {
            Step step = mRecipe.getSteps().get(mCurrentStepNumber);
            mBinding.stepDetailedDescription.setText(step.getDescription());
            if (step.getVideoUrl() == null || step.getVideoUrl().isEmpty()) {
                mIsVideo = false;
            } else mIsVideo = true;
        }
    }

    private void initializePlayer() {
        if (mPlayer == null) {
            mPlayer = ExoPlayerFactory.newSimpleInstance(
                    new DefaultRenderersFactory(getContext()),
                    new DefaultTrackSelector(), new DefaultLoadControl());
            mBinding.stepVideoView.setPlayer(mPlayer);

            mPlayer.setPlayWhenReady(mPlayWhenReady);

        }

        Uri uri = Uri.parse(mRecipe.getSteps().get(mCurrentStepNumber).getVideoUrl());
        MediaSource mediaSource = buildMediaSourse(uri);
        mPlayer.prepare(mediaSource);
        mPlayer.seekTo(mCurrentWindow, mPlaybackPosition);
    }

    @SuppressLint("InlinedApi")
    private void hideSystemUi() {
        mBinding.stepVideoView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }

    private MediaSource buildMediaSourse(Uri uri) {
        return new ExtractorMediaSource.Factory(
                new DefaultHttpDataSourceFactory("baking-app"))
                .createMediaSource(uri);
    }

    private void releasePlayer() {
        if (mPlayer != null) {
            mPlaybackPosition = mPlayer.getCurrentPosition();
            mCurrentWindow = mPlayer.getCurrentWindowIndex();
            mPlayWhenReady = mPlayer.getPlayWhenReady();
            mPlayer.release();
            mPlayer = null;
        }
    }

}
