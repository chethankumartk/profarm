package com.example.chethan.industrain;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class emicalculatemadakke extends AppCompatActivity {

    RecyclerView recyclerView;
    EditText loanamount,baddi,tenure;
    Button b;
    double interest,interestmonthly,totalemipermonth,loan,tenurre;
    double interestdisp,prdisp,baldisp;
    emirecycleadapter mAdapter;
    private List<emiclass> emilist = new ArrayList<>();
    int sno;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emicalculatemadakke);
        getSupportActionBar().setTitle("EmiCalculator");
        recyclerView=findViewById(R.id.emirecycl);
        loanamount=findViewById(R.id.loan);
        baddi=findViewById(R.id.baddi);
        tenure=findViewById(R.id.tenure);
        b=findViewById(R.id.emicalculate);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);




        recyclerView = findViewById(R.id.emirecycl);

        mAdapter = new emirecycleadapter(emilist);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(mAdapter);


            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        interest = Double.parseDouble(baddi.getText().toString().trim());
                        loan = Double.parseDouble(loanamount.getText().toString().trim());
                        tenurre = Double.parseDouble(tenure.getText().toString().trim());

                        interestmonthly = (interest / 12) / 100;

                        totalemipermonth = loan * interestmonthly * ((Math.pow((1 + interestmonthly), tenurre)) / ((Math.pow((1 + interestmonthly), tenurre)) - 1));
                        //totalemipermonth=Math.floor(totalemipermonth);

                        emilist.clear();
                        for (int i = 1; i <= tenurre; i++) {

//                    interestdisp=loan*interestmonthly;
                            interestdisp = loan * interestmonthly;
                            sno = i;
                            prdisp = totalemipermonth - interestdisp;
                            loan = loan - prdisp;
                            baldisp = loan;
                            String p1, i1, b1, s1;
                            s1 = Integer.toString(sno);
                            p1 = Double.toString(Math.round(prdisp));
                            i1 = Double.toString(Math.round(interestdisp));
                            b1 = Double.toString(Math.round(baldisp));
                            emiclass emm = new emiclass(p1, i1, b1, s1);
                            emilist.add(emm);
                            mAdapter.notifyDataSetChanged();
                            mAdapter = new emirecycleadapter(emilist);
                            recyclerView.setAdapter(mAdapter);
                            prdisp = Math.round(prdisp);
                            interestdisp = Math.round(interestdisp);
                            baldisp = Math.round(baldisp);


                        }
                    }
                    catch (Exception e)
                    {
                        Toast.makeText(emicalculatemadakke.this, "Fill All Fields", Toast.LENGTH_SHORT).show();
                    }


                }
            });













    }




    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }







    @Override    protected void onResume() {

        super.onResume();
    }

}
