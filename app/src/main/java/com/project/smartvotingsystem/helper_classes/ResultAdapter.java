package com.project.smartvotingsystem.helper_classes;


import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.project.smartvotingsystem.R;

import java.util.List;


public class ResultAdapter extends RecyclerView.Adapter<ResultAdapter.MyViewHolder> {

    private Context context;
    private List<Participant> participantList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView dot;
        public TextView participantName;
        public TextView email;
        public TextView partyname;
        public ImageView profilePic;
        public TextView votes;


        public MyViewHolder(View view) {
            super(view);
            email = view.findViewById(R.id.email);
            dot = view.findViewById(R.id.dot);
            participantName = view.findViewById(R.id.participant_Name);
            partyname = view.findViewById(R.id.party);
            profilePic = view.findViewById(R.id.image);
            votes = view.findViewById(R.id.votesCard);
        }
    }


    public ResultAdapter(Context context, List<Participant> List) {
        this.context = context;
        this.participantList = List;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_participant_result, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Participant participant = participantList.get(position);

        holder.email.setText(participant.getEmail());

        // Displaying dot from HTML character code
        holder.dot.setText(Html.fromHtml("&#8226;"));

        // Formatting and displaying timestamp
        holder.participantName.setText(participant.getName());

        holder.partyname.setText(participant.getParty());

        //holder.part.setText(participant.getName());
        if(participant.getPic()!=null)
        {
            Glide.with(context)
                    .load(participant.getPic())
                    .into(holder.profilePic);
        }

        holder.votes.setText(String.valueOf(participant.getVotes()));


    }

    @Override
    public int getItemCount() {
        return participantList.size();
    }


}
