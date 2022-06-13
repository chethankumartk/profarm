package com.example.chethan.industrain;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chethan.industrain.homeadapters.Upload1;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {
    Button addd;
    FirebaseAuth auth;
    FirebaseUser user;
    ProgressDialog PD;
    TextView tv1;
    ActionBar actionBar;
    private RecyclerView mRecyclerView;
    CoordinatorLayout coordinatorLayout;
    private ImageAdapter mAdapter;
    String vall;
    FirebaseFirestore db;


    private ProgressBar mProgressCircle;

    private DatabaseReference mDatabaseRef;
    private List<Upload1> mUploads;







    @Override    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRecyclerView = findViewById(R.id.recy);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mProgressCircle = findViewById(R.id.progress_circle);
        //getSupportActionBar().hide();
        coordinatorLayout=findViewById(R.id.l);

        mUploads = new ArrayList<>();

        db=FirebaseFirestore.getInstance();
        final Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        AppBarLayout mAppBarLayout = (AppBarLayout) findViewById(R.id.app_bar);
        mAppBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    isShow = true;
                } else if (isShow) {
                    isShow = false;
                }
            }
        });
        PD = new ProgressDialog(this);
        PD.setMessage("Loading...");
        PD.setCancelable(true);
        PD.setCanceledOnTouchOutside(false);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {


                auth.getCurrentUser().reload().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if(task.isSuccessful())
                        {

                            if(auth.getCurrentUser().isEmailVerified()==true) {
                                startActivity(new Intent(MainActivity.this, uploadmain.class));
                            }
                            else
                            {
                                sendverificationcode();

                                Toast.makeText(MainActivity.this, "Verify Email to Sell Your Product", Toast.LENGTH_SHORT).show();
                            }

                        }
                        else {
                            Toast.makeText(MainActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                        }


                    }
                });



            }
        });






        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        getSupportActionBar().setTitle("My Crops");


       getSupportActionBar().setDisplayHomeAsUpEnabled(true);
       // getSupportActionBar().setHomeButtonEnabled(true);
       // gett();


        gett();



        mRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), mRecyclerView,
                new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Upload1 uploadCurrent = mUploads.get(position);

                Toast.makeText(MainActivity.this, ""+uploadCurrent.getCropname(), Toast.LENGTH_SHORT).show();


               // startActivity(new Intent(MainActivity.this,ViewMukyaActivity.class)
                        //.putExtra("refid",uploadCurrent.getRefid())
                      //  .putExtra("price",uploadCurrent.getPrice())

                        //);

               // showUpdateDeleteDialog(upload.getIdd(), upload.getName(),upload.getImageUrl(),
                 //       upload.getmAddress(),upload.getmCropname(),upload.getPhon());

            }

            @Override
            public void onLongClick(View view, int position) {


                //showUpdateDeleteDialog((Stringupload.getIdd(),upload.getName(),upload.getImageUrl());
            }
        }));

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


    public void gett()
    {
        if(user!=null)
        {
            vall=user.getUid();

            db.collection("users")
                    .whereEqualTo("eid",vall)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {


                                    Upload1 upload = document.toObject(Upload1.class);
                                    mUploads.add(upload);


                                }
                                mAdapter = new ImageAdapter(MainActivity.this, mUploads);

                                mRecyclerView.setAdapter(mAdapter);

                                mProgressCircle.setVisibility(View.INVISIBLE);

                            } else {
                                Toast.makeText(MainActivity.this, "error", Toast.LENGTH_SHORT).show();
                                mProgressCircle.setVisibility(View.INVISIBLE);
                                MainActivity.this.notify();
                                // Log.w(TAG, "Error getting documents.", task.getException());
                            }
                        }
                    });




        }

    }



    @Override
    protected void onResume() {
        if (auth.getCurrentUser() == null) {
            startActivity(new Intent(MainActivity.this, Login.class));
            finish();
        }

        super.onResume();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);



        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.signout) {
            auth.signOut();
            //startActivity(new Intent(MainActivity.this, Login.class));
            startActivity(new Intent(MainActivity.this, Login.class));
            finish();


            FirebaseAuth.AuthStateListener authListener = new FirebaseAuth.AuthStateListener() {
                @Override
                public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                    FirebaseUser user1 = firebaseAuth.getCurrentUser();
                    if (firebaseAuth.getCurrentUser() == null) {
                        startActivity(new Intent(MainActivity.this, Login.class));
                        finish();
                    }


                }
            };

        }

        else if(id == R.id.deleteuser)
        {
            startActivity(new Intent(getApplicationContext(), ForgetPassword.class).putExtra("Mode", 3));

        }
        else if(id == R.id.changeemail)
        {
            startActivity(new Intent(getApplicationContext(), ForgetPassword.class).putExtra("Mode", 2));

        }
        else if(id == R.id.changepass)
        {
            startActivity(new Intent(getApplicationContext(), ForgetPassword.class).putExtra("Mode", 1));

        }
        else if(id == R.id.additem)
        {
            startActivity(new Intent(MainActivity.this,uploadmain.class));

        }
        else if (id == R.id.searchmadakke)
        {
            if(mAdapter!=null)
            {
                SearchView searchView=(SearchView)MenuItemCompat.getActionView(item);
                searchView.setOnQueryTextListener(this);

            }

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {

        newText=newText.toLowerCase();
        List<Upload1> newlist=new ArrayList<>();
        for(Upload1 upload : mUploads)
        {
            String name= upload.getCropname().toLowerCase();
            String Fname=upload.getFarmername().toLowerCase();
            int Price=upload.getPrice();
            String address=upload.getLocality().toLowerCase();

            if ((name.contains(newText)) || (Fname.contains(newText))
                    || (address.contains(newText)))
            {
                newlist.add(upload);
            }
        }
        mAdapter.setFilter(newlist);
        return true;
    }
    public void sendverificationcode()
    {
        auth.getCurrentUser().sendEmailVerification().addOnCompleteListener(this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(MainActivity.this, "Verification code sent to"+auth.getCurrentUser().getEmail(), Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MainActivity.this, "Verification code Not sent", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(MainActivity.this,usermai.class));
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }
}