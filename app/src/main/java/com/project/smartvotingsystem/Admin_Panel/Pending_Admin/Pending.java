package com.project.smartvotingsystem.Admin_Panel.Pending_Admin;


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

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class Pending extends Fragment {

    private TextView noPendings;
    private RecyclerView recyclerView;
    private List<Election> pendingList;
    private PendingAdapter pendingAdapter;
    private DatabaseReference databaseReference;

    private ValueEventListener eventListener;

    boolean voters,participants;

    public Pending() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_pending, container, false);
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle("Pending");

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStackImmediate();
            }
        });



        noPendings=view.findViewById(R.id.empty_pendings_view);
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
                    voters=(postSnapshot.hasChild("Voters"));
                    participants=(postSnapshot.hasChild("Participants"));
                    if((!(postSnapshot.hasChild("Participants")))||(!(postSnapshot.hasChild("Voters"))))
                    pendingList.add(c);

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
                String id = c.getDetailId();
                Bundle bundle = new Bundle();
                bundle.putString("electionID",id);
                bundle.putBoolean("voters",voters);
                bundle.putBoolean("participants",participants);
                PendingCreateElection pendingCreateElection=new PendingCreateElection();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, pendingCreateElection).addToBackStack("pending_election_details").commit();
                pendingCreateElection.setArguments(bundle);


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
            noPendings.setVisibility(View.GONE);
        } else {
            noPendings.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        databaseReference.removeEventListener(eventListener);
    }

}
