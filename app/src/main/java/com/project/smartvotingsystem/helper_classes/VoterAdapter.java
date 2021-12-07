package com.project.smartvotingsystem.helper_classes;


import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import com.project.smartvotingsystem.R;


public class VoterAdapter extends RecyclerView.Adapter<VoterAdapter.MyViewHolder> {

    private Context context;
    private List<Voter> VoterList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView email;
        public TextView dot;
        public TextView voterName;

        public MyViewHolder(View view) {
            super(view);
            email = view.findViewById(R.id.email);
            dot = view.findViewById(R.id.dot);
            voterName = view.findViewById(R.id.voter_Name);
        }
    }


    public VoterAdapter(Context context, List<Voter> List) {
        this.context = context;
        this.VoterList = List;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_voter, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Voter voter = VoterList.get(position);

        holder.email.setText("Email: "+voter.getEmail());

        // Displaying dot from HTML character code
        holder.dot.setText(Html.fromHtml("&#8226;"));

        // Formatting and displaying timestamp
        holder.voterName.setText("Name: "+voter.getName());
    }

    @Override
    public int getItemCount() {
        return VoterList.size();
    }


}
