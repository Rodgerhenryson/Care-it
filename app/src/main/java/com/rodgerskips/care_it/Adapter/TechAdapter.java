package com.rodgerskips.care_it.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.rodgerskips.care_it.Admin.fragment.AssignUploadsFragmentAdmin;
import com.rodgerskips.care_it.Models.History;
import com.rodgerskips.care_it.Models.Tech;
import com.rodgerskips.care_it.R;
import com.rodgerskips.care_it.constants.Contants;
import com.rodgerskips.care_it.constants.PrefManager;
import com.rodgerskips.care_it.constants.RequestHandler;
import com.rodgerskips.care_it.utils.Prevalent;
import com.rodgerskips.care_it.utils.Utils;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;

/**
 * Created by Kiduyu klaus
 * on 30/09/2020 19:24 2020
 */
public class TechAdapter extends RecyclerView.Adapter<TechAdapter.MyViewHolder> {
    Context mcontext;
    private ArrayList<Tech> techArrayList;

    public TechAdapter(Context context, ArrayList<Tech> cList) {
        this.techArrayList = cList;
        this.mcontext = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(mcontext).inflate(R.layout.tech_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull TechAdapter.MyViewHolder holder, int position) {
        Tech tech = techArrayList.get(position);
        holder.email.setText("Email: " + tech.getEmail());
        holder.phone.setText("Phone: " + tech.getPhone());
        holder.status.setText("Status: " + tech.getStatus());
        holder.name.setText("Status: " + tech.getName());

        holder.itemView.setOnClickListener(view -> {

            AssignUpload(tech.getName(), holder.pb, holder.rb, holder.progressBar);
        });
    }

    private void AssignUpload(String name, LinearLayout pb, LinearLayout rb, ProgressBar progressBar) {
        rb.setVisibility(View.GONE);
        pb.setVisibility(View.VISIBLE);
        progressBar.setIndeterminate(true);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Contants.URL_SET_UPLOAD,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        Utils.ShowToast(mcontext,"Successfully Assigned "+name);
                       History history= new History("","","","","","","","","");
                       Prevalent.currentUploadToAssign=history;
                        ((AppCompatActivity)mcontext).getSupportFragmentManager()
                                .beginTransaction().replace(R.id.admin_fragment_container, new AssignUploadsFragmentAdmin())
                                .addToBackStack(null).commit();


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        //Dismissing the progress dialog
                        // loading.dismiss();
                        pb.setVisibility(View.GONE);
                        rb.setVisibility(View.VISIBLE);
                        Log.d("TAG", "onResponse: " + String.valueOf(volleyError.getMessage()));
                        //Showing toast
                        PrefManager.Toast(mcontext, String.valueOf(volleyError.getMessage()));
                        //Toast.makeText(MainActivity.this, volleyError.getMessage().toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //Converting Bitmap to String
                String uploadToAssignId = Prevalent.currentUploadToAssign.getId();

                String Techname = name;

                Map<String, String> params = new Hashtable<String, String>();

                //Adding parameters
                params.put("uploadid", uploadToAssignId);
                params.put("techname", Techname);

                //returning parameters
                return params;
            }
        };

        //Creating a Request Queue
        RequestHandler.getInstance(mcontext).addToRequestQueue(stringRequest);
    }

    @Override
    public int getItemCount() {
        return techArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name, phone, email, status;
        ProgressBar progressBar;
        LinearLayout pb, rb;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.assign_techname);
            phone = itemView.findViewById(R.id.assign_techphone);
            email = itemView.findViewById(R.id.assign_techemail);
            status = itemView.findViewById(R.id.assign_techstatus);
            progressBar = itemView.findViewById(R.id.pd_tech_item);
            pb = itemView.findViewById(R.id.ll_pb);
            rb = itemView.findViewById(R.id.ll_rv);
        }
    }
}
