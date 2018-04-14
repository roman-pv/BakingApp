package com.example.roman.bakingapp.data;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.util.Log;

import com.example.roman.bakingapp.AppExecutors;
import com.example.roman.bakingapp.data.local.RecipeDao;
import com.example.roman.bakingapp.data.local.RecipeDatabase;
import com.example.roman.bakingapp.data.model.Recipe;
import com.example.roman.bakingapp.data.model.RecipeWithStepsAndIngredients;
import com.example.roman.bakingapp.data.remote.RecipesApi;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@Singleton
public class DataRepository {

    private final RecipesApi mApi;
    private final RecipeDao mDao;
    private final RecipeDatabase mDatabase;
    private final AppExecutors mAppExecutors;

    @Inject
    public DataRepository(RecipesApi api, RecipeDao dao, RecipeDatabase database,
                          AppExecutors appExecutors) {
        this.mApi = api;
        this.mDao = dao;
        this.mDatabase = database;
        this.mAppExecutors = appExecutors;

        MutableLiveData<List<Recipe>> networkData = new MutableLiveData<>();

        mApi.getRecipe()
                .enqueue(new Callback<List<Recipe>>() {
                    @Override
                    public void onResponse(Call<List<Recipe>> call,
                                           Response<List<Recipe>> response) {
                        networkData.setValue(response.body());
                    }

                    @Override
                    public void onFailure(Call<List<Recipe>> call, Throwable t) {
                        Log.d("DOWNLOAD", "" + t);
                    }
                });

        networkData.observeForever(newRecipesFromNetwork -> {
            mAppExecutors.diskIO().execute(() -> {
                // Insert our new recipes data into the database
                mDao.bulkInsert(newRecipesFromNetwork);
            });
        });
    }

    public LiveData<List<RecipeWithStepsAndIngredients>> getRecipes() {
        return mDao.getAllRecipes();
    }

    public LiveData<RecipeWithStepsAndIngredients> getRecipeById(int id) {
        return mDao.getRecipeById(id);
    }


}
