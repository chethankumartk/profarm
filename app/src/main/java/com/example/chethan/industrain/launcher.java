package com.example.chethan.industrain;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chethan.industrain.introslider.WelcomeMadadhu;
import com.google.firebase.auth.FirebaseAuth;

import org.w3c.dom.Text;

public class launcher extends AppCompatActivity {
    CoordinatorLayout linearLayout;
    FirebaseAuth auth;
    ImageView imageView;
    int mode;
    int idName;
    public static final String MY_PREFS_NAME = "MyPrefsFile";

    private static int SPLASH_TIME_OUT=1500;

    TextView tv1,tv2;
    Animation animFadein;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_launcher);

        getSupportActionBar().hide();

        tv1=findViewById(R.id.txt1);
        tv2=findViewById(R.id.txt2);
        linearLayout=findViewById(R.id.log1);


        animFadein = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.txtvwfadein);


        Handler handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                tv1.setVisibility(View.VISIBLE);
                tv1.startAnimation(animFadein);


            }
        },300);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                tv1.clearAnimation();

                tv2.setVisibility(View.VISIBLE);

                tv2.startAnimation(animFadein);


            }
        },800);


        mode=getIntent().getIntExtra("mode2",0);
        auth=FirebaseAuth.getInstance();
       /* farmer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String MY_PREFS_NAME = "MyPrefsFile";

                SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                editor.putInt("idName", 12);
                editor.apply();
                startActivity(new Intent(launcher.this,MainActivity.class));

            }
        });
        user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(launcher.this, "not yet designed", Toast.LENGTH_SHORT).show();
               // startActivity(new Intent(launcher.this,usermainActivity.class));
                startActivity(new Intent(launcher.this,usermai.class));

            }
        });
        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                  startActivity(new Intent(launcher.this,nambagge.class));
            }
        });*/

        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                Intent homeIntent = new Intent(launcher.this,WelcomeMadadhu.class);
                startActivity(homeIntent);
                finish();
                overridePendingTransition(R.anim.fadein,R.anim.fadeout);
            }
        },SPLASH_TIME_OUT);

    }
    boolean doubleBackToExitPressedOnce=false;
    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Snackbar.make(linearLayout, "Please click BACK again to exit", Snackbar.LENGTH_LONG).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }

    /*@Override    protected void onResume() {
        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        String restoredText = prefs.getString("text", null);
        if (restoredText != null) {
            idName = prefs.getInt("idName", 0); //0 is the default value.
        }




        if (auth.getCurrentUser() != null) {
            startActivity(new Intent(launcher.this, MainActivity.class));
            finish();
        }
        super.onResume();
    }*/
}
