package com.example.chethan.industrain.newsadapter;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.chethan.industrain.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class NewsRecycler extends AppCompatActivity {
    RecyclerView recyclerView;
    List<news> mUploads;
    NewsAdapter newsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_recycler);
        getSupportActionBar().setTitle("News Feed");
        recyclerView=findViewById(R.id.recycl);
        mUploads=new ArrayList<>();
        recyclerView.setHasFixedSize(true);
        // mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setNestedScrollingEnabled(false);
        final LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        recyclerView.setLayoutManager(layoutManager);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        gett();

    }
    public  void gett(){

        FirebaseFirestore.getInstance().collection("news")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {


                                news upload = document.toObject(news.class);
                                mUploads.add(upload);


                            }
                            newsAdapter = new NewsAdapter(NewsRecycler.this, mUploads);

                            recyclerView.setAdapter(newsAdapter);


                        } else {
                        }
                    }
                });

    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}
