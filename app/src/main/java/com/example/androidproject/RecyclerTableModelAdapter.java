package com.example.androidproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerTableModelAdapter extends RecyclerView.Adapter<RecyclerTableModelAdapter.ViewHolder>{
    private ArrayList<Table> tables;
    public RecyclerTableModelAdapter(ArrayList<Table> tables){
       this.tables = tables;
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private model_table_view view;
        public ViewHolder(model_table_view view) {
            super(view);
            this.view=view;
        }
        public model_table_view getTableView(){
            return view;
        }
    }
    @NonNull
    @Override
    public RecyclerTableModelAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        model_table_view cell = new model_table_view(parent.getContext(),null);
        ViewHolder viewHolder = new ViewHolder(cell);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerTableModelAdapter.ViewHolder holder, int position) {
        final Table table = tables.get(position);
        if (table==null){
            holder.getTableView().setIsNull(true);
            return;

        }
        holder.getTableView().setIsNull(false);
        holder.getTableView().setIsFull(table.isFull());
        holder.getTableView().setIsSmoke(table.isSmoke());
        holder.getTableView().setSeatsNumber(table.getSeats());
        holder.getTableView().setTableNumber(table.getId());
    }

    @Override
    public int getItemCount() {
        return tables.size();
    }
}
