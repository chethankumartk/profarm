package com.example.chethan.industrain;

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
import android.text.TextUtils;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chethan.industrain.homeadapters.Upload1;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class uploadmain extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;


    private Button selimage;
    ImageView image;
    Spinner spinner;
    EditText cropname;
    EditText quantity;
    EditText price;
    TextView name;
    TextView phone;
    TextView noteaboveinfo;
    TextView state;
    TextView district;
    TextView locality;
    TextView address;
    TextView noteclickadd;
    TextView Email;
    Button uploadd;
    String category="";
    Date dateadded;
    Date dateupdated;
    double latitude,longitude;
    private Uri mImageUri;
    private StorageReference mStorageRef;
    DocumentReference documentReference;
    private StorageTask mUploadTask;
    FirebaseAuth auth;
    FirebaseUser user;
    FirebaseFirestore db;
    String vall;
    String refid;
    String username1,state1,district1,locality1,address1;
    SharedPreferences sharedPref;
    String check;
    private static final int PERMISSION_REQUEST_CODE = 1;
    Uri imagemfinal;

    long views=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uploadmain);


        checkPermission();

        selimage=findViewById(R.id.selectimage);
        image=findViewById(R.id.image);
        spinner=findViewById(R.id.spinnercategory);
        cropname=findViewById(R.id.cropname);
        quantity=findViewById(R.id.quantity);
        price=findViewById(R.id.price);
        name=findViewById(R.id.name);
        phone=findViewById(R.id.phone);
        Email=findViewById(R.id.email);
        noteaboveinfo=findViewById(R.id.noteaboveinfo);
        state=findViewById(R.id.state);
        district=findViewById(R.id.district);
        locality=findViewById(R.id.locality);
        address=findViewById(R.id.address);
        noteclickadd=findViewById(R.id.noteclickadd);
        uploadd=findViewById(R.id.uploadd);
      //  dateadded= ServerValue.TIMESTAMP;
        //dateupdated=ServerValue.TIMESTAMP;
        setupSpinner();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        if(user!=null)
        {
            vall=user.getUid();

        }



        db=FirebaseFirestore.getInstance();
        documentReference=db.collection("users").document();
        mStorageRef = FirebaseStorage.getInstance().getReference("users").child(vall);

        sharedPref=getSharedPreferences("myPref",MODE_PRIVATE);
        username1=sharedPref.getString("username","none");
        latitude=Double.parseDouble(sharedPref.getString("latitude","0"));
        check=sharedPref.getString("latitude","null");
        longitude=Double.parseDouble(sharedPref.getString("longitude","0"));
        state1=sharedPref.getString("state","none");
        district1=sharedPref.getString("district","none");
        locality1=sharedPref.getString("locality","none");
        address1=sharedPref.getString("address","none");

        selimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFileChooser();

            }
        });

        uploadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadFile();
            }
        });

        name.setText(username1.toString().toLowerCase().trim());
        try {
            phone.setText(auth.getCurrentUser().getPhoneNumber().toString().toLowerCase().trim());

        }catch (Exception e)
        {


        }
        Email.setText(auth.getCurrentUser().getEmail().toString().trim());
        state.setText(state1.toString().trim());
        district.setText(district1.toString().trim());
        address.setText(address1.toString().trim());
        locality.setText(locality1.toString().trim());
        Date c = Calendar.getInstance().getTime();
        Toast.makeText(this, c.toString(), Toast.LENGTH_SHORT).show();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        dateupdated=c;
        dateadded=c;

    }
    void openFileChooser()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data !=null && data.getData()!=null)
        {
            mImageUri = data.getData();
            if(mImageUri!=null)
            {
                try {
                    Bitmap   bitmap = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), mImageUri);
                    storeImage(bitmap);
                    imagemfinal = Uri.fromFile(getOutputMediaFile().getAbsoluteFile());
                    Picasso.with(this).load(imagemfinal).into(image);

                } catch (IOException e) {
                    e.printStackTrace();
                }
                catch (Exception e)
                {
                    Toast.makeText(getApplicationContext(), ""+e, Toast.LENGTH_SHORT).show();
                }

            }


        }
    }
    private  String getFileExtension (Uri uri)
    {
        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(uri));
    }
    private void uploadFile() {

        final String cropname1=cropname.getText().toString().toLowerCase().trim();
        final String phone1=phone.getText().toString().toLowerCase().trim();
        final String email1=Email.getText().toString().toLowerCase().trim();
        final String quantity1=quantity.getText().toString().trim();
        final String price1=price.getText().toString().trim();






        if ((mImageUri != null) && (latitude>0) && (longitude>0) && (username1.length()>0) && (cropname1.length()>0)
                && (category.length()>0) && (phone1.length()>0) && (email1.length()>0) && (state1.length()>0)
                && (district1.length()>0) && (locality1.length()>0) && (address1.length()>0) && (quantity1.length()>0)
                &&(price1.length()>0) && imagemfinal!=null) {

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

                        //r.getId();

                        Upload1 upload1=new Upload1(refid,vall,latitude,longitude,
                                                     dateadded,dateupdated,downloadUri.toString(),username1,cropname1,
                                                      category,phone1,email1,state1,district1,locality1,
                                                      Integer.parseInt(quantity1.toString().trim()),
                                                       Integer.parseInt(price1.toString().trim()),address1,views);


                        documentReference.set(upload1).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful())
                                {
                                    File file=new File(imagemfinal.getPath());
                                    if(file.exists())
                                    {
                                        if(file.delete())
                                        {
                                            Toast.makeText(getApplicationContext(), "File deleted", Toast.LENGTH_SHORT).show();

                                        }
                                        else {
                                            Toast.makeText(getApplicationContext(), "File not deleted", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    Toast.makeText(uploadmain.this, "upload successful", Toast.LENGTH_SHORT).show();

                                }

                              //  searchitem search=new searchitem(mEditTextFileName.getText().toString().trim());
                                //mDatabaseRef.child(uploadId).setValue(search);

                            }
                        });

                    } else {
                        Toast.makeText(uploadmain.this, "upload failed: " + task.getException().getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                }
            });



            }
           else {
            if(phone1.length()<10)
            {
               //ph.setError("Invalid Phone Number");
                Toast.makeText(this, "Invalid Phone number", Toast.LENGTH_SHORT).show();

            }
            else if (mImageUri==null)
            {
                Toast.makeText(this, "Make sure u mImage filled all fields", Toast.LENGTH_SHORT).show();

            }
            else if (!(latitude>0) || !(longitude>0))
            {
                Toast.makeText(this, "Latitude"+check, Toast.LENGTH_SHORT).show();
            }
            else if (!(cropname1.length()>0))
            {
                Toast.makeText(this, "cropname", Toast.LENGTH_SHORT).show();

            }
            else if (!(username1.length()>0))
            {
                Toast.makeText(this, "username", Toast.LENGTH_SHORT).show();

            }
            else if (!(category.length()>0))
            {
                Toast.makeText(this, "category", Toast.LENGTH_SHORT).show();

            }
            else if(!(email1.length()>0))
            {
                Toast.makeText(this, "email", Toast.LENGTH_SHORT).show();

            }
            else if(!(phone1.length()>0))
            {
                Toast.makeText(this, "phone", Toast.LENGTH_SHORT).show();

            }
            else if(!(quantity1.length()>0) || !(price1.length()>0))
            {
                Toast.makeText(this, "quantity and price", Toast.LENGTH_SHORT).show();

            }
            else if(!(state1.length()>0)|| !(address1.length()>0))
            {
                Toast.makeText(this, "state", Toast.LENGTH_SHORT).show();

            }
        }

    }
    private void setupSpinner() {
        ArrayAdapter categoryspinneradapter = ArrayAdapter.createFromResource(this,
                R.array.ArrayCategory, android.R.layout.simple_spinner_item);

        categoryspinneradapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        spinner.setAdapter(categoryspinneradapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                if (!TextUtils.isEmpty(selection)) {
                    if (selection.equals(getString(R.string.Grains))) {
                        category = getString(R.string.Grains);
                    } else if (selection.equals(getString(R.string.Vegetables))) {
                        category = getString(R.string.Vegetables);
                    } else if (selection.equals(getString(R.string.Greens))) {
                        category = getString(R.string.Greens);
                    } else if (selection.equals(getString(R.string.Fruits))) {
                        category = getString(R.string.Fruits);
                    } else if (selection.equals(getString(R.string.DryFruit))) {
                        category = getString(R.string.DryFruit);
                    }
                }
            }

            // Because AdapterView is an abstract class, onNothingSelected must be defined
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                category = getString(R.string.Others);
            }
        });
    }

    @Override
    protected void onResume() {

        if(auth.getCurrentUser().getPhoneNumber()==null)
        {
            Toast.makeText(this, "Add Phone Number To Upload Crops", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(uploadmain.this,usermai.class));
            finish();
        }
        super.onResume();
    }




    private String storeImage(Bitmap image) {
        File pictureFile = getOutputMediaFile();
        if (pictureFile == null) {
            Toast.makeText(getApplicationContext(), "not created", Toast.LENGTH_SHORT).show();
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
                + getApplicationContext().getPackageName().toString().trim()
                + "/Files");


        if (!mediaStorageDir.exists()){
            if (!mediaStorageDir.mkdirs()){
                Toast.makeText(getApplicationContext(), "directory not created", Toast.LENGTH_SHORT).show();
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
        int result = ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            requestPermission();
            return false;
        }
    }
    private void requestPermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            Toast.makeText(getApplicationContext(), "Write External Storage permission allows us to do store images. Please allow this permission in App Settings.", Toast.LENGTH_LONG).show();
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
