package com.example.ray.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ray.R;
import com.example.ray.activities.AdminAssignDriverVehicle;
import com.example.ray.interfaces.RecyclerViewInterface;
import com.example.ray.models.driversModel;

import java.util.ArrayList;

public class AdminDriversAdapter extends RecyclerView.Adapter<AdminDriversAdapter.driversViewHolder> {

    ArrayList<driversModel> driversList;
    Context context;

    private final RecyclerViewInterface recyclerViewInterface;



    public AdminDriversAdapter(ArrayList<driversModel> driversList, Context context, RecyclerViewInterface recyclerViewInterface) {
        this.driversList = driversList;
        this.context = context;
        this.recyclerViewInterface = recyclerViewInterface;
    }



    @NonNull
    @Override
    public driversViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_drivers_admin_layout, parent, false);
        return new driversViewHolder(view, recyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull driversViewHolder holder, int position) {

        driversModel driver = driversList.get(position);
        holder.t1.setText(driversList.get(position).getFullName());
        holder.t2.setText(driversList.get(position).getEmail());
        holder.t3.setText(driversList.get(position).getPhoneNumber());

    }

    @Override
    public int getItemCount() {

        return driversList.size();
    }

    public static class driversViewHolder extends RecyclerView.ViewHolder {

        TextView t1, t2, t3;

        public driversViewHolder(@NonNull View itemView, RecyclerViewInterface recyclerViewInterface) {
            super(itemView);

            t1 = itemView.findViewById(R.id.nameET);
            t2 = itemView.findViewById(R.id.emailET);
            t3 = itemView.findViewById(R.id.phoneNumberET);

            //set onclick listener on recyclerview

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(recyclerViewInterface!= null){

                        int position = getAdapterPosition();

                        if(position != RecyclerView.NO_POSITION){
                            recyclerViewInterface.onItemClick(position);
                        }

                    }
                }
            });

        }
    }
}
