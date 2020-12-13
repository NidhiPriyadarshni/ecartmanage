package com.example.sweven;

import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.baoyachi.stepview.VerticalStepView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.kofigyan.stateprogressbar.StateProgressBar;

import java.util.ArrayList;
import java.util.List;

public class trackcus extends AppCompatActivity {
    String[] descriptionData = {"Details", "Status", "Photo", "Confirm"};
    public FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    FirebaseUser user;
    VerticalStepView stepView;
    int tp=1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trackcus);
        String pidt= getIntent().getExtras().getString("productId");
        tp=getIntent().getExtras().getInt("status");
        user= FirebaseAuth.getInstance().getCurrentUser();
        stepView=findViewById(R.id.stepview);
        setStepView();
//tp=firebaseFirestore.collection("PRODUCTS").document(pidt).
//TODO:product id se vo particular ka status set karna hain bus



    }

    private void setStepView() {
        stepView.setStepsViewIndicatorComplectingPosition(getList().size()).reverseDraw(false)
                .setStepViewTexts(getList())
                .setLinePaddingProportion(0.85f)
                .setStepsViewIndicatorCompletedLineColor(getColor(R.color.yellow))
                .setStepViewComplectedTextColor(R.color.colorAccent)
                .setStepsViewIndicatorCompleteIcon(getDrawable(R.drawable.completed))
                .setStepsViewIndicatorAttentionIcon(getDrawable(R.drawable.attention))
                .setStepsViewIndicatorDefaultIcon(getDrawable(R.drawable.default_icon));
stepView.setStepsViewIndicatorComplectingPosition(tp);
    }
    private List<String> getList(){
        List<String> list=new ArrayList<>();
        list.add("Ordered and Approved");
        list.add("Packed ");
        list.add("Shipped");
        list.add("Order out for delivery");
        return list;
    }
}