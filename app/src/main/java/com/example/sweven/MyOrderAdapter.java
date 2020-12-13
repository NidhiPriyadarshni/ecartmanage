package com.example.sweven;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

import static com.example.sweven.DBqueries.totalamt;

public class MyOrderAdapter extends RecyclerView.Adapter<MyOrderAdapter.ViewHolder> {


    private List<ProductItemModel> myOrderItemModelList;
    public FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    public FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
    public String uid=user.getUid();

    public MyOrderAdapter(List<ProductItemModel> myOrderItemModelList) {
        this.myOrderItemModelList = myOrderItemModelList;
    }

    @NonNull
    @Override
    public MyOrderAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.current_order_item_layout, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyOrderAdapter.ViewHolder viewHolder, int position) {
        final ProductItemModel productitem=myOrderItemModelList.get(position);
        String itemname=productitem.getName();
        String itempic=productitem.getPicurl();
        final String itemprice="Rs "+productitem.getPrice();
        final String itemqty= productitem.getQty()+"";
        final String productid=productitem.getProductId();
        viewHolder.productQuantity.setText(itemqty);
        if (!itempic.equals("null")){
            Glide.with(viewHolder.itemView.getContext()).load(itempic)./*apply(new RequestOptions().placeholder(R.mipmap.icon_placeholder)).*/into(viewHolder.imageView);
        }else{

        }
        viewHolder.name.setText(itemname);
        viewHolder.price.setText(itemprice);
        viewHolder.status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), trackcus.class);
                intent.putExtra("productId",productid);
                view.getContext().startActivity(intent);
            }
        });

        viewHolder.cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseFirestore.collection("USERS").document(uid).update("order", FieldValue.arrayRemove(productid))
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(viewHolder.itemView.getContext(),"Product successfuly cancelled.",Toast.LENGTH_LONG).show();
                                    firebaseFirestore.collection("USERS").document(user.getUid()).collection("ORDER").document(productid).delete();
                                    double ttl=Double.parseDouble(viewHolder.productQuantity.getText().toString());
                                    ttl=ttl*productitem.getPrice();
                                    myOrderItemModelList.remove(viewHolder.getAdapterPosition());
                                    notifyDataSetChanged();
                                }
                                else Toast.makeText(viewHolder.itemView.getContext(),"Try again.",Toast.LENGTH_LONG).show();

                            }
                        });
            }
        });
    }

    @Override
    public int getItemCount() {
        return myOrderItemModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView productQuantity;
        private ImageView imageView;
        private TextView name;
        private TextView price;
        private Button cancle;
        private Button status;

        public ViewHolder(@NonNull final View itemView) {
            super(itemView);
            productQuantity = itemView.findViewById(R.id.order_qty);
            imageView=itemView.findViewById(R.id.order_product_image);
            name=itemView.findViewById(R.id.order_product_title);
            price =itemView.findViewById(R.id.order_price);
            cancle=itemView.findViewById(R.id.order_cancle);
            status=itemView.findViewById(R.id.order_status);
        }
        /*private void setData(int resource,String title,String deliveredDate,int rating){
            productImage.setImageResource(resource);
            productTitle.setText(title);
            if(deliveredDate.equals("Cancelled")){
                orderIndicator.setImageTintList(ColorStateList.valueOf(itemView.getContext().getColor(R.color.btnRed)));
            }else{
                orderIndicator.setImageTintList(ColorStateList.valueOf(itemView.getContext().getColor(R.color.successGreen)));
            }
            deliveryStatus.setText(deliveredDate);

            //* ********** RATINGS LAYOUT *******
            setRating(rating);
            for (int i = 0; i < rateNowContainer.getChildCount(); i++) {
                final int starPosition = i;
                rateNowContainer.getChildAt(i).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setRating(starPosition);
                    }
                });
            }
            //*********** RATINGS LAYOUT ******

        }
        private void setRating(int starPosition) {
            for (int x = 0; x < rateNowContainer.getChildCount(); x++) {
                ImageView starBtn = (ImageView) rateNowContainer.getChildAt(x);
                starBtn.setImageTintList(ColorStateList.valueOf(Color.parseColor("#bebebe")));
                if (x <= starPosition) {
                    starBtn.setImageTintList(ColorStateList.valueOf(Color.parseColor("#ffbb00")));
                }
            }
        }*/
    }
}
