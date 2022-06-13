package com.example.chethan.industrain;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class emirecycleadapter extends RecyclerView.Adapter<emirecycleadapter.MyViewHolder> {

    private List<emiclass> emilist;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView principalAmount, Interest, balance,sno;

        public MyViewHolder(View view) {
            super(view);
            principalAmount=  view.findViewById(R.id.pram);
            Interest =  view.findViewById(R.id.Interest);
            balance=  view.findViewById(R.id.balance);
            sno=view.findViewById(R.id.sno);
        }
    }


    public emirecycleadapter(List<emiclass> emilist) {
        this.emilist = emilist;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.emirecycler, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        emiclass emic = emilist.get(position);
        holder.principalAmount.setText(emic.getPa());
        holder.Interest.setText(emic.getInterest());
        holder.balance.setText(emic.getBal());
        holder.sno.setText(emic.getSno());
    }

    @Override
    public int getItemCount() {
        return emilist.size();
    }
}