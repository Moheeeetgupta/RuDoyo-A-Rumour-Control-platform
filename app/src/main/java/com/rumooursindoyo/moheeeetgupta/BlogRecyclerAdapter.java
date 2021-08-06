package com.rumooursindoyo.moheeeetgupta;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class BlogRecyclerAdapter extends RecyclerView.Adapter<BlogRecyclerAdapter.ViewHolder> {

    public List<BlogPost> blog_list;
    public Context context;
    public List<User> user_list;

    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    public Object DocumentSnapshot;
    private Object QuerySnapshot;
    private boolean flagLike=false,flagDislike=false,flagNeutral=false;
    private NetworkInfo networkInfo;





    public BlogRecyclerAdapter(List<BlogPost> blog_list, List<User> user_list){

        this.blog_list = blog_list;
        this.user_list=user_list;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.blog_list_item, parent, false);
        context = parent.getContext();
        // Get a reference to the ConnectivityManager to check state of network connectivity
        ConnectivityManager connMgr = (ConnectivityManager)
                context.getSystemService (Context.CONNECTIVITY_SERVICE);

        // Get details on the currently active default data network
         networkInfo = connMgr.getActiveNetworkInfo();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();



        return new ViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        holder.setIsRecyclable(false);

        final String blogPostId = blog_list.get(position).BlogPostId;
        final String currentUserId = Objects.requireNonNull (firebaseAuth.getCurrentUser ()).getUid();


        String desc_data = blog_list.get(position).getDesc();
        holder.setDescText(desc_data);

        String image_url = blog_list.get(position).getImage_url();
        String thumbUri = blog_list.get(position).getImage_thumb();
        holder.setBlogImage(image_url, thumbUri);

        String user_id = blog_list.get(position).getUser_id();
        if(user_id.equals (currentUserId) ){
            holder.blogDeletebtn.setEnabled (true);
            holder.blogDeletebtn.setVisibility (View.VISIBLE);
        }


                  final    String userName = user_list.get(position).getName ();
                  final   String userImage = user_list.get(position).getImage ();
                    holder.setUserData(userName, userImage);

    try {
            long millisecond = blog_list.get(position).getTimestamp().getTime();
            String dateString = DateFormat.format("dd/MM/yyyy", new Date(millisecond)).toString();
            holder.setTime(dateString);
        } catch (Exception e) {

        Toast.makeText (context, "Welcome to DoYo!", Toast.LENGTH_SHORT).show ();

        }



        //Get Likes Count
        firebaseFirestore.collection("Posts/" + blogPostId + "/Likes").addSnapshotListener( new EventListener<QuerySnapshot>() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onEvent(@Nullable QuerySnapshot documentSnapshots,@Nullable FirebaseFirestoreException e) {
                if(documentSnapshots!=null) {

                    if (!documentSnapshots.isEmpty ()) {

                      int count = documentSnapshots.size ();

                        holder.updateLikesCount (count);




                    } else {

                        holder.updateLikesCount (0);

                    }
                }
            }
        });
        //Get Likes
        firebaseFirestore.collection("Posts/" + blogPostId + "/Likes").document(currentUserId).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot,@Nullable FirebaseFirestoreException e) {
                if(documentSnapshot!=null) {
                    if (documentSnapshot.exists ()) {
                        ImageView imageView=(ImageView) holder.blogLikeBtn.getChildAt (0);
                        imageView.setImageDrawable (context.getDrawable (R.drawable.upvote_blue24dp));
                        TextView textView=(TextView)holder.blogLikeBtn.getChildAt (1);
                        int colorId= ContextCompat.getColor (context,R.color.colorPrimary);
                        textView.setTextColor (colorId);
                        textView.setTextSize(14);

                    } else {
                        ImageView imageView=(ImageView) holder.blogLikeBtn.getChildAt (0);
                        imageView.setImageDrawable (context.getDrawable (R.drawable.upvote_black24dp));
                        TextView textView=(TextView)holder.blogLikeBtn.getChildAt (1);
                        textView.setTextColor (Color.BLACK);
                        textView.setTextSize(12);


                    }
                }
            }

        });
        //Likes Feature

        holder.blogLikeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // If there is a network connection, fetch data
                if (networkInfo != null && networkInfo.isConnected()) {
                firebaseFirestore.collection("Posts/" + blogPostId + "/Likes").document(currentUserId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                        if(!task.getResult().exists()){
                            if(flagNeutral==true){
                                firebaseFirestore.collection("Posts/" + blogPostId + "/Confused").document(currentUserId).delete();
                                flagNeutral=false;
                            }else if(flagDislike==true){
                                firebaseFirestore.collection("Posts/" + blogPostId + "/Unfavoured").document(currentUserId).delete();
                                flagDislike=false;
                            }
                            Map<String, Object> likesMap = new HashMap<>();
                            likesMap.put("timestamp", FieldValue.serverTimestamp());

                            firebaseFirestore.collection("Posts/" + blogPostId + "/Likes").document(currentUserId).set(likesMap);
                            flagLike=true;
                        } else {

                            firebaseFirestore.collection("Posts/" + blogPostId + "/Likes").document(currentUserId).delete();
                            flagLike=false;
                        }

                    }
                });
            }else{
                    Toast.makeText (context.getApplicationContext (), "PLease check your internet connection...",Toast.LENGTH_LONG).show ();
                }
            }
        });
        //Get Unfavoured Count


        firebaseFirestore.collection("Posts/" + blogPostId + "/Unfavoured").addSnapshotListener( new EventListener<QuerySnapshot>() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onEvent(@Nullable QuerySnapshot documentSnapshots,@Nullable FirebaseFirestoreException e) {
                if(documentSnapshots!=null) {

                    if (!documentSnapshots.isEmpty ()) {

                 int  countunfavoured = documentSnapshots.size ();

                        holder.updateunfavouredCount (countunfavoured);




                    } else {

                        holder.updateunfavouredCount (0);

                    }

                }

            }
        });
        //Get unfavours
        firebaseFirestore.collection("Posts/" + blogPostId + "/Unfavoured").document(currentUserId).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot,@Nullable FirebaseFirestoreException e) {
                if(documentSnapshot!=null) {
                    if (documentSnapshot.exists ()) {
                        ImageView imageView=(ImageView) holder.unfavouredbtn.getChildAt (0);
                        imageView.setImageDrawable (context.getDrawable (R.drawable.downvote_blue24dp));
                        TextView textView=(TextView)holder.unfavouredbtn.getChildAt (1);
                        int colorId= ContextCompat.getColor (context,R.color.colorPrimary);
                        textView.setTextColor (colorId);
                        textView.setTextSize(14);
                    } else {

                        ImageView imageView=(ImageView) holder.unfavouredbtn.getChildAt (0);
                        imageView.setImageDrawable (context.getDrawable (R.drawable.downvote_black24dp));
                        TextView textView=(TextView)holder.unfavouredbtn.getChildAt (1);
                        textView.setTextColor (Color.BLACK);
                        textView.setTextSize(12);

                    }
                }
            }

        });
        //Unfavoured Feature
        holder.unfavouredbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // If there is a network connection, fetch data
                if (networkInfo != null && networkInfo.isConnected ()) {
                    firebaseFirestore.collection ("Posts/" + blogPostId + "/Unfavoured").document (currentUserId).get ().addOnCompleteListener (new OnCompleteListener<DocumentSnapshot> () {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                            if (!task.getResult ().exists ()) {
                                if (flagNeutral == true) {
                                    firebaseFirestore.collection ("Posts/" + blogPostId + "/Confused").document (currentUserId).delete ();
                                    flagNeutral = false;
                                } else if (flagLike == true) {
                                    firebaseFirestore.collection ("Posts/" + blogPostId + "/Likes").document (currentUserId).delete ();
                                    flagLike = false;
                                }
                                Map<String, Object> likesMap = new HashMap<> ();
                                likesMap.put ("timestamp", FieldValue.serverTimestamp ());

                                firebaseFirestore.collection ("Posts/" + blogPostId + "/Unfavoured").document (currentUserId).set (likesMap);
                                flagDislike = true;
                            } else {

                                firebaseFirestore.collection ("Posts/" + blogPostId + "/Unfavoured").document (currentUserId).delete ();
                                flagDislike = false;
                            }

                        }
                    });
                } else {
                    Toast.makeText (context.getApplicationContext (), "PLease check your internet connection...", Toast.LENGTH_LONG).show ();
                }
            }
        });
        //Get Confused Count
        firebaseFirestore.collection("Posts/" + blogPostId + "/Confused").addSnapshotListener( new EventListener<QuerySnapshot>() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onEvent(@Nullable QuerySnapshot documentSnapshots,@Nullable FirebaseFirestoreException e) {
                if(documentSnapshots!=null) {

                    if (!documentSnapshots.isEmpty ()) {

                      int   countconfused = documentSnapshots.size ();

                        holder.updateconfusedCount (countconfused);



                    } else {

                        holder.updateconfusedCount (0);

                    }
                }
            }
        });
        //Get confused
        firebaseFirestore.collection("Posts/" + blogPostId + "/Confused").document(currentUserId).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot,@Nullable FirebaseFirestoreException e) {
                if(documentSnapshot!=null) {
                    if (documentSnapshot.exists ()) {
                        ImageView imageView=(ImageView) holder.confusedbtn.getChildAt (0);
                        imageView.setImageDrawable (context.getDrawable (R.drawable.neutral_blue24dp));
                        TextView textView=(TextView)holder.confusedbtn.getChildAt (1);
                        int colorId= ContextCompat.getColor (context,R.color.colorPrimary);
                        textView.setTextColor (colorId);
                        textView.setTextSize(14);


                    } else {
                        ImageView imageView=(ImageView) holder.confusedbtn.getChildAt (0);
                        imageView.setImageDrawable (context.getDrawable (R.drawable.neutral_black24dp));
                        TextView textView=(TextView)holder.confusedbtn.getChildAt (1);
                        textView.setTextColor (Color.BLACK);
                        textView.setTextSize(12);

                    }
                }
            }

        });
        //confused Feature
        holder.confusedbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (networkInfo != null && networkInfo.isConnected ()) {
                    firebaseFirestore.collection ("Posts/" + blogPostId + "/Confused").document (currentUserId).get ().addOnCompleteListener (new OnCompleteListener<DocumentSnapshot> () {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                            if (!task.getResult ().exists ()) {
                                if (flagLike == true) {
                                    firebaseFirestore.collection ("Posts/" + blogPostId + "/Likes").document (currentUserId).delete ();
                                    flagLike = false;
                                } else if (flagDislike == true) {
                                    firebaseFirestore.collection ("Posts/" + blogPostId + "/Unfavoured").document (currentUserId).delete ();
                                    flagDislike = false;
                                }
                                Map<String, Object> likesMap = new HashMap<> ();
                                likesMap.put ("timestamp", FieldValue.serverTimestamp ());

                                firebaseFirestore.collection ("Posts/" + blogPostId + "/Confused").document (currentUserId).set (likesMap);
                                flagNeutral = true;
                            } else {

                                firebaseFirestore.collection ("Posts/" + blogPostId + "/Confused").document (currentUserId).delete ();
                                flagNeutral = false;
                            }

                        }
                    });
                } else {
                    Toast.makeText (context.getApplicationContext (), "PLease check your internet connection...", Toast.LENGTH_LONG).show ();
                }
            }
        });


        holder.blogCommentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText (context, "You can not delete your comment from here!", Toast.LENGTH_SHORT).show ();
                Intent commentIntent = new Intent(context, Comments_Activity.class);
                commentIntent.putExtra("blog_post_id", blogPostId);
                context.startActivity(commentIntent);

            }
        });
        holder.blogDeletebtn.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                firebaseFirestore.collection ("Posts").document (blogPostId).delete ().addOnSuccessListener (new OnSuccessListener<Void> () {
                    @Override
                    public void onSuccess(Void aVoid) {
                        blog_list.remove(position);
                        user_list.remove (position);
                        Toast.makeText (context, "Post is being deleted!", Toast.LENGTH_SHORT).show ();
                        Toast.makeText (context, "Please refresh!", Toast.LENGTH_SHORT).show ();

                    }
                });
            }
        });


    }




    @Override
    public int getItemCount() {
        return blog_list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private View mView;

        private TextView descView;
        private ImageView blogImageView;
        private TextView blogDate;

        private TextView blogUserName;
        private CircleImageView blogUserImage;

        private LinearLayout blogLikeBtn;
        private TextView blogLikeCount;

        private LinearLayout blogCommentBtn;
        private TextView blogCommentsCount;
        private Button blogDeletebtn;
        private LinearLayout unfavouredbtn;
        private TextView blogunfavouredCount;
        private LinearLayout confusedbtn;
        private TextView confusedcount;



        public ViewHolder(View itemView) {
            super (itemView);
            mView = itemView;
            confusedbtn =mView.findViewById (R.id.Confused_btn);
            blogLikeBtn = mView.findViewById (R.id.blog_like_btn);
            unfavouredbtn=mView.findViewById (R.id.unfavoured_btn);
            blogCommentBtn = mView.findViewById (R.id.blog_comment_icon);
            blogDeletebtn = mView.findViewById (R.id.blog_delete);

        }





        public void setDescText(String descText) {

            descView = mView.findViewById (R.id.blog_desc);
            descView.setText (descText);

        }

        public void setBlogImage(String downloadUri, String thumbUri) {

            blogImageView = mView.findViewById (R.id.blog_image);

            RequestOptions requestOptions = new RequestOptions ();
            requestOptions.placeholder (R.drawable.image_placeholder);

            Glide.with (context).applyDefaultRequestOptions (requestOptions).load (downloadUri).thumbnail (
                    Glide.with (context).load (thumbUri)
            ).into (blogImageView);

        }

        public void setTime(String date) {

            blogDate = mView.findViewById (R.id.blog_date);
            blogDate.setText (date);

        }

        public void setUserData(String name, String image) {

            blogUserImage = mView.findViewById (R.id.blog_user_image);
            blogUserName = mView.findViewById (R.id.blog_user_name);

            blogUserName.setText (name);

            RequestOptions placeholderOption = new RequestOptions ();
            placeholderOption.placeholder (R.drawable.profile_placeholder);

            Glide.with (context).applyDefaultRequestOptions (placeholderOption).load (image).into (blogUserImage);

        }
        public  void updateLikesCount(int count) {

            blogLikeCount = mView.findViewById (R.id.blog_like_count);
            blogLikeCount.setText (count + " UpVotes");


        }


        public void updateunfavouredCount(int countunfavoured) {
            blogunfavouredCount = mView.findViewById (R.id.unfavoured_count);
            blogunfavouredCount.setText (countunfavoured + " DownVotes");

        }



        public void updateconfusedCount(int countconfused) {
            confusedcount=mView.findViewById (R.id.confused_count);
            confusedcount.setText (countconfused+" Neutral");

        }



    }



}

