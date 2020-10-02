package com.rodgerskips.care_it.Adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.rodgerskips.care_it.Admin.home.AdminHomeActivity;
import com.rodgerskips.care_it.Models.Approve;
import com.rodgerskips.care_it.Models.User;
import com.rodgerskips.care_it.R;
import com.rodgerskips.care_it.activities.LogInActivity;
import com.rodgerskips.care_it.constants.Contants;
import com.rodgerskips.care_it.constants.PrefManager;
import com.rodgerskips.care_it.constants.RequestHandler;
import com.rodgerskips.care_it.customer.home.CustomerHomeActivity;
import com.rodgerskips.care_it.technician.home.TechnicianHomeActivity;
import com.rodgerskips.care_it.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Kiduyu klaus
 * on 24/09/2020 13:56 2020
 */
public class ApproveTech extends RecyclerView.Adapter<ApproveTech.MyViewHolder> {

    Context mcontext;
    private ArrayList<Approve> approveArrayList;


    public ApproveTech(Context context, ArrayList<Approve> cList) {
        this.approveArrayList = cList;
        this.mcontext = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(mcontext).inflate(R.layout.approve_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final Approve approve = approveArrayList.get(position);

        holder.name.setText(approve.getName());
        holder.details.setText(
                approve.getName()+" Detailed information \n" +
                        "FullName: " +approve.getName()+"\n"+
                        "Email: " +approve.getEmail()+"\n"+
                        "Phone: " +approve.getPhone()+"\n"+
                        "Status: " +approve.getStatus()+"\n"

        );
        //if (app)
        holder.decline.setOnClickListener(v -> DeclineRequest(approve.getId()));
        holder.approve.setOnClickListener(v -> ApproveRequest(approve.getId()));
       // holder.approve.setOnClickListener();
        //holder.decline.setOnClickListener();

    }

    private void ApproveRequest(String id ) {
        ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(mcontext);
        progressDialog.setTitle("Accepting the Request");
        progressDialog.setMessage("please Wait...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                Contants.URL_ACCEPT, response -> {
                    progressDialog.dismiss();
                    Utils.ShowToast(mcontext,"Successfully Accepted Approval");

                }, error -> {
                    progressDialog.dismiss();

                    Toast.makeText(
                            mcontext,
                            error.getMessage(),
                            Toast.LENGTH_LONG
                    ).show();
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("approveid", id);


                return params;
            }
        };
        RequestHandler.getInstance(mcontext).addToRequestQueue(stringRequest);

    }

    private void DeclineRequest(String id) {
        ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(mcontext);
        progressDialog.setTitle("Decling the Request");
        progressDialog.setMessage("please Wait...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                Contants.URL_DECLINE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                Utils.ShowToast(mcontext,"Successfully declined Approval");

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();

                Toast.makeText(
                        mcontext,
                        error.getMessage(),
                        Toast.LENGTH_LONG
                ).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("approveid", id);


                return params;
            }
        };
        RequestHandler.getInstance(mcontext).addToRequestQueue(stringRequest);

    }


    public void filterList(ArrayList<Approve> filteredList) {
        approveArrayList = filteredList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return approveArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView  name, details;
        Button approve,decline;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.approve_name);
            details = itemView.findViewById(R.id.approve_details);
            approve = itemView.findViewById(R.id.approve_approve);
            decline = itemView.findViewById(R.id.approve_decline);



        }
    }
}
