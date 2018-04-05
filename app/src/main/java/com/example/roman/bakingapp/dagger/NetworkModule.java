package com.example.roman.bakingapp.dagger;

import android.content.Context;

import com.example.roman.bakingapp.data.remote.RecipesApi;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class NetworkModule {

    @Provides
    Gson provideGson() {
        return new GsonBuilder()
                .create();
    }

    @Provides
    Retrofit provideRetrofit(Gson gson) {
        return new Retrofit.Builder()
                .baseUrl(RecipesApi.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }

    @Provides
    RecipesApi provideMovieDatabaseApi(Retrofit retrofit) {
        return retrofit.create(RecipesApi.class);
    }

    @Provides
    Picasso providePicasso(Context context) {
        return new Picasso.Builder(context)
                .build();
    }
}
