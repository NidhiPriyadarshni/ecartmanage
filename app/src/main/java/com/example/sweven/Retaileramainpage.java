package com.example.sweven;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

import static com.example.sweven.DBqueries.cartItemsList;
import static com.example.sweven.DBqueries.loadCartList;
import static com.example.sweven.DBqueries.retailerItemsList;
import static com.example.sweven.DBqueries.loadRetailerItemsList;

public class Retaileramainpage extends AppCompatActivity {

    RecyclerView recyclerView;
    FloatingActionButton fabtn;
    FirebaseFirestore firebaseFirestore;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retaileramainpage);
        Toolbar toolbar = findViewById(R.id.retailer_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Products in stock");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        recyclerView=findViewById(R.id.retailer_recyclerview);
        fabtn=findViewById(R.id.rfab);
        firebaseFirestore=FirebaseFirestore.getInstance();
        user= FirebaseAuth.getInstance().getCurrentUser();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getBaseContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        loadRetailerItemsList(recyclerView);
        RetailerAdapter adapter = new RetailerAdapter(retailerItemsList);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        fabtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addProduct();
            }
        });
    }
    private void addProduct(){
        AlertDialog.Builder builder=new AlertDialog.Builder(this,R.style.AlertDialogTheme);
        LayoutInflater inflater=LayoutInflater.from(this);
        View v=inflater.inflate(R.layout.add_product,null);
        final EditText id=v.findViewById(R.id.enterid);
        final EditText qty=v.findViewById(R.id.enterqty);
        builder.setView(v).setTitle("Add product").setCancelable(true)
                .setNegativeButton("Discard", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String productid=id.getText().toString().trim();
                        String pqty=qty.getText().toString().trim();
                        Map<String, Object> userdata = new HashMap<>();
                        userdata.put("qty", Integer.parseInt(pqty));
                        firebaseFirestore.collection("RETAILER").document(user.getUid()).update("productList", FieldValue.arrayUnion(productid));
                        firebaseFirestore.collection("RETAILER").document(user.getUid()).collection("PRODUCT_INFO").document(productid).set(userdata, SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful())loadRetailerItemsList(recyclerView);
                            }
                        });
                        Toast.makeText(getBaseContext(),"Product successfully added",Toast.LENGTH_SHORT).show();

                        /*if(n!=""){
                            String s=model.selectCategoryByName(n);
                            if(s==null)model.addCategory(new Category(n));
                            else{

                            }
                        } else{

                        }*/
                    }
                });
        builder.create().show();

    }
}