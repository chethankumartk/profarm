package com.example.chethan.industrain.newsadapter;

import android.graphics.Typeface;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.chethan.industrain.R;
import com.squareup.picasso.Picasso;

public class NewsViewMukya extends AppCompatActivity {
    private TextView maintext,fulltext;
    private ImageView expandedimage;
    private String m,f,imageurl;
    CollapsingToolbarLayout toolbarLayout;
    private TextView date;
    String dt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_view_mukya);
        final Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbarLayout=findViewById(R.id.toolbar_layout);
        getSupportActionBar().setTitle("News");

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

        expandedimage=findViewById(R.id.expandedImage);
        maintext=findViewById(R.id.maintext);
        fulltext=findViewById(R.id.fulltext);
        date=findViewById(R.id.date);
        this.m=getIntent().getStringExtra("maintext");
        this.f=getIntent().getStringExtra("fulltext");
        this.imageurl=getIntent().getStringExtra("imageurl");
        this.dt=getIntent().getStringExtra("date");
        Picasso.with(this)
                .load(imageurl.toString().trim())
                .placeholder(R.drawable.gradient5)
                .fit()
                .into(expandedimage);
        maintext.setText(m);
        fulltext.setText(f);
        date.setText("Posted on: "+dt);



    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}
