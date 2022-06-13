package com.example.chethan.industrain.CropGuidelines;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.chethan.industrain.Login;
import com.example.chethan.industrain.R;
import com.example.chethan.industrain.RecyclerTouchListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class GuideLinesListActivity extends AppCompatActivity  {

    String eid;
    private RecyclerView mRecyclerView;

    String selected="";
    Query nextQuery;
    Toolbar toolbar;
    EditText searchmadakkeedittext;
    RecyclerView mRecyclerview;
    ImageView voicee;
    Query query1;
    searchmadoadapter adapter;
    int firstVisibleItem,visibleItemCount,totalItemCount;
    SharedPreferences sharedPref;

    private TextToSpeech textToSpeech;




    DocumentSnapshot lastvisible;
    GuideAdapter mAdapter;
    crop upload;
    String location;
    List<crop> mUploads;
    FirebaseAuth auth;
    int mode;
    GridLayoutManager layoutManager;
    Query query;










    // private List<Upload> mUploads;
    FirebaseFirestore db;
    boolean isScrolling,isLastItemReached;





    @Override    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide_lines_list);
        // getSupportActionBar().hide();

        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setNavigationBarColor(getResources().getColor(R.color.blackk));
            getWindow().setStatusBarColor(getResources().getColor(R.color.blackk));
        }
        voicee=findViewById(R.id.voicee);

        voicee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listen();
            }
        });



        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        searchmadakkeedittext=findViewById(R.id.searchmadakkeedittext);
        mRecyclerview=findViewById(R.id.searchrecycle);
        mRecyclerview.setHasFixedSize(true);

        // mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        //mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerview.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true));
        searchmadakkeedittext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (charSequence.toString().length() == 0) {

                } // This is used as if user erases the characters in the search field.
                else {

                    query1 = FirebaseFirestore.getInstance().collection("crops").orderBy("na").startAt(charSequence.toString().
                            trim()).endAt(charSequence.toString().trim() + "\uf8ff").limit(5);
                    adapterhaku(query1);
                }
                // adapter.notifyDataSetChanged();

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });








        auth = FirebaseAuth.getInstance();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mUploads=new ArrayList<>();

        mRecyclerView = findViewById(R.id.recycl);
        mRecyclerView.setHasFixedSize(true);
       // mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setNestedScrollingEnabled(false);




        db=FirebaseFirestore.getInstance();


        //mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        layoutManager=new GridLayoutManager(this,2);
        mRecyclerView.setLayoutManager(layoutManager);
        gett();
        mRecyclerview.addOnItemTouchListener(new RecyclerTouchListener(this, mRecyclerview,
                new RecyclerTouchListener.ClickListener() {
                    @Override
                    public void onClick(View view, int position) {
                        crop uploadCurrent = mUploads.get(position);


                        // Toast.makeText(GuideLinesListActivity.this, ""+uploadCurrent.getNa(), Toast.LENGTH_SHORT).show();



                        // showUpdateDeleteDialog(upload.getIdd(), upload.getName(),upload.getImageUrl(),
                        //       upload.getmAddress(),upload.getmCropname(),upload.getPhon());

                    }

                    @Override
                    public void onLongClick(View view, int position) {


                        //showUpdateDeleteDialog((Stringupload.getIdd(),upload.getName(),upload.getImageUrl());
                    }
                }));



        mRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(this, mRecyclerView,
                new RecyclerTouchListener.ClickListener() {
                    @Override
                    public void onClick(View view, int position) {
                        crop uploadCurrent = mUploads.get(position);

                        sharedPref=getSharedPreferences("myPref",MODE_PRIVATE);

                        sharedPref.edit().putString("re", uploadCurrent.getRe()).commit();
                        startActivity(new Intent(GuideLinesListActivity.this,GuidelineActivity.class)
                                .putExtra("refid",uploadCurrent.getRe())
                                .putExtra("name",uploadCurrent.getNa()));

                        // Toast.makeText(GuideLinesListActivity.this, ""+uploadCurrent.getNa(), Toast.LENGTH_SHORT).show();



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

    @Override    protected void onResume() {
        if (auth.getCurrentUser() == null) {
            startActivity(new Intent(GuideLinesListActivity.this, Login.class).putExtra("mode1",1));
            finish();
        }
        super.onResume();
    }

    public void gett()
    {

        //Toast.makeText(this, "1", Toast.LENGTH_SHORT).show();


        query=FirebaseFirestore.getInstance().collection("crops")
                .orderBy("na", Query.Direction.DESCENDING)
                .limit(10);





        query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                mUploads.clear();

                for (QueryDocumentSnapshot documentSnapshots: queryDocumentSnapshots)
                {
                    upload=documentSnapshots.toObject(crop.class);
                    mUploads.add(upload);


                }
                try
                {
                    mAdapter=new GuideAdapter(GuideLinesListActivity.this,mUploads);
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

                            }


                            // Toast.makeText(checking.this, "Next Page Loaded", Toast.LENGTH_SHORT).show();



                                 firstVisibleItem=layoutManager.findFirstVisibleItemPosition();
                                 visibleItemCount=layoutManager.getChildCount();
                                 totalItemCount=layoutManager.getItemCount();

                            {

                            }

                            if ( (isScrolling && firstVisibleItem + visibleItemCount == totalItemCount) && !isLastItemReached) {
                                isScrolling=false;

                                nextQuery=FirebaseFirestore.getInstance()
                                        .collection("crops")
                                        .orderBy("na", Query.Direction.DESCENDING)
                                        .startAfter(lastvisible)
                                        .limit(10);









                                nextQuery
                                        .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                    @Override
                                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                        for (QueryDocumentSnapshot documentSnapshots: queryDocumentSnapshots)
                                        {
                                            upload=documentSnapshots.toObject(crop.class);
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


                                        if(queryDocumentSnapshots.size() < 10)
                                        {
                                            isLastItemReached=true;
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
        inittts();


    }
    public void adapterhaku( Query query11)
    {

        query11.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    List<crop> mUploads=new ArrayList<>();


                    for(QueryDocumentSnapshot document : task.getResult()) {

                        crop upload = document.toObject(crop.class);
                        mUploads.add(upload);
                    }
                    adapter = new searchmadoadapter(GuideLinesListActivity.this, mUploads);
                    mRecyclerview.setAdapter(adapter);
                }
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 100){
            if (resultCode == RESULT_OK && null != data) {
                ArrayList<String> res = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                String inSpeech = res.get(0);
                Toast.makeText(this, ""+inSpeech, Toast.LENGTH_SHORT).show();

                query1 = FirebaseFirestore.getInstance().collection("crops").whereEqualTo("na",inSpeech).limit(5);
                adapterhaku(query1);
                //startActivity(new Intent(getApplicationContext(),usermainActivity.class).putExtra("cropsearch",inSpeech));

                //recognition(inSpeech);
            }
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
    public void listen()
    {
        Intent i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        i.putExtra(RecognizerIntent.EXTRA_PROMPT, "Try saying Something");

        try {
            startActivityForResult(i, 100);
        } catch (ActivityNotFoundException a) {
           // Toast.makeText(usermai.this, "Your device doesn't support Speech Recognition", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void onDestroy() {
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
        super.onDestroy();
    }



}