package com.example.chethan.industrain;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.arlib.floatingsearchview.suggestions.SearchSuggestionsAdapter;
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;
import com.arlib.floatingsearchview.util.Util;
import com.example.chethan.industrain.Fragments.Home;
import com.example.chethan.industrain.Fragments.Notification;
import com.example.chethan.industrain.Fragments.Profile;
import com.example.chethan.industrain.Fragments.Wishlist;
import com.example.chethan.industrain.Fragments.search;
import com.example.chethan.industrain.FragmentsAdapter.searchadapter;
import com.example.chethan.industrain.ViewMadiroList.cropviewer;
import com.example.chethan.industrain.data.ColorSuggestion;
import com.example.chethan.industrain.data.ColorWrapper;
import com.example.chethan.industrain.data.DataHelper;
import com.example.chethan.industrain.data.suggest;
import com.example.chethan.industrain.googlemaps.autocompleteapi;
import com.example.chethan.industrain.homeadapters.Upload1;
import com.example.chethan.industrain.paymentgateway.paymadakke;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class usermai extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,SearchView.OnQueryTextListener,AppBarLayout.OnOffsetChangedListener {
    public static final long FIND_SUGGESTION_SIMULATED_DELAY = 1500;
    Button btnSignOut;
    FirebaseAuth auth;
    FirebaseUser user;
    CoordinatorLayout linearLayout;
    String a;
    String kkk="";
    private AdView mAdView;
    int MY_PERMISSIONS_REQUEST_READ_CONTACTS=10;


    FloatingSearchView searchView;
    DrawerLayout drawer;
    ImageView imgvw;
    CircleImageView imgv;
    private String mLastQuery = "";
    private boolean mIsDarkSearchTheme = false;
    private TextToSpeech textToSpeech;
    String location;
    TextView name;
    Toolbar toolbar;
    SharedPreferences sharedPref;
    FloatingActionButton fab;
    RelativeLayout relativeLayout;
    ActionBarDrawerToggle toggle;

    EditText searchmadakkeedittext;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home://home
                    loadFragment(new Home());
                    getSupportActionBar().hide();
                    fab.hide();
                    searchView.setVisibility(View.VISIBLE);
                    if (Build.VERSION.SDK_INT >= 21) {
                        getWindow().setNavigationBarColor(getResources().getColor(R.color.blackk));
                        getWindow().setStatusBarColor(getResources().getColor(R.color.blackk));
                    }

                    return true;

                case R.id.navigation_dashboard://search
                    getSupportActionBar().show();
                    searchView.setVisibility(View.INVISIBLE);
                    fab.hide();

                    relativeLayout.setVisibility(View.VISIBLE);
                    getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#058e3f")));


                    loadFragment(new search());
                    if (Build.VERSION.SDK_INT >= 21) {
                        getWindow().setNavigationBarColor(getResources().getColor(R.color.colorPrimary));
                        getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary));
                    }

                    return true;
                case R.id.navigation_notifications://Wishlist
                    getSupportActionBar().show();
                    fab.hide();
                    searchView.setVisibility(View.INVISIBLE);
                    getSupportActionBar().setTitle(Html.fromHtml("<font color='#058e3f'>WishList</font>"));
                    getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ffffff")));

                    relativeLayout.setVisibility(View.GONE);
                    if (Build.VERSION.SDK_INT >= 21) {
                        getWindow().setNavigationBarColor(getResources().getColor(R.color.colorPrimary));
                        getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary));
                    }

                    loadFragment(new Wishlist());
                    return true;

                case R.id.profile:
                    getSupportActionBar().show();
                    relativeLayout.setVisibility(View.GONE);
                    searchView.setVisibility(View.INVISIBLE);
                    fab.hide();
                    getSupportActionBar().setTitle(Html.fromHtml("<font color='#058e3f'>Profile </font>"));
                    getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ffffff")));


                    if (Build.VERSION.SDK_INT >= 21) {
                        getWindow().setNavigationBarColor(getResources().getColor(R.color.colorPrimary));
                        getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary));
                    }

                    loadFragment(new Profile());

                    return  true;
                case R.id.notification:
                    getSupportActionBar().show();
                    relativeLayout.setVisibility(View.GONE);
                    searchView.setVisibility(View.INVISIBLE);
                    fab.hide();
                    getSupportActionBar().setTitle(Html.fromHtml("<font color='#058e3f'>Notifications </font>"));
                    getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ffffff")));


                    if (Build.VERSION.SDK_INT >= 21) {
                        getWindow().setNavigationBarColor(getResources().getColor(R.color.colorPrimary));
                        getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary));
                    }

                    loadFragment(new Notification());

                    return  true;

            }
            return false;
        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {








        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        //getWindow().setFlags(WindowManager.LayoutParams.,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_usermai);
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setNavigationBarColor(getResources().getColor(R.color.blackk));
            getWindow().setStatusBarColor(getResources().getColor(R.color.blackk));
        }
      // Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
       // setSupportActionBar(toolbar);
       // getSupportActionBar().setTitle("FarmCart");
      //  getSupportActionBar().setTitle(Html.fromHtml("<font color='#7e7e7e'>Farmcart</font>"));



        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?

                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_READ_CONTACTS);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.

        }
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?

            // No explanation needed; request the permission
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_READ_CONTACTS);

            // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
            // app-defined int constant. The callback method gets the
            // result of the request.

        }





        MobileAds.initialize(this, "ca-app-pub-3940256099942544~3347511713");


        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        AdSize customAdSize = new AdSize(320, 200);
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




        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) navigation.getLayoutParams();
       // layoutParams.setBehavior(new BottomNavigationViewBehavior());


         toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        fab=findViewById(R.id.fab);
        searchmadakkeedittext=findViewById(R.id.searchmadakkeedittext);
        relativeLayout=findViewById(R.id.searchadvance);



        fab.hide();

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        linearLayout=findViewById(R.id.userlayout);

        searchView=findViewById(R.id.search_view);



         sharedPref=getSharedPreferences("myPref",MODE_PRIVATE);

         location = sharedPref.getString("location", "default");



        getSupportActionBar().hide();

        loadFragment(new Home());




        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        setupDrawer();
        setupSearchBar();

       /* searchView.setOnMenuItemClickListener(new FloatingSearchView.OnMenuItemClickListener() {
            @Override
            public void onActionMenuItemSelected(MenuItem item) {

            }

        });*/

        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);






        inittts();



        //not working
      /*  if (mAdapter!=null)
        {
            searchView.setOnQueryChangeListener(new FloatingSearchView.OnQueryChangeListener() {
                @Override
                public void onSearchTextChanged(String oldquery, String newText) {




                    newText=newText.toLowerCase();
                    List<Upload> newlist=new ArrayList<>();
                    for(Upload upload : mUploads)
                    {
                        String name= upload.getmCropname().toLowerCase();
                        String Fname=upload.getName().toLowerCase();
                        String Price=upload.getPrice().toLowerCase();
                        String address=upload.getmAddress().toLowerCase();
                        if ((name.contains(newText)) || (Fname.contains(newText)) || (Price.contains(newText))
                                || (address.contains(newText)))
                        {
                            newlist.add(upload);
                        }
                    }
                    mAdapter.setFilter(newlist);

                }

            });

        }*/




      //nav header dhu image thagaloke
       // View hView =  navigationView.inflateHeaderView(R.layout.nav_header_usermai);
        View hView =  navigationView.getHeaderView(0);

         imgvw = hView.findViewById(R.id.imageView);
        imgv = hView.findViewById(R.id.profImage);
        name=hView.findViewById(R.id.name);
       //Picasso.with(this).load().fit().into(imgvw);
        String username1=sharedPref.getString("username","none");
        name.setText(username1);

        Picasso.with(this)
                .load(R.drawable.nav_header)
                .placeholder(null)
                .fit()
                .into(imgvw);
        Picasso.with(this)
                .load(R.drawable.nav_header)
                .placeholder(null)
                .fit()
                .into(imgv);
        if(auth.getCurrentUser()!=null)
        {
            DocumentReference docRef = FirebaseFirestore.getInstance().collection("profile").document(auth.getUid());
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document != null) {

                            String iu = document.getString("iu");
                            Picasso.with(usermai.this)
                                    .load(iu)
                                    .placeholder(R.drawable.account)
                                    .fit()
                                    .into(imgv);


                        } else {

                        }
                    } else {

                    }
                }
            });

        }



        //imgvw.setImageResource(R.drawable.fruit1);

        // TextView tv = (TextView)hView.findViewById(R.id.textview);






    }







    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame, fragment);
        transaction.commit();
    }







    private void setupDrawer() {
        searchView.attachNavigationDrawerToMenuButton(drawer);

    }






    public void setupSearchBar(){

        searchView.setOnMenuItemClickListener(new FloatingSearchView.OnMenuItemClickListener() {
            @Override
            public void onActionMenuItemSelected(MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.signout) {
                    auth.signOut();
                    //startActivity(new Intent(MainActivity.this, Login.class));
                    startActivity(new Intent(usermai.this, Login.class));
                    finish();


                    FirebaseAuth.AuthStateListener authListener = new FirebaseAuth.AuthStateListener() {
                        @Override
                        public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                            FirebaseUser user1 = firebaseAuth.getCurrentUser();
                            if (firebaseAuth.getCurrentUser() == null) {
                                startActivity(new Intent(usermai.this, Login.class));
                                finish();
                            }


                        }
                    };

                }
                else if(id == R.id.action_voice_rec)
                {
                    listen();


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


            }


        });




        searchView.setOnQueryChangeListener(new FloatingSearchView.OnQueryChangeListener() {



            @Override
            public void onSearchTextChanged(String oldQuery, final String newQuery) {

                if (!oldQuery.equals("") && newQuery.equals("")) {
                    searchView.clearSuggestions();
                } else {

                    //this shows the top left circular progress
                    //you can call it where ever you want, but
                    //it makes sense to do it when loading something in
                    //the background.
                    searchView.showProgress();

                    //simulates a query call to a data source
                    //with a new query.



                    //idhunna mathe vapas hakbeku
                  /* DataHelper.findSuggestions(usermai.this, newQuery, 5,
                            FIND_SUGGESTION_SIMULATED_DELAY, new DataHelper.OnFindSuggestionsListener() {

                                @Override
                                public void onResults(List<ColorSuggestion> results) {

                                    //this will swap the data and
                                    //render the collapse/expand animations as necessary
                                    searchView.swapSuggestions(results);

                                    //let the users know that the background
                                    //process has completed
                                    searchView.hideProgress();
                                }
                            });*/

                    FirebaseFirestore db;
                    Query query;
                    db=FirebaseFirestore.getInstance();



                    query = db.collection("users").orderBy("cropname").startAt(newQuery.toString().toLowerCase().
                            trim()).endAt(newQuery.toString().toLowerCase().trim() + "\uf8ff");


                    query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if(task.isSuccessful()){
                                List<ColorSuggestion> mUploads=new ArrayList<>();


                                for(QueryDocumentSnapshot document : task.getResult()) {

                                    Upload1 upload = document.toObject(Upload1.class);
                                    upload.getCropname();
                                    ColorSuggestion s=new ColorSuggestion(upload.getCropname().toString());

                                    mUploads.add(s);

                                }
                                searchView.swapSuggestions(mUploads);
                                searchView.hideProgress();

                            }
                        }
                    });



                   /* List<ColorSuggestion> test=new ArrayList<>();
                    ColorSuggestion s=new ColorSuggestion("a");
                    test.add(s);
                    searchView.swapSuggestions(test);
                    searchView.hideProgress();*/
                }

               // Log.d(TAG, "onSearchTextChanged()");
            }
        });


        searchView.setOnSearchListener(new FloatingSearchView.OnSearchListener() {
            @Override
            public void onSuggestionClicked(final SearchSuggestion searchSuggestion) {
                ColorSuggestion colorSuggestion = (ColorSuggestion) searchSuggestion;
                final String a=colorSuggestion.getBody();

                //  Toast.makeText(getActivity(), "Working "+a, Toast.LENGTH_SHORT).show();


                // ColorSuggestion colorSuggestion = (ColorSuggestion) searchSuggestion;
               DataHelper.findColors(usermai.this, colorSuggestion.getBody(),
                        new DataHelper.OnFindColorsListener() {



                            @Override
                            public void onResults(List<ColorWrapper> results) {
                                Toast.makeText(usermai.this, "first"+a, Toast.LENGTH_SHORT).show();
                                //show search results
                            }

                        });
                Toast.makeText(usermai.this, "second "+a, Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(),usermainActivity.class).putExtra("cropsearch",a));

                // Log.d(TAG, "onSuggestionClicked()");

                mLastQuery = searchSuggestion.getBody();
            }

            @Override
            public void onSearchAction(final String query) {
                mLastQuery = query;
              //  Toast.makeText(usermai.this, "Working"+query, Toast.LENGTH_SHORT).show();


                DataHelper.findColors(usermai.this, query,
                        new DataHelper.OnFindColorsListener() {

                            @Override
                            public void onResults(List<ColorWrapper> results) {
                                //show search results
                                Toast.makeText(usermai.this, "Working"+query, Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(),usermainActivity.class).putExtra("cropsearch",query));


                            }

                        });
              //  Log.d(TAG, "onSearchAction()");
            }
        });

        searchView.setOnFocusChangeListener(new FloatingSearchView.OnFocusChangeListener() {
            @Override
            public void onFocus() {

                //show suggestions when search bar gains focus (typically history suggestions)
                searchView.swapSuggestions(DataHelper.getHistory(usermai.this, 3));

              //  Log.d(TAG, "onFocus()");
            }

            //to show the searchbar ttle
            @Override
            public void onFocusCleared() {

                //set the title of the bar so that when focus is returned a new query begins
              //  searchView.setSearchBarTitle(mLastQuery);

                //you can also set setSearchText(...) to make keep the query there when not focused and when focus returns
                //mSearchView.setSearchText(searchSuggestion.getBody());

               // Log.d(TAG, "onFocusCleared()");
            }
        });

        searchView.setOnHomeActionClickListener(new FloatingSearchView.OnHomeActionClickListener() {
            @Override
            public void onHomeClicked() {

               // Log.d(TAG, "onHomeClicked()");
            }
        });


        searchView.setOnBindSuggestionCallback(new SearchSuggestionsAdapter.OnBindSuggestionCallback() {
            @Override
            public void onBindSuggestion(View suggestionView, ImageView leftIcon,
                                         TextView textView, SearchSuggestion item, int itemPosition) {
                ColorSuggestion colorSuggestion = (ColorSuggestion) item;

                String textColor = mIsDarkSearchTheme ? "#ffffff" : "#000000";
                String textLight = mIsDarkSearchTheme ? "#bfbfbf" : "#787878";

                if (colorSuggestion.getIsHistory()) {
                    leftIcon.setImageDrawable(ResourcesCompat.getDrawable(getResources(),
                            R.drawable.ic_history_black_24dp, null));

                    Util.setIconColor(leftIcon, Color.parseColor(textColor));
                    leftIcon.setAlpha(.36f);
                } else {
                    leftIcon.setAlpha(0.0f);
                    leftIcon.setImageDrawable(null);
                }

                textView.setTextColor(Color.parseColor(textColor));
                String text = colorSuggestion.getBody()
                        .replaceFirst(searchView.getQuery(),
                                "<font color=\"" + textLight + "\">" + searchView.getQuery() + "</font>");
                textView.setText(Html.fromHtml(text));
            }

        });





    }

    public void listen()
    {
        Intent i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        i.putExtra(RecognizerIntent.EXTRA_PROMPT, "Try saying Something");

        try {
            startActivityForResult(i, 100);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(usermai.this, "Your device doesn't support Speech Recognition", Toast.LENGTH_SHORT).show();
        }
    }



    public void inittts()
    {
        textToSpeech = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    int result = textToSpeech.setLanguage(Locale.ENGLISH);
                    if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Log.e("TTS", "This Language is not supported");
                    }
                   // speak("Hello");

                } else {
                    Log.e("TTS", "Initilization Failed!");
                }
            }
        });
    }

    private void speak(String text){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
        }else{
            textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null);
        }
    }





    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 100){
            if (resultCode == RESULT_OK && null != data) {
                ArrayList<String> res = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                String inSpeech = res.get(0);
                Toast.makeText(this, ""+inSpeech, Toast.LENGTH_SHORT).show();

                startActivity(new Intent(getApplicationContext(),usermainActivity.class).putExtra("cropsearch",inSpeech));

                //recognition(inSpeech);
            }
        }
    }





    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu2, menu);
       // MenuItem item = menu.findItem(R.id.searchmadakke);
        //MaterialSearchView searchView=findViewById(R.id.search_view);
        //searchView.setMenuItem(item);



        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.signout) {
            auth.signOut();
            //startActivity(new Intent(MainActivity.this, Login.class));
            startActivity(new Intent(usermai.this, Login.class));
            finish();


            FirebaseAuth.AuthStateListener authListener = new FirebaseAuth.AuthStateListener() {
                @Override
                public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                    FirebaseUser user1 = firebaseAuth.getCurrentUser();
                    if (firebaseAuth.getCurrentUser() == null) {
                        startActivity(new Intent(usermai.this, Login.class));
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

        else if (id == R.id.searchmadakke)
        {
           // if(mAdapter!=null)
            //{
               // MaterialSearchView searchView=(MaterialSearchView) MenuItemCompat.getActionView(item);
              //  searchView.setOnQueryTextListener((MaterialSearchView.OnQueryTextListener) this);
               // searchView.setMenuItem(item);
               /* searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        //Do some magic
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {
                        //Do some magic
                        newText=newText.toLowerCase();
                        List<Upload> newlist=new ArrayList<>();
                        for(Upload upload : mUploads)
                        {
                            String name= upload.getmCropname().toLowerCase();
                            String Fname=upload.getName().toLowerCase();
                            String Price=upload.getPrice().toLowerCase();
                            String address=upload.getmAddress().toLowerCase();
                            if ((name.contains(newText)) || (Fname.contains(newText)) || (Price.contains(newText))
                                    || (address.contains(newText)))
                            {
                                newlist.add(upload);
                            }
                        }
                        mAdapter.setFilter(newlist);
                        return true;
                    }
                });*/

            //}

        }

        return super.onOptionsItemSelected(item);
    }






    @Override
    public void onDestroy() {
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
        super.onDestroy();
    }



    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.farmer) {
            final String MY_PREFS_NAME = "MyPrefsFile";

            SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
            editor.putInt("idName", 12);
            editor.apply();
            startActivity(new Intent(usermai.this,MainActivity.class));
           // overridePendingTransition(R.anim.slidein,R.anim.slideout);



        } else if (id == R.id.changeloc) {

            startActivity(new Intent(usermai.this,autocompleteapi.class));
            finish();

        } else if (id == R.id.paymadu) {
            startActivity(new Intent(usermai.this,paymadakke.class));

        } else if (id == R.id.checkvisitors) {
            startActivity(new Intent(usermai.this,cropviewer.class));

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.about) {

            startActivity(new Intent(usermai.this,nambagge.class));

        }else if(id == R.id.updatefarmerdetails)
        {
            startActivity(new Intent(usermai.this,updatefarmerdetails.class));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        //SearchView.attachNavigationDrawerToMenuButton(mDrawerLayout);

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    @Override    protected void onResume() {
        if (auth.getCurrentUser() == null) {
            startActivity(new Intent(usermai.this, Login.class).putExtra("mode1",1));
            finish();
        }
        sharedPref=getSharedPreferences("myPref",MODE_PRIVATE);

        location = sharedPref.getString("location", "default");

        if(location.equalsIgnoreCase("default"))
        {


            // save your string in SharedPreferences
            sharedPref.edit().putString("location", "Tumkur").commit();


            //Location autocomplete madtittu below activity but library is dead now bere enadru madbeku
            //startActivity(new Intent(usermai.this,autocompleteapi.class));
            //finish();
        }
        super.onResume();
    }


    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        newText=newText.toLowerCase();
        List<Upload> newlist=new ArrayList<>();
        return true;
    }
    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        searchView.setTranslationY(verticalOffset);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }


    public FloatingActionButton getFloatingActionButton(){
        return fab;
    }

    public EditText getSearchmadakkeedittext()
    {
        return searchmadakkeedittext;
    }

    public ActionBarDrawerToggle getToggle()
    {
        return toggle;
    }
}