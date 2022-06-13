package com.example.chethan.industrain;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Register extends AppCompatActivity {

    private EditText inputEmail, inputPassword;
    private FirebaseAuth auth;
    private Button btnSignUp;
    private ProgressDialog PD;
    int mode;
    FrameLayout editt;
    private Uri mImageUri;
    FirebaseStorage mFir;
    DocumentReference documentReference;
    Uri imagemfinal;


    FirebaseUser mUser;
    private StorageReference mStorageRef;
    ImageView profImage;


    EditText username;
    SharedPreferences sharedPref;
    private static final int PERMISSION_REQUEST_CODE = 1;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().hide();
        checkPermission();

        PD = new ProgressDialog(this);
        PD.setMessage("Loading...");
        PD.setCancelable(true);
        PD.setCanceledOnTouchOutside(false);
        profImage=findViewById(R.id.profImage);

        auth = FirebaseAuth.getInstance();
        mode = getIntent().getIntExtra("mode2",0);
        username=findViewById(R.id.Username);
        editt=findViewById(R.id.editt);



        if (auth.getCurrentUser() != null && auth.getCurrentUser().isEmailVerified()==true) {
            startActivity(new Intent(Register.this, MainActivity.class));
            finish();
        }

        inputEmail = (EditText) findViewById(R.id.email);
        inputPassword = (EditText) findViewById(R.id.password);
        btnSignUp = (Button) findViewById(R.id.sign_up_button);

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override            public void onClick(View view) {
                final String email = inputEmail.getText().toString().trim();
                final String password = inputPassword.getText().toString().trim();
                final String usernamestr=username.getText().toString().trim();


                try {
                    if (password.length() > 0 && email.length() > 0 && usernamestr.length()>0) {
                        if(vali(email))
                        {
                            PD.show();
                            auth.createUserWithEmailAndPassword(email, password)
                                    .addOnCompleteListener(Register.this, new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            if (!task.isSuccessful()) {

                                                try {
                                                    throw task.getException();
                                                } catch(FirebaseAuthWeakPasswordException e) {
                                                    Toast.makeText(
                                                            Register.this,
                                                            "Fill All Fields",
                                                            Toast.LENGTH_LONG).show();
                                                } catch(FirebaseAuthInvalidCredentialsException e) {
                                                    Toast.makeText(
                                                            Register.this,
                                                            "Invalid",
                                                            Toast.LENGTH_LONG).show();
                                                } catch(FirebaseAuthUserCollisionException e) {
                                                    Toast.makeText(
                                                            Register.this,
                                                            "Fill All Fields",
                                                            Toast.LENGTH_LONG).show();
                                                } catch(Exception e) {
                                                    Toast.makeText(
                                                            Register.this,
                                                            "Fill All Fields",
                                                            Toast.LENGTH_LONG).show();
                                                }
                                            } else {
                                                documentReference=FirebaseFirestore.getInstance().collection("profile").document(auth.getUid());

                                                mStorageRef = FirebaseStorage.getInstance().getReference("profile").child(auth.getUid());

                                                mUser=auth.getCurrentUser();


                                                if(mImageUri!=null && imagemfinal!=null)
                                                {
                                                    mFir= FirebaseStorage.getInstance();
                                                                        final StorageReference fileReference = mStorageRef.child(System.currentTimeMillis()
                                                                                + "." + getFileExtension(mImageUri));

                                                                        fileReference.putFile(imagemfinal).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                                                                            @Override
                                                                            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                                                                                if (!task.isSuccessful()) {
                                                                                    throw task.getException();
                                                                                }
                                                                                return fileReference.getDownloadUrl();
                                                                            }


                                                                        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                                                                            @Override
                                                                            public void onComplete(@NonNull Task<Uri> task) {
                                                                                if (task.isSuccessful()) {
                                                                                    Uri downloadUri = task.getResult();
                                                                                    //Toast.makeText(uploadmain.this, "Upload successful", Toast.LENGTH_LONG).show();

                                                                                    String token_id = FirebaseInstanceId.getInstance().getToken();
                                                                                    String eidd=auth.getUid();
                                                                                    Map<String, Object> user1 = new HashMap<>();
                                                                                    user1.put("username", usernamestr);
                                                                                    user1.put("token_id",token_id);
                                                                                    user1.put("eid",eidd);
                                                                                    user1.put("iu",downloadUri.toString().trim());

                                                                                    FirebaseFirestore.getInstance().collection("profile").
                                                                                            document(auth.getUid().toString().trim())
                                                                                            .set(user1)
                                                                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                                @Override
                                                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                                                    if(task.isSuccessful())
                                                                                                    {
                                                                                                        sendverificationcode();


                                                                                                        sharedPref = getSharedPreferences("myPref",MODE_PRIVATE);

                                                                                                        // save your string in SharedPreferences
                                                                                                        sharedPref.edit().putString("username", usernamestr).commit();
                                                                                                        if (mode == 1)
                                                                                                        {

                                                                                                            Intent intent = new Intent(Register.this, usermai.class);
                                                                                                            startActivity(intent);
                                                                                                            finish();
                                                                                                        }
                                                                                                        else
                                                                                                        {

                                                                                                            Intent intent = new Intent(Register.this, MainActivity.class);
                                                                                                            startActivity(intent);
                                                                                                            finish();
                                                                                                        }

                                                                                                        Toast.makeText(Register.this, "Account created Successfully", Toast.LENGTH_SHORT).show();


                                                                                                    }



                                                                                                }
                                                                                            });




                                                                                } else {
                                                                                    Toast.makeText(Register.this, "Update failed: " + task.getException().getMessage(),
                                                                                            Toast.LENGTH_SHORT).show();
                                                                                }
                                                                            }
                                                                        });








                                                    }



                                                      //  .set("name",usernamestr)



                                               // Intent intent = new Intent(Register.this, Login.class);
                                               // startActivity(intent);
                                                //finish();



                                            }
                                        }
                                    });
                        } else {
                            Toast.makeText(
                                    Register.this,
                                    "Fill All Fields",
                                    Toast.LENGTH_LONG).show();
                        }}
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        editt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                openFileChooser();
            }
        });




    }
    public boolean vali(String a)
    {
        final String EMAIL_PATTERN =
                "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                        + "[A-Za-z]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(a);
        if (matcher.find()) {
            String email = a.substring(matcher.start(), matcher.end());
        } else {
            // TODO handle condition when input doesn't have an email address
            Toast.makeText(
                    Register.this,
                    "Invalid Email",
                    Toast.LENGTH_LONG).show();
            inputEmail.setError("Enter valid Email");
            return false;

        }
        return true;


    }
    public void sendverificationcode()
    {
        mUser.sendEmailVerification().addOnCompleteListener(this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(Register.this, "Verification code sent to"+mUser.getEmail(), Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Register.this, "Verification code Not sent", Toast.LENGTH_SHORT).show();
            }
        });

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data !=null && data.getData()!=null)
        {
            mImageUri = data.getData();
            if(mImageUri!=null)
            {
                try {
                    Bitmap   bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), mImageUri);
                    storeImage(bitmap);
                    imagemfinal = Uri.fromFile(getOutputMediaFile().getAbsoluteFile());
                    Picasso.with(Register.this).load(imagemfinal).fit().into(profImage);

                } catch (IOException e) {
                    e.printStackTrace();
                }
                catch (Exception e)
                {
                    Toast.makeText(this, ""+e, Toast.LENGTH_SHORT).show();
                }

            }



        }
    }




    void openFileChooser()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,1);
    }
    private  String getFileExtension (Uri uri)
    {
        ContentResolver cr = this.getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(uri));
    }


    private String storeImage(Bitmap image) {
        File pictureFile = getOutputMediaFile();
        if (pictureFile == null) {
            Toast.makeText(this, "not created", Toast.LENGTH_SHORT).show();
            return null;
        }
        try {
            FileOutputStream fos = new FileOutputStream(pictureFile);
            image.compress(Bitmap.CompressFormat.JPEG, 30, fos);
            fos.close();
        } catch (FileNotFoundException e) {
        } catch (IOException e) {
        }
        return null;
    }

    private  File getOutputMediaFile(){

        File mediaStorageDir = new File(Environment.getExternalStorageDirectory()
                + "/Android/data/"
                + this.getPackageName().toString().trim()
                + "/Files");


        if (!mediaStorageDir.exists()){


            if (!mediaStorageDir.mkdirs()){
                Toast.makeText(this, "directory not created", Toast.LENGTH_SHORT).show();
                return null;
            }
        }
        String timeStamp = new SimpleDateFormat("ddMMyyyy_HHmm").format(new Date());
        File mediaFile;
        String mImageName="MI_"+ timeStamp +".jpg";
        mediaFile = new File(mediaStorageDir.getPath() + File.separator + mImageName);
        return mediaFile;
    }
















    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            requestPermission();
            return false;
        }
    }
    private void requestPermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            Toast.makeText(this, "Write External Storage permission allows us to do store images. Please allow this permission in App Settings.", Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (requestCode == 100) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // do something
            }
            return;
        }
    }



}

