package com.example.sweven;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import static com.example.sweven.DBqueries.loadCartList;
import static com.example.sweven.DBqueries.cartItemsList;
import static com.example.sweven.DBqueries.totalamt;

public class DeliveryActivity extends AppCompatActivity implements CartAdapter.OnQtyChangeListener {

    private RecyclerView deliveryRecyclerView;
    private Button changeOrAddNewAddressBtn;
    private Button continueBtn;
    private TextView name;
    private TextView address;
    private TextView pincode;
    private TextView phone;
    public TextView totalv;
    public static final int SELECT_ADDRESS = 0;
    FirebaseFirestore firestore;
    FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        totalv=(TextView)findViewById(R.id.total_cart_amountt);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Delivery");
        name=(TextView)findViewById(R.id.full_name);
        address=(TextView)findViewById(R.id.address);
        pincode=(TextView)findViewById(R.id.pincode);
        phone=(TextView)findViewById(R.id.phone);
        firestore=FirebaseFirestore.getInstance();
        firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
        deliveryRecyclerView = findViewById(R.id.delivery_recyclerview);
        changeOrAddNewAddressBtn = findViewById(R.id.change_or_add_address_btn);
        continueBtn=findViewById(R.id.cart_continue_btn);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        deliveryRecyclerView.setLayoutManager(linearLayoutManager);
        totalv.setText(Integer.toString((int) totalamt));
        name.setText(firebaseUser.getDisplayName());
        firestore.collection("USERS").document(firebaseUser.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot doc=task.getResult();
                    if(doc.contains("address")){
                        address.setText(doc.get("address").toString());
                        pincode.setText(doc.get("pincode").toString());
                        phone.setText(doc.get("phone").toString());
                        name.setText(doc.get("receiverName").toString());
                    }
                }
            }
        });
        loadCartList(deliveryRecyclerView,this,totalv);
        CartAdapter cartAdapter = new CartAdapter(cartItemsList,this);
        deliveryRecyclerView.setAdapter(cartAdapter);
        cartAdapter.notifyDataSetChanged();
        changeOrAddNewAddressBtn.setVisibility(View.VISIBLE);
        changeOrAddNewAddressBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent MyAddressesIntent = new Intent(DeliveryActivity.this, MyAddressesActivity.class);
                MyAddressesIntent.putExtra("MODE",SELECT_ADDRESS);
                startActivity(MyAddressesIntent);
            }
        });
        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(DeliveryActivity.this,PaymentActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onQtyChange() {
        totalv.setText("Rs. "+ Integer.toString((int) totalamt) +"/-");
    }
}