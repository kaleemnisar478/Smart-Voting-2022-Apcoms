package com.project.smartvotingsystem.Voters_Panel.Coming_Voter;


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


/**
 * A simple {@link Fragment} subclass.
 */
public class Coming_Voter extends Fragment {

    private TextView noComing;
    private RecyclerView recyclerView;
    private List<Election> pendingList;
    private PendingAdapter pendingAdapter;
    private DatabaseReference databaseReference;

    private ValueEventListener eventListener;

    public Coming_Voter() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_coming, container, false);

        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle("Coming");

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStackImmediate();
            }
        });

        noComing=view.findViewById(R.id.empty_coming_view);
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

                        if(diffwithStart>=0)
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


        return view;

    }



    private void toggleEmptyParticipants() {
        // you can check notesList.size() > 0

        if (pendingAdapter.getItemCount() > 0) {
            noComing.setVisibility(View.GONE);
        } else {
            noComing.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        databaseReference.removeEventListener(eventListener);
    }

}
