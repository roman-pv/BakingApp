package com.example.roman.bakingapp.ui.detail;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.roman.bakingapp.R;
import com.example.roman.bakingapp.data.model.Ingredient;
import com.example.roman.bakingapp.data.model.Recipe;
import com.example.roman.bakingapp.data.model.Step;
import com.example.roman.bakingapp.databinding.FragmentStepsDetailsBinding;
import com.example.roman.bakingapp.ui.main.MainActivity;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import dagger.android.support.AndroidSupportInjection;

/**
 *
 */
public class StepsDetailsFragment extends Fragment {

    private FragmentStepsDetailsBinding mBinding;

    public static final int INGREDIENTS_VIEW = -1;
    private static final String PLAYBACK_POSITION_KEY =
            "com.example.roman.bakingapp.ui.detail.playback_position";

    private int mCurrentStepNumber;
    private Recipe mRecipe;

    private boolean mIsVideo;

    private ExoPlayer mPlayer;
    private long mPlaybackPosition;
    private int mCurrentWindow;
    private boolean mPlayWhenReady = true;


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
            mRecipe = getArguments().getParcelable(MainActivity.EXTRA_RECIPE);
            mCurrentStepNumber = getArguments().getInt("step_num");

            checkAndCorrectStepNumber();
            displayCurrentStepInfo();

            if (mIsVideo) {
                mBinding.stepVideoView.setVisibility(View.VISIBLE);
                mBinding.stepVideoPlaceholder.setVisibility(View.GONE);

                if (savedInstanceState != null) {
                    mPlaybackPosition = savedInstanceState.getLong(PLAYBACK_POSITION_KEY);
                }
            }
        }
        return view;
    }

    private void checkAndCorrectStepNumber() {
        if (mCurrentStepNumber >= mRecipe.getSteps().size()) {
            mCurrentStepNumber = INGREDIENTS_VIEW;
        } else if (mCurrentStepNumber < 0 && mCurrentStepNumber != INGREDIENTS_VIEW) {
            mCurrentStepNumber = mRecipe.getSteps().size() - 1;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (Util.SDK_INT > 23) {
            if (mIsVideo) {
                initializePlayer();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (Util.SDK_INT <= 23) {
            if (mIsVideo) {
                initializePlayer();
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT <= 23) {
            releasePlayer();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23) {
            releasePlayer();
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putLong(PLAYBACK_POSITION_KEY, mPlaybackPosition);
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
