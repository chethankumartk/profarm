package com.example.chethan.industrain.ViewMadiroList;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.example.chethan.industrain.Login;
import com.example.chethan.industrain.R;
import com.example.chethan.industrain.RecyclerTouchListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class viewers extends AppCompatActivity  {
    FirebaseAuth auth;
    FirebaseUser user;
    ProgressDialog PD;
    String eid;
    CoordinatorLayout linearLayout;
    private RecyclerView mRecyclerView;

    String a,b;
    String kkk="";
    FloatingActionButton fab;
    String selected="";
    Query nextQuery;
    private ProgressBar mProgressBar;
    DocumentSnapshot lastvisible;
    vieweradapter mAdapter;
    viewcontract upload;
    SharedPreferences sharedPref;
    String location;
    List<viewcontract> mUploads;
    int mode;
    LinearLayoutManager layoutManager;
    Query query;









    private ProgressBar mProgressCircle;

    // private List<Upload> mUploads;
    RelativeLayout relativeLayout;
    FirebaseFirestore db;
    boolean isScrolling,isLastItemReached;





    @Override    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checking);

        getSupportActionBar().setTitle("Viewers");




        this.eid=getIntent().getStringExtra("refid");


        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        linearLayout=findViewById(R.id.userlayout);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        relativeLayout=findViewById(R.id.empty_view);
        sharedPref=getSharedPreferences("myPref",MODE_PRIVATE);

        location = sharedPref.getString("location", "default");
        mUploads=new ArrayList<>();



        PD = new ProgressDialog(this);

        PD.setMessage("Loading...");
        PD.setCancelable(true);
        PD.setCanceledOnTouchOutside(false);
        mRecyclerView = findViewById(R.id.recy);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setNestedScrollingEnabled(false);

        mProgressCircle = findViewById(R.id.progress_circle);
        fab=findViewById(R.id.fab);



        db=FirebaseFirestore.getInstance();

        mProgressBar = (ProgressBar) findViewById(R.id.progressbar);

        layoutManager=new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        gett();


        mRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(this, mRecyclerView,
                new RecyclerTouchListener.ClickListener() {
                    @Override
                    public void onClick(View view, int position) {




                        // showUpdateDeleteDialog(upload.getIdd(), upload.getName(),upload.getImageUrl(),
                        //       upload.getmAddress(),upload.getmCropname(),upload.getPhon());

                    }

                    @Override
                    public void onLongClick(View view, int position) {


                        //showUpdateDeleteDialog((Stringupload.getIdd(),upload.getName(),upload.getImageUrl());
                    }
                }));
        fab.hide();

        mProgressBar.setVisibility(View.INVISIBLE);
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override    protected void onResume() {
        if (auth.getCurrentUser() == null) {
            startActivity(new Intent(viewers.this, Login.class).putExtra("mode1",1));
            finish();
        }
        super.onResume();
    }

    public void gett()
    {

        //Toast.makeText(this, "1", Toast.LENGTH_SHORT).show();


        query=FirebaseFirestore.getInstance().collection("viewers")
                .whereEqualTo("refid",eid.toString().trim())
                .orderBy("refidd", Query.Direction.DESCENDING)
                .limit(15);





        query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                mUploads.clear();

                for (QueryDocumentSnapshot documentSnapshots: queryDocumentSnapshots)
                {
                    upload=documentSnapshots.toObject(viewcontract.class);
                    mUploads.add(upload);


                }
                try
                {
                    mAdapter=new vieweradapter(viewers.this,mUploads);
                    mRecyclerView.setAdapter(mAdapter);
                    // lastvisible=upload.getRefid();
                    lastvisible = queryDocumentSnapshots.getDocuments()
                            .get(queryDocumentSnapshots.size() -1);




                }catch (Exception e)
                {

                }

                //Toast.makeText(checking.this, "First page loaded", Toast.LENGTH_SHORT).show();

                RecyclerView.OnScrollListener onScrollListener=new RecyclerView.OnScrollListener() {
                    @Override
                    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                        super.onScrollStateChanged(recyclerView, newState);

                        if (newState== AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL)
                        {
                            isScrolling=true;

                        }
                    }

                    @Override
                    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                        super.onScrolled(recyclerView, dx, dy);
                        if(dy>0)

                        {
                            if(!isLastItemReached)
                            {
                                mProgressBar.setVisibility(View.VISIBLE);

                            }


                            // Toast.makeText(checking.this, "Next Page Loaded", Toast.LENGTH_SHORT).show();


                            int firstVisibleItem=layoutManager.findFirstVisibleItemPosition();
                            int visibleItemCount=layoutManager.getChildCount();
                            int totalItemCount=layoutManager.getItemCount();

                            if ( (isScrolling && firstVisibleItem + visibleItemCount == totalItemCount) && !isLastItemReached) {
                                isScrolling=false;

                                nextQuery=FirebaseFirestore.getInstance()
                                        .collection("viewers")
                                        .whereEqualTo("refid",eid.toString().trim())
                                        .orderBy("refidd", Query.Direction.DESCENDING)
                                        .startAfter(lastvisible)
                                        .limit(15);









                                nextQuery
                                        .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                    @Override
                                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                        for (QueryDocumentSnapshot documentSnapshots: queryDocumentSnapshots)
                                        {
                                            upload=documentSnapshots.toObject(viewcontract.class);
                                            mUploads.add(upload);


                                        }
                                        mAdapter.notifyDataSetChanged();

                                        //lastvisible=upload.getRefid();


                                        try {
                                            lastvisible = queryDocumentSnapshots.getDocuments()
                                                    .get(queryDocumentSnapshots.size() -1);


                                        }catch (Exception e)
                                        {

                                        }
                                        //  Toast.makeText(checking.this, "Next Page Loaded", Toast.LENGTH_SHORT).show();
                                        mProgressBar.setVisibility(View.INVISIBLE);


                                        if(queryDocumentSnapshots.size() < 15)
                                        {
                                            isLastItemReached=true;
                                            mProgressBar.setVisibility(View.INVISIBLE);
                                        }




                                    }
                                });




                            }
                        }

                    }
                };
                mRecyclerView.addOnScrollListener(onScrollListener);




            }
        });

    }

}
