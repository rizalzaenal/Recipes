package com.rizalzaenal.recipes.ui.recipedetail;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.rizalzaenal.recipes.data.model.Step;
import com.rizalzaenal.recipes.databinding.ItemStepBinding;
import java.util.ArrayList;
import java.util.List;

public class StepAdapter extends RecyclerView.Adapter<StepAdapter.StepViewHolder> {
    private List<Step> steps = new ArrayList<>();
    ItemStepBinding binding;
    ClickListener listener;

    public StepAdapter(ClickListener listener){
        this.listener = listener;
    }

    @NonNull @Override
    public StepViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = ItemStepBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new StepViewHolder(binding.getRoot());
    }

    @Override public void onBindViewHolder(@NonNull StepViewHolder holder, int position) {
        binding.stepTitle.setText(steps.get(position).getShortDescription());
    }

    @Override public int getItemCount() {
        return steps.size();
    }

    class StepViewHolder extends RecyclerView.ViewHolder {

        public StepViewHolder(@NonNull View itemView) {
            super(itemView);
            binding.getRoot().setOnClickListener(v -> {
                listener.onItemClick(steps.get(getAdapterPosition()));
            });
        }
    }

    public void setSteps(List<Step> steps) {
        this.steps = steps;
        notifyDataSetChanged();
    }

    interface ClickListener {
        void onItemClick(Step step);
    }
}
