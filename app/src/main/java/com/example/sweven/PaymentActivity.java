package com.example.sweven;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
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

import static com.example.sweven.DBqueries.totalamt;
import static com.example.sweven.DBqueries.cartItemsList;

public class PaymentActivity extends AppCompatActivity {

    private Button cod;
    private Button card;
    private Button netBanking;
    private Button pay;
    private TextView totalv;
    private Boolean codSelected=false;
    FirebaseFirestore firebaseFirestore;
    FirebaseUser user;
    int listSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        Toolbar toolbar = (Toolbar) findViewById(R.id.payment_toolbar);
        cod=findViewById(R.id.payment_cod);
        card=findViewById(R.id.payment_card);
        netBanking=findViewById(R.id.payment_net);
        totalv=findViewById(R.id.payment_total);
        pay=findViewById(R.id.pay_now_btn);
        firebaseFirestore=FirebaseFirestore.getInstance();
        user= FirebaseAuth.getInstance().getCurrentUser();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Payment");
        card.setClickable(false);
        netBanking.setClickable(false);
        totalv.setText("Rs. "+ Integer.toString((int) totalamt) +"/-");
        cod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                codSelected=!codSelected;
                if(codSelected){
                    cod.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.check,0);
                    pay.setText("Place Order");
                }
                else {
                    cod.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0);
                    pay.setText("Pay Now");
                }
            }
        });
        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(codSelected){
                    listSize=cartItemsList.size();
                    if(cartItemsList.isEmpty())Toast.makeText(getBaseContext(),"No Product found",Toast.LENGTH_SHORT).show();
                    for(final ProductItemModel product:cartItemsList){
                        final Map<String, Object> userdata = new HashMap<>();
                        userdata.put("qty",product.getQty());
                        userdata.put("status",0);
                        userdata.put("price",product.getPrice());
                        firebaseFirestore.collection("USERS").document(user.getUid()).collection("ORDER").document(product.getProductId()).set(userdata, SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                listSize--;
                                if(task.isSuccessful())firebaseFirestore.collection("USERS").document(user.getUid()).collection("CART").document(product.getProductId()).delete();
                            }
                        });
                        firebaseFirestore.collection("USERS").document(user.getUid()).update("order", FieldValue.arrayUnion(product.getProductId()))
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        firebaseFirestore.collection("USERS").document(user.getUid()).update("cart", FieldValue.arrayRemove(product.getProductId()));
                                        if(listSize==0){
                                            Toast.makeText(getBaseContext(),"Order Successfully placed",Toast.LENGTH_SHORT).show();
                                            Intent intent=new Intent(PaymentActivity.this,MainActivity.class);
                                            startActivity(intent);
                                            finish();
                                        }
                                    }
                                });

                    }
                }
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