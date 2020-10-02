package com.rodgerskips.care_it.Adapter;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.rodgerskips.care_it.Models.Approve;
import com.rodgerskips.care_it.Models.History;
import com.rodgerskips.care_it.Models.Tech;
import com.rodgerskips.care_it.R;
import com.rodgerskips.care_it.constants.Contants;
import com.rodgerskips.care_it.constants.RequestHandler;
import com.rodgerskips.care_it.utils.Prevalent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Kiduyu klaus
 * on 24/09/2020 18:12 2020
 */
public class AssignAdapter extends RecyclerView.Adapter<AssignAdapter.MyViewHolder> {
    Context mcontext;
    private ArrayList<History> historyArrayList;


    public AssignAdapter(Context context, ArrayList<History> cList) {
        this.historyArrayList = cList;
        this.mcontext = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(mcontext).inflate(R.layout.assign_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull AssignAdapter.MyViewHolder holder, int position) {
        final History assign = historyArrayList.get(position);
        holder.name.setText(assign.getUser());
        holder.assign.setOnClickListener(v -> {
            Prevalent.currentUploadToAssign=assign;
            ChooseTech(assign.getUser(), assign.getDescription());
        });

    }

    private void ChooseTech(String user, String description) {
        final Dialog mDialog = new Dialog(mcontext, R.style.Theme_AppCompat_Translucent);
        mDialog.setContentView(R.layout.assign_dialog);
        ImageView image_rate_close = mDialog.findViewById(R.id.image_close);
        image_rate_close.setOnClickListener(view -> mDialog.dismiss());
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.setCancelable(false);
        RecyclerView recyclerView = mDialog.findViewById(R.id.rv_assign_tech);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(mcontext, RecyclerView.VERTICAL, false));
        ArrayList<Tech> techArrayList = new ArrayList<>();
        recyclerView.setFocusable(false);

        String urlForJsonObject = Contants.URL_GET_TECH;
        ProgressBar progressBar=mDialog.findViewById(R.id.assign_loading);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,
                urlForJsonObject,
                null,
                jsonObject -> {
                    try {
                        JSONArray jsonArray = jsonObject.getJSONArray("Tech");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject tech = jsonArray.getJSONObject(i);

                            String tech_name = tech.getString("name");
                            String tech_email = tech.getString("email");
                            String tech_phone = tech.getString("phone");
                            String tech_description = tech.getString("description");
                            String tech_skills = tech.getString("skills");
                            String tech_status = tech.getString("status");

                            progressBar.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.VISIBLE);
                            techArrayList.add(new Tech(tech_name,tech_email,tech_phone,tech_description,tech_skills,tech_status));

                        }

                        TechAdapter techAdapter = new TechAdapter(mcontext, techArrayList);
                        recyclerView.setAdapter(techAdapter);
                    } catch (JSONException e) {
                        progressBar.setVisibility(View.GONE);
                        e.printStackTrace();
                    }
                }, volleyError -> {
            progressBar.setVisibility(View.GONE);
            volleyError.printStackTrace();
        });

        RequestHandler.getInstance(mcontext).addToRequestQueue(request);
        mDialog.show();
    }

    public void filterList(ArrayList<History> filteredList) {
        historyArrayList = filteredList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return historyArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name, details;
        Button assign;
        Spinner spinner;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.approve_name_assign);
            details = itemView.findViewById(R.id.approve_details);
            assign = itemView.findViewById(R.id.approve_assign);

        }
    }
}
