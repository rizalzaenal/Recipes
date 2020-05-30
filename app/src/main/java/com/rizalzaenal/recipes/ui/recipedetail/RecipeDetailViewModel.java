package com.rizalzaenal.recipes.ui.recipedetail;

import androidx.lifecycle.ViewModel;
import com.rizalzaenal.recipes.data.model.Recipe;
import com.rizalzaenal.recipes.data.network.RecipeClient;
import io.reactivex.disposables.CompositeDisposable;

public class RecipeDetailViewModel extends ViewModel {
    private RecipeClient recipeClient;
    private CompositeDisposable compositeDisposable;
    private Recipe recipe;

    public RecipeDetailViewModel(RecipeClient recipeClient, CompositeDisposable compositeDisposable){
        this.recipeClient = recipeClient;
        this.compositeDisposable = compositeDisposable;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }

    public Recipe getRecipe() {
        return recipe;
    }

    @Override protected void onCleared() {
        compositeDisposable.dispose();
        super.onCleared();
    }
}
