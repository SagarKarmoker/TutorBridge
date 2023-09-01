package com.cse489.tutorbridge;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Handler;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.cse489.tutorbridge.modal.MentorProfileClass;
import com.cse489.tutorbridge.modal.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;

public class ProfileFragment extends Fragment {
    LinearLayout proLay;
    LottieAnimationView progressBar;
    ImageView editProfileBtn;
    ShapeableImageView profilePic;
    Button selectPicBtn;
    MaterialCardView verifiedCard;
    FloatingActionButton walletBtn;
    boolean editBtnClicked = false;

    MaterialTextView userName,proName, proEmail, proMobile, proEdu, proExp, proStatus;

    private FirebaseFirestore db;
    FirebaseStorage storage;
    DocumentReference docRef;
    StorageReference storageRef;
    StorageReference imageRef;

    Handler handler;
    private double curBalance = 0;

    FirebaseAuth auth;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        Context context = view.getContext(); // Initialize the Context variable

        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();
        auth = FirebaseAuth.getInstance();
        imageRef = storageRef.child("mentors/profile.png");

        progressBar = view.findViewById(R.id.progressBar);
        proLay = view.findViewById(R.id.profileLayout);

        walletBtn = view.findViewById(R.id.walletBtn);
        editProfileBtn = view.findViewById(R.id.editProfileBtn);
        selectPicBtn = view.findViewById(R.id.selectPicBtn);
        verifiedCard = view.findViewById(R.id.verifiedCard);
        userName = view.findViewById(R.id.userName);
        proName = view.findViewById(R.id.proName);
        proEmail = view.findViewById(R.id.proEmail);
        proMobile = view.findViewById(R.id.proMobile);
        proEdu = view.findViewById(R.id.proEdu);
        proExp = view.findViewById(R.id.proExp);
        proStatus = view.findViewById(R.id.proStatus);
        profilePic = view.findViewById(R.id.profilePic);

        proLay.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        walletBtn.setVisibility(View.GONE);
        editProfileBtn.setVisibility(View.GONE);

        SharedPreferences pref = context.getSharedPreferences("TutorBridge", MODE_PRIVATE);
        String json = pref.getString("CurrentUser", "");
        boolean isMentor = pref.getBoolean("isMentor", false);
        //System.out.println("Profile" + isMentor);
        Gson gson = new Gson();

        try {
            if(isMentor){
                MentorProfileClass currentUser = gson.fromJson(json, MentorProfileClass.class);
                System.out.println("Profile "+ currentUser.toString());

                userName.setText("User ID: " + currentUser.getUuid());
                proName.setText(currentUser.getName());
                proEmail.setText(currentUser.getEmail());
                proMobile.setText(currentUser.getPhone());
                proEdu.setText(currentUser.getEducation());
                proExp.setText(currentUser.getExpert());
                proStatus.setText("Status: " + currentUser.getStatus());
                String s = currentUser.getStatus();
                assert s != null;
                if(s.equals("Unverified")){
                    int colorValue = Color.parseColor("#FFB4AB");
                    verifiedCard.setCardBackgroundColor(colorValue);
                }
                curBalance = currentUser.getWallet();

            }
            else {
                User currentUser = gson.fromJson(json, User.class);
                System.out.println("Profile "+ currentUser.toString());
                userName.setText("User ID: " + currentUser.getName());
                proName.setText(currentUser.getName());
                proEmail.setText(currentUser.getEmail());
//                proMobile.setText(currentUser.getPhone());
//                proEdu.setText(currentUser.getEducation());
//                proExp.setText(currentUser.getExpert());
//                proStatus.setText("Status: " + currentUser.getStatus());
//                String s = currentUser.getStatus();
//                assert s != null;
//                if(s.equals("Unverified")){
//                    int colorValue = Color.parseColor("#FFB4AB");
//                    verifiedCard.setCardBackgroundColor(colorValue);
//                }
//                curBalance = currentUser.getWallet();
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        new Handler().post(new Runnable() {
            @Override
            public void run() {
                //TODO fix user hardcoded
                fetchData(context);

                //image retrive
                // Download the image
                imageRef.getBytes(Long.MAX_VALUE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {
                        // Convert the byte array to a Bitmap or load it into an ImageView
                        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                        profilePic.setImageBitmap(bitmap);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Handle any errors
                    }
                });
            }
        });

        if(!pref.getBoolean("isMentor", false)){
            walletBtn.setVisibility(View.GONE);
        }
        walletBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotoWallet = new Intent(requireContext(), RequestPayment.class);
                //send some wallet and user data
                System.out.println(curBalance);
                gotoWallet.putExtra("balance", curBalance);
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

    @Override
    public void onStart() {
        super.onStart();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                proLay.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
                walletBtn.setVisibility(View.VISIBLE);
                editProfileBtn.setVisibility(View.VISIBLE);
            }
        }, 1500);

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

    private void fetchData(Context context){
        db.collection("mentor_list")
                .whereEqualTo("uuid", auth.getCurrentUser().getUid())
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            // Access the document path (Firestore reference)
                            String documentPath = document.getReference().getPath();
                            Log.d("Document Path", documentPath);



                            // Now, you have the document path for the document matching the UUID
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Handle failure
                    }
                });
    }
}