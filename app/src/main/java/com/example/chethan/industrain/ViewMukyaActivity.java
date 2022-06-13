package com.example.chethan.industrain;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chethan.industrain.ViewMadiroList.ViewContactOfFarmer;
import com.example.chethan.industrain.ViewMadiroList.viewcontract;
import com.example.chethan.industrain.fragmentclasses.WishlistAdd;
import com.example.chethan.industrain.googlemaps.MapsActivity;
import com.example.chethan.industrain.homeadapters.Upload1;
import com.example.chethan.industrain.homeadapters.fruitsadapter;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ViewMukyaActivity extends AppCompatActivity   {
    private GoogleMap mMap;
    double lat,lan;
    String lat1,lan2;
    String refid,imageurl,location;
    ImageView collapseimage;
    TextView viewcontact,distancee;
    TextView product,category,state,district,locality,farmer,address,price,quantity;
    Upload1 upload;
    CollapsingToolbarLayout toolbarLayout;
    SharedPreferences sharedPref;
    ImageView mapp;
    private fruitsadapter mAdapter;
    TextView cname,cemail,cphone;
    double latt,longg;
    private List<Upload1> mUploads;
    String username1;
    TextView farmerprofile;
    double distance;
    String Eid;
    String fareid;
    FirebaseFirestore db;
    private Menu menu;
    private Button viewcontactinfo;
    private FirebaseFirestore mFirestore;
    FloatingActionButton fab;
    DocumentReference documentReference;
    DocumentReference documentReference1;
    DatabaseReference databaseReference;


    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_mukya);

        final Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        int cost=getIntent().getIntExtra("price",0);
        Eid=getIntent().getStringExtra("eidd");
        toolbarLayout=findViewById(R.id.toolbar_layout);
        mapp=findViewById(R.id.map);
        databaseReference= FirebaseDatabase.getInstance().getReference("users");

        getSupportActionBar().setTitle("₹ "+String.valueOf(cost));
        final Typeface tf = Typeface.createFromAsset(getApplication().getAssets(), "font/Raleway-Regular.ttf");
        toolbarLayout.setCollapsedTitleTypeface(tf);
        toolbarLayout.setExpandedTitleTypeface(tf);
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
        farmerprofile=findViewById(R.id.farmerprofile);


        cname=findViewById(R.id.cname);
        cemail=findViewById(R.id.cemail);
        fab=findViewById(R.id.fab);
        viewcontactinfo=findViewById(R.id.viewcontactinfo);
        viewcontactinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewmadakke();

            }
        });
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewmadakke();
            }
        });
        cphone=findViewById(R.id.cphone);
        mUploads = new ArrayList<>();
        mFirestore = FirebaseFirestore.getInstance();


        recyclerView =findViewById(R.id.similaritemsrecycl);
        recyclerView.setHasFixedSize(true);
        // mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        sharedPref=getSharedPreferences("myPref",MODE_PRIVATE);
        quantity=findViewById(R.id.quantity);
        username1=sharedPref.getString("username","none");

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(this, recyclerView,
                new RecyclerTouchListener.ClickListener() {
                    @Override
                    public void onClick(View view, int position) {
                        Upload1 uploadCurrent = mUploads.get(position);

                      //  Toast.makeText(getActivity(), ""+uploadCurrent.getCropname(), Toast.LENGTH_SHORT).show();


                        startActivity(new Intent(ViewMukyaActivity.this,ViewMukyaActivity.class)
                                .putExtra("refid",uploadCurrent.getRefid())
                                .putExtra("price",uploadCurrent.getPrice())
                                .putExtra("eidd",uploadCurrent.getEid())

                        );

                        // showUpdateDeleteDialog(upload.getIdd(), upload.getName(),upload.getImageUrl(),
                        //       upload.getmAddress(),upload.getmCropname(),upload.getPhon());

                    }

                    @Override
                    public void onLongClick(View view, int position) {


                        //showUpdateDeleteDialog((Stringupload.getIdd(),upload.getName(),upload.getImageUrl());
                    }
                }));


        lat1 = sharedPref.getString("latitude", "0");
        location = sharedPref.getString("location", "default");

        lan2=sharedPref.getString("longitude","0");

        viewcontact=findViewById(R.id.viewcontact);
        viewcontact.setClickable(false);
        viewcontact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DocumentReference documentReferencee=FirebaseFirestore.getInstance().collection("profile").document(upload.getEid())
                        .collection("Notifications").document();
               String rid=documentReferencee.getId();


                String namma_id=FirebaseAuth.getInstance().getUid().toString().trim();
                Map<String, Object> notificationMessage = new HashMap<>();
                notificationMessage.put("message", ""+String.valueOf(Math.ceil(distance))+" away from you");
                notificationMessage.put("from", namma_id);
                notificationMessage.put("cid",upload.getRefid());
                notificationMessage.put("s","n");
                notificationMessage.put("rid",rid);




                documentReferencee.set(notificationMessage).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful())
                        {

                        }

                        //  searchitem search=new searchitem(mEditTextFileName.getText().toString().trim());
                        //mDatabaseRef.child(uploadId).setValue(search);

                    }
                });






                viewmadakke();
                //Toast.makeText(ViewMukyaActivity.this, "working", Toast.LENGTH_SHORT).show();
            }
        });

        farmerprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ViewMukyaActivity.this,Farmerprofile.class).
                        putExtra("Eid",upload.getEid()).putExtra("name",upload.getFarmername()));
            }
        });

        refid=getIntent().getStringExtra("refid");
        collapseimage=findViewById(R.id.expandedImage);
        distancee=findViewById(R.id.distance);

        price=findViewById(R.id.price);
        product=findViewById(R.id.cropname);
        category=findViewById(R.id.category);
        state=findViewById(R.id.state);
        district=findViewById(R.id.district);
        locality=findViewById(R.id.locality);
        farmer=findViewById(R.id.farmer);
        address=findViewById(R.id.address);
        Picasso.with(getApplicationContext())
                .load(R.drawable.map)
                .placeholder(R.drawable.gradient5)
                .fit()
                .into(mapp);
        FirebaseFirestore.getInstance().collection("users").whereEqualTo("refid",refid)
                .limit(1).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if(task.isSuccessful())
                        {
                            int b=task.getResult().size();
                            //open maddhaga ee crop illa andre wishlist alli delete madakke
                            if(b==0)
                            {
                                Query query=FirebaseFirestore.getInstance().collection("WishList").
                                        whereEqualTo("id1",refid)
                                        .whereEqualTo("eid",FirebaseAuth.getInstance().getUid().toString().trim());

                                query.get()
                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    int a=task.getResult().size();


                                                        for (DocumentSnapshot document : task.getResult()) {
                                                            FirebaseFirestore.getInstance().collection("WishList").
                                                                    document(document.getId()).delete();


                                                        }




                                                } else {


                                                }
                                            }
                                        });

                            }
                            else {
                                for (QueryDocumentSnapshot document : task.getResult()) {


                                    upload = document.toObject(Upload1.class);
                                    Query query=FirebaseFirestore.getInstance().collection("WishList").
                                            whereEqualTo("id1",upload.getRefid())
                                            .whereEqualTo("eid",FirebaseAuth.getInstance().getUid().toString().trim());

                                    query.get()
                                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                    if (task.isSuccessful()) {
                                                        int a=task.getResult().size();
                                                        if(a==0)
                                                        {

                                                        }
                                                        else {

                                                            menu.getItem(1).setIcon(ContextCompat.
                                                                    getDrawable(ViewMukyaActivity.this, R.drawable.wishlistfull));

                                                        }


                                                    } else {


                                                    }
                                                }
                                            });

                                    imageurl=upload.getImageurl().toString().trim();
                                    Picasso.with(getApplicationContext())
                                            .load(imageurl.toString().trim())
                                            .placeholder(R.drawable.gradient5)
                                            .fit()
                                            .into(collapseimage);

                                    lat=Double.parseDouble(lat1);
                                    lan=Double.parseDouble(lan2);
                                    LatLng latLngA = new LatLng(upload.getLatitude(),upload.getLongitude());
                                    LatLng latLngB = new LatLng(lat,lan);
                                    latt=upload.getLatitude();
                                    longg=upload.getLongitude();
                                    fareid=upload.getEid();
                                    viewcontact.setClickable(true);
                                    Location locationA = new Location("point A");
                                    locationA.setLatitude(latLngA.latitude);
                                    locationA.setLongitude(latLngA.longitude);
                                    Location locationB = new Location("point B");
                                    locationB.setLatitude(latLngB.latitude);
                                    locationB.setLongitude(latLngB.longitude);
                                    distance = locationA.distanceTo(locationB);
                                    distance=distance*0.001;
                                    distancee.setText(String.valueOf(Math.ceil(distance))+" Km away from you");
                                    price.setText(String.valueOf(upload.getPrice())+"/Kg");
                                    product.setText(upload.getCropname().toString().trim());
                                    quantity.setText(String.valueOf(upload.getQuantity())+" Kg");
                                    category.setText(upload.getCategory().toString().trim());
                                    state.setText(upload.getState().toString().trim());
                                    district.setText(upload.getDistrict().toString().trim());
                                    locality.setText(upload.getLocality().toString().trim());
                                    farmer.setText(upload.getFarmername().toString().trim());
                                    address.setText(upload.getAddress().toString().trim());
                                    cemail.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail().toString());
                                    cname.setText(username1);
                                    try {
                                        cphone.setText(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber());

                                    }catch (Exception e)
                                    {
                                        cphone.setText("Update your Phone");
                                    }





                                }

                            }


                        }
                    }
                });
        gett();
        mapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ViewMukyaActivity.this, MapsActivity.class).putExtra("latitude",latt)
                .putExtra("longitude",longg));
            }
        });






        //illi firebase database iradhu






        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(!dataSnapshot.hasChildren()){
                    // currentPage--;
                }
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Upload upload = postSnapshot.getValue(Upload.class);

                }



            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });



    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


         if(id == R.id.wishlist)
        {
           final String eid= FirebaseAuth.getInstance().getUid();

            Query query1=FirebaseFirestore.getInstance().collection("WishList")
                    .whereEqualTo("eid",eid);


            query1.get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                int a=task.getResult().size();
                                if(a<15)
                                {
                                    Query query=FirebaseFirestore.getInstance().collection("WishList").whereEqualTo("id1",upload.getRefid())
                                            .whereEqualTo("eid",eid);

                                    query.get()
                                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                    if (task.isSuccessful()) {
                                                        int a=task.getResult().size();
                                                        if(a==0)
                                                        {
                                                            documentReference1=FirebaseFirestore.getInstance().
                                                                    collection("WishList").document();
                                                            WishlistAdd wishlistAdd=new WishlistAdd(upload.getRefid(),eid,upload.getImageurl(),
                                                                    upload.getCropname(),upload.getLocality(),upload.getPrice());
                                                            documentReference1.set(wishlistAdd).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {

                                                                    //  searchitem search=new searchitem(mEditTextFileName.getText().toString().trim());
                                                                    //mDatabaseRef.child(uploadId).setValue(search);
                                                                    Toast.makeText(ViewMukyaActivity.this, "added", Toast.LENGTH_SHORT).show();

                                                                    item.setIcon(R.drawable.wishlistfull);



                                                                }
                                                            });

                                                        }
                                                        else {

                                                            for (DocumentSnapshot document : task.getResult()) {
                                                                FirebaseFirestore.getInstance().collection("WishList").document(document.getId()).delete();
                                                                Toast.makeText(ViewMukyaActivity.this, "deleted", Toast.LENGTH_SHORT).show();

                                                                item.setIcon(R.drawable.wishlistwhite);

                                                            }

                                                        }


                                                    } else {


                                                    }
                                                }
                                            });

                                }
                                else {

                                    Toast.makeText(ViewMukyaActivity.this, "Length exceeded You can add only 15 items to your wishlist:(", Toast.LENGTH_SHORT).show();
                                }


                            } else {

                                Toast.makeText(ViewMukyaActivity.this, "Error", Toast.LENGTH_SHORT).show();

                            }
                        }
                    });

        }
        else if(id == R.id.sharee)
        {

        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.viewmukyamenu, menu);

        this.menu=menu;


        return true;
    }

    public void gett()
    {
        if(FirebaseAuth.getInstance().getCurrentUser()!=null) {
            FirebaseFirestore.getInstance().collection("users")
                    .whereEqualTo("district",location)
                    .limit(10)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {


                                    Upload1 upload = document.toObject(Upload1.class);
                                    mUploads.add(upload);


                                }
                                mAdapter = new fruitsadapter(ViewMukyaActivity.this, mUploads);
                                recyclerView.setAdapter(mAdapter);

                            } else {
                                Toast.makeText(ViewMukyaActivity.this, task.getException().toString(), Toast.LENGTH_LONG).show();
                            }
                        }
                    });


        }

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
       // overridePendingTransition(R.anim.slidedown,R.anim.slideup);

    }
    public void viewmadakke()
    {
        db=FirebaseFirestore.getInstance();
        final FirebaseAuth auth=FirebaseAuth.getInstance();
        final Query query1=db.collection("viewers")
                .whereEqualTo("eid",auth.getUid().toString().trim())
                .whereEqualTo("refid",refid);



        query1.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            int a=task.getResult().size();
                            if(a==0)
                            {
                                try {
                                    if (auth.getCurrentUser().getPhoneNumber().toString()!="")
                                    {

                                        showalertdialog();
                                    }
                                    else {
                                        Toast.makeText(ViewMukyaActivity.this,
                                                "Update your phone Number to View Contact", Toast.LENGTH_SHORT).show();

                                    }



                                }catch (Exception e)
                                {
                                    Toast.makeText(ViewMukyaActivity.this,
                                            "Update your phone Number to View Contact", Toast.LENGTH_SHORT).show();
                                }
                            }
                            else if(a>0)
                            {

                                startActivity(new Intent(ViewMukyaActivity.this,ViewContactOfFarmer.class).
                                        putExtra("refid",upload.getRefid()));

                            }


                        } else {

                            Toast.makeText(ViewMukyaActivity.this, "Error", Toast.LENGTH_SHORT).show();

                        }
                    }
                });





    }
    public void showalertdialog()
    {
        final long views=upload.getViews()+1;
        documentReference=db.collection("viewers").document();
        final String refidd=documentReference.getId();
        final FirebaseAuth auth=FirebaseAuth.getInstance();
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("You are going to provide your information to");
                alertDialogBuilder.setPositiveButton("yes",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {


                                final viewcontract upload1=new viewcontract(auth.getCurrentUser().getEmail().toString(),
                                        auth.getCurrentUser().getPhoneNumber().toString(),refid,username1,auth.getUid(),refidd);

                                documentReference.set(upload1).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {


                                        FirebaseFirestore.getInstance().collection("users").document(refid).
                                                update("views" ,views);

                                    }
                                });
                                startActivity(new Intent(ViewMukyaActivity.this,ViewContactOfFarmer.class).
                                        putExtra("refid",upload.getRefid()));

                            }
                        });

        alertDialogBuilder.setNegativeButton("No",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }
}