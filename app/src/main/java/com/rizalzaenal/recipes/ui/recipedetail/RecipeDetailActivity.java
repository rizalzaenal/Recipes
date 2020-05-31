package com.rizalzaenal.recipes.ui.recipedetail;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import com.rizalzaenal.recipes.R;
import com.rizalzaenal.recipes.data.model.Ingredient;
import com.rizalzaenal.recipes.data.model.Recipe;
import com.rizalzaenal.recipes.data.network.RetrofitClient;
import com.rizalzaenal.recipes.databinding.ActivityRecipeDetailBinding;
import com.rizalzaenal.recipes.ui.widget.RecipeWidgetProvider;
import com.rizalzaenal.recipes.utils.PrefsStatic;
import java.util.List;

public class RecipeDetailActivity extends AppCompatActivity {
    public static final String EXTRA_RECIPE = "EXTRA_RECIPE";
    SharedPreferences sharedPreferences;

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
                  new RecipeDetailViewModel(RetrofitClient.getInstance().getRecipeClient());
                return (T) viewModel;
            }
        }).get(RecipeDetailViewModel.class);


        if (viewModel.getRecipe() == null) {
            Intent intent = getIntent();
            Recipe recipe = intent.getParcelableExtra(EXTRA_RECIPE);
            viewModel.setRecipe(recipe);
        }

        if (getSupportActionBar() != null){
            getSupportActionBar().setTitle(viewModel.getRecipe().getName());
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }


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

    @Override public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.widget_add_menu, menu);

        sharedPreferences = getSharedPreferences(PrefsStatic.PREFERENCE_ID, MODE_PRIVATE);
        if (sharedPreferences.getInt(PrefsStatic.PREFERENCE_RECIPE_ID, -1) == viewModel.getRecipe().getId()){
            menu.findItem(R.id.add_to_widget).setIcon(R.drawable.ic_baseline_favorite_24);
        }

        return super.onCreateOptionsMenu(menu);
    }

    @Override public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            onBackPressed();
        }else if (item.getItemId() == R.id.add_to_widget){
            boolean isAlreadyAdded = sharedPreferences.getInt(PrefsStatic.PREFERENCE_RECIPE_ID, -1) == viewModel.getRecipe().getId();

            if (isAlreadyAdded){
                sharedPreferences
                  .edit()
                  .remove(PrefsStatic.PREFERENCE_RECIPE_ID)
                  .remove(PrefsStatic.PREFERENCE_RECIPE_TITLE)
                  .remove(PrefsStatic.PREFERENCE_RECIPE_INGREDIENT)
                  .apply();
                item.setIcon(R.drawable.ic_baseline_favorite_border_24);
                Toast.makeText(this, R.string.recipe_deleted, Toast.LENGTH_SHORT).show();
            }else {
                sharedPreferences
                  .edit()
                  .putInt(PrefsStatic.PREFERENCE_RECIPE_ID, viewModel.getRecipe().getId())
                  .putString(PrefsStatic.PREFERENCE_RECIPE_TITLE, viewModel.getRecipe().getName())
                  .putString(PrefsStatic.PREFERENCE_RECIPE_INGREDIENT, getFormatedIngredientList())
                  .apply();
                item.setIcon(R.drawable.ic_baseline_favorite_24);
                Toast.makeText(this, R.string.recipe_added_to_widget, Toast.LENGTH_SHORT).show();
            }

            ComponentName provider = new ComponentName(this, RecipeWidgetProvider.class);
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
            int[] ids = appWidgetManager.getAppWidgetIds(provider);
            RecipeWidgetProvider bakingWidgetProvider = new RecipeWidgetProvider();
            bakingWidgetProvider.onUpdate(this, appWidgetManager, ids);

        }
        return super.onOptionsItemSelected(item);
    }

    private String getFormatedIngredientList(){
        StringBuilder builder = new StringBuilder();
        List<Ingredient> ingredients = viewModel.getRecipe().getIngredients();

        for (int i = 0; i < ingredients.size(); i++) {

            int stepNumber = i + 1;
            Ingredient current = ingredients.get(i);
            builder.append(
              stepNumber + ". " + current.getQuantity() + " " + current.getMeasure() + " " + current
                .getIngredient() + "\n");
        }

        return builder.toString();
    }

}