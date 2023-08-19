package com.cse489.tutorbridge;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ProfileFragment extends Fragment {
    ImageView editProfileBtn;
    Button selectPicBtn;
    MaterialCardView verifiedCard;
    FloatingActionButton walletBtn;
    boolean editBtnClicked = false;
    private Context context;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        context = view.getContext(); // Initialize the Context variable

        editProfileBtn = view.findViewById(R.id.editProfileBtn);
        selectPicBtn = view.findViewById(R.id.selectPicBtn);
        verifiedCard = view.findViewById(R.id.verifiedCard);
        walletBtn = view.findViewById(R.id.walletBtn);

        walletBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotoWallet = new Intent(requireContext(), RequestPayment.class);
                //send some wallet and user data
                startActivity(gotoWallet);
            }
        });

        editProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });


        // Inflate the layout for this fragment
        return view;
    }



    @SuppressLint("UseCompatLoadingForDrawables")
    private void showDialog() {
        new MaterialAlertDialogBuilder(requireContext())
                .setTitle(R.string.warning)
                .setMessage(R.string.warn_msg)
                .setIcon(R.drawable.error)
                .setPositiveButton(R.string.yes, (dialog, which) -> {
                    // Respond to positive button press
                    Log.d("editProfile", "clicked");
                    if(!editBtnClicked){
                        editBtnClicked = true;
                        editProfileBtn.setImageDrawable(getResources().getDrawable(R.drawable.close));
                        selectPicBtn.setVisibility(View.VISIBLE);
                        verifiedCard.setVisibility(View.GONE);
                    }
                    else{
                        editProfileBtn.setImageDrawable(getResources().getDrawable(R.drawable.edit));
                        selectPicBtn.setVisibility(View.GONE);
                        verifiedCard.setVisibility(View.VISIBLE);
                        editBtnClicked = false;
                    }
                })
                .setNeutralButton(R.string.cancel, (dialog, which) -> {
                    // Respond to neutral button press.
                    dialog.dismiss();
                })
                .show();
    }
}