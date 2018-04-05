package com.example.roman.bakingapp.ui.main;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.roman.bakingapp.R;
import com.example.roman.bakingapp.data.model.Recipe;
import com.example.roman.bakingapp.databinding.RecyclerViewItemBinding;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RecipesAdapter extends RecyclerView.Adapter<RecipesAdapter.RecipesAdapterViewHolder> {

    private Context mContext;
    private List<Recipe> mRecipes;
    private Picasso mPicasso;
    private RecipesAdapterOnItemClickHandler mClickHandler;

    public RecipesAdapter(Context context, Picasso picasso) {
        this.mContext = context;
        this.mPicasso = picasso;
    }

    public void setOnItemClickHandler(RecipesAdapterOnItemClickHandler onItemClickHandler) {
        this.mClickHandler = onItemClickHandler;
    }


    @Override
    public RecipesAdapter.RecipesAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(
                R.layout.recycler_view_item, viewGroup, false);
        view.setFocusable(true);
        return new RecipesAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecipesAdapter.RecipesAdapterViewHolder holder, int position) {

        Recipe currentRecipe = mRecipes.get(position);

        String image = currentRecipe.getImage();
        String recipeName = currentRecipe.getName();
        int numberOfServings = currentRecipe.getServings();
        int numberOfSteps = currentRecipe.getSteps().size();
        String servings = String.format(mContext.getString(R.string.servings), numberOfServings);
        String steps = String.format(mContext.getString(R.string.steps), numberOfSteps);

        if ( image != null && !image.isEmpty()) {
            mPicasso.with(mContext)
                    .load(currentRecipe.getImage())
                    .placeholder(R.drawable.food_placeholder_image)
                    .error(R.drawable.food_placeholder_image)
                    .into(holder.binding.recipeImageView);
        } else {
            mPicasso.with(mContext)
                    .load(R.drawable.food_placeholder_image)
                    .into(holder.binding.recipeImageView);
        }

        holder.binding.recipeTitleTextView.setText(recipeName);
        holder.binding.servingsTextView.setText(mContext.getResources()
                .getQuantityString(R.plurals.servings, numberOfServings, numberOfServings));
        holder.binding.stepsTextView.setText(mContext.getResources()
                .getQuantityString(R.plurals.steps, numberOfSteps, numberOfSteps));
    }

    @Override
    public int getItemCount() {
        if (null == mRecipes) return 0;
        else return mRecipes.size();
    }

    void swapRecipesList(List<Recipe> newRecipes) {
        mRecipes = newRecipes;
        notifyDataSetChanged();
    }

    /**
     * The interface that receives onReviewItemClick messages.
     */
    public interface RecipesAdapterOnItemClickHandler {
        void onItemClick(Recipe recipe);
    }

    class RecipesAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final RecyclerViewItemBinding binding;

        public RecipesAdapterViewHolder(View view) {
            super(view);
            binding = DataBindingUtil.bind(view);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            Recipe recipe = mRecipes.get(adapterPosition);
            mClickHandler.onItemClick(recipe);
        }
    }

}
