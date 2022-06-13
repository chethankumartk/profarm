package com.example.chethan.industrain.introslider;

import android.content.Intent;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.chethan.industrain.R;
import com.example.chethan.industrain.usermai;


public class WelcomeMadadhu extends AppCompatActivity implements View.OnClickListener{

    private ViewPager mPager;
    private int[] layouts={R.layout.first,R.layout.fsecond,R.layout.fthird};
    private welcomeadapter mpagerAdapter;
    private LinearLayout Dots_Layout;
    private ImageView[] dots;
    private Button BnNext,BnSkip;
    private ZoomOut zoomOut;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        setContentView(R.layout.activity_welcome_madadhu);
        zoomOut=new ZoomOut();


        if(new preferencekodomanager(this).checkPreference())
        {
            loadHome();
        }


        if(Build.VERSION.SDK_INT>19)
        {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        else
        {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }


        mPager=(ViewPager)findViewById(R.id.viewPager);
        mpagerAdapter=new welcomeadapter(layouts,this);
        mPager.setAdapter(mpagerAdapter);
        Dots_Layout = (LinearLayout)findViewById(R.id.dotsLayout);
        BnNext=(Button)findViewById(R.id.bnNext);
        BnSkip=(Button)findViewById(R.id.bnSkip);
        BnNext.setOnClickListener(this);
        BnSkip.setOnClickListener(this);
        mPager.setPageTransformer(true,zoomOut);

        createDots(0);

        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener()
        {
            @Override
            public void onPageScrolled(int position,float positionOffset,int positionOffsetPixels)
            {

            }



            @Override
            public void onPageSelected(int position)
            {
                createDots(position);
                if(position==layouts.length-1)
                {
                    BnNext.setText("start");
                    BnSkip.setVisibility(View.INVISIBLE);
                }

                else
                {
                    BnNext.setText("Next");
                    BnSkip.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onPageScrollStateChanged(int state)
            {

            }
        });
    }

    private void createDots(int current_position)
    {
        if(Dots_Layout!=null)
            Dots_Layout.removeAllViews();
        dots = new ImageView[layouts.length];

        for (int i=0;i<layouts.length;i++)
        {
            dots[i]=new ImageView(this);
            if(i==current_position)
            {
                dots[i].setImageDrawable(ContextCompat.getDrawable(this,R.drawable.dotsactive));
            }
            else
            {
                dots[i].setImageDrawable(ContextCompat.getDrawable(this,R.drawable.dotsdefault));
            }
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(4,0,4,0);
            Dots_Layout.addView(dots[i],params);
        }
    }

    @Override
    public void onClick(View v) {
        switch(v.getId())
        {
            case R.id.bnNext:loadNextSlide();
                break;
            case R.id.bnSkip:loadHome();
                new preferencekodomanager(this).writePreference();


                break;
        }
    }

    private void loadHome()
    {
        startActivity(new Intent(this,usermai.class));
        finish();
    }
    private void loadNextSlide()
    {
        int next_slide = mPager.getCurrentItem()+1;
        if (next_slide<layouts.length)
        {
            mPager.setCurrentItem(next_slide);
        }
        else
        {
            loadHome();
            new preferencekodomanager(this).writePreference();
        }


    }
}
