package com.rizalzaenal.recipes.ui.recipedetail;

import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import com.rizalzaenal.recipes.R;
import com.rizalzaenal.recipes.data.model.Recipe;
import com.rizalzaenal.recipes.data.network.RetrofitClient;
import com.rizalzaenal.recipes.databinding.ActivityRecipeDetailBinding;
import io.reactivex.disposables.CompositeDisposable;

public class RecipeDetailActivity extends AppCompatActivity {
    public static final String EXTRA_RECIPE = "EXTRA_RECIPE";

    ActivityRecipeDetailBinding binding;
    RecipeDetailViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_recipe_detail);

        viewModel = new ViewModelProvider(this, new ViewModelProvider.Factory() {
            @NonNull @Override public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
                RecipeDetailViewModel viewModel =
                  new RecipeDetailViewModel(RetrofitClient.getInstance().getRecipeClient(),
                    new CompositeDisposable());
                return (T) viewModel;
            }
        }).get(RecipeDetailViewModel.class);


        if (viewModel.getRecipe() == null) {
            Intent intent = getIntent();
            Recipe recipe = intent.getParcelableExtra(EXTRA_RECIPE);
            viewModel.setRecipe(recipe);
        }

        getSupportActionBar().setTitle(viewModel.getRecipe().getName());

        if (getResources().getBoolean(R.bool.is_sw600)){
            getSupportFragmentManager().beginTransaction()
              .add(R.id.fragment_recipe, RecipeDetailFragment.newInstance())
              .commit();
        }else {
            getSupportFragmentManager().beginTransaction()
              .add(R.id.fragment_container, RecipeDetailFragment.newInstance())
              .commit();
        }


    }
}