package com.example.chethan.industrain.Fragments;


import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chethan.industrain.FragmentsAdapter.circleadapter;
import com.example.chethan.industrain.MyAdapter;
import com.example.chethan.industrain.R;
import com.example.chethan.industrain.RecyclerTouchListener;
import com.example.chethan.industrain.ViewMukyaActivity;
import com.example.chethan.industrain.checking;
import com.example.chethan.industrain.homeadapters.Upload1;
import com.example.chethan.industrain.homeadapters.fruitsadapter;
import com.example.chethan.industrain.homeadapters.usethrowadapter;
import com.example.chethan.industrain.emicalculatemadakke;
import com.example.chethan.industrain.mostpopularadapter;
import com.example.chethan.industrain.newsadapter.NewsAdapter;
import com.example.chethan.industrain.newsadapter.NewsRecycler;
import com.example.chethan.industrain.newsadapter.news;
import com.example.chethan.industrain.usermai;
import com.gigamole.infinitecycleviewpager.HorizontalInfiniteCycleViewPager;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class Home extends Fragment {
    ProgressDialog PD;
    List<Integer> lstimages=new ArrayList<>();
    Button btnemical;
    Button newsfeed;
    private RecyclerView mRecyclerView;
    private usethrowadapter mAdapter;
    private fruitsadapter mAdapter1;
    private fruitsadapter mAdapter2;
    private mostpopularadapter mAdapterpop;
    private circleadapter circleadapter1;
    private ProgressBar mProgressCircle;
    private List<Upload1> mUploads;
    private List<Upload1> mUploadsf;
    private List<Upload1> mUploadss;
    private List<Upload1> mUploadspop;
    private List<news> muploadsnews;
    private AdView mAdView;

    private RecyclerView circlerecyclerview;
    private List<Upload1> mUploadscircle;
    NewsAdapter mAdapternews;
    private TextView showmore,showmoref,showmoresuggest,showmoremostpop;

    FirebaseFirestore db;
    String category;
    String location;
    RecyclerView recyclerViewnews;
    SharedPreferences sharedPref;
    FrameLayout veg,gre,gra,fru,dry,oth;
    RecyclerView recyclerView;
    RecyclerView recyclerViewf;
    RecyclerView recyclerViewm;
    FirebaseAuth auth;
    FirebaseUser user;
    FloatingActionButton floatingActionButton;
    NestedScrollView nestedScrollView;
    private TextView newsall;




    public Home() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_home, container, false);


        btnemical=view.findViewById(R.id.emicalculator);


        newsfeed=view.findViewById(R.id.usermainn);
        showmore=view.findViewById(R.id.showmore);
        showmoref=view.findViewById(R.id.showmore1);
        showmoresuggest=view.findViewById(R.id.showmoresuggest);
        showmoremostpop=view.findViewById(R.id.showmorepopular);
        veg=view.findViewById(R.id.veg);
        gre=view.findViewById(R.id.gre);
        gra=view.findViewById(R.id.gra);
        fru=view.findViewById(R.id.fru);
        dry=view.findViewById(R.id.dry);
        oth=view.findViewById(R.id.oth);

        sharedPref=getActivity().getSharedPreferences("myPref",MODE_PRIVATE);

        location = sharedPref.getString("location", "default");

        category=sharedPref.getString("category","grains");


        veg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(),checking.class).putExtra("filters","Vegetables"));

            }
        });
        gre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(),checking.class).putExtra("filters","Greens"));

            }
        });
        gra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(),checking.class).putExtra("filters","grains"));

            }
        });
        fru.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(),checking.class).putExtra("filters","Fruits"));

            }
        });
        dry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(),checking.class).putExtra("filters","DryFruit"));

            }
        });
        oth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(),checking.class).putExtra("filters","Others"));

            }
        });


        showmoremostpop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(),checking.class).putExtra("filters","none"));

            }
        });
        showmoresuggest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(),checking.class).putExtra("filters",category));

                //  startActivity(new Intent(getActivity(),checking.class).putExtra("filters",category));

            }
        });
        showmoref.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // startActivity(new Intent(getActivity(),checking.class));

                 startActivity(new Intent(getActivity(),checking.class).putExtra("filters","Fruits"));

            }
        });
        showmore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(),checking.class).putExtra("filters","none"));


                // startActivity(new Intent(getActivity(),checking.class).putExtra("filters","Enilla"));
            }
        });



        db=FirebaseFirestore.getInstance();


        PD = new ProgressDialog(getActivity());
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();


        PD.setMessage("Loading...");
        PD.setCancelable(true);
        PD.setCanceledOnTouchOutside(false);
        recyclerViewf = view.findViewById(R.id.suggestedforu);
        recyclerViewf.setHasFixedSize(true);
        // mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerViewf.setNestedScrollingEnabled(false);
        recyclerViewf.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));


        circlerecyclerview = view.findViewById(R.id.circlrecycll);
        circlerecyclerview.setHasFixedSize(true);
        // mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        circlerecyclerview.setNestedScrollingEnabled(false);
        circlerecyclerview.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));


        recyclerViewnews=view.findViewById(R.id.newsrecycl);
        newsall=view.findViewById(R.id.newsviewall);
        newsall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), NewsRecycler.class));
            }
        });
        recyclerViewnews.setHasFixedSize(true);
        // mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerViewnews.setNestedScrollingEnabled(false);
        recyclerViewnews.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));


        recyclerViewm = view.findViewById(R.id.mostpopularrecyl);
        recyclerViewm.setHasFixedSize(true);
        // mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerViewm.setNestedScrollingEnabled(false);
        recyclerViewm.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));

        recyclerView = view.findViewById(R.id.fruitsrecycl);
        recyclerView.setHasFixedSize(true);
        // mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));


        mRecyclerView = view.findViewById(R.id.recy);
        mRecyclerView.setHasFixedSize(true);
       // mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));


        mProgressCircle = view.findViewById(R.id.progress_circle);
        mRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), mRecyclerView,
                new RecyclerTouchListener.ClickListener() {
                    @Override
                    public void onClick(View view, int position) {
                        Upload1 uploadCurrent = mUploads.get(position);

                        Toast.makeText(getActivity(), ""+uploadCurrent.getCropname(), Toast.LENGTH_SHORT).show();


                        startActivity(new Intent(getActivity(),ViewMukyaActivity.class)
                                .putExtra("refid",uploadCurrent.getRefid())
                                .putExtra("price",uploadCurrent.getPrice())
                                .putExtra("eidd",uploadCurrent.getEid())


                        );
                        sharedPref.edit().putString("category", uploadCurrent.getCategory()).commit();


                        // showUpdateDeleteDialog(upload.getIdd(), upload.getName(),upload.getImageUrl(),
                        //       upload.getmAddress(),upload.getmCropname(),upload.getPhon());

                    }

                    @Override
                    public void onLongClick(View view, int position) {


                        //showUpdateDeleteDialog((Stringupload.getIdd(),upload.getName(),upload.getImageUrl());
                    }
                }));
        recyclerViewnews.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), recyclerViewnews,
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


        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), recyclerView,
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
        recyclerViewf.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), recyclerViewf,
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




        mUploads = new ArrayList<>();
        mUploadsf=new ArrayList<>();
        mUploadss=new ArrayList<>();
        mUploadspop=new ArrayList<>();
        muploadsnews=new ArrayList<>();
        mUploadscircle=new ArrayList<>();

        initData();
        HorizontalInfiniteCycleViewPager pager=(HorizontalInfiniteCycleViewPager)view.findViewById(R.id.hor);
        MyAdapter adapter=new MyAdapter(lstimages,getActivity());
        pager.setAdapter(adapter);

        btnemical.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(),emicalculatemadakke.class));

                Toast.makeText(getContext(), "working", Toast.LENGTH_SHORT).show();

                //  Toast.makeText(usermai.this, "working"+a, Toast.LENGTH_SHORT).show();



            }
        });
        newsfeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //startActivity(new Intent(getApplication(), MapsActivity.class));
                // startActivity(new Intent(usermai.this,sumnecheckmadakkee.class));

                // Toast.makeText(usermai.this, "Innu Implement madbeku", Toast.LENGTH_SHORT).show();
                //startActivity(new Intent(getApplicationContext(),usermainActivity.class));
                startActivity(new Intent(getActivity(),NewsRecycler.class));
                getActivity().overridePendingTransition(R.anim.slidedown,R.anim.slideup);


            }
        });
         floatingActionButton = ((usermai) getActivity()).getFloatingActionButton();
         nestedScrollView = view.findViewById(R.id.nestedscrollview);
        nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY > oldScrollY) {
                    //floatingActionButton.hide();
                } else {
                    //floatingActionButton.show();
                }
            }
        });
        gett();
        gett1();
        gett2();
        get3();
        getNews();
        gettCircle();






        mAdView = view.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                // Code to be executed when an ad request fails.
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
            }

            @Override
            public void onAdLeftApplication() {
                // Code to be executed when the user has left the app.
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when the user is about to return
                // to the app after tapping on an ad.
            }
        });







        // Inflate the layout for this fragment

        return view;

    }

    private void initData() {


        lstimages.add(R.drawable.fibre);
        lstimages.add(R.drawable.antioxy);
        lstimages.add(R.drawable.fruits3);
        lstimages.add(R.drawable.fruit2);

        lstimages.add(R.drawable.fruits);
        lstimages.add(R.drawable.broccoli);



    }
    public void gett()
    {
        if(user!=null) {
            String vall = user.getEmail();
            db.collection("users")
                    .whereEqualTo("district",location)
                    .limit(15)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {


                                    Upload1 upload = document.toObject(Upload1.class);
                                    mUploads.add(upload);


                                }
                                mAdapter = new usethrowadapter(getActivity(), mUploads);

                                mRecyclerView.setAdapter(mAdapter);

                                mProgressCircle.setVisibility(View.INVISIBLE);

                            } else {
                                Toast.makeText(getActivity(), task.getException().toString(), Toast.LENGTH_LONG).show();
                                mProgressCircle.setVisibility(View.INVISIBLE);
                                //  usermai.this.notify();
                                Log.w( "", task.getResult().toString());
                            }
                        }
                    });


        }

    }
    public void gett1()
    {
        if(user!=null) {
            String vall = user.getEmail();
            db.collection("users")
                    .whereEqualTo("district",location)
                    .whereEqualTo("category","Fruits")
                    .limit(15)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {


                                    Upload1 upload = document.toObject(Upload1.class);
                                    mUploadsf.add(upload);


                                }
                                mAdapter1 = new fruitsadapter(getActivity(), mUploadsf);

                                recyclerView.setAdapter(mAdapter1);

                                mProgressCircle.setVisibility(View.INVISIBLE);

                            } else {
                                Toast.makeText(getActivity(), task.getException().toString(), Toast.LENGTH_LONG).show();
                                mProgressCircle.setVisibility(View.INVISIBLE);
                                //  usermai.this.notify();
                                Log.w( "", task.getResult().toString());
                            }
                        }
                    });


        }

    }
    public void gett2()
    {
        if(user!=null) {
            String vall = user.getEmail();
            db.collection("users")
                    .whereEqualTo("district",location)
                    .whereEqualTo("category",category)
                    .limit(15)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {


                                    Upload1 upload = document.toObject(Upload1.class);
                                    mUploadss.add(upload);


                                }
                                mAdapter2 = new fruitsadapter(getActivity(), mUploadss);

                                recyclerViewf.setAdapter(mAdapter2);

                                mProgressCircle.setVisibility(View.INVISIBLE);

                            } else {
                                Toast.makeText(getActivity(), task.getException().toString(), Toast.LENGTH_LONG).show();
                                mProgressCircle.setVisibility(View.INVISIBLE);
                                //  usermai.this.notify();
                                Log.w( "", task.getResult().toString());
                            }
                        }
                    });


        }

    }


    public void get3()
    {
        if(user!=null) {
            String vall = user.getEmail();
            db.collection("users")
                    .whereEqualTo("district",location)
                    .orderBy("views", Query.Direction.DESCENDING)
                    .limit(15)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {


                                    Upload1 upload = document.toObject(Upload1.class);
                                    mUploadspop.add(upload);


                                }
                                mAdapterpop = new mostpopularadapter(getActivity(), mUploadspop);

                                recyclerViewm.setAdapter(mAdapterpop);

                                mProgressCircle.setVisibility(View.INVISIBLE);

                            } else {
                                Toast.makeText(getActivity(), task.getException().toString(), Toast.LENGTH_LONG).show();
                                mProgressCircle.setVisibility(View.INVISIBLE);
                                //  usermai.this.notify();
                                Log.w( "", task.getResult().toString());
                            }
                        }
                    });


        }

    }
    public void getNews()
    {
        if(user!=null) {
            String vall = user.getEmail();
            db.collection("news")
                    .limit(15)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {


                                    news upload = document.toObject(news.class);
                                    muploadsnews.add(upload);


                                }
                                mAdapternews = new NewsAdapter(getActivity(), muploadsnews);

                                recyclerViewnews.setAdapter(mAdapternews);

                                mProgressCircle.setVisibility(View.INVISIBLE);

                            } else {
                                Toast.makeText(getActivity(), task.getException().toString(), Toast.LENGTH_LONG).show();
                                mProgressCircle.setVisibility(View.INVISIBLE);
                                //  usermai.this.notify();
                                Log.w( "", task.getResult().toString());
                            }
                        }
                    });


        }

    }
    public void gettCircle()
    {
        if(user!=null) {
            String vall = user.getEmail();
            db.collection("users")
                    .whereEqualTo("district",location)
                    .limit(15)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {


                                    Upload1 upload = document.toObject(Upload1.class);
                                    mUploadscircle.add(upload);


                                }
                                circleadapter1 = new circleadapter(getActivity(), mUploadscircle);

                                circlerecyclerview.setAdapter(circleadapter1);

                                mProgressCircle.setVisibility(View.INVISIBLE);

                            } else {
                                Toast.makeText(getActivity(), task.getException().toString(), Toast.LENGTH_LONG).show();
                                mProgressCircle.setVisibility(View.INVISIBLE);
                                //  usermai.this.notify();
                                Log.w( "", task.getResult().toString());
                            }
                        }
                    });


        }

    }

    @Override
    public void onStart() {
        super.onStart();

    }




}






