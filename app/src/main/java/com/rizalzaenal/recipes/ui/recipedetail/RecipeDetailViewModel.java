package com.rizalzaenal.recipes.ui.recipedetail;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.rizalzaenal.recipes.data.model.Recipe;
import com.rizalzaenal.recipes.data.model.Step;
import com.rizalzaenal.recipes.data.network.RecipeClient;

public class RecipeDetailViewModel extends ViewModel {
    private RecipeClient recipeClient;
    //private CompositeDisposable compositeDisposable;
    private Recipe recipe;
    private MutableLiveData<Step> _openStep = new MutableLiveData<>();
    public LiveData<Step> openStep = _openStep;
    public MutableLiveData<Step> currentStep = new MutableLiveData<>();

    public RecipeDetailViewModel(RecipeClient recipeClient){
        this.recipeClient = recipeClient;
       // this.compositeDisposable = compositeDisposable;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }

    public Recipe getRecipe() {
        return recipe;
    }

    public void onClickStep(Step step){
        _openStep.postValue(step);
    }

    public void onNewStep(Step step){
        currentStep.postValue(step);
    }

    @Override protected void onCleared() {
       // compositeDisposable.dispose();
        super.onCleared();
    }
}
