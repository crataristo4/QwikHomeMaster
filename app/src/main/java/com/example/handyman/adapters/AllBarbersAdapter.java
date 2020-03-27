package com.example.handyman.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.handyman.R;
import com.example.handyman.databinding.LayoutListItemsBinding;
import com.example.handyman.models.ServicePerson;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class AllBarbersAdapter extends FirebaseRecyclerAdapter<ServicePerson,
        AllBarbersAdapter.AllBarbersViewHolder> {
    private Context mContext;

    public AllBarbersAdapter(@NonNull FirebaseRecyclerOptions<ServicePerson> options, Context context) {
        super(options);
        mContext = context;

    }

    @Override
    protected void onBindViewHolder(@NonNull AllBarbersViewHolder allBarbersViewHolder,
                                    int i, @NonNull ServicePerson servicePerson) {

        allBarbersViewHolder.cardView.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.fade_scale_animation));
        allBarbersViewHolder.listItemsServicesBinding.setServiceType(servicePerson);


    }

    @NonNull
    @Override
    public AllBarbersViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutListItemsBinding listItemsServicesBinding = DataBindingUtil.inflate
                (LayoutInflater.from(viewGroup.getContext()),
                        R.layout.layout_list_items, viewGroup, false);

        return new AllBarbersViewHolder(listItemsServicesBinding);
    }

    static class AllBarbersViewHolder extends RecyclerView.ViewHolder {

        LayoutListItemsBinding listItemsServicesBinding;
        CardView cardView;

        AllBarbersViewHolder(@NonNull LayoutListItemsBinding listItemsServicesBinding) {
            super(listItemsServicesBinding.getRoot());

            this.listItemsServicesBinding = listItemsServicesBinding;
            cardView = listItemsServicesBinding.mMaterialCard;
        }
    }
}
