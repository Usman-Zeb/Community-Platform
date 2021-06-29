package com.example.community_stack;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.community_stack.models.ChatDialog;
import com.example.community_stack.models.Message;
import com.example.community_stack.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.stfalcon.chatkit.commons.models.IDialog;
import com.stfalcon.chatkit.dialogs.DialogsList;
import com.stfalcon.chatkit.dialogs.DialogsListAdapter;

import java.util.ArrayList;
import java.util.Map;

public class SuggestionActivity extends AppCompatActivity {
    DialogsList dialogList;
    DialogsListAdapter dialogsListAdapter;
    ChatDialog suggestionDialog = new ChatDialog();
    private FirebaseFirestore db;
    String Topic;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggestions);
        Intent intent = getIntent();
        Topic = intent.getStringExtra("Topic");
        setTitle("\\"+Topic);
        firebaseAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();



        dialogList = (DialogsList) findViewById(R.id.queries_list);

        dialogsListAdapter = new DialogsListAdapter<ChatDialog>(null);

        dialogList.setAdapter(dialogsListAdapter);

        dialogsListAdapter.setOnDialogClickListener(new DialogsListAdapter.OnDialogClickListener() {
            @Override
            public void onDialogClick(IDialog dialog) {
                Intent intent = new Intent(SuggestionActivity.this, SuggestionRoom.class);
                intent.putExtra("Topic", Topic);
                intent.putExtra("suggestionID", dialog.getId());
                startActivity(intent);
            }

        });


        FloatingActionButton floatingActionButton = findViewById(R.id.create_query_btn);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showQueryDialog();
                Log.d("Button", "Is this working?");
            }
        });


        ArrayList<ChatDialog> init_suggestions = new ArrayList<>();
        db.collection(Topic)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            ArrayList<Map<String, Object>> data = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                data.add(document.getData());
                            }


                            for (Map<String, Object> singleData : data) {
                                ChatDialog chatDialog = new ChatDialog();
                                Log.d("Data Name: ", singleData.get("dialogName").toString() + "\n");
                                chatDialog.dialogPhoto = singleData.get("dialogPhoto").toString();
                                chatDialog.createdBy= singleData.get("createdBy").toString();
                                chatDialog.suggestion_details=singleData.get("suggestion_details").toString();
                                chatDialog.upvotes=(long) singleData.get("upvotes");
                                chatDialog.id = singleData.get("id").toString();
                                chatDialog.dialogName = singleData.get("dialogName").toString();
                                chatDialog.users = (ArrayList<User>) singleData.get("users");
                                chatDialog.lastMessage = (Message) singleData.get("lastMessage");
                                init_suggestions.add(chatDialog);

                            }


                        }
                        dialogsListAdapter.setItems(init_suggestions);

                    }

                });
    }

    private void showQueryDialog(){

        AlertDialog.Builder mBuilder = new AlertDialog.Builder(SuggestionActivity.this);
        View mView = getLayoutInflater().inflate(R.layout.suggestion_dialog, null);
        final EditText mSuggestionName = (EditText) mView.findViewById(R.id.suggestion_name_dialog);
        final EditText mSuggestionDetails = (EditText) mView.findViewById(R.id.suggestion_details);
        Button mCreate = (Button) mView.findViewById(R.id.gc_btn_dialog);


        mBuilder.setView(mView);
        AlertDialog dialog = mBuilder.create();
        dialog.show();

        mCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!mCreate.getText().toString().isEmpty())
                {
                    Toast.makeText(SuggestionActivity.this, "Suggestion Created", Toast.LENGTH_SHORT).show();
                    dialog.hide();
                    createQuery(mSuggestionName.getText().toString(), mSuggestionDetails.getText().toString());
                }
            }
        });
    }

    private void createQuery(String query_name, String query_details){

        suggestionDialog.dialogName=query_name;
        suggestionDialog.suggestion_details=query_details;



        if(firebaseAuth.getCurrentUser().getPhotoUrl() == null){

            suggestionDialog.dialogPhoto= "https://www.designbust.com/download/1060/png/microsoft_logo_transparent512.png";
        }
        else{
            suggestionDialog.dialogPhoto= firebaseAuth.getCurrentUser().getPhotoUrl().toString();}
        suggestionDialog.unreadCount=0;
        suggestionDialog.lastMessage=null;
        suggestionDialog.users.add(null);
        suggestionDialog.upvotes=0;
        suggestionDialog.createdBy=firebaseAuth.getCurrentUser().getDisplayName();

        DocumentReference reference = db.collection(Topic).document();
        suggestionDialog.id = reference.getId();
        reference.set(suggestionDialog.hashMap());

        dialogsListAdapter.addItem(suggestionDialog);


    }
}