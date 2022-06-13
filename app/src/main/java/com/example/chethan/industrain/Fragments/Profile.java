package com.example.chethan.industrain.Fragments;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chethan.industrain.CircleImageView;
import com.example.chethan.industrain.Login;
import com.example.chethan.industrain.R;
import com.example.chethan.industrain.phoneauthentication.UpdatePhone;
import com.example.chethan.industrain.phoneauthentication.verifyphone;
import com.example.chethan.industrain.usermai;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
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

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class Profile extends Fragment {
    private ActionBarDrawerToggle toggle;
    private TextView wishes;
    LinearLayout editprofile;
    String username,emaill;
    EditText Usernae,email,new2,new1,Current;
    String  username1;
    TextView Email,phoneno;
    FrameLayout editt;
    private StorageReference mStorageRef;
    FirebaseAuth auth;
    SharedPreferences sharedPref;
    LinearLayout changepassword;
    String cur,New1,New2;
    LinearLayout logoutt;
    LinearLayout updateph;
    EditText phoner;
    DocumentReference documentReference;
    CircleImageView profImage;
    LinearLayout forgot;
    private Uri mImageUri;
    FirebaseStorage mFir;
    private ProgressDialog PD;
    Uri imagemfinal;
    private static final int PERMISSION_REQUEST_CODE = 1;








    public Profile() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View view=inflater.inflate(R.layout.fragment_profile,container,false);
        toggle = ((usermai) getActivity()).getToggle();
        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.colorPrimary));
        wishes=view.findViewById(R.id.wishes);
        profImage=view.findViewById(R.id.profImage);
        auth=FirebaseAuth.getInstance();
        logoutt=view.findViewById(R.id.logoutt);
        editt=view.findViewById(R.id.editt);
        updateph=view.findViewById(R.id.UpdatePhoneno);
        documentReference=FirebaseFirestore.getInstance().collection("profile").document(auth.getUid());
        checkPermission();
        mStorageRef = FirebaseStorage.getInstance().getReference("profile").child(auth.getUid());

        updateph.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updatephone();
            }
        });
        logoutt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                auth.signOut();
                startActivity(new Intent(getActivity(), Login.class));
                getActivity().finish();
            }
        });

        changepassword=view.findViewById(R.id.changepassword);
        Email=view.findViewById(R.id.Email);
        phoneno=view.findViewById(R.id.phoneno);

        forgot=view.findViewById(R.id.forget);

        changepassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changepasss();

            }
        });



        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                auth.sendPasswordResetEmail(auth.getCurrentUser().getEmail()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getActivity(), "We have sent you instructions to reset your password!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getActivity(), "Failed to send reset email!", Toast.LENGTH_SHORT).show();
                        }

                    }
                });

            }
        });



        getTimeFromAndroid();
        editprofile=view.findViewById(R.id.profileedit);
        Email=view.findViewById(R.id.Email);
        phoneno=view.findViewById(R.id.phoneno);
        editprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                editprof();

            }
        });
        editt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                openFileChooser();
            }
        });



        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }
    private void getTimeFromAndroid() {


       /* FirebaseFirestore.getInstance().collection("profile").document(FirebaseAuth.getInstance().getUid().trim())
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if(task.isSuccessful())
                {

                    DocumentSnapshot documentSnapshot=task.getResult();
                    username1=documentSnapshot.get("username").toString();

                }

            }
        });*/
        Email.setText(auth.getCurrentUser().getEmail().toString());

        DocumentReference docRef = FirebaseFirestore.getInstance().collection("profile").document(auth.getUid());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null) {

                        String iu = document.getString("iu");
                        Picasso.with(getContext())
                                .load(iu)
                                .placeholder(R.drawable.account)
                                .fit()
                                .into(profImage);


                    } else {

                    }
                } else {

                }
            }
        });


        if (auth.getCurrentUser().getPhoneNumber()!=null)
        {
            phoneno.setText(auth.getCurrentUser().getPhoneNumber().toString());
        }

        sharedPref=getActivity().getSharedPreferences("myPref",MODE_PRIVATE);
        username1=sharedPref.getString("username","none");

        Date dt = new Date();
        int hours = dt.getHours();

        if(hours>=0 && hours<=12){
            wishes.setText("Good Morning "+username1+"!");
        }else if(hours>12 && hours<=16){
            wishes.setText("Good Afternoon "+username1+"!");

        }else if(hours>16 && hours<=21){
            wishes.setText("Good Evening "+username1+"!");

        }else if(hours>21 && hours<=24){
            wishes.setText("Hope u had a Goodday "+username1+"!");
        }






    }

    public void sett()
    {


            Map<String, Object> user1 = new HashMap<>();
            user1.put("username", username);
            FirebaseFirestore.getInstance().collection("profile").
                    document(FirebaseAuth.getInstance().getUid().toString().trim())
                    .update(user1)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful())
                            {
                                sharedPref = ((usermai)getActivity()).getSharedPreferences("myPref",MODE_PRIVATE);

                                // save your string in SharedPreferences
                                sharedPref.edit().putString("username", username).commit();


                                {
                                    Toast.makeText(getActivity(), "Updated", Toast.LENGTH_SHORT).show();
                                }

                            }



                        }
                    });





    }

    public void check()
    {
        if(username.length()>0 && emaill.length()>0)
        {
            FirebaseAuth.getInstance().getCurrentUser().updateEmail(emaill)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                sett();
                                Toast.makeText(getActivity(), "Email Updated", Toast.LENGTH_SHORT).show();

                            }
                            else
                            {
                                Toast.makeText(getActivity(), "try Logging out and logging in again", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

        }

    }

    public void editprof()
    {
        final Dialog dialog=new Dialog(getActivity());
        dialog.setTitle("Personal Info");
        dialog.setContentView(R.layout.dialogeditprofile);
        Button SAVE=dialog.findViewById(R.id.save);
        Usernae=dialog.findViewById(R.id.Username);
        email=dialog.findViewById(R.id.email);
        Usernae.setText(username1);
        email.setText(auth.getCurrentUser().getEmail());
        SAVE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                username=Usernae.getText().toString().trim();
                emaill=email.getText().toString().trim();

                check();
            }
        });

        dialog.show();

    }
    public void changepasss()
    {
        final Dialog dialog=new Dialog(getActivity());
        dialog.setContentView(R.layout.dialogchangepassword);
        Button SAVE=dialog.findViewById(R.id.save);
        Current=dialog.findViewById(R.id.current);
        new1=dialog.findViewById(R.id.new1);
        new2=dialog.findViewById(R.id.new2);
        SAVE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cur=Current.getText().toString().trim();
                New1=new1.getText().toString().trim();
                New2=new2.getText().toString().trim();
                if(New1.equals(New2))
                {

                    updatepass();
                }


            }
        });

        dialog.show();

    }

    public void updatepass()
    {
        AuthCredential credential = EmailAuthProvider
                .getCredential(auth.getCurrentUser().getEmail().toString().trim(), cur);

        auth.getCurrentUser().reauthenticate(credential)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            auth.getCurrentUser().updatePassword(New1).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(getActivity(), "Password Updated", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(getActivity(), "Password Not Updated", Toast.LENGTH_SHORT).show();

                                    }
                                }
                            });
                        } else {
                            Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();

                        }
                    }
                });

    }


    public void updatephone()
    {

        final Dialog dialog=new Dialog(getActivity());
        dialog.setTitle("Personal Info");
        dialog.setContentView(R.layout.dialogupdatephone);
        phoner=dialog.findViewById(R.id.NewPhone);
        phoner.setText(auth.getCurrentUser().getPhoneNumber().toString().trim());
        Button SAVE=dialog.findViewById(R.id.save);
        SAVE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phh=phoner.getText().toString().trim();

                if(phh.isEmpty() || phh.length() < 10){
                    phoner.setError("Enter a valid mobile");
                    phoner.requestFocus();
                    return;
                }
                else {

                    if (auth.getCurrentUser().getPhoneNumber()!=null)
                    {
                        Intent intent = new Intent(getActivity(), UpdatePhone.class);
                        intent.putExtra("mobile", phh);
                        startActivity(intent);

                    }
                    else
                    {
                        Intent intent = new Intent(getActivity(), verifyphone.class);
                        intent.putExtra("mobile", phh);
                        startActivity(intent);

                    }

                }


            }
        });

        dialog.show();







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
                   Bitmap   bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), mImageUri);
                   storeImage(bitmap);
                   imagemfinal = Uri.fromFile(getOutputMediaFile().getAbsoluteFile());
               } catch (IOException e) {
                   e.printStackTrace();
               }
               catch (Exception e)
               {
                   Toast.makeText(getActivity(), ""+e, Toast.LENGTH_SHORT).show();
               }

           }

            if(mImageUri!=null && imagemfinal!=null)
            {











                PD = new ProgressDialog(getContext());
                PD.setMessage("Updating...");
                PD.setCancelable(true);
                PD.setCanceledOnTouchOutside(false);

                PD.show();

                mFir=FirebaseStorage.getInstance();




                DocumentReference docc = FirebaseFirestore.getInstance().collection("profile").document(auth.getUid());
                docc.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull final Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document != null) {

                                String iu = document.getString("iu");
                                if(iu!=null) {
                                    StorageReference

                                            photoRef = mFir.getReferenceFromUrl(iu);

                                    photoRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {



                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception exception) {

                                            Toast.makeText(getActivity(), "notworking"+task.getException(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
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




                                                documentReference.update("iu",downloadUri.toString().trim()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if(task.isSuccessful())
                                                        {
                                                            Picasso.with(getActivity()).load(imagemfinal).fit().into(profImage);
                                                            PD.dismiss();




                                                            File file=new File(imagemfinal.getPath());
                                                            if(file.exists())
                                                            {
                                                                if(file.delete())
                                                                {
                                                                    Toast.makeText(getActivity(), "File deleted", Toast.LENGTH_SHORT).show();

                                                                }
                                                                else {
                                                                    Toast.makeText(getActivity(), "File not deleted", Toast.LENGTH_SHORT).show();
                                                                }
                                                            }
                                                           /* if (Build.VERSION.SDK_INT < 11)
                                                                path = RealPathUtils.getRealPathFromURI_BelowAPI11(getActivity(), imagemfinal);

                                                                // SDK >= 11 && SDK < 19
                                                            else if (Build.VERSION.SDK_INT < 19)
                                                                path = RealPathUtils.getRealPathFromURI_API11to18(getActivity(), imagemfinal);

                                                                // SDK > 19 (Android 4.4)
                                                            else
                                                                path = RealPathUtils.getRealPathFromURI_API19(getActivity(), imagemfinal);
                                                            // Get the file instance
                                                            File file = new File(path);

                                                            if(file.exists())
                                                            {
                                                                if(file.delete())
                                                                {
                                                                    Toast.makeText(getActivity(), "File deleted", Toast.LENGTH_SHORT).show();

                                                                }
                                                                else {
                                                                    Toast.makeText(getActivity(), "File not deleted", Toast.LENGTH_SHORT).show();
                                                                }
                                                            }*/







                                                            Toast.makeText(getActivity(), "Update successful", Toast.LENGTH_SHORT).show();

                                                        }

                                                        //  searchitem search=new searchitem(mEditTextFileName.getText().toString().trim());
                                                        //mDatabaseRef.child(uploadId).setValue(search);

                                                    }
                                                });

                                            } else {
                                                Toast.makeText(getActivity(), "Update failed: " + task.getException().getMessage(),
                                                        Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });


                                }
                                else {




                                }


                            } else if(document==null){


                            }
                        } else {
                            PD.dismiss();

                        }
                    }
                });














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
        ContentResolver cr = getActivity().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(uri));
    }


    private String storeImage(Bitmap image) {
        File pictureFile = getOutputMediaFile();
        if (pictureFile == null) {
            Toast.makeText(getActivity(), "not created", Toast.LENGTH_SHORT).show();
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
                + getContext().getPackageName().toString().trim()
                + "/Files");


        if (!mediaStorageDir.exists()){
            if (!mediaStorageDir.mkdirs()){
                Toast.makeText(getActivity(), "directory not created", Toast.LENGTH_SHORT).show();
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
        int result = ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            requestPermission();
            return false;
        }
    }
    private void requestPermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            Toast.makeText(getActivity(), "Write External Storage permission allows us to do store images. Please allow this permission in App Settings.", Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
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
