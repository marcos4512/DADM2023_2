package com.unal.consumidor.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.unal.consumidor.R;
import com.unal.consumidor.model.BornRegistry;

import java.util.ArrayList;

public class BornsAdapter extends RecyclerView.Adapter<BornsAdapter.BornsViewHolder>{

    ArrayList<BornRegistry> bornList;

    public BornsAdapter(ArrayList<BornRegistry> bornList){
        this.bornList = bornList;
    }

    @NonNull
    @Override
    public BornsAdapter.BornsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.data_design, null, false);
        return new BornsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BornsAdapter.BornsViewHolder holder, int position) {
        holder.genderTxt.setText(bornList.get(position).getGender());
        holder.weightTxt.setText(bornList.get(position).getWeight());
        holder.sizeTxt.setText(bornList.get(position).getSize());
        holder.dateTxt.setText(bornList.get(position).getDate());
        holder.momAgeTxt.setText(bornList.get(position).getMomAge());
        holder.dadAgeTxt.setText(bornList.get(position).getDadAge());

    }

    @Override
    public int getItemCount() {
        return bornList.size();
    }

    public class BornsViewHolder extends RecyclerView.ViewHolder {

        TextView genderTxt, weightTxt, sizeTxt, dateTxt, momAgeTxt, dadAgeTxt;
        public BornsViewHolder(@NonNull View itemView) {
            super(itemView);
            genderTxt = itemView.findViewById(R.id.gender_tv);
            weightTxt = itemView.findViewById(R.id.weight_tv);
            sizeTxt = itemView.findViewById(R.id.size_tv);
            dateTxt = itemView.findViewById(R.id.date_tv);
            momAgeTxt = itemView.findViewById(R.id.mom_age_tv);
            dadAgeTxt = itemView.findViewById(R.id.dad_age_tv);
        }
    }
}
