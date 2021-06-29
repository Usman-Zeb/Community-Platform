package com.example.community_stack;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentSuggestion#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentSuggestion extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    String suggestion;
    String suggestion_details;
    String Topic;
    String suggestionID;
    long upvotes;
    String createdBy;
    FirebaseFirestore db;

    TextView suggestion_view;
    TextView suggestion_details_view;
    TextView createdBy_view;
    Button upvote_btn;
    TextView total_upvotes;


    private String mParam1;
    private String mParam2;

    public FragmentSuggestion() {

    }


    public static FragmentSuggestion newInstance(String param1, String param2) {
        FragmentSuggestion fragment = new FragmentSuggestion();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_suggestion, container, false);

        Topic = getArguments().getString("Topic");
        suggestionID = getArguments().getString("suggestionID");
        getActivity().setTitle(Topic);
        db = FirebaseFirestore.getInstance();
        Log.d("THIS IS MY ID: ", suggestionID);
        Log.d("THIS IS MY TOPIC", Topic);


        suggestion_view = view.findViewById(R.id.suggestion_box);
        suggestion_details_view = view.findViewById(R.id.suggestion_box_details);
        createdBy_view = view.findViewById(R.id.suggestion_creator);
        upvote_btn = view.findViewById(R.id.upvotes);
        total_upvotes = view.findViewById(R.id.total_upvotes);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        db.collection(Topic).document(suggestionID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                DocumentSnapshot document = task.getResult();
                if (document.exists()) {

                    suggestion = document.get("dialogName").toString();
                    suggestion_details = document.get("suggestion_details").toString();
                    upvotes = (long) document.get("upvotes");
                    createdBy = document.get("createdBy").toString();
                    //Log.d("The suggestion",suggestion);

                    suggestion_view.append(suggestion);
                    //Log.d("suggestion details: ", suggestion_details);
                    suggestion_details_view.append(suggestion_details);
                    createdBy_view.append(createdBy);
                    total_upvotes.setText(Long.toString(upvotes));

                }

            }

        });

        upvote_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                upvotes++;
                Map<String, Object> up = new HashMap<>();
                up.put("upvotes", upvotes);

                db.collection(Topic).document(suggestionID).update(up);
            }
        });


        DocumentReference upvoteRef = db.collection(Topic).document(suggestionID);

        upvoteRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                upvotes=(long) value.get("upvotes");
                total_upvotes.setText(Long.toString(upvotes));
            }
        });

    }


}