package com.example.chethan.industrain.ViewMadiroList;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chethan.industrain.R;
import com.example.chethan.industrain.homeadapters.Upload1;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class ViewContactOfFarmer extends AppCompatActivity {

    private TextView tvphone,tvemail;
    private Button addcontact;
    private ProgressDialog PD;


    String refid;
    Upload1 upload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewcontact);
        getSupportActionBar().setTitle("Farmer Contact Information");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        this.refid=getIntent().getStringExtra("refid");
        PD = new ProgressDialog(this);
        PD.setMessage("Loading...");
        PD.setCancelable(true);
        PD.setCanceledOnTouchOutside(false);
        PD.show();


        tvphone=findViewById(R.id.cphone);
        tvemail=findViewById(R.id.cemail);
        addcontact=findViewById(R.id.addcontact);
        FirebaseFirestore.getInstance().collection("users").whereEqualTo("refid",refid)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
               if(task.isSuccessful())
               {
                   for (QueryDocumentSnapshot document : task.getResult()) {


                        upload = document.toObject(Upload1.class);
                       tvemail.setText(upload.getEmail().toString().trim());
                       tvphone.setText(upload.getPhone().toString().trim());

                       PD.dismiss();


                   }

               }
            }
        });
        addcontact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent contactIntent = new Intent(ContactsContract.Intents.Insert.ACTION);
                contactIntent.setType(ContactsContract.RawContacts.CONTENT_TYPE);

                contactIntent
                        .putExtra(ContactsContract.Intents.Insert.NAME, upload.getFarmername())
                        .putExtra(ContactsContract.Intents.Insert.PHONE, upload.getPhone());

                startActivityForResult(contactIntent, 1);

            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        if (requestCode == 1)
        {
            if (resultCode == Activity.RESULT_OK) {
                Toast.makeText(this, "Added Contact", Toast.LENGTH_SHORT).show();
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(this, "Cancelled Added Contact", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
