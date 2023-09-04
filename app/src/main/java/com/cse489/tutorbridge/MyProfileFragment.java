package com.cse489.tutorbridge;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cse489.tutorbridge.modal.MentorProfileClass;
import com.cse489.tutorbridge.modal.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;

import org.w3c.dom.Text;

public class MyProfileFragment extends Fragment {

    TextView proName, proSub, proEdu, logoutBtn;
    LinearLayout verifiedAccount, paymentWallet, proHistory,proAch,proPrivacy;

    ImageView profileSecPic;
    FirebaseAuth auth;
    Fragment myfragment;
    Gson gson;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        auth= FirebaseAuth.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_profile, container, false);
        Context context = view.getContext();
        gson = new Gson();

        proName= view.findViewById(R.id.proName);
        proEdu= view.findViewById(R.id.proEdu);
        logoutBtn= view.findViewById(R.id.logoutBtn);
        profileSecPic = view.findViewById(R.id.profileSecPic);

        verifiedAccount = view.findViewById(R.id.verifiedAccount);
        paymentWallet = view.findViewById(R.id.paymentWallet);
        proHistory = view.findViewById(R.id.proHistory);
        proAch = view.findViewById(R.id.proAch);
        proPrivacy = view.findViewById(R.id.proPrivacy);

        // Get the FragmentManager
        FragmentManager fragmentManager = getParentFragmentManager(); // Use getChildFragmentManager() if you are in a child fragment.
        // Start a FragmentTransaction
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        SharedPreferences pref = context.getSharedPreferences("TutorBridge", MODE_PRIVATE);
        String json = pref.getString("CurrentUser", "");
        boolean isMentor = pref.getBoolean("isMentor", false);


        try{
            if(isMentor){
                MentorProfileClass currentUser = gson.fromJson(json, MentorProfileClass.class);
                System.out.println("Profile "+ currentUser.toString());

                proName.setText(currentUser.getName());
                proEdu.setText(currentUser.getExpert());
            }
            else {
                User currentUser = gson.fromJson(json, User.class);
                proName.setText(currentUser.getName());
                proEdu.setText("Student");

            }
        }catch (Exception e){
            e.printStackTrace();
        }

        proAch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Coming soon", Toast.LENGTH_SHORT).show();
            }
        });

        proPrivacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Coming soon", Toast.LENGTH_SHORT).show();
            }
        });

        verifiedAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myfragment = new ProfileFragment();
                transaction.replace(R.id.fragment_container, myfragment).addToBackStack(null).commit();
            }
        });


        paymentWallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isMentor){
                    Intent i = new Intent(requireContext(), RequestPayment.class);
                    startActivity(i);
                }
                else {
                    Toast.makeText(context, "You don't have the access", Toast.LENGTH_SHORT).show();
                }
            }
        });

        proHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myfragment = new HistoryFragment();
                transaction.replace(R.id.fragment_container, myfragment).addToBackStack(null).commit();
            }
        });

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //logout
                auth.signOut();
                SharedPreferences preferences = context.getSharedPreferences("TutorBridge", Context.MODE_PRIVATE);
                SharedPreferences.Editor edit = preferences.edit();
                edit.putBoolean("TutorIsLoggedIn", false);
                edit.putString("CurrentUser", "");
                edit.apply();
                Intent i = new Intent(getContext(), initialActivity.class);
                startActivity(i);
            }
        });

        return view;
    }
}