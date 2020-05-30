package com.rizalzaenal.recipes.ui.main;

import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.rizalzaenal.recipes.R;
import com.rizalzaenal.recipes.data.network.RetrofitClient;
import com.rizalzaenal.recipes.databinding.ActivityMainBinding;
import io.reactivex.disposables.CompositeDisposable;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    MainViewModel viewModel;
    RecipeAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        adapter = new RecipeAdapter(recipe -> {
            Toast.makeText(this, recipe.getName(), Toast.LENGTH_SHORT).show();
        });

        viewModel = new ViewModelProvider(this, new ViewModelProvider.Factory() {
            @NonNull @Override public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
                MainViewModel viewModel =
                  new MainViewModel(RetrofitClient.getInstance().getRecipeClient(),
                    new CompositeDisposable());
                return (T) viewModel;
            }
        }).get(MainViewModel.class);

        viewModel.getRecipes();
        setupObservers();

        if (getResources().getBoolean(R.bool.isTablet)){
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