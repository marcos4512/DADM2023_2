package com.unal.registroempresas.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.unal.registroempresas.ActualizarRegistroActivity;
import com.unal.registroempresas.R;
import com.unal.registroempresas.VerRegistroActivity;
import com.unal.registroempresas.model.Company;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ListaCompanyAdapter extends RecyclerView.Adapter<ListaCompanyAdapter.CompanyViewHolder> {

    ArrayList<Company> companyList;
    ArrayList<Company> originalCompanyList;

    public ListaCompanyAdapter(ArrayList<Company> companyList){
        this.companyList = companyList;
        originalCompanyList = new ArrayList<>();
        originalCompanyList.addAll(companyList);
    }

    @NonNull
    @Override
    public CompanyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.company_list_design, null, false);
        return new CompanyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListaCompanyAdapter.CompanyViewHolder holder, int position) {
        holder.nameText.setText(companyList.get(position).getName());
        holder.urlText.setText(companyList.get(position).getUrl());
        holder.phoneText.setText(companyList.get(position).getPhone());
        holder.emailText.setText(companyList.get(position).getEmail());
        holder.productText.setText(companyList.get(position).getProductService());
        holder.clasificationTex.setText(companyList.get(position).getClassification());
    }

    @Override
    public int getItemCount() {
        return companyList.size();
    }

    public class CompanyViewHolder extends RecyclerView.ViewHolder {

        TextView nameText, urlText, phoneText, emailText, productText, clasificationTex;
        public CompanyViewHolder(@NonNull View itemView) {
            super(itemView);
            nameText = itemView.findViewById(R.id.nameText);
            urlText = itemView.findViewById(R.id.urlText);
            phoneText = itemView.findViewById(R.id.phoneText);
            emailText = itemView.findViewById(R.id.emailText);
            productText = itemView.findViewById(R.id.productText);
            clasificationTex = itemView.findViewById(R.id.clasificationText);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Context context = view.getContext();
                    Intent intent = new Intent(context, VerRegistroActivity.class);
                    intent.putExtra("ID", companyList.get(getAdapterPosition()).getId());
                    context.startActivity(intent);
                }
            });
        }
    }

    public void filter(String searchingText){
        if (searchingText.length() == 0){
            companyList.clear();
            companyList.addAll(originalCompanyList);
        } else {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                List<Company> collection = companyList.stream().filter(i -> i.getName().toLowerCase().contains(searchingText.toLowerCase())).collect(Collectors.toList());
                companyList.clear();
                companyList.addAll(collection);
            } else {
                for (Company cpny : originalCompanyList) {
                    if (cpny.getName().toLowerCase().contains(searchingText.toLowerCase())) {
                        companyList.add(cpny);
                    }
                }
            }
        }
        notifyDataSetChanged();
    }
}
