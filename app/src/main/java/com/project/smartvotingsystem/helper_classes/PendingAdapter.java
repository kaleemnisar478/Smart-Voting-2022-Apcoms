package com.project.smartvotingsystem.helper_classes;


import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.project.smartvotingsystem.R;

import java.util.List;


public class PendingAdapter extends RecyclerView.Adapter<PendingAdapter.MyViewHolder> {

    private Context context;
    private List<Election> electionList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView dot;
        public TextView title;
        public TextView startDate;
        public TextView endDate;
        public TextView startTime;
        public TextView endTime;

        public MyViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.title);
            dot = view.findViewById(R.id.dot);
            startDate = view.findViewById(R.id.startDate);
            endDate = view.findViewById(R.id.endDate);
            startTime = view.findViewById(R.id.startTime);
            endTime = view.findViewById(R.id.endTime);

        }
    }


    public PendingAdapter(Context context, List<Election> List) {
        this.context = context;
        this.electionList = List;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_pendings, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Election election = electionList.get(position);

        holder.title.setText(election.getTitle());

        // Displaying dot from HTML character code
        holder.dot.setText(Html.fromHtml("&#8226;"));

        // Formatting and displaying timestamp
        holder.startDate.setText("Start Date : "+election.getStartDate());

        holder.endDate.setText("End Date : "+election.getEndDate());

        holder.startTime.setText("Start Time: "+election.getStartTime());

        holder.endTime.setText("End Time: "+election.getEndTime());

    }

    @Override
    public int getItemCount() {
        return electionList.size();
    }


}
