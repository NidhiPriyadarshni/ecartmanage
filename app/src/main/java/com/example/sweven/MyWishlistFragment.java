package com.example.sweven;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;

import static com.example.sweven.DBqueries.loadWishlistList;
import static com.example.sweven.DBqueries.wishlistItemsList;


/**
 * A simple {@link Fragment} subclass.
 */
public class MyWishlistFragment extends Fragment {

    public MyWishlistFragment() {
        // Required empty public constructor
    }

    private RecyclerView wishlistRecyclerView;
    private ImageView noInternetConnection;
    public static SwipeRefreshLayout swipeRefreshLayout;
    private FirebaseFirestore firebaseFirestore;
    private WishlistAdapter adapter;
    ConnectivityManager connectivityManager;
    NetworkInfo networkInfo;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_wishlist, container, false);
        noInternetConnection = view.findViewById(R.id.my_wishlist_no_internet_connection);
        swipeRefreshLayout = view.findViewById(R.id.my_wishlist_refresh_layout);
        swipeRefreshLayout.setColorSchemeColors(getContext().getResources().getColor(R.color.colorPrimary), getContext().getResources().getColor(R.color.colorPrimary), getContext().getResources().getColor(R.color.colorPrimary));
        wishlistRecyclerView = view.findViewById(R.id.my_wishlist_recyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        wishlistRecyclerView.setLayoutManager(layoutManager);

        connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        networkInfo = connectivityManager.getActiveNetworkInfo();


        if (networkInfo != null && networkInfo.isConnected() == true) {
            noInternetConnection.setVisibility(View.GONE);

            loadWishlistList(wishlistRecyclerView,getContext());
            /*if(wishlistItemsList.size()==0){
                loadWishlistList(wishlistRecyclerView,getContext());
            }else{
                adapter=new WishlistAdapter(wishlistItemsList);
                adapter.notifyDataSetChanged();wishlistRecyclerView.setAdapter(adapter);
                Toast.makeText(getContext(),"list already exists",Toast.LENGTH_LONG).show();
            }
            //wishlistRecyclerView.setAdapter(adapter);*/



        } else {
            noInternetConnection.setVisibility(View.VISIBLE);
        }


        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                swipeRefreshLayout.setRefreshing(true);
                wishlistItemsList.clear();
                if (networkInfo != null && networkInfo.isConnected() == true) {
                    noInternetConnection.setVisibility(View.GONE);

                    adapter = new WishlistAdapter(wishlistItemsList);
                    wishlistRecyclerView.setAdapter(adapter);
                    loadWishlistList(wishlistRecyclerView,getContext());



                } else {
                    noInternetConnection.setVisibility(View.VISIBLE);
                }
                swipeRefreshLayout.setRefreshing(false);

            }
        });

        return view;
    }



}
