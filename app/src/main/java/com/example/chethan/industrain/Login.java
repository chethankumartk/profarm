package com.example.chethan.industrain;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;
import com.squareup.picasso.Picasso;

public class Login extends AppCompatActivity {

    private EditText inputEmail, inputPassword;
    private FirebaseAuth auth;
    private Button btnSignUp, btnLogin;

    private ProgressDialog PD;
     int mode;
     ImageView image;

    @Override    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.login2);
        image=findViewById(R.id.image);
        Picasso.with(this).load(R.drawable.fruits).fit().into(image);


        getSupportActionBar().hide();
       // startActivity(new Intent(Login.this,launcher.class));
        PD = new ProgressDialog(this);
        PD.setMessage("Loading...");
        PD.setCancelable(true);
        PD.setCanceledOnTouchOutside(false);
        auth = FirebaseAuth.getInstance();
         mode = getIntent().getIntExtra("mode1", 0);



        inputEmail = (EditText) findViewById(R.id.email);
        inputPassword = (EditText) findViewById(R.id.password);
        btnSignUp = (Button) findViewById(R.id.sign_up_button);
        btnLogin = (Button) findViewById(R.id.sign_in_button);
       // Toast.makeText(this, "Make sure that you have a stable Internet Connection", Toast.LENGTH_SHORT).show();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override            public void onClick(View view) {
                final String email = inputEmail.getText().toString().trim();
                final String password = inputPassword.getText().toString().trim();

                try {

                    if (password.length() > 0 && email.length() > 0) {
                        PD.show();


                        auth.signInWithEmailAndPassword(email, password)
                                .addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            String token_id = FirebaseInstanceId.getInstance().getToken();

                                            FirebaseFirestore.getInstance().collection("profile").
                                                    document(auth.getUid().toString().trim())
                                                    .update("token_id",token_id)
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if(task.isSuccessful())
                                                            {

                                                                Intent intent = new Intent(Login.this, usermai.class);
                                                                startActivity(intent);
                                                                finish();
                                                                // save your string in SharedPreferences

                                                            }



                                                        }
                                                    });






                                        } else {

                                                try {
                                                    throw task.getException();
                                                } catch(FirebaseAuthWeakPasswordException e) {
                                                    Toast.makeText(
                                                            Login.this,
                                                            "Fill All Fields",
                                                            Toast.LENGTH_LONG).show();
                                                } catch(FirebaseAuthInvalidCredentialsException e) {
                                                    Toast.makeText(
                                                            Login.this,
                                                            "Invalid",
                                                            Toast.LENGTH_LONG).show();
                                                } catch(FirebaseAuthUserCollisionException e) {
                                                    Toast.makeText(
                                                            Login.this,
                                                            "Fill All Fields",
                                                            Toast.LENGTH_LONG).show();
                                                } catch(Exception e) {
                                                    Toast.makeText(
                                                            Login.this,
                                                            "Fill All Fields",
                                                            Toast.LENGTH_LONG).show();
                                                }



                                        }
                                        PD.dismiss();
                                    }
                                });
                    } else {
                        Toast.makeText(
                                Login.this,
                                "Fill All Fields",
                                Toast.LENGTH_LONG).show();
                    }
                }catch (Exception e)
                {
                    Toast.makeText(Login.this, "invalid", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override            public void onClick(View view) {
                if(mode==1)
                {
                    startActivity(new Intent(Login.this,Register.class).putExtra("mode2",1));
                }
                else
                {

                    startActivity(new Intent(Login.this,Register.class).putExtra("mode2",2));
                }

            }
        });

        findViewById(R.id.forget_password_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), ForgetPassword.class).putExtra("Mode", 0));
            }
        });

    }

    @Override    protected void onResume() {
        if (auth.getCurrentUser() != null) {
            if(mode == 1)
            {
                startActivity(new Intent(Login.this, usermainActivity.class));
                finish();
            }
            else
            {
                startActivity(new Intent(Login.this, MainActivity.class));
                finish();
            }

        }
        super.onResume();
    }

   /* boolean doubleBackToExitPressedOnce=false;

    @Override
    public void onBackPressed() {

        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Snackbar.make(linearLayout, "Please click BACK again to exit", Snackbar.LENGTH_LONG).show();

        //Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }*/



}
