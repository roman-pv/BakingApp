package com.example.roman.bakingapp.data;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.util.Log;

import com.example.roman.bakingapp.data.model.Recipe;
import com.example.roman.bakingapp.data.remote.RecipesApi;

import java.util.List;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DataRepository {

    private final RecipesApi mApi;

    @Inject
    public DataRepository(RecipesApi api) {
        this.mApi = api;
    }

    public LiveData<List<Recipe>> getRecipes() {

        final MutableLiveData<List<Recipe>> recipes = new MutableLiveData<>();

            mApi.getRecipe()
                    .enqueue(new Callback<List<Recipe>>() {
                        @Override
                        public void onResponse(Call<List<Recipe>> call,
                                               Response<List<Recipe>> response) {
                            recipes.setValue(response.body());
                        }

                        @Override
                        public void onFailure(Call<List<Recipe>> call, Throwable t) {
                            Log.d("DOWNLOAD", "" + t);
                        }
                    });

        return recipes;
    }


}
