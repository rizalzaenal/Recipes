package com.rizalzaenal.recipes.ui.main;

import android.content.Intent;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.test.espresso.IdlingResource;
import com.rizalzaenal.recipes.R;
import com.rizalzaenal.recipes.data.network.RetrofitClient;
import com.rizalzaenal.recipes.databinding.ActivityMainBinding;
import com.rizalzaenal.recipes.ui.recipedetail.RecipeDetailActivity;
import com.rizalzaenal.recipes.utils.BasicIdlingResource;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    MainViewModel viewModel;
    RecipeAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BasicIdlingResource.setIdleResourceTo(false);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        adapter = new RecipeAdapter(recipe -> {
            //Toast.makeText(this, recipe.getName(), Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, RecipeDetailActivity.class);
            intent.putExtra(RecipeDetailActivity.EXTRA_RECIPE, recipe);
            startActivity(intent);
        });

        viewModel = new ViewModelProvider(this, new ViewModelProvider.Factory() {
            @NonNull @Override public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
                MainViewModel viewModel =
                  new MainViewModel(RetrofitClient.getInstance().getRecipeClient());
                return (T) viewModel;
            }
        }).get(MainViewModel.class);

        viewModel.getRecipes();
        setupObservers();

        if (getResources().getBoolean(R.bool.is_sw600)){
            binding.rvMain.setLayoutManager(new GridLayoutManager(this, 3));
        }else {
            binding.rvMain.setLayoutManager(new LinearLayoutManager(this));
        }
        binding.rvMain.setAdapter(adapter);

    }

    private void setupObservers(){
        viewModel.recipes.observe(this, recipes -> {
            adapter.setRecipes(recipes);
        });

        viewModel.error.observe(this, s -> {
            Toast.makeText(this, s, Toast.LENGTH_LONG).show();
        });
    }
}