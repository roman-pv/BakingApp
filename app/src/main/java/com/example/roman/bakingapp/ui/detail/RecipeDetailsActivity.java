package com.example.roman.bakingapp.ui.detail;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.roman.bakingapp.R;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;

public class RecipeDetailsActivity extends AppCompatActivity
implements HasSupportFragmentInjector {

    @Inject
    DispatchingAndroidInjector<Fragment> fragmentDispatchingAndroidInjector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_detail);

        if (savedInstanceState == null) {
            Bundle bundle = getIntent().getExtras();
            RecipeDetailsFragment stepsFragment = new RecipeDetailsFragment();
            stepsFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.steps_details_fragment, stepsFragment)
                    .commit();
        }
    }

    @Override
    public AndroidInjector<Fragment> supportFragmentInjector() {
        return fragmentDispatchingAndroidInjector;
    }
}
