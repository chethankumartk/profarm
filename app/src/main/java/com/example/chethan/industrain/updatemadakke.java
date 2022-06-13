package com.example.chethan.industrain;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class updatemadakke extends AppCompatActivity {


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
    Button update,deletee;
    private Uri mImageUri;
    private StorageReference mStorageRef;
    DocumentReference documentReference;
    private StorageTask mUploadTask;
    FirebaseAuth auth;
    FirebaseUser user;
    FirebaseFirestore db;
    String vall;
    String category="";
    String refid1,eid1,imageurl1,farmername1,cropname1,username1,state1,district1,locality1,address1,phone1,email1;
    int quantity1,price1;
    Date dateadded1;
    Date dateupdated1;
    double latitude1,longitude1;
    SharedPreferences sharedPref;
    String check;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_updatemadakke);




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
        update=findViewById(R.id.update);
        deletee=findViewById(R.id.delete);
        //  dateadded= ServerValue.TIMESTAMP;
        //dateupdated=ServerValue.TIMESTAMP;
        setupSpinner();




        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        db=FirebaseFirestore.getInstance();


        if(user!=null) {
            vall = user.getUid();
            this.refid1 = getIntent().getStringExtra("refid");
            this.eid1=getIntent().getStringExtra("eid");
            this.latitude1=getIntent().getDoubleExtra("latitude",0);
            this.longitude1=getIntent().getDoubleExtra("longitude",0);
            this.dateadded1=new Date(getIntent().getStringExtra("dateadded"));
           // this.dateupdated1=new Date(getIntent().getStringExtra("dateupdated"));
            this.imageurl1=getIntent().getStringExtra("imageurl");
            this.farmername1=getIntent().getStringExtra("farmername");
            this.cropname1=getIntent().getStringExtra("cropname");
            this.category=getIntent().getStringExtra("category");
            this.phone1=getIntent().getStringExtra("phone");
            this.email1=getIntent().getStringExtra("email");
            this.state1=getIntent().getStringExtra("state");
            this.district1=getIntent().getStringExtra("district");
            this.locality1=getIntent().getStringExtra("locality");
            this.quantity1=getIntent().getIntExtra("quantity",0);
            this.price1=getIntent().getIntExtra("price",0);
            this.address1=getIntent().getStringExtra("address");
            Date c = Calendar.getInstance().getTime();
            dateupdated1=c;




            name.setText(farmername1.toString().toLowerCase().trim());
            phone.setText(phone1);
            Email.setText(email1);
            state.setText(state1.toString().trim());
            district.setText(district1.toString().trim());
            address.setText(address1.toString().trim());
            locality.setText(locality1.toString().trim());
            quantity.setText(String.valueOf(quantity1));
            price.setText(String.valueOf(price1));
            cropname.setText(cropname1);




           /* update=findViewById(R.id.buttonUpdateArtist);
            DElete=findViewById(R.id.buttonDeleteArtist);
            etname=findViewById(R.id.farmername);
            etcrop=findViewById(R.id.cropname);
            etaddress=findViewById(R.id.addresss);
            etphone=findViewById(R.id.phonee);
            etprice=findViewById(R.id.priceee);
            topp=findViewById(R.id.top1);
            topp.setText(NAME);*/



            update.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updatemadi(refid1, eid1,latitude1,longitude1,dateadded1,dateupdated1,imageurl1
                            ,farmername1,cropname1,category,phone1,email1,state1,district1,locality1
                            ,quantity1,price1,address1);
                }
            });
            deletee.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deletemadi(refid1, imageurl1);
                }
            });


        }
        DocumentReference nycRef = db.collection("cities").document();


    }

    private void updatemadi(String refid1,String eid1,double latitude1,double longitude1,Date dateadded1,
                            Date dateupdated1,String imageurl1,String farmername1,String cropname1,
                            String category,String phone1,String email1,String state1,String district1,String locality1,
                            int quantity1,int price1,String address1){

        cropname1=cropname.getText().toString().trim();
         quantity1=Integer.parseInt(quantity.getText().toString());
         price1=Integer.parseInt(price.getText().toString().trim());
        if ((!TextUtils.isEmpty(cropname1)) && quantity1>0 && price1>0)
        {
            updatemadbidi(refid1, eid1,latitude1,longitude1,dateadded1,dateupdated1,imageurl1
                    ,farmername1,cropname1,category,phone1,email1,state1,district1,locality1
                    ,quantity1,price1,address1);
           // startActivity(new Intent(updatemadakke.this, MainActivity.class));
            //finish();
        }
        else
        {
            Toast.makeText(this, "Enter all fields", Toast.LENGTH_SHORT).show();
        }

    }

    private Boolean updatemadbidi(String refid1,String eid1,double latitude1,double longitude1,Date dateadded1,
                            Date dateupdated1,String imageurl1,String farmername1,String cropname1,
                            String category,String phone1,String email1,String state1,String district1,String locality1,
                            int quantity1,int price1,String address1){


        //DatabaseReference dr= FirebaseDatabase.getInstance().getReference("users").child(id);
        db.collection("users").document(refid1).
                update("refid",refid1
                ,"eid",eid1
                ,"latitude",latitude1
                ,"longitude",longitude1
                ,"dateadded",dateadded1
                ,"dateupdated",dateupdated1
                ,"imageurl",imageurl1.toString().trim()
                ,"farmername",farmername1
                        ,"cropname",cropname1
                        ,"category",category
                        ,"phone",phone1
                        ,"email",email1
                        ,"state",state1
                        ,"district",district1
                        ,"locality",locality1
                        ,"quantity",quantity1
                        ,"price",price1
                        ,"address",address1
        );

       // dr.setValue(upload);
        Toast.makeText(this, "Data Updated", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(updatemadakke.this,MainActivity.class));
        finish();
        return true;
    }


    private Boolean deletemadi(final String id, String image) {
       // DatabaseReference dr=FirebaseDatabase.getInstance().getReference("users").child(id);
        //dr.removeValue();
        FirebaseStorage mFirebaseStorage;
        mFirebaseStorage=FirebaseStorage.getInstance();
        db.collection("users").document(id).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(updatemadakke.this, "deleted", Toast.LENGTH_SHORT).show();


                }
                else
                {
                    Toast.makeText(updatemadakke.this, "not deleted", Toast.LENGTH_SHORT).show();

                }

            }
        });




/*
        documentReference.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                {
                    Toast.makeText(MainActivity.this, "deleted", Toast.LENGTH_SHORT).show();
                }
            }
        });

 */

        StorageReference photoRef = mFirebaseStorage.getReferenceFromUrl(image);
        photoRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                // File deleted successfully
                //Log.d("wo", "onSuccess: deleted file");
                Toast.makeText(updatemadakke.this, "working", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Uh-oh, an error occurred!
                //Log.d(TAG, "onFailure: did not delete file");
                Toast.makeText(updatemadakke.this, "notworking", Toast.LENGTH_SHORT).show();
            }
        });



        Toast.makeText(getApplicationContext(), "data deleted", Toast.LENGTH_LONG).show();

        return true;
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(updatemadakke.this,MainActivity.class));
        finish();
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
                    else if (selection.equals(getString(R.string.Others))) {
                        category = getString(R.string.Others);
                    }
                }
            }

            // Because AdapterView is an abstract class, onNothingSelected must be defined
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                category=getIntent().getStringExtra("category");
                }
        });
    }


}
