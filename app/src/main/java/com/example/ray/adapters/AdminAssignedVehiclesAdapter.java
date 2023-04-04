package com.example.ray.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ray.R;
import com.example.ray.interfaces.RecyclerViewInterface;
import com.example.ray.models.vehiclesModel;

import java.util.ArrayList;

public class AdminAssignedVehiclesAdapter extends RecyclerView.Adapter<AdminAssignedVehiclesAdapter.MyViewHolder> implements RecyclerViewInterface {

    Context context;
    ArrayList<vehiclesModel> vehiclesList;
    private final RecyclerViewInterface recyclerViewInterface;

    public AdminAssignedVehiclesAdapter(Context context, ArrayList<vehiclesModel> vehiclesList, RecyclerViewInterface recyclerViewInterface) {
        this.context = context;
        this.vehiclesList = vehiclesList;
        this.recyclerViewInterface = recyclerViewInterface;
    }

    @NonNull
    @Override
    public AdminAssignedVehiclesAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_assigned_vehicles_admin_layout, parent, false);
        return new AdminAssignedVehiclesAdapter.MyViewHolder(view, recyclerViewInterface);

    }

    @Override
    public void onBindViewHolder(@NonNull AdminAssignedVehiclesAdapter.MyViewHolder holder, int position) {


        holder.model4.setText(vehiclesList.get(position).getModel());
        holder.plate4.setText(vehiclesList.get(position).getPlate());
        holder.driver4.setText(vehiclesList.get(position).getDriver());

    }

    @Override
    public int getItemCount() {

        return vehiclesList.size();
    }

    @Override
    public void onItemClick(int position) {

    }

    //grab the views from the layout file
    public static class MyViewHolder extends RecyclerView.ViewHolder {

        //initialize variables from the recyclerview layout

        TextView model4,plate4,driver4;
        public MyViewHolder(@NonNull View itemView, RecyclerViewInterface recyclerViewInterface) {
            super(itemView);

            model4 = itemView.findViewById(R.id.model4TV);
            plate4 = itemView.findViewById(R.id.plate4TV);
            driver4 = itemView.findViewById(R.id.driver4TV);



            //attach onclick listener
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(recyclerViewInterface != null){

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
