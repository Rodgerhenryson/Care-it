package com.rodgerskips.care_it.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.rodgerskips.care_it.Models.History;
import com.rodgerskips.care_it.R;
import com.rodgerskips.care_it.constants.PrefManager;
import com.rodgerskips.care_it.customer.fragments.FragmentHistoryDetails;
import com.rodgerskips.care_it.technician.fragments.TechDetailsFragment;

import java.util.ArrayList;

/**
 * Created by Kiduyu klaus
 * on 18/09/2020 12:40 2020
 */
public class PendingAdapter extends RecyclerView.Adapter<PendingAdapter.MyViewHolder> {

    Context mcontext;
    ArrayList<History> historyArrayList;


    public PendingAdapter(Context context, ArrayList<History> tList) {
        this.historyArrayList = tList;
        this.mcontext = context;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(mcontext).inflate(R.layout.history_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        History model = historyArrayList.get(position);
        holder.single_application_timeago.setText("less than a day ago");
        holder.single_application_county.setText("The Admin");
        holder.single_application_username.setText(PrefManager.getInstance(mcontext).getUser().getName());
        holder.single_application_school.setText("from : "+model.getLocation());
        holder.edt_myappications_descri11.setText("An Upload to The Admin was set on "+model.getReg_date()+"\nThe Description of the upload was"+model.getDescription());

        Log.d("TAG", "onBindViewHolder: "+model.getImage());
        holder.next.setOnClickListener(view ->

                PassArguments(model.getUser(),model.getDescription(),
                        model.getLocation(),
                        model.getLatitude(),
                        model.getLongitude(),
                        model.getImage(),
                        model.getReg_date(),
                        model.getStatus())
                );


    }

    private void PassArguments(String user, String description, String location, String latitude, String longitude, String image, String reg_date, String status) {

        TechDetailsFragment f = new TechDetailsFragment();
        Bundle args = new Bundle();
        args.putString("user",user);
        args.putString("description",description);
        args.putString("location",location);
        args.putString("latitude",latitude);
        args.putString("longitude",longitude);
        args.putString("image",image);
        args.putString("reg_date",reg_date);
        args.putString("status",status);
        Log.d("TAG", "PassArguments: "+image);
        f.setArguments(args);


        ((AppCompatActivity)mcontext).getSupportFragmentManager()
                .beginTransaction().replace(R.id.tech_fragment_container, f)
                .addToBackStack(null).commit();
    }

    @Override
    public int getItemCount() {
        return historyArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView single_application_username, single_application_county, single_application_school, single_application_timeago, edt_myappications_descri11;
        ImageView single_application_image,next;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            single_application_image = itemView.findViewById(R.id.single_application_user);
            single_application_username = itemView.findViewById(R.id.single_application_username);
            single_application_county = itemView.findViewById(R.id.single_application_county);
            single_application_school = itemView.findViewById(R.id.single_application_school);
            single_application_timeago = itemView.findViewById(R.id.single_application_timeago);
            edt_myappications_descri11 = itemView.findViewById(R.id.edt_myappications_descri11);
            next = itemView.findViewById(R.id.view_more_arrow);

        }
    }
}
