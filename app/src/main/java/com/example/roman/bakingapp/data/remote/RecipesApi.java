package com.example.roman.bakingapp.data.remote;

import com.example.roman.bakingapp.data.model.Recipe;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Allows Retrofit to get access to the server to get JSON files with recipes
 */
public interface RecipesApi {

    String BASE_URL = "http://go.udacity.com/";

    @GET("android-baking-app-json")
    Call<List<Recipe>> getRecipe();
}
