package com.rodgerskips.care_it.Admin.fragment;

import android.app.ProgressDialog;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.rodgerskips.care_it.Adapter.TechAdapter;
import com.rodgerskips.care_it.Models.Tech;
import com.rodgerskips.care_it.R;
import com.rodgerskips.care_it.constants.Contants;
import com.rodgerskips.care_it.constants.RequestHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Kiduyu klaus
 * on 16/09/2020 09:30 2020
 */
public class ViewUsersFragmentAdmin extends Fragment {
    private TechAdapter approveTech;
    public static int confirmation = 0;
    public static boolean isRefreshed;
    EditText search;
    ProgressDialog pDialog;
    private RequestQueue mRequestQueue;
    RecyclerView recycler;
    SwipeRefreshLayout swipeRefreshLayout;
    TextView customer, technician;
    private ArrayList<Tech> approveArrayList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.admin_fragment_users,container,false);

        recycler = view.findViewById(R.id.rv_users_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
        recycler.setLayoutManager(layoutManager);
        recycler.setFocusable(false);
        getCustomer();

        customer=view.findViewById(R.id.users_customer);
        technician=view.findViewById(R.id.users_technician);

        customer.setOnClickListener(v -> {
            customer.setBackground(ContextCompat.getDrawable(getActivity(),R.drawable.bg_round));
            technician.setBackground(ContextCompat.getDrawable(getActivity(),R.drawable.review_header_bg));
            getCustomer();
            customer.setEnabled(false);
            technician.setEnabled(true);
        });

        technician.setOnClickListener(v -> {
            technician.setBackground(ContextCompat.getDrawable(getActivity(),R.drawable.bg_round));
            customer.setBackground(ContextCompat.getDrawable(getActivity(),R.drawable.review_header_bg));
            getTechnitian();
            technician.setEnabled(false);
            customer.setEnabled(true);
        });

        return view;
    }



    private void getCustomer() {
        approveArrayList.clear();
        pDialog=new ProgressDialog(getActivity());
        pDialog.setTitle("Fetching Customer");
        pDialog.setMessage("Please wait, while we are checking the database.");
        pDialog.setCanceledOnTouchOutside(false);
        pDialog.show();


        String urlForJsonObject = Contants.URL_USERS_API;
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

                            pDialog.dismiss();

                            approveArrayList.add(new Tech(tech_name,tech_email,tech_phone,"","","Approved"));

                        }

                        TechAdapter techAdapter = new TechAdapter(getActivity(), approveArrayList);
                        recycler.setAdapter(techAdapter);
                    } catch (JSONException e) {
                        pDialog.dismiss();
                        e.printStackTrace();
                    }
                }, volleyError -> {
            pDialog.dismiss();
            volleyError.printStackTrace();
        });

        RequestHandler.getInstance(getActivity()).addToRequestQueue(request);
    }


    private void getTechnitian() {
        approveArrayList.clear();
        pDialog=new ProgressDialog(getActivity());
        pDialog.setTitle("Fetching Technicians");
        pDialog.setMessage("Please wait, while we are checking the database.");
        pDialog.setCanceledOnTouchOutside(false);
        pDialog.show();


        String urlForJsonObject = Contants.URL_TECH_API;
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

                            pDialog.dismiss();

                            approveArrayList.add(new Tech(tech_name,tech_email,tech_phone,"","","Approved"));

                        }

                        TechAdapter techAdapter = new TechAdapter(getActivity(), approveArrayList);
                        recycler.setAdapter(techAdapter);
                    } catch (JSONException e) {
                        pDialog.dismiss();
                        e.printStackTrace();
                    }
                }, volleyError -> {
            pDialog.dismiss();
            volleyError.printStackTrace();
        });

        RequestHandler.getInstance(getActivity()).addToRequestQueue(request);
    }
}
