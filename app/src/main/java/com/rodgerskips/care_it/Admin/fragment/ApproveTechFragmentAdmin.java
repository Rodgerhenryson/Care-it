package com.rodgerskips.care_it.Admin.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.rodgerskips.care_it.Adapter.ApproveTech;
import com.rodgerskips.care_it.Models.Approve;
import com.rodgerskips.care_it.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.rodgerskips.care_it.constants.Contants;
import com.rodgerskips.care_it.constants.RequestHandler;
import com.rodgerskips.care_it.utils.Utils;
import com.shashank.sony.fancytoastlib.FancyToast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

/**
 * Created by Kiduyu klaus
 * on 16/09/2020 09:30 2020
 */
public class ApproveTechFragmentAdmin extends Fragment {
    private ApproveTech approveTech;
    public static int confirmation = 0;
    public static boolean isRefreshed;
    EditText search;
    ProgressDialog pDialog;
    private RequestQueue mRequestQueue;
    RecyclerView recycler;
    SwipeRefreshLayout swipeRefreshLayout;
    LinearLayout linearLayout, recyclerly;
    private ArrayList<Approve> approveArrayList = new ArrayList<>();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.admin_fragment_approve_tech, container, false);
        pDialog = new ProgressDialog(getActivity());
        linearLayout = view.findViewById(R.id.lyt_not_found_approve);
        recyclerly = view.findViewById(R.id.ly_recycle_approve);
        search = view.findViewById(R.id.search_editText_approve);


        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());
            }
        });

        recycler = view.findViewById(R.id.recyclerview_approve);

        swipeRefreshLayout = view.findViewById(R.id.consultant_refresh_herbs);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);

        recycler.setLayoutManager(layoutManager);
        recycler.setFocusable(false);
        fetchData();
        //Loading.showProgressDialog(getActivity(),true);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                isRefreshed = true;
                fetchData();
                swipeRefreshLayout.setRefreshing(false);
                FancyToast.makeText(getActivity(), "Data refreshed!", FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, false).show();
            }
        });


        return view;
    }

    private void fetchData() {
        pDialog.setTitle("Fetching Technicians");
        pDialog.setMessage("Please wait, while we are checking the database.");
        pDialog.setCanceledOnTouchOutside(false);
        pDialog.show();
        String urlForJsonObject = Contants.URL_APPROVE_TECH;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,
                urlForJsonObject,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        try {
                            JSONArray jsonArray = jsonObject.getJSONArray("Tech");

                            if (jsonArray.length()==0){
                                pDialog.dismiss();
                                Utils.ShowToast(getActivity(),"Empty Set");
                                linearLayout.setVisibility(View.VISIBLE);
                                recycler.setVisibility(View.GONE);
                            } else {
                                linearLayout.setVisibility(View.GONE);
                                recycler.setVisibility(View.VISIBLE);
                                if (isRefreshed) {
                                    approveArrayList.clear();
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject consultant = jsonArray.getJSONObject(i);
                                        String id = consultant.getString("id");
                                        String name = consultant.getString("name");
                                        String email = consultant.getString("email");
                                        String phone = consultant.getString("phone");
                                        String status = consultant.getString("status");


                                        pDialog.dismiss();
                                        approveArrayList.add(new Approve(id,name,email,phone,"description","skills",status));

                                    }

                                } else {
                                    approveArrayList.clear();
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject consultant = jsonArray.getJSONObject(i);
                                        String id = consultant.getString("id");
                                        String name = consultant.getString("name");
                                        String email = consultant.getString("email");
                                        String phone = consultant.getString("phone");
                                        String status = consultant.getString("status");
                                        pDialog.dismiss();
                                        approveArrayList.add(new Approve(id,name,email,phone,"description","skills",status));

                                    }
                                }

                                approveTech = new ApproveTech(getActivity(), approveArrayList);
                                recycler.setAdapter(approveTech);
                                approveTech.notifyDataSetChanged();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                pDialog.dismiss();
                volleyError.printStackTrace();
            }
        });
        RequestHandler.getInstance(getActivity()).addToRequestQueue(request);
    }

    private void filter(String text) {
        ArrayList<Approve> filteredList = new ArrayList<>();
        for (Approve item : approveArrayList) {
            if (item.getName().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
                linearLayout.setVisibility(View.GONE);
                recycler.setVisibility(View.VISIBLE);
            } else{
                linearLayout.setVisibility(View.VISIBLE);
                recycler.setVisibility(View.GONE);
            }
        }
        approveTech = new ApproveTech(getActivity(), filteredList);
        recycler.setAdapter(approveTech);
        approveTech.notifyDataSetChanged();

    }
}
