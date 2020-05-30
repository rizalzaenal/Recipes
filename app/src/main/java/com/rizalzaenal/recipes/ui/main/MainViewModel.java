package com.rizalzaenal.recipes.ui.main;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.rizalzaenal.recipes.data.model.Recipe;
import com.rizalzaenal.recipes.data.network.RecipeClient;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import java.util.List;

public class MainViewModel extends ViewModel {
    private RecipeClient recipeClient;
    private CompositeDisposable compositeDisposable;
    private MutableLiveData<List<Recipe>> _recipes = new MutableLiveData<>();
    public LiveData<List<Recipe>> recipes = _recipes;
    private MutableLiveData<String> _error = new MutableLiveData<>();
    public LiveData<String> error = _error;


    public MainViewModel(RecipeClient recipeClient, CompositeDisposable compositeDisposable){
        this.recipeClient = recipeClient;
        this.compositeDisposable = compositeDisposable;
    }

    public void getRecipes(){
        Disposable disposable = recipeClient.getRecipes()
          .subscribeOn(Schedulers.io())
          .subscribeWith(new DisposableSingleObserver<List<Recipe>>() {

              @Override public void onSuccess(List<Recipe> recipes) {
                  _recipes.postValue(recipes);
              }

              @Override public void onError(Throwable e) {
                  _error.postValue(e.getMessage());
              }
          });
        compositeDisposable.add(disposable);
    }

    @Override protected void onCleared() {
        compositeDisposable.dispose();
        super.onCleared();
    }
}
