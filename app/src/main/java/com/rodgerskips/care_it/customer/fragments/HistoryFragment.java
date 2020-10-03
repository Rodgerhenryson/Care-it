package com.rodgerskips.care_it.customer.fragments;

import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.rodgerskips.care_it.Adapter.HistoryAdapter;
import com.rodgerskips.care_it.Models.History;
import com.rodgerskips.care_it.R;
import com.rodgerskips.care_it.constants.Contants;
import com.rodgerskips.care_it.constants.PrefManager;
import com.rodgerskips.care_it.constants.RequestHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Kiduyu klaus
 * on 16/09/2020 09:30 2020
 */
public class HistoryFragment extends Fragment {
    private HistoryAdapter historyAdapter;
    public static int confirmation = 0;
    public static boolean isRefreshed;
    RecyclerView recycler;
    FloatingActionButton floatingActionButton;
    ArrayList<History> historyArrayList = new ArrayList<>();
    ProgressDialog pDialog;


    TextView tv;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_history, container, false);
        recycler = layout.findViewById(R.id.recyclerView_history);
        pDialog = new ProgressDialog(getActivity());
        floatingActionButton = layout.findViewById(R.id.floating_action_button);

        tv = layout.findViewById(R.id.history_nothing_found);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
        recycler.setLayoutManager(layoutManager);
        recycler.setFocusable(false);

        floatingActionButton.setOnClickListener(v -> goToUploads());
        fetchData();

        return layout;
    }

    private void goToUploads() {
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new UploadFragment()).commit();
    }

    private void fetchData() {
        pDialog.setTitle("Fetching Upload History");
        pDialog.setMessage("Please wait, while we are checking the database.");
        pDialog.setCanceledOnTouchOutside(false);
        pDialog.show();
        String urlForJsonObject = Contants.URL_HISTORY_CUSTOMER;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,
                urlForJsonObject,
                null,
                jsonObject -> {
                    try {
                        JSONArray jsonArray = jsonObject.getJSONArray("History");

                        if (jsonArray.length() == 0) {
                            pDialog.dismiss();
                            recycler.setVisibility(View.GONE);
                            floatingActionButton.setVisibility(View.VISIBLE);
                            tv.setVisibility(View.VISIBLE);
                            //PrefManager.Toast(getActivity(),"Nothing");

                        } else {
                            historyArrayList.clear();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject consultant = jsonArray.getJSONObject(i);


                                String id = consultant.getString("id");
                                String user = consultant.getString("user");
                                String description = consultant.getString("description");
                                String location = consultant.getString("location");
                                String latitude = consultant.getString("latitude");
                                String longitude = consultant.getString("longitude");
                                String image = consultant.getString("image");
                                String reg_date = consultant.getString("reg_date");
                                String status = consultant.getString("status");

                               /* DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:SS");
                                LocalDateTime dateTime=LocalDateTime.parse(reg_date, formatter);


                                */


                                pDialog.dismiss();
                                historyArrayList.add(new History(id,user,description,location,latitude,longitude,image,reg_date,status));

                            }


                            historyAdapter = new HistoryAdapter(getActivity(), historyArrayList);
                            recycler.setAdapter(historyAdapter);
                        }


                    } catch (JSONException e) {
                        pDialog.dismiss();
                        e.printStackTrace();
                    }

                }, volleyError -> {
                    pDialog.dismiss();
                    volleyError.printStackTrace();
                });
        //mRequestQueue.add(request);
        RequestHandler.getInstance(getActivity()).addToRequestQueue(request);
    }
}
