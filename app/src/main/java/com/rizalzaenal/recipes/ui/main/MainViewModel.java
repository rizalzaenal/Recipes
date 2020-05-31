package com.rizalzaenal.recipes.ui.main;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.rizalzaenal.recipes.data.model.Recipe;
import com.rizalzaenal.recipes.data.network.RecipeClient;
import com.rizalzaenal.recipes.utils.BasicIdlingResource;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainViewModel extends ViewModel {
    private RecipeClient recipeClient;
    //private CompositeDisposable compositeDisposable;
    private MutableLiveData<List<Recipe>> _recipes = new MutableLiveData<>();
    public LiveData<List<Recipe>> recipes = _recipes;
    private MutableLiveData<String> _error = new MutableLiveData<>();
    public LiveData<String> error = _error;


    public MainViewModel(RecipeClient recipeClient){
        this.recipeClient = recipeClient;
        //this.compositeDisposable = compositeDisposable;
    }

    public void getRecipes(){
        //Disposable disposable = recipeClient.getRecipes()
        //  .subscribeOn(Schedulers.io())
        //  .subscribeWith(new DisposableSingleObserver<List<Recipe>>() {
        //
        //      @Override public void onSuccess(List<Recipe> recipes) {
        //          _recipes.postValue(recipes);
        //      }
        //
        //      @Override public void onError(Throwable e) {
        //          _error.postValue(e.getMessage());
        //      }
        //  });
        //compositeDisposable.add(disposable);

        recipeClient.getRecipes()
          .enqueue(new Callback<List<Recipe>>() {
              @Override
              public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                  _recipes.postValue(response.body());
                  BasicIdlingResource.setIdleResourceTo(true);
              }

              @Override public void onFailure(Call<List<Recipe>> call, Throwable t) {
                  _error.postValue(t.getMessage());
              }
          });
    }

    @Override protected void onCleared() {
        //compositeDisposable.dispose();
        super.onCleared();
    }
}
