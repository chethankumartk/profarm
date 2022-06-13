package com.example.chethan.industrain;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chethan.industrain.homeadapters.Upload1;
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

public class checking extends AppCompatActivity  {
    FirebaseAuth auth;
    FirebaseUser user;
    ProgressDialog PD;
    String cropsearch;
    CoordinatorLayout linearLayout;
    private RecyclerView mRecyclerView;

    String a,b;
    String kkk="";
    FloatingActionButton fab;
    String selected="";
    Query nextQuery;
    int plh=0,phl=0,dad=1;
    private TextView removefilter;



    int total_item=0, last_visible_item;
    boolean isLoading=false,isMaxData=false;
    private ProgressBar mProgressBar;
    boolean status=false;
    String last_node="",last_key="";
    DocumentSnapshot lastvisible;
    sumnecheckadapter mAdapter;
    Upload1 upload;
    SharedPreferences sharedPref;
    public int stateg=1,statev=1,stateG=1,stated=1,statef=1,stateo=1;
    String location;
    List<Upload1> mUploads;
    String filters="";
    int mode;
    private RadioGroup rg;
    private RadioButton radioButton;
    TextView oth,gra,veg,dry,fru,gre;
    private TextView apply;
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
        // getSupportActionBar().hide();

        this.cropsearch=getIntent().getStringExtra("cropsearch");
        this.mode=getIntent().getIntExtra("mode",0);

       // this.filters=getIntent().getStringExtra("filters");
       // if(filters == "Enilla")
        //{
          //  filters="";
        //}
        getSupportActionBar().setTitle("Shop");
        String a=getIntent().getStringExtra("filters");






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
        if(!a.equalsIgnoreCase("none"))
        {
            this.filters=a;
            if(filters.equals("Fruits"))
            {
                selected="Fruits";
                statef=2;

            }
            if(filters.equals("grains"))
            {
                selected="grains";
                stateg=2;

            }
            if(filters.equals("Greens"))
            {
                selected="Greens";
                stateG=2;

            }
            if(filters.equals("DryFruit"))
            {
                selected="DryFruit";
                stated=2;

            }
            if(filters.equals("Vegetables"))
            {
                selected="Vegetables";
                statev=2;

            }
            if(filters.equals("Others"))
            {
                selected="Others";
                stateo=2;

            }


        }



        db=FirebaseFirestore.getInstance();

        mProgressBar = (ProgressBar) findViewById(R.id.progressbar);

         layoutManager=new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        gett();


        mRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(this, mRecyclerView,
                new RecyclerTouchListener.ClickListener() {
                    @Override
                    public void onClick(View view, int position) {
                        Upload1 uploadCurrent = mUploads.get(position);

                        Toast.makeText(checking.this, ""+uploadCurrent.getCropname(), Toast.LENGTH_SHORT).show();



                        // showUpdateDeleteDialog(upload.getIdd(), upload.getName(),upload.getImageUrl(),
                        //       upload.getmAddress(),upload.getmCropname(),upload.getPhon());

                    }

                    @Override
                    public void onLongClick(View view, int position) {


                        //showUpdateDeleteDialog((Stringupload.getIdd(),upload.getName(),upload.getImageUrl());
                    }
                }));

        mProgressBar.setVisibility(View.INVISIBLE);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Filterdialog();
            }
        });

    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override    protected void onResume() {
        if (auth.getCurrentUser() == null) {
            startActivity(new Intent(checking.this, Login.class).putExtra("mode1",1));
            finish();
        }
        super.onResume();
    }
    public void Filterdialog()
    {

        final Dialog dialog=new Dialog(this);
        dialog.setContentView(R.layout.dialogfilter);
        gra=dialog.findViewById(R.id.grain);
        veg=dialog.findViewById(R.id.vegetable);
        dry=dialog.findViewById(R.id.dry);
        oth=dialog.findViewById(R.id.other);
        fru=dialog.findViewById(R.id.fruit);
        gre=dialog.findViewById(R.id.green);
        apply=dialog.findViewById(R.id.apply);
        removefilter=dialog.findViewById(R.id.removefilter);
        removefilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stateg=1;
                stated=1;
                statev=1;
                stateo=1;
                stateG=1;
                statef=1;
                selected="";
                dad=1;
                plh=0;phl=1;
                removefilter.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.orngeback));
                removefilter.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.white));
                veg.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.searchback1));
                veg.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.colorPrimary));
                gre.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.searchback1));
                gre.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.colorPrimary));
                dry.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.searchback1));
                dry.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.colorPrimary));
                oth.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.searchback1));
                oth.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.colorPrimary));
                fru.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.searchback1));
                fru.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.colorPrimary));
                gra.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.searchback1));
                gra.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.colorPrimary));


            }
        });
        if(stateg==2)
        {
        gra.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.orngeback));
        gra.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.white));

        selected="";
        stateg=1;
        }
        else if(statev==2)
        {
            veg.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.orngeback));
            veg.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.white));
        }
        else if(stateo==2)
        {
            oth.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.orngeback));
            oth.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.white));
        }
        else if(stateG==2)
        {
            gre.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.orngeback));
            gre.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.white));
        }
        else if(statef==2)
        {
            fru.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.orngeback));
            fru.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.white));

        }
        else if(stated==2)
        {
            dry.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.orngeback));
            dry.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.white));
        }










        gra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (stateg==1)
                {
                    gra.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.orngeback));
                    gra.setTextColor(Color.parseColor("#ffffff"));
                    selected="grains";
                    stateg=2;

                }
                else if(stateg==2)
                {
                    gra.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.searchback1));
                    gra.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.colorPrimary));

                    selected="";
                    stateg=1;
                }
                veg.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.searchback1));
                veg.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.colorPrimary));
                gre.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.searchback1));
                gre.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.colorPrimary));
                dry.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.searchback1));
                dry.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.colorPrimary));
                oth.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.searchback1));
                oth.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.colorPrimary));
                fru.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.searchback1));
                fru.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.colorPrimary));
                statef=1;
                stateG=1;
                stateo=1;
                statev=1;
                stated=1;





            }
        });
        veg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (statev==1)
                {
                    veg.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.orngeback));
                    veg.setTextColor(Color.parseColor("#ffffff"));
                    selected="Vegetables";
                    statev=2;

                }
                else if(statev==2)
                {
                    veg.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.searchback1));
                    veg.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.colorPrimary));
                    selected="";
                    statev=1;
                }
                gra.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.searchback1));
                gra.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.colorPrimary));
                gre.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.searchback1));
                gre.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.colorPrimary));
                dry.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.searchback1));
                dry.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.colorPrimary));
                oth.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.searchback1));
                oth.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.colorPrimary));
                fru.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.searchback1));
                fru.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.colorPrimary));
                statef=1;
                stateG=1;
                stateo=1;
                stateg=1;
                stated=1;




            }
        });
        oth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (stateo==1)
                {
                    oth.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.orngeback));
                    oth.setTextColor(Color.parseColor("#ffffff"));
                    selected="Others";
                    stateo=2;

                }
                else if(stateo==2)
                {
                    oth.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.searchback1));
                    oth.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.colorPrimary));
                    selected="";
                    stateo=1;
                }
                veg.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.searchback1));
                veg.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.colorPrimary));
                gre.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.searchback1));
                gre.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.colorPrimary));
                dry.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.searchback1));
                dry.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.colorPrimary));
                gra.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.searchback1));
                gra.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.colorPrimary));
                fru.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.searchback1));
                fru.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.colorPrimary));
                statef=1;
                stateG=1;
                stateg=1;
                statev=1;
                stated=1;


            }
        });
        gre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (stateG==1)
                {
                    gre.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.orngeback));
                    gre.setTextColor(Color.parseColor("#ffffff"));
                    selected="Greens";
                    stateG=2;

                }
                else if(stateG==2)
                {
                    gre.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.searchback1));
                    gre.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.colorPrimary));
                    selected="";
                    stateG=1;
                }

                veg.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.searchback1));
                veg.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.colorPrimary));
                gra.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.searchback1));
                gra.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.colorPrimary));
                dry.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.searchback1));
                dry.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.colorPrimary));
                oth.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.searchback1));
                oth.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.colorPrimary));
                fru.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.searchback1));
                fru.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.colorPrimary));
                statef=1;
                stateg=1;
                stateo=1;
                statev=1;
                stated=1;


            }
        });
        fru.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (statef==1)
                {
                    fru.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.orngeback));
                    fru.setTextColor(Color.parseColor("#ffffff"));
                    selected="Fruits";
                    statef=2;

                }
                else if(statef==2)
                {
                    fru.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.searchback1));
                    fru.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.colorPrimary));
                    selected="";
                    statef=1;
                }
                veg.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.searchback1));
                veg.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.colorPrimary));
                gre.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.searchback1));
                gre.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.colorPrimary));
                dry.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.searchback1));
                dry.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.colorPrimary));
                oth.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.searchback1));
                oth.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.colorPrimary));
                gra.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.searchback1));
                gra.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.colorPrimary));
                stateg=1;
                stateG=1;
                stateo=1;
                statev=1;
                stated=1;



            }
        });
        dry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (stated==1)
                {
                    dry.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.orngeback));
                    dry.setTextColor(Color.parseColor("#ffffff"));
                    selected="DryFruit";
                    stated=2;

                }
                else if(stated==2)
                {
                    dry.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.searchback1));
                    dry.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.colorPrimary));
                    selected="";
                    stated=1;
                }

                veg.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.searchback1));
                veg.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.colorPrimary));
                gre.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.searchback1));
                gre.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.colorPrimary));
                gra.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.searchback1));
                gra.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.colorPrimary));
                oth.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.searchback1));
                oth.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.colorPrimary));
                fru.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.searchback1));
                fru.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.colorPrimary));
                statef=1;
                stateG=1;
                stateo=1;
                statev=1;
                stateg=1;


            }
        });
        rg=dialog.findViewById(R.id.radio);
        if(dad==1)
        {
            RadioButton r=dialog.findViewById(R.id.radiora);
            r.setChecked(true);
        }
        else if(plh==1)
        {
            RadioButton r=dialog.findViewById(R.id.radiolh);
            r.setChecked(true);

        }
        else if(phl==1)
        {
            RadioButton r=dialog.findViewById(R.id.radiohl);
            r.setChecked(true);

        }
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {



                if (checkedId == R.id.radiohl) {
                    plh=0;
                    dad=0;
                    phl=1;
                    Toast.makeText(checking.this, "1", Toast.LENGTH_SHORT).show();




                } else if (checkedId == R.id.radiolh) {
                    dad=0;
                    phl=0;
                    plh=1;
                    Toast.makeText(checking.this, "2", Toast.LENGTH_SHORT).show();


                }
                else if(checkedId == R.id.radiora)
                {
                    plh=0;
                    phl=0;
                    dad=1;
                    Toast.makeText(checking.this, "3", Toast.LENGTH_SHORT).show();

                }

                }
        });


        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filters=selected;
                try
                {
                    isLastItemReached=false;
                    mUploads.clear();
                    mAdapter.notifyDataSetChanged();


                }catch (Exception e)
                {

                }
                gett();
                //Toast.makeText(checking.this, filters, Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });









        dialog.show();


    }

    public void gett()
    {
        if(filters=="")
        {
            //Toast.makeText(this, "1", Toast.LENGTH_SHORT).show();

            if (dad ==1)
            {
                query=FirebaseFirestore.getInstance().collection("users")
                        .whereEqualTo("district",location)
                        .orderBy("dateupdated", Query.Direction.DESCENDING)
                        .limit(10);

            }
            else if(phl==1)
            {
                query=FirebaseFirestore.getInstance().collection("users")
                        .whereEqualTo("district",location)
                        .orderBy("price", Query.Direction.ASCENDING)
                        .limit(10);

            }
            else if (plh==1)
            {
                Toast.makeText(this, "1", Toast.LENGTH_SHORT).show();
                query=FirebaseFirestore.getInstance().collection("users")
                        .whereEqualTo("district",location)
                        .orderBy("price", Query.Direction.DESCENDING)
                        .limit(10);

            }

        }
        else if (filters!=""){
           // Toast.makeText(this, "2", Toast.LENGTH_SHORT).show();

            if (dad ==1)
            {
                query=FirebaseFirestore.getInstance().collection("users")
                        .whereEqualTo("district",location)
                        .whereEqualTo("category",filters)
                        .orderBy("dateupdated", Query.Direction.DESCENDING)
                        .limit(10);

            }
            else if(phl==1)
            {
                query=FirebaseFirestore.getInstance().collection("users")
                        .whereEqualTo("district",location)
                        .whereEqualTo("category",filters)
                        .orderBy("price", Query.Direction.ASCENDING)
                        .limit(10);

            }
            else if (plh==1)
            {
                Toast.makeText(this, "1", Toast.LENGTH_SHORT).show();

                query=FirebaseFirestore.getInstance().collection("users")
                        .whereEqualTo("district",location)
                        .whereEqualTo("category",filters)
                        .orderBy("price", Query.Direction.DESCENDING)
                        .limit(10);

            }


        }

        query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                mUploads.clear();

                for (QueryDocumentSnapshot documentSnapshots: queryDocumentSnapshots)
                {
                    upload=documentSnapshots.toObject(Upload1.class);
                    mUploads.add(upload);


                }
                try
                {
                    mAdapter=new sumnecheckadapter(checking.this,mUploads);
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


                            fab.hide();
                            // Toast.makeText(checking.this, "Next Page Loaded", Toast.LENGTH_SHORT).show();


                            int firstVisibleItem=layoutManager.findFirstVisibleItemPosition();
                            int visibleItemCount=layoutManager.getChildCount();
                            int totalItemCount=layoutManager.getItemCount();

                            if ( (isScrolling && firstVisibleItem + visibleItemCount == totalItemCount) && !isLastItemReached) {
                                isScrolling=false;
                                if (filters=="")
                                {
                                    if(plh==1)
                                    {
                                        nextQuery=FirebaseFirestore.getInstance()
                                                .collection("users")
                                                .whereEqualTo("district",location)
                                                .orderBy("price", Query.Direction.DESCENDING)
                                                .startAfter(lastvisible)
                                                .limit(10);

                                    }
                                    else if(phl==1)
                                    {
                                        nextQuery=FirebaseFirestore.getInstance()
                                                .collection("users")
                                                .whereEqualTo("district",location)
                                                .orderBy("price", Query.Direction.ASCENDING)
                                                .startAfter(lastvisible)
                                                .limit(10);

                                    }
                                    else if(dad==1)
                                    {
                                        nextQuery=FirebaseFirestore.getInstance()
                                                .collection("users")
                                                .whereEqualTo("district",location)
                                                .orderBy("dateupdated", Query.Direction.DESCENDING)
                                                .startAfter(lastvisible)
                                                .limit(10);

                                    }
//                                    Toast.makeText(checking.this, "1", Toast.LENGTH_SHORT).show();

                                }
                                else if(filters!=""){
                                    if(dad==1)
                                    {
                                        nextQuery=FirebaseFirestore.getInstance()
                                                .collection("users")
                                                .whereEqualTo("district",location)
                                                .whereEqualTo("category",filters)
                                                .orderBy("dateupdated", Query.Direction.DESCENDING)
                                                .startAfter(lastvisible)
                                                .limit(10);

                                    }
                                    else if(phl==1)
                                    {
                                        nextQuery=FirebaseFirestore.getInstance()
                                                .collection("users")
                                                .whereEqualTo("district",location)
                                                .whereEqualTo("category",filters)
                                                .orderBy("price", Query.Direction.ASCENDING)
                                                .startAfter(lastvisible)
                                                .limit(10);

                                    }
                                    else if (plh==1)
                                    {
                                        nextQuery=FirebaseFirestore.getInstance()
                                                .collection("users")
                                                .whereEqualTo("district",location)
                                                .whereEqualTo("category",filters)
                                                .orderBy("price", Query.Direction.DESCENDING)
                                                .startAfter(lastvisible)
                                                .limit(10);

                                    }


                                }






                                nextQuery
                                        .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                    @Override
                                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                        for (QueryDocumentSnapshot documentSnapshots: queryDocumentSnapshots)
                                        {
                                            upload=documentSnapshots.toObject(Upload1.class);
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


                                        if(queryDocumentSnapshots.size() < 10)
                                        {
                                            isLastItemReached=true;
                                            mProgressBar.setVisibility(View.INVISIBLE);
                                        }




                                    }
                                });




                            }
                        }
                        else if(dy<0) {
                            fab.show();
                        }

                    }
                };
                mRecyclerView.addOnScrollListener(onScrollListener);




            }
        });

    }

}