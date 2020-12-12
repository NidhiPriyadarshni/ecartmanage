package com.example.sweven;

import android.app.Dialog;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.sweven.DBqueries.totalamt;

public class RetailerAdapter extends RecyclerView.Adapter<RetailerAdapter.ViewHolder> {


    public FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    public FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
    public String uid=user.getUid();
    public List<ProductItemModel> cartItemsList;
    public RetailerAdapter(List<ProductItemModel> cartItemsList){
        this.cartItemsList=cartItemsList;
    }



    @NonNull
    @Override
    public RetailerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.retailer_item_layout,viewGroup,false);
        return new RetailerAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final RetailerAdapter.ViewHolder viewHolder, int i) {
        final ProductItemModel productitem=cartItemsList.get(i);
        String itemname=productitem.getName();
        String itempic=productitem.getPicurl();
        final String itemprice="Rs "+productitem.getPrice();
        final String itemqty= productitem.getQty()+"";//String.valueOf(((int) productitem.getQty()));
        final String productid=productitem.getProductId();
        viewHolder.productQuantity.setText(itemqty);
        if (!itempic.equals("null")){
            Glide.with(viewHolder.itemView.getContext()).load(itempic)./*apply(new RequestOptions().placeholder(R.mipmap.icon_placeholder)).*/into(viewHolder.imageView);
        }else{

        }
        viewHolder.name.setText(itemname);
        viewHolder.price.setText(itemprice);

        viewHolder.productQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog quantityDialog = new Dialog(viewHolder.itemView.getContext());
                quantityDialog.setContentView(R.layout.quantity_dialog);
                quantityDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                quantityDialog.setCancelable(false);
                final EditText quantityNo =quantityDialog.findViewById(R.id.quantiy_no);
                Button cancelBtn = quantityDialog.findViewById(R.id.cancel_btn);
                Button okBtn = quantityDialog.findViewById(R.id.ok_btn);
                cancelBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        quantityDialog.dismiss();
                    }
                });
                okBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Map<String, Object> userdata = new HashMap<>();
                        userdata.put("qty",Double.parseDouble(quantityNo.getText().toString()));
                        firebaseFirestore.collection("RETAILER").document(uid).collection("PRODUCT_INFO").document(productid).set(userdata, SetOptions.merge());
                        viewHolder.productQuantity.setText(quantityNo.getText());
                        quantityDialog.dismiss();

                    }
                });
                quantityDialog.show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return cartItemsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{


        private TextView productQuantity;
        private ImageView imageView;
        private TextView name;
        private TextView price;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            productQuantity = itemView.findViewById(R.id.retailer_qty);
            imageView=itemView.findViewById(R.id.retailer_product_image);
            name=itemView.findViewById(R.id.retailer_product_title);
            price =itemView.findViewById(R.id.retailer_price);

        }
    }

}

