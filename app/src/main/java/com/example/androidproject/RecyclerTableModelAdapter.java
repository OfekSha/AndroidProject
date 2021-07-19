package com.example.androidproject;

import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerTableModelAdapter extends RecyclerView.Adapter<RecyclerTableModelAdapter.ViewHolder>{
    private ArrayList<Table> tables;
    private Table selectedTable;
    private ViewHolder selectedHolder;
    public Table getSelectedTable(){
        return selectedTable;
    }
    public RecyclerTableModelAdapter(ArrayList<Table> tables){
       this.tables = tables;
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView table_tv;
        private TextView seats_tv;
        private TextView smoke_tv;
        private TextView chair_tv;
        private final int RED= 0xffff0000;
        private final int GREEN= 0xff00ff00;
        private View view;
        public ViewHolder(View view) {
            super(view);
            this.view=view;
            table_tv = (TextView) view.findViewById(R.id.table);
            seats_tv = (TextView) view.findViewById(R.id.seats);
            smoke_tv=(TextView) view.findViewById(R.id.smoke);
            chair_tv=(TextView) view.findViewById(R.id.chair);

        }
        public void isSelected(boolean yes){
            if (yes){
                view.setBackground(ContextCompat.getDrawable(view.getContext(), R.drawable.table_border_selected) );
            }
            else{
                view.setBackground(ContextCompat.getDrawable(view.getContext(), R.drawable.table_border) );
            }
        }
        public View getView() {
            return view;
        }

        public void setTableNumber(int value) {

            table_tv.setText("#"+value);
        }

        public void setIsFull(boolean value) {
            if (value) {
                table_tv.setBackgroundTintList(ColorStateList.valueOf(RED));
                chair_tv.setBackgroundTintList(ColorStateList.valueOf(RED));
                //table_tv.setBackgroundResource(R.drawable.table_red);
                //chair_tv.setBackgroundResource(R.drawable.chair_red);
            }
            else {
                table_tv.setBackgroundTintList(ColorStateList.valueOf(GREEN));
                chair_tv.setBackgroundTintList(ColorStateList.valueOf(GREEN));
               // table_tv.setBackgroundResource(R.drawable.table_green);
                //chair_tv.setBackgroundResource(R.drawable.chair_green);
            }
        }

        public void setIsSmoke(boolean value) {
            if (value) {
                smoke_tv.setVisibility(View.VISIBLE);
            }
            else {
                smoke_tv.setVisibility(View.INVISIBLE);
            }
        }

        public void setSeatsNumber(int value) {
            seats_tv.setText("Seats: "+value);
        }

        public void setIsNull(boolean value) {
            if (value) {
                smoke_tv.setVisibility(View.INVISIBLE);
                table_tv.setVisibility(View.INVISIBLE);
                chair_tv.setVisibility(View.INVISIBLE);
                seats_tv.setText("");
                table_tv.setText("");
            }
            else{
                smoke_tv.setVisibility(View.VISIBLE);
                table_tv.setVisibility(View.VISIBLE);
                chair_tv.setVisibility(View.VISIBLE);
            }
        }
    }
    @NonNull
    @Override
    public RecyclerTableModelAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View cell= layoutInflater.inflate(R.layout.model_table_view, parent, false);
        ViewHolder viewHolder = new ViewHolder(cell);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerTableModelAdapter.ViewHolder holder, int position) {
        final Table table = tables.get(position);
        holder.getView().setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                if (selectedHolder!=null)
                selectedHolder.isSelected(false);
                selectedTable=table;
                selectedHolder=holder;
                selectedHolder.isSelected(true);
            }
        });
        if (table==null){
            holder.setIsNull(true);
        }
        else {
            holder.setIsFull(table.isFull());
            holder.setIsSmoke(table.isSmoke());
            holder.setSeatsNumber(table.getSeats());
            holder.setTableNumber(table.getId());
        }
    }

    @Override
    public int getItemCount() {
        return tables.size();
    }
}
