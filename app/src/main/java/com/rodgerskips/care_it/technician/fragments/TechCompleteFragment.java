package com.rodgerskips.care_it.technician.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.rodgerskips.care_it.Adapter.CompletedAdapter;
import com.rodgerskips.care_it.Adapter.PendingAdapter;
import com.rodgerskips.care_it.Models.History;
import com.rodgerskips.care_it.R;
import com.rodgerskips.care_it.constants.PrefManager;
import com.rodgerskips.care_it.constants.RequestHandler;
import com.rodgerskips.care_it.utils.Utils;
import com.shashank.sony.fancytoastlib.FancyToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Kiduyu klaus
 * on 01/10/2020 14:19 2020
 */
public class TechCompleteFragment extends Fragment {
    private static final String TAG = "TechPendingFragment";
    private  String ROOT_URL = "https://the-bookhub.co.ke/care-it/CompletedApi.php";
    //private  String ROOT_URL = "https://the-bookhub.co.ke/care-it/PendingApi.php?id=";
    String techname = PrefManager.getInstance(getActivity()).getUser().getName();

    private CompletedAdapter assignAdapter;
    public static int confirmation = 0;
    public static boolean isRefreshed;
    EditText search;
    ProgressDialog pDialog;
    private RequestQueue mRequestQueue;
    RecyclerView recycler;
    SwipeRefreshLayout swipeRefreshLayout;
    LinearLayout linearLayout, recyclerly;
    private ArrayList<History> assignArraylist = new ArrayList<>();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.tech_fragment_completed,container,false);
        recycler = view.findViewById(R.id.recyclerview_tech_completed);


        swipeRefreshLayout = view.findViewById(R.id.techfragmentcompleted);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);

        recycler.setLayoutManager(layoutManager);
        recycler.setFocusable(false);
        getCompleted();
        //Loading.showProgressDialog(getActivity(),true);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                isRefreshed = true;
                getCompleted();
                swipeRefreshLayout.setRefreshing(false);
                FancyToast.makeText(getActivity(), "Data refreshed!", FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, false).show();
            }
        });
        return view;
    }

    private void getCompleted() {
        ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle("Checking Completed Uploads");
        progressDialog.setMessage("please Wait...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        String urlpending=ROOT_URL;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,
                urlpending,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        try {
                            JSONArray jsonArray = jsonObject.getJSONArray("Tech");

                            if (jsonArray.length()==0){
                                progressDialog.dismiss();
                                Utils.ShowToast(getActivity(),"Empty Set");
                               /* linearLayout.setVisibility(View.VISIBLE);
                                recycler.setVisibility(View.GONE); */
                            } else {
                                 /*linearLayout.setVisibility(View.GONE);
                                recycler.setVisibility(View.VISIBLE);*/
                                if (isRefreshed) {
                                    assignArraylist.clear();
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject consultant = jsonArray.getJSONObject(i);
                                        String id = consultant.getString("id");
                                        String name = consultant.getString("user");
                                        String description = consultant.getString("description");
                                        String location = consultant.getString("location");
                                        String lati = consultant.getString("latitude");
                                        String longi = consultant.getString("longitude");
                                        String reg_date = consultant.getString("reg_date");
                                        String status = consultant.getString("status");

                                        progressDialog.dismiss();
                                        assignArraylist.add(new History(id,name,description,location,lati,longi,"",reg_date,status));

                                    }

                                } else {
                                    assignArraylist.clear();
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject consultant = jsonArray.getJSONObject(i);
                                        String id = consultant.getString("id");
                                        String name = consultant.getString("user");
                                        String description = consultant.getString("description");
                                        String location = consultant.getString("location");
                                        String lati = consultant.getString("latitude");
                                        String longi = consultant.getString("longitude");
                                        String reg_date = consultant.getString("reg_date");
                                        String status = consultant.getString("status");



                                        progressDialog.dismiss();
                                        assignArraylist.add(new History(id,name,description,location,lati,longi,"",reg_date,status));
                                    }
                                }

                                assignAdapter = new CompletedAdapter(getActivity(), assignArraylist);
                                recycler.setAdapter(assignAdapter);
                                assignAdapter.notifyDataSetChanged();
                            }

                        } catch (JSONException e) {
                            progressDialog.dismiss();
                            e.printStackTrace();
                            Toast.makeText(getActivity(), String.valueOf(e.getMessage()), Toast.LENGTH_SHORT).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                progressDialog.dismiss();
                volleyError.printStackTrace();
            }
        });
        RequestHandler.getInstance(getActivity()).addToRequestQueue(request);

    }
}
