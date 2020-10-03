package com.rodgerskips.care_it.technician.fragments;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.github.ornolfr.ratingview.RatingView;
import com.rodgerskips.care_it.MapsActivity;
import com.rodgerskips.care_it.R;
import com.rodgerskips.care_it.activities.LogInActivity;
import com.rodgerskips.care_it.constants.Contants;
import com.rodgerskips.care_it.constants.PrefManager;

import libs.mjn.prettydialog.PrettyDialog;
import libs.mjn.prettydialog.PrettyDialogCallback;

/**
 * Created by Kiduyu klaus
 * on 18/09/2020 13:51 2020
 */
public class TechDetailsFragment extends Fragment {
    String user,description,location,latitude,longitude,image,reg_date,status;
    ImageView imageView;
    TextView date_tv,status_tv,description_tv,rate_tv,map_tv;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_details,container,false);

        user=getArguments().getString("user");
        description=getArguments().getString("description");
        location=getArguments().getString("location");
        latitude=getArguments().getString("latitude");
        longitude=getArguments().getString("longitude");
        image=getArguments().getString("image");
        reg_date=getArguments().getString("reg_date");
        status=getArguments().getString("status");

        rate_tv=view.findViewById(R.id.text_tap_rate);


        date_tv=view.findViewById(R.id.text_details_title);

        date_tv.setText("Uploaded on: "+reg_date);


        imageView=view.findViewById(R.id.image_history_details);

        String path= Contants.ROOT_IMAGE+image;

        status_tv=view.findViewById(R.id.text_details_status);
        status_tv.setText(status);


        description_tv=view.findViewById(R.id.text_descri_details);
        description_tv.setText(description);

        map_tv=view.findViewById(R.id.text_distance_details);

        map_tv.setOnClickListener(v -> startMaps());
        Log.d("TAG", "image: "+path);
        //Utils.SetImageToView(getActivity(),path,imageView);

        Glide.with(getContext()).load(path).into(imageView);

      /*  rate_tv.setOnClickListener(view1 -> {

            if (PrefManager.getInstance(getActivity()).isLoggedIn()) {
                showRate();
            } else {
                final PrettyDialog dialog = new PrettyDialog(getActivity());
                dialog.setTitle(getString(R.string.dialog_warning))
                        .setTitleColor(R.color.dialog_text)
                        .setMessage(getString(R.string.login_require))
                        .setMessageColor(R.color.dialog_text)
                        .setAnimationEnabled(false)
                        .setIcon(R.drawable.pdlg_icon_close, R.color.dialog_color, new PrettyDialogCallback() {
                            @Override
                            public void onClick() {
                                dialog.dismiss();
                            }
                        })
                        .addButton(getString(R.string.dialog_ok), R.color.dialog_white_text, R.color.dialog_color, new PrettyDialogCallback() {
                            @Override
                            public void onClick() {
                                dialog.dismiss();
                                Intent intent_login = new Intent(getActivity(), LogInActivity.class);
                                intent_login.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent_login);
                            }
                        })
                        .addButton(getString(R.string.dialog_no), R.color.dialog_white_text, R.color.dialog_color, new PrettyDialogCallback() {
                            @Override
                            public void onClick() {
                                dialog.dismiss();
                            }
                        });
                dialog.setCancelable(false);
                dialog.show();
            }
        }); */

        return view;
    }

    private void startMaps() {
        Intent intent = new Intent(getActivity(), MapsActivity.class);
        intent.putExtra("lat",latitude);
        intent.putExtra("long",longitude);
        startActivity(intent);
    }

    private void showRate() {
        final String deviceId;
        final Dialog mDialog = new Dialog(getActivity(), R.style.Theme_AppCompat_Translucent);
        mDialog.setContentView(R.layout.rate_dialog);
        final RatingView ratingView = mDialog.findViewById(R.id.ratingView);
        ImageView image_rate_close = mDialog.findViewById(R.id.image_close);
        final EditText editTextReview = mDialog.findViewById(R.id.edt_d_review);
        ratingView.setRating(0);
        Button button = mDialog.findViewById(R.id.btn_submit);

        image_rate_close.setOnClickListener(view -> mDialog.dismiss());

        button.setOnClickListener(v -> {
            if (editTextReview.getText().length() == 0) {
                Toast.makeText(getActivity(), getString(R.string.require_review), Toast.LENGTH_SHORT).show();
            } else {


                mDialog.dismiss();
            }
        });
        mDialog.show();
    }
}
