package com.rizalzaenal.recipes.ui.recipedetail;

import android.os.Bundle;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.rizalzaenal.recipes.R;
import com.rizalzaenal.recipes.data.model.Ingredient;
import com.rizalzaenal.recipes.data.network.RetrofitClient;
import com.rizalzaenal.recipes.databinding.FragmentRecipeDetailBinding;
import io.reactivex.disposables.CompositeDisposable;
import java.util.List;

public class RecipeDetailFragment extends Fragment {
    RecipeDetailViewModel viewModel;
    FragmentRecipeDetailBinding binding;
    StepAdapter adapter;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public RecipeDetailFragment() {
        // Required empty public constructor
    }

    public static RecipeDetailFragment newInstance() {
        RecipeDetailFragment fragment = new RecipeDetailFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding = FragmentRecipeDetailBinding.bind(view);

        viewModel = new ViewModelProvider(getActivity(), new ViewModelProvider.Factory() {
            @NonNull @Override public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
                RecipeDetailViewModel viewModel =
                  new RecipeDetailViewModel(RetrofitClient.getInstance().getRecipeClient(),
                    new CompositeDisposable());
                return (T) viewModel;
            }
        }).get(RecipeDetailViewModel.class);

        setupIngredientText();
        setupRecyclerView();
    }

    private void setupRecyclerView(){
        adapter = new StepAdapter(step -> {
            //Toast.makeText(getContext(), step.getShortDescription(), Toast.LENGTH_SHORT).show();
            viewModel.onClickStep(step);
        });

        adapter.setSteps(viewModel.getRecipe().getSteps());
        binding.rvSteps.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rvSteps.setAdapter(adapter);
    }

    private void setupIngredientText() {
        StringBuilder builder = new StringBuilder();
        List<Ingredient> ingredients = viewModel.getRecipe().getIngredients();
        for (int i = 0; i < ingredients.size(); i++) {

            int stepNumber = i + 1;
            Ingredient current = ingredients.get(i);
            builder.append(
              stepNumber + ". " + current.getQuantity() + " " + current.getMeasure() + " " + current
                .getIngredient() + "\n");
        }
        binding.ingredients.setText(builder.toString());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_recipe_detail, container, false);
    }

    @Override public void onDestroyView() {
        binding = null;
        super.onDestroyView();
    }
}