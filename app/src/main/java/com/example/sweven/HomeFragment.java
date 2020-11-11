package com.example.sweven;


import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import static com.example.sweven.DBqueries.loadProductList;
import static com.example.sweven.DBqueries.productItemsList;
import static com.example.sweven.DBqueries.categoryModelList;
import static com.example.sweven.DBqueries.lists;
import static com.example.sweven.DBqueries.loadCategories;
import static com.example.sweven.DBqueries.loadCategoriesNames;
import static com.example.sweven.DBqueries.loadFragmentData;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {


    public HomeFragment() {
        // Required empty public constructor
    }

    private List<ProductItemModel> productlist = new ArrayList<>();
    private RecyclerView homePageRecyclerView;
    private ImageView noInternetConnection;
    public static SwipeRefreshLayout swipeRefreshLayout;
    private FirebaseFirestore firebaseFirestore;
    private ProductListAdapter adapter;
    ConnectivityManager connectivityManager;
    NetworkInfo networkInfo;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        noInternetConnection = view.findViewById(R.id.no_internet_connection);
        swipeRefreshLayout = view.findViewById(R.id.refresh_layout);
        swipeRefreshLayout.setColorSchemeColors(getContext().getResources().getColor(R.color.colorPrimary), getContext().getResources().getColor(R.color.colorPrimary), getContext().getResources().getColor(R.color.colorPrimary));
        homePageRecyclerView = view.findViewById(R.id.homepage_recyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        homePageRecyclerView.setLayoutManager(layoutManager);

        connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        networkInfo = connectivityManager.getActiveNetworkInfo();


        if (networkInfo != null && networkInfo.isConnected() == true) {
            noInternetConnection.setVisibility(View.GONE);

            if (productItemsList.size() == 0) {
                loadProductList(homePageRecyclerView, getContext());
            } else {
                adapter = new ProductListAdapter(productItemsList);
                adapter.notifyDataSetChanged();
            }
            homePageRecyclerView.setAdapter(adapter);


        } else {
            noInternetConnection.setVisibility(View.VISIBLE);
        }


        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                swipeRefreshLayout.setRefreshing(true);
                productItemsList.clear();
                if (networkInfo != null && networkInfo.isConnected() == true) {
                    noInternetConnection.setVisibility(View.GONE);
                    adapter = new ProductListAdapter(productItemsList);
                    homePageRecyclerView.setAdapter(adapter);
                    loadProductList(homePageRecyclerView, getContext());

                } else {
                    noInternetConnection.setVisibility(View.VISIBLE);
                }
                swipeRefreshLayout.setRefreshing(false);

            }
        });

        return view;
    }

}