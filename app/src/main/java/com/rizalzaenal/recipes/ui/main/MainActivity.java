package com.rizalzaenal.recipes.ui.main;

import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.rizalzaenal.recipes.R;
import com.rizalzaenal.recipes.data.network.RetrofitClient;
import com.rizalzaenal.recipes.databinding.ActivityMainBinding;
import io.reactivex.disposables.CompositeDisposable;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    MainViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

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

        binding.rvMain.setLayoutManager(new LinearLayoutManager(this));


    }

    private void setupObservers(){
        viewModel.recipes.observe(this, recipes -> {
            Toast.makeText(this, recipes.toString(), Toast.LENGTH_LONG).show();
        });

        viewModel.error.observe(this, s -> {
            Toast.makeText(this, s, Toast.LENGTH_LONG).show();
        });
    }
}