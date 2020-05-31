package com.rizalzaenal.recipes.ui.recipedetail;

import android.content.Intent;
import android.util.Log;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.core.app.NavUtils;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
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
        Log.i("RecipeDetailActivity", "Oncreate Called!");
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
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (getResources().getBoolean(R.bool.is_sw600)){
            getSupportFragmentManager().beginTransaction()
              .add(R.id.fragment_recipe, RecipeDetailFragment.newInstance())
              .add(R.id.fragment_step, StepFragment.newInstance(viewModel.getRecipe().getSteps().get(0)))
              .commit();
            viewModel.onNewStep(viewModel.getRecipe().getSteps().get(0));
        }else {
            getSupportFragmentManager().beginTransaction()
              .add(R.id.fragment_container, RecipeDetailFragment.newInstance())
              .commit();
        }

        viewModel.openStep.observe(this, step -> {
            if (getResources().getBoolean(R.bool.is_sw600)){
                viewModel.onNewStep(step);
            }else {
                getSupportFragmentManager().beginTransaction()
                  .replace(R.id.fragment_container, StepFragment.newInstance(step))
                  .addToBackStack("fragment-step")
                  .commit();
                viewModel.onNewStep(step);
            }

        });

    }

    @Override public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    //@Override public void onBackPressed() {
    //    if (getSupportFragmentManager().getBackStackEntryCount() > 0){
    //        getSupportFragmentManager().popBackStack();
    //    }else {
    //        super.onBackPressed();
    //    }
    //
    //}
}