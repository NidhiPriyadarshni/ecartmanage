package com.example.sweven;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import static com.example.sweven.RegisterActivity.onResetPasswordFragment;


/**
 * A simple {@link Fragment} subclass.
 */
public class SignInFragment extends Fragment {


    public SignInFragment() {
        // Required empty public constructor
    }

    private TextView donthaveanaccount;
    private FrameLayout parentFrameLayout;
    private EditText email, password;
    private Button signinbtn;
    private Button signinbtnwh;
    private Button signinbtnrt;
    private ImageButton closebtn;
    private ProgressBar progressBar;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    private String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+.[a-z]+";
    private TextView forgotpassword;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sign_in, container, false);
        donthaveanaccount = view.findViewById(R.id.tv_dont_have_an_account);
        parentFrameLayout = getActivity().findViewById(R.id.register_framelayout);
        email = view.findViewById(R.id.sign_in_email);
        password = view.findViewById(R.id.sign_in_password);
        closebtn = view.findViewById(R.id.sign_in_close_btn);
        progressBar = view.findViewById(R.id.progressBar2);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore=FirebaseFirestore.getInstance();
        signinbtn = view.findViewById(R.id.sign_in_btn);
        signinbtnrt = view.findViewById(R.id.sign_in_btn_ret);
        signinbtnwh = view.findViewById(R.id.sign_in_btn_wh);

        forgotpassword = view.findViewById(R.id.sign_in_forgot_password);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        donthaveanaccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFragment(new SignUpFragment());
            }
        });

        forgotpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onResetPasswordFragment = true;
                setFragment(new ResetPasswordFragment());
            }
        });

        closebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainIntent();
            }
        });
        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkInputs();
            }


            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkInputs();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        signinbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkEmailandPattern(1);
            }
        });
        signinbtnwh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkEmailandPattern(2);
            }
        });
        signinbtnrt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkEmailandPattern(3);
            }
        });

    }


    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_from_right, R.anim.slideout_from_left);
        fragmentTransaction.replace(parentFrameLayout.getId(), fragment);
        fragmentTransaction.commit();
    }

    private void checkInputs() {
        if (!TextUtils.isEmpty(email.getText())) {
            if (!TextUtils.isEmpty(password.getText())) {
                signinbtn.setEnabled(true);
                signinbtn.setTextColor(Color.rgb(0, 0, 0));
                signinbtnrt.setEnabled(true);
                signinbtnrt.setTextColor(Color.rgb(0, 0, 0));
                signinbtnwh.setEnabled(true);
                signinbtnwh.setTextColor(Color.rgb(0, 0, 0));
            } else {
                signinbtn.setEnabled(false);
                signinbtn.setTextColor(Color.argb(50, 0, 0, 0));
                signinbtnrt.setEnabled(false);
                signinbtnrt.setTextColor(Color.argb(50, 0, 0, 0));
                signinbtnwh.setEnabled(false);
                signinbtnwh.setTextColor(Color.argb(50, 0, 0, 0));
            }
        } else {
            signinbtn.setEnabled(false);
            signinbtn.setTextColor(Color.argb(50, 0, 0, 0));
            signinbtnrt.setEnabled(false);
            signinbtnrt.setTextColor(Color.argb(50, 0, 0, 0));
            signinbtnwh.setEnabled(false);
            signinbtnwh.setTextColor(Color.argb(50, 0, 0, 0));
        }
    }

    private void checkEmailandPattern(final int c) {
        if (email.getText().toString().matches(emailPattern)) {
            if (password.length() >=8) {
                progressBar.setVisibility(View.VISIBLE);
                signinbtn.setEnabled(false);
                signinbtn.setTextColor(Color.argb(50,0,0,0));
                firebaseAuth.signInWithEmailAndPassword(email.getText().toString(),password.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            FirebaseUser user=firebaseAuth.getCurrentUser();
 if(c==1){
     firebaseFirestore.collection("USERS").document(user.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
         @Override
         public void onComplete(@NonNull Task<DocumentSnapshot> task) {
             if(task.isSuccessful()){
                 DocumentSnapshot documentSnapshot=task.getResult();
                 if(documentSnapshot.exists())mainIntent();
                 else Toast.makeText(getContext(),"Invalid Customer",Toast.LENGTH_LONG).show();
             }
         }
     });
 }
 else if(c==2)
 {

     firebaseFirestore.collection("WAREHOUSE MANAGER").document(user.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
         @Override
         public void onComplete(@NonNull Task<DocumentSnapshot> task) {
             if(task.isSuccessful()){
                 DocumentSnapshot documentSnapshot=task.getResult();
                 if(documentSnapshot.exists()){
                     Intent mainIntent = new Intent(getActivity(),warehousemain.class);
                     startActivity(mainIntent);
                     getActivity().finish();
                 }
                 else Toast.makeText(getContext(),"Invalid Warehouse Manager",Toast.LENGTH_LONG).show();
             }
         }
     });
 }
 else if(c==3)
 {
     firebaseFirestore.collection("RETAILER").document(user.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
         @Override
         public void onComplete(@NonNull Task<DocumentSnapshot> task) {
             if(task.isSuccessful()){
                 DocumentSnapshot documentSnapshot=task.getResult();
                 if(documentSnapshot.exists()){
                     Intent mainIntent = new Intent(getActivity(),Retaileramainpage.class);
                     startActivity(mainIntent);
                     getActivity().finish();
                 }
                 else Toast.makeText(getContext(),"Invalid Retailer",Toast.LENGTH_LONG).show();
             }
         }
     });
 }
                        } else{
                            progressBar.setVisibility(View.INVISIBLE);
                            signinbtn.setEnabled(true);
                            signinbtn.setTextColor(Color.rgb(0,0,0));
                            String error = task.getException().getMessage();
                            Toast.makeText(getActivity(),error,Toast.LENGTH_LONG).show();
                        }
                    }
                });
            } else {
                Toast.makeText(getActivity(),"Incorrect Email or Password",Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(getActivity(),"Incorrect Email or Password",Toast.LENGTH_LONG).show();
        }
    }
    private void mainIntent(){
        Intent mainIntent = new Intent(getActivity(), MainActivity.class);
        startActivity(mainIntent);
        getActivity().finish();
    }
}

