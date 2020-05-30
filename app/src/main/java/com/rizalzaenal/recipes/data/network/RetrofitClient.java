package com.rizalzaenal.recipes.data.network;

import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static RetrofitClient instance;
    private static final String BASE_URL = "https://d17h27t6h515a5.cloudfront.net/";

    private RecipeClient recipeClient;

    private RetrofitClient(){
        buildRetrofit();
    }

    public static RetrofitClient getInstance(){
        if (instance == null){
            instance = new RetrofitClient();
        }
        return instance;
    }

    private void buildRetrofit(){
        OkHttpClient client = new OkHttpClient.Builder()
          .connectTimeout(30, TimeUnit.SECONDS)
          .readTimeout(30, TimeUnit.SECONDS)
          .addInterceptor(new HttpLoggingInterceptor())
          .build();

        Retrofit retrofit = new Retrofit.Builder()
          .baseUrl(BASE_URL)
          .client(client)
          .addConverterFactory(GsonConverterFactory.create())
          .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
          .build();

        recipeClient = retrofit.create(RecipeClient.class);
    }

    public RecipeClient getRecipeClient() {
        return recipeClient;
    }

}
