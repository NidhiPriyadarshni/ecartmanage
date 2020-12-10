package com.example.sweven;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import static com.example.sweven.DBqueries.loadCartList;
import static com.example.sweven.DBqueries.cartItemsList;
import static com.example.sweven.DBqueries.totalamt;


/**
 * A simple {@link Fragment} subclass.
 */
public class MyCartFragment extends Fragment implements CartAdapter.OnQtyChangeListener {


    public MyCartFragment() {
        // Required empty public constructor
    }

    private RecyclerView cartItemsRecyclerView;
    private Button continueBtn;
    public TextView totalv;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_cart, container, false);
        cartItemsRecyclerView = view.findViewById(R.id.cart_items_recyclerview);
        continueBtn=view.findViewById(R.id.cart_continue_btn);
        totalv=(TextView) view.findViewById(R.id.total_cart_amounts);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        cartItemsRecyclerView.setLayoutManager(linearLayoutManager);

        loadCartList(cartItemsRecyclerView,this,totalv);
        CartAdapter cartAdapter = new CartAdapter(cartItemsList,this);
        cartItemsRecyclerView.setAdapter(cartAdapter);
        cartAdapter.notifyDataSetChanged();
        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent deliveryIntent = new Intent(getContext(),DeliveryActivity.class);
                getContext().startActivity(deliveryIntent);
            }
        });
        return view;
    }

    @Override
    public void onQtyChange() {
        totalv.setText("Rs. "+ Integer.toString((int) totalamt) +"/-");
    }
}
