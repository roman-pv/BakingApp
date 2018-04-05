package com.example.roman.bakingapp.ui.detail;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.roman.bakingapp.R;

import dagger.android.AndroidInjection;

public class DetailActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
    }

}
