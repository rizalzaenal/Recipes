package com.rizalzaenal.recipes.ui.main;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.rizalzaenal.recipes.R;
import com.rizalzaenal.recipes.data.model.Recipe;
import com.rizalzaenal.recipes.databinding.ItemRecipeBinding;
import java.util.ArrayList;
import java.util.List;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {
    private List<Recipe> recipes = new ArrayList<>();
    private ItemRecipeBinding viewBinding;
    private ClickListener listener;

    public RecipeAdapter(ClickListener listener){
        this.listener = listener;
    }

    @NonNull @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        viewBinding = ItemRecipeBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new RecipeViewHolder(viewBinding.getRoot());
    }

    @Override public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {
        viewBinding.recipeName.setText(recipes.get(position).getName());
        Glide.with(viewBinding.getRoot().getContext())
          .load(recipes.get(position).getImage())
          .placeholder(R.drawable.kitchen)
          .into(viewBinding.ivImage);
    }

    @Override public int getItemCount() {
        return recipes.size();
    }

    class RecipeViewHolder extends RecyclerView.ViewHolder {

        public RecipeViewHolder(@NonNull View itemView) {
            super(itemView);
            viewBinding.getRoot().setOnClickListener(v -> {
                listener.onItemClick(recipes.get(getAdapterPosition()));
            });
        }

    }

    public void setRecipes(List<Recipe> recipes) {
        this.recipes = recipes;
        notifyDataSetChanged();
    }

    interface ClickListener {
        void onItemClick(Recipe recipe);
    }
}
