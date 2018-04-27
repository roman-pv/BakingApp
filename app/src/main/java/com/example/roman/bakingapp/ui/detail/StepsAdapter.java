package com.example.roman.bakingapp.ui.detail;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.roman.bakingapp.R;
import com.example.roman.bakingapp.data.model.Step;
import com.example.roman.bakingapp.databinding.RecyclerViewStepItemBinding;

import java.util.List;

public class StepsAdapter extends RecyclerView.Adapter<StepsAdapter.StepsAdapterViewHolder> {
    private Context mContext;
    private List<Step> mSteps;
    private StepsAdapter.StepsAdapterOnItemClickHandler mClickHandler;

    public StepsAdapter(Context context) {
        this.mContext = context;
    }

    public void setOnItemClickHandler(StepsAdapter.StepsAdapterOnItemClickHandler onItemClickHandler) {
        this.mClickHandler = onItemClickHandler;
    }


    @Override
    public StepsAdapter.StepsAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(
                R.layout.recycler_view_step_item, viewGroup, false);
        view.setFocusable(true);
        return new StepsAdapter.StepsAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(StepsAdapter.StepsAdapterViewHolder holder, int position) {

        Step currentStep = mSteps.get(position);

        String shortDescription = currentStep.getShortDescription();
        shortDescription = shortDescription.replaceAll("[.]$", "");

        holder.binding.stepDescriptionTextView.setText(shortDescription);

        if (position == 0) {
            holder.binding.circle.setVisibility(View.INVISIBLE);
        }



    }

    @Override
    public int getItemCount() {
        if (null == mSteps) return 0;
        else return mSteps.size();
    }

    void swapStepsList(List<Step> newSteps) {
        mSteps = newSteps;
        notifyDataSetChanged();
    }

    /**
     * The interface that receives onReviewItemClick messages.
     */
    public interface StepsAdapterOnItemClickHandler {
        void onItemClick(Step step);
    }

    class StepsAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final RecyclerViewStepItemBinding binding;

        public StepsAdapterViewHolder(View view) {
            super(view);
            binding = DataBindingUtil.bind(view);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            Step step = mSteps.get(adapterPosition);
            mClickHandler.onItemClick(step);
        }
    }
}
