package com.rumooursindoyo.moheeeetgupta;


import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Comments_Activity extends AppCompatActivity {

    private Toolbar commentToolbar;

    private EditText comment_field;
    private ImageView comment_post_btn;

    /**
     * RecyclerView is the ViewGroup that contains the views corresponding to your data.
     * It's a view itself, so you add RecyclerView into your layout the way you would add any other UI element.
     * After the view holder is created, the RecyclerView binds it to its data. You define the view holder by extending RecyclerView.
     *
     * RecyclerView is a widget that is more flexible and advanced version of GridView and ListView.
     * It is a container for displaying large datasets which can be scrolled efficiently by maintaining limited number of view
     *
     *  RecyclerView is a UI component which allows us to create a scrolling list.
     *  It is basically a new ViewGroup used to render any adapter-based view in horizontal/vertical /grid or
     *  staggered grid manner using the Viewholder pattern.
     */

    private RecyclerView comment_list;

    /**
     * Adapter :- An Adapter object acts as a bridge between an AdapterView and the underlying data for that view.
     * The Adapter provides access to the data items.
     * The Adapter is also responsible for making a View for each item in the data set.
     */
    private Comments_Recycler_Adapter commentsRecyclerAdapter;
    private List<Comments> commentsList;

    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;

    private String blog_post_id;
    private String current_user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments_);

        commentToolbar = findViewById(R.id.comment_toolbar);
        setSupportActionBar(commentToolbar);
        getSupportActionBar().setTitle("Comments"); // setting title to Actionbar

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        current_user_id = firebaseAuth.getCurrentUser().getUid();
        blog_post_id =getIntent ().getStringExtra("blog_post_id"); // getting the id of particular blog post on which user have to give reviews

        comment_field = findViewById(R.id.comment_field);
        comment_post_btn = findViewById(R.id.comment_post_btn);
        comment_list = findViewById(R.id.comment_list);

        //RecyclerView Firebase List
        commentsList = new ArrayList<>();
        commentsRecyclerAdapter = new Comments_Recycler_Adapter (commentsList);
        comment_list.setHasFixedSize(true);
        comment_list.setLayoutManager(new LinearLayoutManager (this));
        comment_list.setAdapter(commentsRecyclerAdapter);


        // code snippet for retriving comments to show in comment section of that particular blog post tapped by the user.
        firebaseFirestore.collection("Posts/" + blog_post_id + "/Comments")
                .addSnapshotListener(Comments_Activity.this, new EventListener<QuerySnapshot>() {
                    /**
                     * QuerySnapshot :- A QuerySnapshot contains zero or more DocumentSnapshot objects representing the results of a
                     * query. The documents can be accessed as an array via the docs property or enumerated using the forEach method.
                     * The number of documents can be determined via the empty and size properties.
                     */

                    /**
                     * DocumentSnapshot :- A DocumentSnapshot contains data read from a document in your Firestore database.
                     * The data can be extracted with .For a DocumentSnapshot that points to a non-existing document,
                     * any data access will return 'undefined'. You can use the exists property to explicitly verify a document's existence
                     */
                    @Override
                    public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {

                        if (!documentSnapshots.isEmpty()) {

                            /**
                             * DocumentChange :- A DocumentChange represents a change to the documents matching a query.
                             * It contains the document affected and a the type of change that occurred (added, modified, or removed).
                             */
                            for (DocumentChange doc : documentSnapshots.getDocumentChanges()) {

                                if (doc.getType() == DocumentChange.Type.ADDED) {

                                    String commentId = doc.getDocument().getId();
                                    Comments comments = doc.getDocument().toObject(Comments.class);
                                    commentsList.add(comments);
                                    commentsRecyclerAdapter.notifyDataSetChanged();


                                }
                            }

                        }

                    }
                });

        // code sniipet for posting reviews
        comment_post_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String comment_message = comment_field.getText().toString();


                if (comment_message.isEmpty()) {
                    comment_field.setError( "Please enter comment" );
                    return;
                }

                Map<String, Object> commentsMap = new HashMap<>();
                commentsMap.put("message", comment_message);
                commentsMap.put("user_id", current_user_id);

                /**
                 * FieldValue :- The FIELDVALUE function returns all matching fields(s) from the linked data type specified in the value argument.
                 * The FIELDVALUE function belongs to the Lookup & Reference family of functions.
                 */

                /**
                 * serverTimestamp() :-
                 When you call FieldValue. serverTimestamp() , you'll get back a FieldValue type object that stands in for
                 the current moment in time, as reckoned by Google.
                 You can't get time values out of it, and you can't do time math with it
                 */

                commentsMap.put("timestamp", FieldValue.serverTimestamp());

                /**
                 * DocumentReference  :- It refers to a document location in a Cloud Firestore database and can be used to write,
                 * read, or listen to the location. There may or may not exist a document at the referenced location.
                 * A DocumentReference can also be used to create a CollectionReference to a subcollection.
                 */
                firebaseFirestore.collection("Posts/" + blog_post_id + "/Comments").add(commentsMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {

                        if(!task.isSuccessful()){

                            Toast.makeText(Comments_Activity.this, "Error Posting Comment : " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                        } else {

                            comment_field.setText("");

                        }

                    }
                });

            }
        });



    }
}

