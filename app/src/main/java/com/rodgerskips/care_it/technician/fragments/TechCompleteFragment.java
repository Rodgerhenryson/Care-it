package com.rodgerskips.care_it.technician.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.rodgerskips.care_it.R;

/**
 * Created by Kiduyu klaus
 * on 01/10/2020 14:19 2020
 */
public class TechCompleteFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.tech_fragment_home,container,false);


        return view;
    }
}
