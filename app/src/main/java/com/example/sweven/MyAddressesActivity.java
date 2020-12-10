package com.example.sweven;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.sweven.DeliveryActivity.SELECT_ADDRESS;

public class MyAddressesActivity extends AppCompatActivity {

    private static RecyclerView myaddressesRecyclerView;
    private static AddressesAdapter addressesAdapter;
    private Button deliverHereBtn;
    private Button addAddress;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseUser user;
    int mode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_addresses);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("My Addresses");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        myaddressesRecyclerView = findViewById(R.id.addresses_recyclerview);
        deliverHereBtn = findViewById(R.id.deliver_here_btn);
        addAddress=findViewById(R.id.add_address);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        myaddressesRecyclerView.setLayoutManager(linearLayoutManager);
        final List<AddressesModel> addressesModelList = new ArrayList<>();
        firebaseFirestore=FirebaseFirestore.getInstance();
        user= FirebaseAuth.getInstance().getCurrentUser();
        mode = getIntent().getIntExtra("MODE",-1);
        firebaseFirestore.collection("USERS").document(user.getUid()).collection("ADDRESS_LIST").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    QuerySnapshot querySnapshot=task.getResult();
                    if(querySnapshot.size()>0)for(DocumentSnapshot doc:querySnapshot){
                        AddressesModel addressesModel=doc.toObject(AddressesModel.class);
                        addressesModelList.add(addressesModel);
                        addressesAdapter = new AddressesAdapter(addressesModelList,mode);
                        myaddressesRecyclerView.setAdapter(addressesAdapter);
                        addressesAdapter.notifyDataSetChanged();
                    }
                }
            }
        });

        if(mode==SELECT_ADDRESS){
            deliverHereBtn.setVisibility(View.VISIBLE);
        }else{
            deliverHereBtn.setVisibility(View.GONE);
        }
        addressesAdapter = new AddressesAdapter(addressesModelList,mode);
        myaddressesRecyclerView.setAdapter(addressesAdapter);
        ((SimpleItemAnimator)myaddressesRecyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
        addressesAdapter.notifyDataSetChanged();
        addAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MyAddressesActivity.this,AddAddressActivity.class);
                startActivity(intent);
                finish();
            }
        });
        deliverHereBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddressesModel addressesModel=addressesModelList.get(addressesAdapter.preSelectedPosition);
                Map<Object, String> userdata = new HashMap<>();
                userdata.put("receiverName",addressesModel.getFullName());
                userdata.put("address",addressesModel.getAddress());
                userdata.put("pincode",addressesModel.getPincode());
                userdata.put("phone",addressesModel.getPhone());
                firebaseFirestore.collection("USERS").document(user.getUid()).set(userdata, SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Intent intent=new Intent(MyAddressesActivity.this,DeliveryActivity.class);
                            startActivity(intent);
                            finish();
                        }else {
                            //Toast.makeText(this,"Could not select, try again!",Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });
    }

    public static  void refreshItem(int deSelect, int select){
        addressesAdapter.notifyItemChanged(deSelect);
        addressesAdapter.notifyItemChanged(select);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()== android.R.id.home){
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
