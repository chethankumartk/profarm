package com.example.chethan.industrain.CropGuidelines;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.AppBarLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.chethan.industrain.R;
import com.example.chethan.industrain.introslider.ZoomOut;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

public class GuidelineActivity extends AppCompatActivity {
    private TextView mProfileLabel;
    private TextView mUsersLabel;
    private TextView mNotificationLabel;
    private LinearLayout linearLayout;
    private ImageView cropimage;

    private ViewPager mMainPager;
    crop cropp;
    String refid,name;

    private PagerViewAdapter mPagerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guideline);
        ZoomOut zoomOut=new ZoomOut();
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
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Berries");
        final Bundle bundle=new Bundle();
        bundle.putString("name",refid);
        name=getIntent().getStringExtra("name");

        getSupportActionBar().setTitle(name);



        refid=getIntent().getStringExtra("refid");


        cropimage=findViewById(R.id.cropimage);

        mProfileLabel = (TextView) findViewById(R.id.profileLabel);
        mUsersLabel = (TextView) findViewById(R.id.usersLabel);
        mNotificationLabel = (TextView) findViewById(R.id.notificationsLabel);

        mMainPager = (ViewPager) findViewById(R.id.mainPager);
        linearLayout=findViewById(R.id.ll2);
        mMainPager.setOffscreenPageLimit(3);
        mPagerViewAdapter = new PagerViewAdapter(getSupportFragmentManager(),bundle);
        mMainPager.setAdapter(mPagerViewAdapter);
        mMainPager.setPageTransformer(true,zoomOut);


        mProfileLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mMainPager.setCurrentItem(0);

            }
        });

        mUsersLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mMainPager.setCurrentItem(1);

            }
        });

        mNotificationLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mMainPager.setCurrentItem(2);

            }
        });

        mMainPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onPageSelected(int position) {

                changeTabs(position);

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        FirebaseFirestore.getInstance().collection("crops").whereEqualTo("re",refid)
                .limit(1).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if(task.isSuccessful())
                        {

                            for (QueryDocumentSnapshot document : task.getResult()) {


                                cropp = document.toObject(crop.class);
                                Picasso.with(GuidelineActivity.this)
                                        .load(cropp.getIu())
                                        .fit()
                                        .placeholder(R.drawable.shadow)
                                        .into(cropimage);

                            }




                        }
                    }
                });


    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void changeTabs(int position) {

        if(position == 0){

            mProfileLabel.setTextColor(getColor(R.color.white));
            mProfileLabel.setBackground(getDrawable(R.drawable.chenagkanakke));
            mUsersLabel.setBackground(getDrawable(R.drawable.cropguidetabback5));
            mNotificationLabel.setBackground(getDrawable(R.drawable.welcomeback));

            linearLayout.setBackground(getDrawable(R.drawable.cropguidetabback));
            mUsersLabel.setTextColor(getColor(R.color.colorPrimary));

            mNotificationLabel.setTextColor(getColor(R.color.colorPrimary));

        }

        if(position == 1){

            mProfileLabel.setTextColor(getColor(R.color.colorPrimary));
            mProfileLabel.setBackground(getDrawable(R.drawable.cropguidetabback4));
            mUsersLabel.setBackground(getDrawable(R.drawable.cropguidetabback2));
            mNotificationLabel.setBackground(getDrawable(R.drawable.welcomeback));

            linearLayout.setBackground(getDrawable(R.drawable.cropguidetabback));

            mUsersLabel.setTextColor(getColor(R.color.white));

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                mNotificationLabel.setTextColor(getColor(R.color.colorPrimary));
            }

        }

        if(position == 2){

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                mProfileLabel.setTextColor(getColor(R.color.colorPrimary));
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                mUsersLabel.setTextColor(getColor(R.color.colorPrimary));
            }
            mProfileLabel.setBackground(getDrawable(R.drawable.cropguidetabback4));
            mUsersLabel.setBackground(getDrawable(R.drawable.cropguidetabback5));
            mNotificationLabel.setBackground(getDrawable(R.drawable.cropguidetabback3));

            linearLayout.setBackground(getDrawable(R.drawable.cropguidetabback));


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                mNotificationLabel.setTextColor(getColor(R.color.white));
            }

        }

    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


}
