package com.example.sweven;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.ArraySet;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class DBqueries {
    public static String userid= FirebaseAuth.getInstance().getCurrentUser().getUid();
    public static FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    public static List<CategoryModel> categoryModelList = new ArrayList<>();
    public static List<ProductItemModel> productItemsList =new ArrayList<>();
    public static List<ProductItemModel> wishlistItemsList =new ArrayList<>();
    public static List<ProductItemModel> cartItemsList =new ArrayList<>();
    public static List<ProductItemModel> orderItemsList =new ArrayList<>();
    public static List<List<HomePageModel>> lists = new ArrayList<>();
    public static List<String> loadCategoriesNames = new ArrayList<>();
    public static double totalamt;
    static List<String> wishlistid=new ArrayList<>();
    static List<String> cartid=new ArrayList<>();





    public static void loadCategories(final RecyclerView categoryRecyclerview, final Context context) {


        firebaseFirestore.collection("CATEGORIES")/*.orderBy("index")*/.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                        categoryModelList.add(new CategoryModel(documentSnapshot.get("icon").toString(), documentSnapshot.get("categoryName").toString()));
                    }
                    CategoryAdapter categoryAdapter = new CategoryAdapter(categoryModelList);
                    categoryRecyclerview.setAdapter(categoryAdapter);
                    categoryAdapter.notifyDataSetChanged();
                } else {
                    String error = task.getException().getMessage();
                    Toast.makeText(context, error, Toast.LENGTH_LONG).show();
                }
            }
        });
    }


    public static void loadFragmentData(final RecyclerView homePageRecyclerview, final Context context, final int index, String categoryName){
        firebaseFirestore.collection("CATEGORIES")
                .document(categoryName.toUpperCase())
                .collection("TOP_DEALS")
                .orderBy("index").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                if ((long) documentSnapshot.get("view_type") == 0) {
                                    List<SliderModel> sliderModelList = new ArrayList<>();
                                    long no_of_banners = (long) documentSnapshot.get("no_of_banners");
                                    for (long x = 1; x < no_of_banners + 1; x++) {
                                        sliderModelList.add(new SliderModel(documentSnapshot.get("banner_" + x).toString(), documentSnapshot.get("banner_" + x + "_background").toString()));
                                    }
                                    lists.get(index).add(new HomePageModel(0, sliderModelList));

                                } else if ((long) documentSnapshot.get("view_type") == 1) {
                                    lists.get(index).add(new HomePageModel(1, documentSnapshot.get("strip_ad_banner").toString(), documentSnapshot.get("background").toString()));
                                } else if ((long) documentSnapshot.get("view_type") == 2) {

                                    List<WishlistModel> viewAllProductList = new ArrayList<>();

                                    List<HorizontalProductScrollModel> horizontalProductScrollModelList = new ArrayList<>();
                                    long no_of_products = (long) documentSnapshot.get("no_of_products");
                                    for (long x = 1; x < no_of_products + 1; x++) {
                                        horizontalProductScrollModelList.add(
                                                new HorizontalProductScrollModel
                                                        (documentSnapshot.get("product_ID_" + x).toString(),
                                                                documentSnapshot.get("product_image_" + x).toString(),
                                                                documentSnapshot.get("product_title_" + x).toString(),
                                                                documentSnapshot.get("product_subtitle_" + x).toString(),
                                                                documentSnapshot.get("product_price_" + x).toString()));
                                        viewAllProductList.add(
                                                new WishlistModel(documentSnapshot.get("product_image_"+x).toString()
                                                        ,documentSnapshot.get("product_full_title_" + x).toString()
                                                        ,(long)documentSnapshot.get("free_coupons_" + x)
                                                        ,documentSnapshot.get("average_rating_" + x).toString()
                                                        ,(long)documentSnapshot.get("total_ratings_" + x)
                                                        ,documentSnapshot.get("product_price_" + x).toString()
                                                        ,documentSnapshot.get("cutted_price_" + x).toString()
                                                        ,(boolean)documentSnapshot.get("COD_" + x)));
                                    }
                                    lists.get(index).add(new HomePageModel(2, documentSnapshot.get("layout_title").toString(), documentSnapshot.get("layout_background").toString(), horizontalProductScrollModelList,viewAllProductList));

                                } else if ((long) documentSnapshot.get("view_type") == 3) {
                                    List<HorizontalProductScrollModel> gridLayoutModelList = new ArrayList<>();
                                    long no_of_products = (long) documentSnapshot.get("no_of_products");
                                    for (long x = 1; x < no_of_products + 1; x++) {
                                        gridLayoutModelList.add(
                                                new HorizontalProductScrollModel
                                                        (documentSnapshot.get("product_ID_" + x).toString(),
                                                                documentSnapshot.get("product_image_" + x).toString(),
                                                                documentSnapshot.get("product_title_" + x).toString(),
                                                                documentSnapshot.get("product_subtitle_" + x).toString(),
                                                                documentSnapshot.get("product_price_" + x).toString()));
                                    }
                                    lists.get(index).add(new HomePageModel(3, documentSnapshot.get("layout_title").toString(), documentSnapshot.get("layout_background").toString(), gridLayoutModelList));

                                }

                            }
                            HomePageAdapter adapter = new HomePageAdapter(lists.get(index));
                            homePageRecyclerview.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                            //HomeFragment.swipeRefreshLayout.setRefreshing(false);

                        } else {
                            String error = task.getException().getMessage();
                            Toast.makeText(context, error, Toast.LENGTH_LONG).show();
                        }
                    }
                });


    }

    public static void loadProductList(final RecyclerView recyclerView, final Context context) {


        firebaseFirestore.collection("PRODUCTS")/*.orderBy("index")*/.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()&&task.getResult()!=null) {
                    for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                        String productId="M..",name="xyz",picurl=null;
                        double price=100;
                        boolean outOfStock=false;
                        if(documentSnapshot.get("productId")!=null)productId=documentSnapshot.get("productId").toString();
                        if(documentSnapshot.get("name")!=null)name=documentSnapshot.get("name").toString();
                        if(documentSnapshot.get("price")!=null)price=documentSnapshot.getDouble("price");
                        if(documentSnapshot.get("picUrl")!=null)picurl=documentSnapshot.get("picUrl").toString();
                        if(documentSnapshot.get("isOutOfStock")!=null)outOfStock=documentSnapshot.getBoolean("isOutOfStock");
                        productItemsList.add(new ProductItemModel(productId,name,price,picurl,outOfStock));

                        if(wishlistid!=null&&wishlistid.contains(productId))wishlistItemsList.add(productItemsList.get(productItemsList.size()-1));


                        if(productId!=null) {
                           try {
                               if (wishlistid.contains(productId))
                                   wishlistItemsList.add(productItemsList.get(productItemsList.size() - 1));
                           }
                           catch (NullPointerException e)
                           {

                           }
                        }

                    }
                    ProductListAdapter adapter=new ProductListAdapter(productItemsList);
                    adapter.notifyDataSetChanged();
                    recyclerView.setAdapter(adapter);

                } else {
                    String error = task.getException().getMessage();
                    Toast.makeText(context, error, Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public static void loadWishlistList(final RecyclerView wishlistRecyclerView, final Context context) {


        firebaseFirestore.collection("USERS").document(userid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()&&task.getResult()!=null){
                    DocumentSnapshot document=task.getResult();
                    List<String> productidlist=(List<String>) document.get("wishlist");

                    if(wishlistItemsList!=null)wishlistItemsList.clear();
                    if(productidlist!=null)for(String id:productidlist){
                 

                        firebaseFirestore.collection("PRODUCTS").document(id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if(task.isSuccessful()&&task.getResult()!=null){
                                    DocumentSnapshot documentSnapshot=task.getResult();
                                    String productId = "M..", name = "xyz", picurl = null;
                                    double price = 100;
                                    boolean outOfStock = false;
                                    if (documentSnapshot.get("productId") != null)
                                        productId = documentSnapshot.get("productId").toString();
                                    if (documentSnapshot.get("name") != null)
                                        name = documentSnapshot.get("name").toString();
                                    if (documentSnapshot.get("price") != null)
                                        price = documentSnapshot.getDouble("price");
                                    if (documentSnapshot.get("picUrl") != null)
                                        picurl = documentSnapshot.get("picUrl").toString();
                                    if (documentSnapshot.get("isOutOfStock") != null)
                                        outOfStock = documentSnapshot.getBoolean("isOutOfStock");
                                    wishlistItemsList.add(new ProductItemModel(productId, name, price, picurl, outOfStock));
                                    WishlistAdapter adapter=new WishlistAdapter(wishlistItemsList);
                                    adapter.notifyDataSetChanged();
                                    wishlistRecyclerView.setAdapter(adapter);

                                }
                            }
                        });
                    }
                }}
            }
        });

    }


    public static void loadCartList(final RecyclerView cartRecyclerView, final CartAdapter.OnQtyChangeListener listener, final TextView totaltv) {


        firebaseFirestore.collection("USERS").document(userid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot document=task.getResult();
                    List<String> productidlist=(List<String>) document.get("cart");
                    if(cartItemsList!=null)cartItemsList.clear();
                    totalamt = 0;

                    if(productidlist!=null)for(String id:productidlist){
                   

                        firebaseFirestore.collection("PRODUCTS").document(id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if(task.isSuccessful()&&task.getResult()!=null){
                                    DocumentSnapshot documentSnapshot=task.getResult();
                                    final String[] productId = {"M.."};
                                    String name = "xyz";
                                    String picurl = null;
                                    double price = 0;
                                    final double[] qty = {1};
                                    boolean outOfStock = false;
                                    if (documentSnapshot.get("productId") != null)
                                        productId[0] = documentSnapshot.get("productId").toString();
                                    if (documentSnapshot.get("name") != null)
                                        name = documentSnapshot.get("name").toString();
                                    if (documentSnapshot.get("price") != null)
                                        price = documentSnapshot.getDouble("price");
                                    if (documentSnapshot.get("picUrl") != null)
                                        picurl = documentSnapshot.get("picUrl").toString();
                                    if (documentSnapshot.get("isOutOfStock") != null)
                                        outOfStock = documentSnapshot.getBoolean("isOutOfStock");
                                    final String finalName = name;
                                    final double finalPrice = price;
                                    final String finalPicurl = picurl;
                                    final boolean finalOutOfStock = outOfStock;
                                    firebaseFirestore.collection("USERS").document(userid).collection("CART").document(productId[0]).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                            if(task.isSuccessful()&&task.getResult()!=null){
                                                DocumentSnapshot doc=task.getResult();
                                                qty[0] =1;
                                                if (doc.get("qty") != null)
                                                    qty[0] = doc.getDouble("qty");
                                                cartItemsList.add(new ProductItemModel(productId[0], finalName, finalPrice, finalPicurl,finalOutOfStock, (int) qty[0]));
                                                CartAdapter adapter=new CartAdapter(cartItemsList,listener);
                                                adapter.notifyDataSetChanged();
                                                cartRecyclerView.setAdapter(adapter);
                                                totalamt+= qty[0]*finalPrice;
                                                totaltv.setText("Rs. "+ totalamt +"/-");
                                            }
                                        }
                                    });



                                }
                            }
                        });
                    }}
                }
            }
        });

    }

    public static void loadOrderList(final RecyclerView orderRecyclerView) {


        firebaseFirestore.collection("USERS").document(userid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot document=task.getResult();
                    List<String> productidlist=(List<String>) document.get("order");
                    if(orderItemsList!=null)orderItemsList.clear();
                    if(productidlist!=null)for(String id:productidlist){
                        firebaseFirestore.collection("PRODUCTS").document(id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if(task.isSuccessful()&&task.getResult()!=null){
                                    DocumentSnapshot documentSnapshot=task.getResult();
                                    final String[] productId = {"M.."};
                                    String name = "xyz";
                                    String picurl = null;
                                    double price = 0;
                                    final double[] qty = {1};
                                    boolean outOfStock = false;
                                    if (documentSnapshot.get("productId") != null)
                                        productId[0] = documentSnapshot.get("productId").toString();
                                    if (documentSnapshot.get("name") != null)
                                        name = documentSnapshot.get("name").toString();
                                    if (documentSnapshot.get("price") != null)
                                        price = documentSnapshot.getDouble("price");
                                    if (documentSnapshot.get("picUrl") != null)
                                        picurl = documentSnapshot.get("picUrl").toString();
                                    if (documentSnapshot.get("isOutOfStock") != null)
                                        outOfStock = documentSnapshot.getBoolean("isOutOfStock");
                                    final String finalName = name;
                                    final double[] finalPrice = {price};
                                    final String finalPicurl = picurl;
                                    final boolean finalOutOfStock = outOfStock;
                                    firebaseFirestore.collection("USERS").document(userid).collection("ORDER").document(productId[0]).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                            if(task.isSuccessful()&&task.getResult()!=null){
                                                DocumentSnapshot doc=task.getResult();
                                                qty[0] =1;
                                                if (doc.get("qty") != null)
                                                    qty[0] = doc.getDouble("qty");
                                                if(doc.get("price")!=null) finalPrice[0] =doc.getDouble("price");
                                                orderItemsList.add(new ProductItemModel(productId[0], finalName, finalPrice[0], finalPicurl,finalOutOfStock, (int) qty[0]));
                                                MyOrderAdapter adapter=new MyOrderAdapter(orderItemsList);
                                                adapter.notifyDataSetChanged();
                                                orderRecyclerView.setAdapter(adapter);
                                            }
                                        }
                                    });



                                }
                            }
                        });
                    }
                }
            }
        });

    }


    public static void loadWishlist() {


        firebaseFirestore.collection("USERS").document(userid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()&&task.getResult()!=null){
                    DocumentSnapshot document=task.getResult();
                    if(wishlistid!=null)wishlistid.clear();
                    wishlistid=(List<String>) document.get("wishlist");
                    if(wishlistItemsList!=null)wishlistItemsList.clear();
                    /*for(String id:productidlist){
                        firebaseFirestore.collection("PRODUCTS").document(id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if(task.isSuccessful()){
                                    DocumentSnapshot documentSnapshot=task.getResult();
                                    String productId = "M..", name = "xyz", picurl = null;
                                    double price = 100;
                                    boolean outOfStock = false;
                                    if (documentSnapshot.get("productId") != null)
                                        productId = documentSnapshot.get("productId").toString();
                                    if (documentSnapshot.get("name") != null)
                                        name = documentSnapshot.get("name").toString();
                                    if (documentSnapshot.get("price") != null)
                                        price = documentSnapshot.getDouble("price");
                                    if (documentSnapshot.get("picUrl") != null)
                                        picurl = documentSnapshot.get("picUrl").toString();
                                    if (documentSnapshot.get("isOutOfStock") != null)
                                        outOfStock = documentSnapshot.getBoolean("isOutOfStock");
                                    wishlistItemsList.add(new ProductItemModel(productId, name, price, picurl, outOfStock));
                                    String productId="M..",name="xyz",picurl=null;
                                    double price=100;
                                    boolean outOfStock=false;
                                    if(documentSnapshot.get("productId")!=null)productId=documentSnapshot.get("productId").toString();
                                    if(documentSnapshot.get("name")!=null)name=documentSnapshot.get("name").toString();
                                    if(documentSnapshot.get("price")!=null)price=documentSnapshot.getDouble("price");
                                    if(documentSnapshot.get("picUrl")!=null)picurl=documentSnapshot.get("picUrl").toString();
                                    if(documentSnapshot.get("isOutOfStock")!=null)outOfStock=documentSnapshot.getBoolean("isOutOfStock");
                                    wishlistItemsList.add(new ProductItemModel(productId,name,price,picurl,outOfStock));


                                }
                            }
                        });
                    }*/
                }
            }
        });

    }


    public static void loadCart() {


        firebaseFirestore.collection("USERS").document(userid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()&&task.getResult()!=null){
                    DocumentSnapshot document=task.getResult();
                    List<String> productidlist=(List<String>) document.get("cart");

                    if(cartItemsList!=null)cartItemsList.clear();

                    if(productidlist!=null)for(String id:productidlist){

                    

                        firebaseFirestore.collection("PRODUCTS").document(id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if(task.isSuccessful()&&task.getResult()!=null){
                                    DocumentSnapshot documentSnapshot=task.getResult();
                                    String productId = "M..", name = "xyz", picurl = null;
                                    double price = 0;
                                    boolean outOfStock = false;
                                    if (documentSnapshot.get("productId") != null)
                                        productId = documentSnapshot.get("productId").toString();
                                    if (documentSnapshot.get("name") != null)
                                        name = documentSnapshot.get("name").toString();
                                    if (documentSnapshot.get("price") != null)
                                        price = documentSnapshot.getDouble("price");
                                    if (documentSnapshot.get("picUrl") != null)
                                        picurl = documentSnapshot.get("picUrl").toString();
                                    if (documentSnapshot.get("isOutOfStock") != null)
                                        outOfStock = documentSnapshot.getBoolean("isOutOfStock");
                                    cartItemsList.add(new ProductItemModel(productId, name, price, picurl, outOfStock));




                                }
                            }
                        });
                    }
                }}
                else
                {
                    Log.d("ERR","ERRor");
                }
            }
        });

    }
}
