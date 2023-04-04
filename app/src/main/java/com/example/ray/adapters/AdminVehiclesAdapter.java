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

public class AdminVehiclesAdapter extends RecyclerView.Adapter<AdminVehiclesAdapter.MyViewHolder> implements RecyclerViewInterface{

    Context context;
    ArrayList<vehiclesModel> vehiclesList;
    private final RecyclerViewInterface recyclerViewInterface;

    //constructor
    public AdminVehiclesAdapter(Context context, ArrayList<vehiclesModel> vehiclesList, RecyclerViewInterface recyclerViewInterface){
        this.context =  context;
        this.vehiclesList = vehiclesList;
        this.recyclerViewInterface = recyclerViewInterface;
    }


    @NonNull
    @Override

    //inflate the layout (Giving a look at our rows)
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_vehicles_admin_layout, parent, false);
        return new MyViewHolder(view, recyclerViewInterface);
    }

    //Assign views created in recycler layout file
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        
        holder.model7.setText(vehiclesList.get(position).getModel());
        holder.plate7.setText(vehiclesList.get(position).getPlate());


    }

    //helps with the number of items in the recyclerView
    @Override
    public int getItemCount() {
        
        return vehiclesList.size();
    }
    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void startListening() {


    }

    public void stopListening() {
    }

    @Override
    public void onItemClick(int position) {

    }

    //grab the views from the layout file
    public static class MyViewHolder extends RecyclerView.ViewHolder {

        //initialize variables from the recyclerview layout

        TextView model7,plate7;
        public MyViewHolder(@NonNull View itemView, RecyclerViewInterface recyclerViewInterface) {
            super(itemView);

            model7 = itemView.findViewById(R.id.modelTV);
            plate7 = itemView.findViewById(R.id.plateTV);



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
