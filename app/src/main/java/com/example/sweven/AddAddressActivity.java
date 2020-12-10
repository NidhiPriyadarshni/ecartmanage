package com.example.sweven;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;


public class AddAddressActivity extends AppCompatActivity {


    private Button saveBtn;
    private TextView name;
    private TextView phone;
    private TextView city;
    private TextView street;
    private TextView building;
    private TextView pincode;
    private TextView state;
    private TextView landmark;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_address);
        Toolbar toolbar =findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Add a new Address");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        firebaseFirestore=FirebaseFirestore.getInstance();
        user= FirebaseAuth.getInstance().getCurrentUser();
        name=findViewById(R.id.addnew_name);
        phone=findViewById(R.id.addnew_mobile_no);
        city=findViewById(R.id.addnew_city);
        street=findViewById(R.id.addnew_locality);
        building=findViewById(R.id.addnew_flat_no);
        pincode=findViewById(R.id.addnew_pincode);
        state=findViewById(R.id.addnew_state);
        landmark=findViewById(R.id.addnew_landmark);
        saveBtn = findViewById(R.id.save_btn);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String address=building.getText().toString()+", "+street.getText().toString()+", near "+landmark.getText().toString()+", "+city.getText().toString()+", "+state.getText().toString();
                String newpincode=pincode.getText().toString();
                String newphone=phone.getText().toString();
                String newname=name.getText().toString();
                AddressesModel addressesModel=new AddressesModel(newname,address,newpincode,newphone);
                Map<Object, String> userdata = new HashMap<>();
                userdata.put("receiverName",newname);
                userdata.put("address",address);
                userdata.put("pincode",newpincode);
                userdata.put("phone",newphone);
                firebaseFirestore.collection("USERS").document(user.getUid()).set(userdata, SetOptions.merge());
                firebaseFirestore.collection("USERS").document(user.getUid()).collection("ADDRESS_LIST").add(addressesModel);
                Intent deliveryIntent = new Intent(AddAddressActivity.this,DeliveryActivity.class);
                startActivity(deliveryIntent);
                finish();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

/*package com.example.sweven;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;


public class AddAddressActivity extends AppCompatActivity {


    private Button saveBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_address);
        Toolbar toolbar =findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Add a new Address");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        saveBtn = findViewById(R.id.save_btn);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent deliveryIntent = new Intent(AddAddressActivity.this,DeliveryActivity.class);
                startActivity(deliveryIntent);
                finish();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}*/
