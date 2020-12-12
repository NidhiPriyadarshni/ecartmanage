package com.example.sweven;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

public class warehousemain extends AppCompatActivity {
    RecyclerView recyclerView;
    FloatingActionButton fabtn;
    FirebaseFirestore firebaseFirestore;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_warehousemain);
        recyclerView=findViewById(R.id.warehouse_recyclerview);
        fabtn=findViewById(R.id.wfab);
        firebaseFirestore=FirebaseFirestore.getInstance();
        user= FirebaseAuth.getInstance().getCurrentUser();
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
                        firebaseFirestore.collection("WAREHOUSE MANAGER").document(user.getUid()).update("productList", FieldValue.arrayUnion(productid));
                        firebaseFirestore.collection("WAREHOUSE MANAGER").document(user.getUid()).collection("PRODUCT_INFO").document(productid).set(userdata, SetOptions.merge());
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