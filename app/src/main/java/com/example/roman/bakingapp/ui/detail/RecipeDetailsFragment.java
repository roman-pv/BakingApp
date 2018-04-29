package com.example.roman.bakingapp.ui.detail;

import android.annotation.SuppressLint;
import android.arch.lifecycle.ViewModelProvider;
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
import com.example.roman.bakingapp.RecipeUtilities;
import com.example.roman.bakingapp.dagger.Injectable;
import com.example.roman.bakingapp.data.model.RecipeWithStepsAndIngredients;
import com.example.roman.bakingapp.data.model.Step;
import com.example.roman.bakingapp.databinding.FragmentRecipeDetailsBinding;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;

import javax.inject.Inject;

/**
 * Some lines of code, related to ExoPlayer, were taken from this lesson:
 * https://codelabs.developers.google.com/codelabs/exoplayer-intro
 */
public class RecipeDetailsFragment extends Fragment implements Injectable {

    public static final int INGREDIENTS_VIEW = -1;
    private static final String PLAYBACK_POSITION_KEY =
            "com.example.roman.bakingapp.ui.detail.playback_position";
    private static final String PLAY_WHEN_READY_KEY =
            "com.example.roman.bakingapp.ui.detail.play_when_ready";
    @Inject
    ViewModelProvider.Factory mFactory;
    StepsViewModel mViewModel;
    private FragmentRecipeDetailsBinding mBinding;
    private int mCurrentStepNumber;
    private RecipeWithStepsAndIngredients mRecipe;
    private int mRecipeId;
    private boolean mIsTablet;
    private boolean mIsVideo;
    private ExoPlayer mPlayer;
    private long mPlaybackPosition;
    private int mCurrentWindow;
    private boolean mPlayWhenReady = true;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mBinding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_recipe_details, container, false);
        View view = mBinding.getRoot();

        mIsTablet = getResources().getBoolean(R.bool.isTablet);

        mViewModel = ViewModelProviders.of(this, mFactory)
                .get(StepsViewModel.class);

        if (getArguments() != null) {
            mCurrentStepNumber = getArguments().getInt(RecipeUtilities.EXTRA_STEP_NUMBER);
            mRecipeId = getArguments().getInt(RecipeUtilities.EXTRA_RECIPE_ID);
        }

        if (savedInstanceState != null) {
            mPlaybackPosition = savedInstanceState.getLong(PLAYBACK_POSITION_KEY);
            mPlayWhenReady = savedInstanceState.getBoolean(PLAY_WHEN_READY_KEY);
        }

        if (!mIsTablet && getResources().getConfiguration().orientation
                == Configuration.ORIENTATION_PORTRAIT) {
            mBinding.previousImageView.setOnClickListener((View v) -> {
                int stepNumber = mCurrentStepNumber - 1;
                changeStep(stepNumber);
            });
            mBinding.previousStepTextView.setOnClickListener((View v) -> {
                int stepNumber = mCurrentStepNumber - 1;
                changeStep(stepNumber);
            });
            mBinding.nextImageView.setOnClickListener((View v) -> {
                int stepNumber = mCurrentStepNumber + 1;
                changeStep(stepNumber);
            });
            mBinding.nextStepTextView.setOnClickListener((View v) -> {
                int stepNumber = mCurrentStepNumber + 1;
                changeStep(stepNumber);
            });
        }

        return view;
    }

    private void changeStep(int newStepNumber) {
        Bundle bundle = new Bundle();
        bundle.putInt(RecipeUtilities.EXTRA_STEP_NUMBER, newStepNumber);
        bundle.putInt(RecipeUtilities.EXTRA_RECIPE_ID, mRecipeId);
        RecipeDetailsFragment stepsFragment = new RecipeDetailsFragment();
        stepsFragment.setArguments(bundle);
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.steps_details_fragment, stepsFragment)
                .commit();
    }

    @Override
    public void onStart() {
        super.onStart();
        mViewModel.getRecipe(mRecipeId).observe(this, this::handleRecipe);
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
            if (!mIsTablet &&
                    getResources().getConfiguration().orientation
                            == Configuration.ORIENTATION_LANDSCAPE) {
                hideSystemUi();
                mBinding.stepDetailedDescription.setVisibility(View.GONE);
            }
            initializePlayer();
        } else {
            mBinding.noVideoIconImageView.setVisibility(View.VISIBLE);
        }

    }


    private void checkAndCorrectStepNumber() {
        if (mCurrentStepNumber >= mRecipe.getSteps().size()) {
            mCurrentStepNumber = 0;
        } else if (mCurrentStepNumber < 0) {
            mCurrentStepNumber = mRecipe.getSteps().size() - 1;
        }
    }

    private void displayCurrentStepInfo() {
        Step step = mRecipe.getSteps().get(mCurrentStepNumber);
        if (mIsTablet || getResources().getConfiguration().orientation
                == Configuration.ORIENTATION_PORTRAIT) {
            mBinding.stepDetailedDescription.setText(
                    RecipeUtilities.clearDescriptionString(step.getDescription()));
        }
        if (!mIsTablet && getResources().getConfiguration().orientation
                == Configuration.ORIENTATION_PORTRAIT) {
            String currentStepCount = "";

            if (mCurrentStepNumber != 0) {
                currentStepCount = getActivity().getResources()
                        .getString(R.string.step_count,
                                mCurrentStepNumber, mRecipe.getSteps().size() - 1);
            } else {
                currentStepCount = getActivity().getString(R.string.introduction_step);
            }

            mBinding.stepsNavigationStepCount.setText(currentStepCount);
        }
        mIsVideo = isVideo(step);
    }

    private boolean isVideo(Step step) {
        boolean isVideo = step.getVideoUrl() != null && !step.getVideoUrl().isEmpty();
        String thumbnailUrl = step.getThumbnailUrl();
        if (thumbnailUrl != null && !thumbnailUrl.isEmpty()) {
            if (!isVideo) {
                isVideo = RecipeUtilities.isVideoUrl(thumbnailUrl);
            }
        }
        return isVideo;
    }

    private void initializePlayer() {
        if (mPlayer == null) {
            mPlayer = ExoPlayerFactory.newSimpleInstance(
                    new DefaultRenderersFactory(getContext()),
                    new DefaultTrackSelector(), new DefaultLoadControl());
            mBinding.stepVideoView.setPlayer(mPlayer);

            mPlayer.setPlayWhenReady(mPlayWhenReady);

        }

        String videoUrl = mRecipe.getSteps().get(mCurrentStepNumber).getVideoUrl();
        if (videoUrl.isEmpty()) videoUrl = mRecipe.getSteps().get(mCurrentStepNumber).getThumbnailUrl();
        Uri uri = Uri.parse(videoUrl);
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
