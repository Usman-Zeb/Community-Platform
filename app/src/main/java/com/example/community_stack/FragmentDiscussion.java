package com.example.community_stack;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.community_stack.models.ChatDialog;
import com.example.community_stack.models.Message;
import com.example.community_stack.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;
import com.stfalcon.chatkit.commons.ImageLoader;
import com.stfalcon.chatkit.messages.MessageInput;
import com.stfalcon.chatkit.messages.MessagesList;
import com.stfalcon.chatkit.messages.MessagesListAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentDiscussion#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentDiscussion extends Fragment {

    private static final String TAG = "Chats List";
    FirebaseUser user;
    FirebaseAuth firebaseAuth;

    MessagesList messagesList;
    MessagesListAdapter messagesListAdapter;
    User currentUser = new User();
    Message message = new Message();
    ArrayList<Message> init_messages = new ArrayList<Message>();
    private FirebaseFirestore db;
    String Topic;
    String suggestionID;
    MessageInput inputView;
    Map<String, Object> group;



    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private String mParam1;
    private String mParam2;

    public FragmentDiscussion() {
        // Required empty public constructor
    }

    public static FragmentDiscussion newInstance(String param1, String param2) {
        FragmentDiscussion fragment = new FragmentDiscussion();
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
       View view = inflater.inflate(R.layout.fragment_discussion, container, false);


        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        currentUser.name= user.getDisplayName();
        currentUser.id=user.getUid();
        Topic = getArguments().getString("Topic");
        suggestionID = getArguments().getString("suggestionID");
        getActivity().setTitle(Topic);

        messagesList = (MessagesList) view.findViewById(R.id.messagesList);

        messagesListAdapter = new MessagesListAdapter<Message>(firebaseAuth.getUid().toString(), new ImageLoader() {
            @Override
            public void loadImage(ImageView imageView, @Nullable String url, @Nullable Object payload) {
                Picasso.get().load(url).into(imageView);
            }
        });
        inputView = view.findViewById(R.id.input);
        messagesList.setAdapter(messagesListAdapter);
        db = FirebaseFirestore.getInstance();


        inputView.setInputListener(new MessageInput.InputListener() {
            @Override
            public boolean onSubmit(CharSequence input) {
                Message newMessage =  new Message();
                newMessage.createdAt = new Date();
                newMessage.text = input.toString();
                newMessage.id = "newMessage";
                HashMap<String,Object> currentUser = new HashMap<String,Object>();
                currentUser.put("id",firebaseAuth.getUid());
                if(firebaseAuth.getCurrentUser().getPhotoUrl() == null){

                    currentUser.put("avatar", "https://www.designbust.com/download/1060/png/microsoft_logo_transparent512.png");
                }
                else{
                    currentUser.put("avatar", firebaseAuth.getCurrentUser().getPhotoUrl().toString());

                }
                currentUser.put("name",user.getDisplayName().toString());
                newMessage.setAuthor(currentUser);
                db.collection(Topic).document(suggestionID).collection("messages").add(newMessage.hashMap());
                String title = user.getDisplayName().toString() + " in " + suggestionID;

                return true;
            }
        });



        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        CollectionReference messageRef = db.collection(Topic   ).document(suggestionID).collection("messages");
        messageRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    System.err.println("Listen failed:" + error);
                    return;
                }
                init_messages = new ArrayList<Message>();
                if (value !=null)
                {

                    for (DocumentSnapshot doc : value) {
                        {
                            Map<String, Object> newData = doc.getData();
                            Message message = new Message();
                            Log.d("Data Name: ", newData.get("id").toString() + "\n");
                            message.id = newData.get("id").toString();
                            message.setAuthor((HashMap<String,Object>) newData.get("author"));
                            message.text = newData.get("text").toString();
                            message.createdAt =  doc.getTimestamp("createdAt").toDate();
                            init_messages.add(message);
                        }
                    }
                    Collections.sort(init_messages, new Comparator<Message>() {
                        @Override
                        public int compare(Message message, Message t1) {
                            return message.getDateTime().compareTo(t1.getDateTime());
                        }

                    });
                    messagesListAdapter.clear();
                    messagesListAdapter.addToEnd(init_messages, true);
                }

            }
        });
    }
}