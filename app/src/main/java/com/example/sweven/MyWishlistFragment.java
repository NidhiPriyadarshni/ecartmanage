package com.example.sweven;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class MyWishlistFragment extends Fragment {


    public MyWishlistFragment() {
        // Required empty public constructor
    }

    private RecyclerView myWishlistRecyclerView;
    public static FirebaseFirestore firebaseFirestore;
    public FirebaseUser user;
    public String uid;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=  inflater.inflate(R.layout.fragment_my_wishlist, container, false);
        myWishlistRecyclerView= view.findViewById(R.id.my_wishlist_recyclerview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        myWishlistRecyclerView.setLayoutManager(linearLayoutManager);
        List<WishlistModel> wishlistModelList = new ArrayList<>();

        firebaseFirestore = FirebaseFirestore.getInstance();
        user= FirebaseAuth.getInstance().getCurrentUser();
        uid=user.getUid();

        WishlistAdapter wishlistAdapter = new WishlistAdapter(wishlistModelList,true);
        myWishlistRecyclerView.setAdapter(wishlistAdapter);
        wishlistAdapter.notifyDataSetChanged();
        return  view;
        }

}
