package com.example.roman.bakingapp.ui.detail;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import com.example.roman.bakingapp.R;
import com.example.roman.bakingapp.data.model.Recipe;
import com.example.roman.bakingapp.data.model.Step;
import com.example.roman.bakingapp.ui.main.MainActivity;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;

public class DetailActivity extends AppCompatActivity implements HasSupportFragmentInjector {

    @Inject
    DispatchingAndroidInjector<Fragment> fragmentDispatchingAndroidInjector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Bundle bundle = getIntent().getExtras();
        StepsOverviewFragment stepsFragment = new StepsOverviewFragment();
        stepsFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.steps_overview_fragment, stepsFragment)
                .commit();

    }

    @Override
    public AndroidInjector<Fragment> supportFragmentInjector() {
        return fragmentDispatchingAndroidInjector;
    }

}
