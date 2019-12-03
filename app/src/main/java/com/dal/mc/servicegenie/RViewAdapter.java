package com.dal.mc.servicegenie;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RViewAdapter extends RecyclerView.Adapter<RViewAdapter.ViewHolder> {

    ArrayList<Booking> bookingList;
    Context context;

    public RViewAdapter(ArrayList<Booking> bookingList) {
        this.bookingList = bookingList;
    }

    @NonNull
    @Override
    //inflate the layout for the view of every item in the Recycler View
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.booking_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        context = parent.getContext();
        return viewHolder;
    }

    @Override
    //Binding the data on views
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {


        if (bookingList.size() > 0) {
            Booking booking = bookingList.get(position);


            holder.serviceName.setText(booking.getServiceName());
            holder.serviceDateTime.setText(booking.getTimeNDate().getTime().toString());
            holder.serviceProfName.setText(booking.getProfInfo());
            holder.serviceStatus.setText(booking.getStatus());
            holder.serviceCost.setText(booking.getCost().toString());
        }

    }


    @Override
    //number of recyclerview required
    public int getItemCount() {
        return bookingList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView serviceName,serviceDateTime,serviceProfName,serviceStatus,serviceCost;
        Button helpBtn;

        public ViewHolder(View itemView) {
            super(itemView);
            serviceName = (TextView) itemView.findViewById(R.id.serviceName);
            serviceDateTime = (TextView) itemView.findViewById(R.id.serviceDateTime);
            serviceProfName = (TextView) itemView.findViewById(R.id.serviceProfName);
            serviceStatus = (TextView) itemView.findViewById(R.id.serviceStatus);
            serviceCost = (TextView) itemView.findViewById(R.id.serviceCost);
            helpBtn = (Button) itemView.findViewById(R.id.helpBtn);

        }

    }

}
