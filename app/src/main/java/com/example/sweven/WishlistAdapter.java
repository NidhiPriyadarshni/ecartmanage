package com.example.sweven;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

import java.util.List;




public class WishlistAdapter extends RecyclerView.Adapter<WishlistAdapter.ViewHolder> {


    public FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    public FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
    public String uid=user.getUid();
    public List<ProductItemModel> wishlistItemsList;
    public WishlistAdapter(List<ProductItemModel> wishlistItemsList){
        this.wishlistItemsList=wishlistItemsList;
    }



    @NonNull
    @Override
    public WishlistAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.product_list_layout,viewGroup,false);
        return new WishlistAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final WishlistAdapter.ViewHolder viewHolder, int i) {
        final ProductItemModel productitem=wishlistItemsList.get(i);
        String itemname=productitem.getName();
        String itempic=productitem.getPicurl();
        String itemprice="Rs "+productitem.getPrice();
        final String productid=productitem.getProductId();
        boolean isavailable=!(productitem.isOutOfStock());
        viewHolder.wishlist.setText("Remove");
        if (!itempic.equals("null")){
            Glide.with(viewHolder.itemView.getContext()).load(itempic)./*apply(new RequestOptions().placeholder(R.mipmap.icon_placeholder)).*/into(viewHolder.imageView);
        }else{

        }
        viewHolder.name.setText(itemname);
        viewHolder.price.setText(itemprice);
        if(!isavailable){
            viewHolder.outofstock.setVisibility(View.VISIBLE);
            viewHolder.addtocart.setVisibility(View.GONE);

        }else{
            viewHolder.outofstock.setVisibility(View.GONE);
            viewHolder.addtocart.setVisibility(View.VISIBLE);

        }
        viewHolder.wishlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseFirestore.collection("USERS").document(uid).update("wishlist", FieldValue.arrayRemove(productid))
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(viewHolder.itemView.getContext(),"Product successfuly removed from Wishlist.",Toast.LENGTH_LONG).show();
                                    wishlistItemsList.remove(viewHolder.getAdapterPosition());
                                    notifyDataSetChanged();
                                }
                                else Toast.makeText(viewHolder.itemView.getContext(),"Try again.",Toast.LENGTH_LONG).show();

                            }
                        });
            }
        });
        viewHolder.addtocart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseFirestore.collection("USERS").document(uid).update("cart", FieldValue.arrayUnion(productid))
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()) Toast.makeText(viewHolder.itemView.getContext(),"Product successfuly added to your Cart.",Toast.LENGTH_LONG).show();
                                else Toast.makeText(viewHolder.itemView.getContext(),"Try again.",Toast.LENGTH_LONG).show();
                            }
                        });
            }
        });
    }

    @Override
    public int getItemCount() {
        return wishlistItemsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView imageView;
        private TextView name;
        private TextView price;
        private Button addtocart;
        private Button wishlist;
        private Button outofstock;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.product_item_pic);
            name=itemView.findViewById(R.id.product_item_name);
            price =itemView.findViewById(R.id.product_item_price);
            addtocart=itemView.findViewById(R.id.product_item_cart);
            wishlist=itemView.findViewById(R.id.product_item_wishlist);
            outofstock=itemView.findViewById(R.id.product_item_outofstock);

        }
    }

}
