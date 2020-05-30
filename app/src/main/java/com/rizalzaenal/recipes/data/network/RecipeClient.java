package com.rizalzaenal.recipes.data.network;

import com.rizalzaenal.recipes.data.model.Recipe;
import io.reactivex.Single;
import java.util.List;
import retrofit2.http.GET;

public interface RecipeClient {
    @GET("topher/2017/May/59121517_baking/baking.json")
    Single<List<Recipe>> getRecipes();

}
