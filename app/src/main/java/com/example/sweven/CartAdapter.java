package com.example.sweven;

import android.app.Activity;
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

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {


    public FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    public FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
    public String uid=user.getUid();
    public List<ProductItemModel> cartItemsList;
    private OnQtyChangeListener listener;
    public CartAdapter(List<ProductItemModel> cartItemsList,OnQtyChangeListener listener){
        this.cartItemsList=cartItemsList;
        this.listener=listener;
    }



    @NonNull
    @Override
    public CartAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cart_item_layout,viewGroup,false);
        return new CartAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final CartAdapter.ViewHolder viewHolder, int i) {
        final ProductItemModel productitem=cartItemsList.get(i);
        String itemname=productitem.getName();
        String itempic=productitem.getPicurl();
        final String itemprice="Rs "+productitem.getPrice();
        final String itemqty= productitem.getQty()+"";//String.valueOf(((int) productitem.getQty()));
        final String productid=productitem.getProductId();
        boolean isavailable=!(productitem.isOutOfStock());
        viewHolder.remove.setText("Remove");
        viewHolder.productQuantity.setText(itemqty);
        if (!itempic.equals("null")){
            Glide.with(viewHolder.itemView.getContext()).load(itempic)./*apply(new RequestOptions().placeholder(R.mipmap.icon_placeholder)).*/into(viewHolder.imageView);
        }else{

        }
        viewHolder.name.setText(itemname);
        viewHolder.price.setText(itemprice);
        if(!isavailable){
            viewHolder.outofstock.setVisibility(View.VISIBLE);

        }else{
            viewHolder.outofstock.setVisibility(View.GONE);

        }
        viewHolder.remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseFirestore.collection("USERS").document(uid).update("cart", FieldValue.arrayRemove(productid))
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(viewHolder.itemView.getContext(),"Product successfuly removed from Cart.",Toast.LENGTH_LONG).show();

                                    double ttl=Double.parseDouble(viewHolder.productQuantity.getText().toString());
                                    ttl=ttl*productitem.getPrice();
                                    totalamt=totalamt-ttl;
                                    listener.onQtyChange();
                                    cartItemsList.remove(viewHolder.getAdapterPosition());
                                    notifyDataSetChanged();
                                }
                                else Toast.makeText(viewHolder.itemView.getContext(),"Try again.",Toast.LENGTH_LONG).show();

                            }
                        });
            }
        });
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
                        double ttl=Double.parseDouble(quantityNo.getText().toString())-Double.parseDouble(viewHolder.productQuantity.getText().toString());
                        ttl=ttl*productitem.getPrice();
                        totalamt=totalamt+ttl;
                        int t=(int)totalamt;
                        Map<String, Object> userdata = new HashMap<>();
                        userdata.put("qty",Double.parseDouble(quantityNo.getText().toString()));
                        firebaseFirestore.collection("USERS").document(uid).collection("CART").document(productid).set(userdata, SetOptions.merge());
                        listener.onQtyChange();
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

        private ImageView freeCouponIcon;
        private TextView freeCoupons;
        private TextView cuttedPrice;
        private TextView offersApplied;
        private TextView couponsApplied;
        private TextView productQuantity;
        private ImageView imageView;
        private TextView name;
        private TextView price;
        private TextView remove;
        private Button outofstock;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            freeCouponIcon = itemView.findViewById(R.id.free_coupon_icon);
            freeCoupons = itemView.findViewById(R.id.tv_free_coupon);
            cuttedPrice = itemView.findViewById(R.id.cutted_price);
            offersApplied = itemView.findViewById(R.id.offers_applied);
            couponsApplied = itemView.findViewById(R.id.coupons_applied);
            productQuantity = itemView.findViewById(R.id.product_quantity);
            imageView=itemView.findViewById(R.id.product_image);
            name=itemView.findViewById(R.id.product_title);
            price =itemView.findViewById(R.id.product_price);
            outofstock=itemView.findViewById(R.id.outofstock);
            remove=itemView.findViewById(R.id.remove_item_btn);

        }
    }
    public interface OnQtyChangeListener{
        void onQtyChange();
    }

}

