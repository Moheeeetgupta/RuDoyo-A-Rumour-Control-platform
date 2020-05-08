package com.rumooursindoyo.moheeeetgupta;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class Comments_Recycler_Adapter extends RecyclerView.Adapter<Comments_Recycler_Adapter.ViewHolder> {

    public List<Comments> commentsList;
    public Context context;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    public List<BlogPost> blog_list;
    private String blog_post_id;
    public Comments_Recycler_Adapter(List<Comments> commentsList){

        this.commentsList = commentsList;


    }


    @Override
    public Comments_Recycler_Adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_list, parent, false);
        context = parent.getContext();
        firebaseFirestore = FirebaseFirestore.getInstance ();
        firebaseAuth = FirebaseAuth.getInstance();
        return new Comments_Recycler_Adapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final Comments_Recycler_Adapter.ViewHolder holder, final int position) {

        holder.setIsRecyclable (false);
        String currentuserid = firebaseAuth.getCurrentUser ().getUid ();


        String commentMessage = commentsList.get (position).getMessage ();
        holder.setComment_message (commentMessage);
        String user_id = commentsList.get (position).getUser_id ();

        //For retrieving the username and image during comment
        firebaseFirestore.collection ("Users").document (user_id).get ().addOnCompleteListener (new OnCompleteListener<DocumentSnapshot> () {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful ()) {
                    String userName = task.getResult ().getString ("name");
                    String userImage = task.getResult ().getString ("image");
                    holder.setUserData (userName, userImage);
                } else {

                }

            }
        });
    }


    @Override
    public int getItemCount() {


        if(commentsList != null) {

            return commentsList.size();

        } else {

            return 0;

        }

    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private View mView;

        private TextView comment_message;
        private TextView commentusername;
        private CircleImageView commentimage;

        public ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;

        }

        public void setComment_message(String message){

            comment_message = mView.findViewById(R.id.comment_message);
            comment_message.setText(message);

        }
        public void setUserData(String name,String image){
            commentimage =mView.findViewById (R.id.comment_image);
            commentusername=mView.findViewById (R.id.comment_username);
            commentusername.setText (name);
            RequestOptions placeholderOption =new RequestOptions ();
            placeholderOption.placeholder (R.drawable.profile_placeholder);
            Glide.with(context).applyDefaultRequestOptions (placeholderOption).load(image).into(commentimage);

        }

    }

}
