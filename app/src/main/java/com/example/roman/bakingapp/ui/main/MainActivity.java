package com.example.roman.bakingapp.ui.main;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import com.example.roman.bakingapp.R;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;

public class MainActivity extends AppCompatActivity implements HasSupportFragmentInjector {

    public final static String EXTRA_RECIPE_ID = "com.example.roman.bakingapp.extra.recipe_id";

    @Inject
    DispatchingAndroidInjector<Fragment> fragmentDispatchingAndroidInjector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            RecipesFragment recipesFragment = new RecipesFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.recipes_fragment, recipesFragment)
                    .commit();
        }

    }

    @Override
    public AndroidInjector<Fragment> supportFragmentInjector() {
        return fragmentDispatchingAndroidInjector;
    }
}
