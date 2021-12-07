package com.project.smartvotingsystem.Admin_Panel.InProgress_Admin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.smartvotingsystem.R;
import com.project.smartvotingsystem.helper_classes.Election;
import com.project.smartvotingsystem.helper_classes.PendingAdapter;
import com.project.smartvotingsystem.helper_classes.RecyclerTouchListener;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class InProgress extends Fragment {

    private TextView noInProgress;
    private RecyclerView recyclerView;
    private List<Election> pendingList;
    private PendingAdapter pendingAdapter;
    private DatabaseReference databaseReference;

    private ValueEventListener eventListener;

    public InProgress() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_in_progress, container, false);
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle("In Progress");

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStackImmediate();
            }
        });

        noInProgress=view.findViewById(R.id.empty_inProgress_view);
        recyclerView=view.findViewById(R.id.recyclerview);


        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        pendingList=new ArrayList<>();
        pendingAdapter = new PendingAdapter(getContext(), pendingList);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
//        recyclerView.addItemDecoration(new MyDividerItemDecoration(this, LinearLayoutManager.VERTICAL, 16));
        recyclerView.setAdapter(pendingAdapter);
        toggleEmptyParticipants();



        databaseReference= FirebaseDatabase.getInstance().getReference("Election");
        eventListener = databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                pendingList.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Election c = postSnapshot.getValue(Election.class);
                    String start = c.getStartDate()+" "+c.getStartTime();//+"Jan 10, 2021 11:27 PM";
                    String end=c.getEndDate()+" "+c.getEndTime();
                    DateFormat format = new SimpleDateFormat("MMM d, yyyy hh:mm a", Locale.ENGLISH);
                    try {
                        Date startDateTime = format.parse(start);
                        Date endDateTime = format.parse(end);
                        Date currentDateTime=new Date();
                        long diffwithStart = (int)( (startDateTime.getTime() - currentDateTime.getTime())
                                / (1000 * 60 ) );
                        long diffwithEnd = (int)( (endDateTime.getTime() - currentDateTime.getTime())
                                / (1000 * 60 ) );

                        if(diffwithStart<=0&&diffwithEnd>=0)
                        pendingList.add(c);


                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                pendingAdapter.notifyDataSetChanged();
                toggleEmptyParticipants();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(),databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getContext(),
                recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, final int position) {
                Election c=pendingList.get(position);
//                openAddParticipantDialog(c,"edit");

            }

            @Override
            public void onLongClick(View view, int position) {
                Election c=pendingList.get(position);
                //openAddParticipantDialog(c,"edit");

            }
        }));

//        title=view.findViewById(R.id.time);
//
//
//
//        new CountDownTimer(300000, 1000) {
//
//            public void onTick(long milliseconds) {
//
//                long dy = TimeUnit.MILLISECONDS.toDays(milliseconds);
//                final long yr = dy / 365;
//                dy %= 365;
//                final long mn = dy / 30;
//                dy %= 30;
//                final long wk = dy / 7;
//                dy %= 7;
//                final long hr = TimeUnit.MILLISECONDS.toHours(milliseconds)
//                        - TimeUnit.DAYS.toHours(TimeUnit.MILLISECONDS.toDays(milliseconds));
//                final long min = TimeUnit.MILLISECONDS.toMinutes(milliseconds)
//                        - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(milliseconds));
//                final long sec = TimeUnit.MILLISECONDS.toSeconds(milliseconds)
//                        - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(milliseconds));
//                final long ms = TimeUnit.MILLISECONDS.toMillis(milliseconds)
//                        - TimeUnit.SECONDS.toMillis(TimeUnit.MILLISECONDS.toSeconds(milliseconds));
//
//
//                title.setText("Years :"+yr+" Months :"+mn+"Weeks"+wk+" Days :"+dy+"Hours"+hr+" Minutes "+min+" Seconds "+sec+" Milliseconds"+ mn);
//            }
//
//            public void onFinish() {
//                title.setText("done!");
//            }
//        }.start();


        return view;

    }

    private void toggleEmptyParticipants() {
        // you can check notesList.size() > 0

        if (pendingAdapter.getItemCount() > 0) {
            noInProgress.setVisibility(View.GONE);
        } else {
            noInProgress.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        databaseReference.removeEventListener(eventListener);
    }

}
