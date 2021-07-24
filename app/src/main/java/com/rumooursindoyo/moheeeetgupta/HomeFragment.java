package com.rumooursindoyo.moheeeetgupta;


import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    private RecyclerView blog_list_view;
    private List<BlogPost> blog_list;
    private List<User> user_list;

    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    private BlogRecyclerAdapter blogRecyclerAdapter;

    private DocumentSnapshot lastVisible;
    private Boolean isFirstPageFirstLoad = true;
    private Object QuerySnapshot;

    public HomeFragment() {
        // Required empty public constructor
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        blog_list = new ArrayList<>();
        user_list =new ArrayList<User> ();

        blog_list_view = view.findViewById(R.id.blog_list_view);

        firebaseAuth = FirebaseAuth.getInstance();

        blogRecyclerAdapter = new BlogRecyclerAdapter(blog_list,user_list);
        blog_list_view.setLayoutManager(new LinearLayoutManager (container.getContext()));
        blog_list_view.setAdapter(blogRecyclerAdapter);
        blog_list_view.setHasFixedSize(true);

        if(firebaseAuth.getCurrentUser() != null) {

            firebaseFirestore = FirebaseFirestore.getInstance ();

            blog_list_view.addOnScrollListener (new RecyclerView.OnScrollListener () {
                @Override
                public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled (recyclerView, dx, dy);

                    Boolean reachedBottom = !recyclerView.canScrollVertically (1);

                    if (reachedBottom) {

                        loadMorePost ();

                    }

                }
            });

            Query firstQuery = firebaseFirestore.collection ("Posts").orderBy ("timestamp", Query.Direction.DESCENDING).limit (4);
            firstQuery.addSnapshotListener (Objects.requireNonNull (getActivity ()), new EventListener<QuerySnapshot> () {
                @Override
                public void onEvent(@Nullable final QuerySnapshot documentSnapshots, @Nullable FirebaseFirestoreException e) {

                    if (documentSnapshots != null) {


                            if (!documentSnapshots.isEmpty ()) {

                            if (isFirstPageFirstLoad) {

                                lastVisible = documentSnapshots.getDocuments ().get (documentSnapshots.size () - 1);
                                blog_list.clear ();
                                user_list.clear ();

                            }

                            for (DocumentChange doc : documentSnapshots.getDocumentChanges ()) {

                                if (doc.getType () == DocumentChange.Type.ADDED) {

                                    String blogPostId = doc.getDocument ().getId ();
                                    final BlogPost blogPost = doc.getDocument ().toObject (BlogPost.class).withId (blogPostId);
                                    String blogUserId = doc.getDocument ().getString ("user_id");
                                    firebaseFirestore.collection ("Users").document (blogUserId).get ().addOnCompleteListener (new OnCompleteListener<DocumentSnapshot> () {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                            if(task.getResult ().exists ())
                                            {

                                            if (task.isSuccessful ()) {
                                                User user = task.getResult ().toObject (User.class);

                                                if (isFirstPageFirstLoad) {
                                                    user_list.add (user);

                                                    blog_list.add (blogPost);

                                                } else {
                                                    user_list.add (0, user);

                                                    blog_list.add (0, blogPost);

                                                }
                                                blogRecyclerAdapter.notifyDataSetChanged ();

                                            }
                                        }}
                                    });


                                }
                            }

                            isFirstPageFirstLoad = false;

                        }

                    }
                }
            });
        }


        // Inflate the layout for this fragment
        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void loadMorePost(){

        if(firebaseAuth.getCurrentUser() != null) {

            Query nextQuery = firebaseFirestore.collection("Posts")
                    .orderBy("timestamp", Query.Direction.DESCENDING)
                    .startAfter(lastVisible)
                    .limit(4);

            nextQuery.addSnapshotListener(Objects.requireNonNull (getActivity ()), new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot documentSnapshots,@Nullable FirebaseFirestoreException e) {
                    if(documentSnapshots!=null){
                    if (!documentSnapshots.isEmpty()) {

                        lastVisible = documentSnapshots.getDocuments().get(documentSnapshots.size() - 1);
                        for (DocumentChange doc : documentSnapshots.getDocumentChanges()) {

                            if (doc.getType() == DocumentChange.Type.ADDED) {

                                String blogPostId = doc.getDocument().getId();
                                final BlogPost blogPost = doc.getDocument().toObject(BlogPost.class).withId(blogPostId);
                                String blogUserId =doc.getDocument ().getString ("user_id");
                                firebaseFirestore.collection ("Users").document (blogUserId).get ().addOnCompleteListener (new OnCompleteListener<DocumentSnapshot> () {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                       if(task.getResult ().exists ()){

                                        if(task.isSuccessful ()){
                                            User user =task.getResult ().toObject (User.class);


                                                user_list.add(user);

                                                blog_list.add(blogPost);


                                            blogRecyclerAdapter.notifyDataSetChanged();

                                        }
                                       }else {

                                       }
                                    }
                                });

                            }

                        }
                    }

                }}
            });

        }

    }}

