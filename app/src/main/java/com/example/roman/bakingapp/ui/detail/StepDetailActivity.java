package com.example.roman.bakingapp.ui.detail;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.roman.bakingapp.R;
import com.example.roman.bakingapp.data.model.Recipe;
import com.example.roman.bakingapp.ui.main.MainActivity;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;

public class StepDetailActivity extends AppCompatActivity
implements HasSupportFragmentInjector {

    @Inject
    DispatchingAndroidInjector<Fragment> fragmentDispatchingAndroidInjector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_detail);

        Recipe recipe = getIntent().getParcelableExtra(MainActivity.EXTRA_RECIPE);
        int stepNum = getIntent().getIntExtra("step_num", 0);

        Bundle bundle = new Bundle();
        bundle.putParcelable(MainActivity.EXTRA_RECIPE, recipe);
        bundle.putInt("step_num", stepNum);

        StepsDetailsFragment stepsFragment = new StepsDetailsFragment();
        stepsFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.steps_details_fragment, stepsFragment)
                .commit();
    }

    @Override
    public AndroidInjector<Fragment> supportFragmentInjector() {
        return fragmentDispatchingAndroidInjector;
    }
}
