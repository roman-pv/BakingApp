package com.example.roman.bakingapp.data.local;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.roman.bakingapp.R;

import javax.inject.Inject;

public class RecipeIdSharedPreferences {

    private SharedPreferences mSharedPreferences;
    private Context mContext;

    @Inject
    public RecipeIdSharedPreferences(SharedPreferences sharedPreferences, Context context) {
        this.mSharedPreferences = sharedPreferences;
        this.mContext = context;
    }

    public int getRecipeIdPreference() {
        return mSharedPreferences.getInt(mContext.getString(R.string.pref_recipe_id_key), 0);
    }

    public void setRecipeIdPreference(int recipeId) {
        mSharedPreferences.edit()
                .putInt(mContext.getString(R.string.pref_recipe_id_key), recipeId)
                .apply();
    }
}
