package com.rumooursindoyo.moheeeetgupta;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;

public class AccountFragment extends Fragment {
    private static final int RESULT_OK =-1 ;
    private CircleImageView setupImage;
    private Uri mainImageURI = null;

    private String user_id;

    private boolean isChanged = false;

    private EditText setupName;
    private Button setupBtn;
   // private ProgressBar setupProgress;
    ViewGroup progressView;
    protected boolean isProgressShowing = false;


    private StorageReference storageReference;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;

    private Bitmap compressedImageFile;

    public AccountFragment() {
        // Required empty public constructor
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate (R.layout.activity_setup_acivity, container, false);

            super.onCreate(savedInstanceState);



            firebaseAuth = FirebaseAuth.getInstance();
            user_id = Objects.requireNonNull (firebaseAuth.getCurrentUser ()).getUid();

            firebaseFirestore = FirebaseFirestore.getInstance();
            storageReference = FirebaseStorage.getInstance().getReference();



            setupImage =view.findViewById(R.id.setup_image);
            setupName = view.findViewById(R.id.Setup_name);
            setupBtn = view.findViewById(R.id.Setup_button);
            //setupProgress =view.findViewById(R.id.setup_progress);

            //setupProgress.setVisibility(View.VISIBLE);
            hideProgressingView(); // new
            setupBtn.setEnabled(false);
        setupBtn.setText ("Save new name"); // new

            firebaseFirestore.collection("Users").document(user_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot> () {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                    if(task.isSuccessful()){

                        if(task.getResult().exists()){

                            String name = task.getResult().getString("name");
                            String image = task.getResult().getString("image");

                            mainImageURI = Uri.parse(image);

                            setupName.setText(name);

                            RequestOptions placeholderRequest = new RequestOptions();
                            placeholderRequest.placeholder(R.drawable.default_image);

                            Glide.with(AccountFragment.this).setDefaultRequestOptions(placeholderRequest).load(image).into(setupImage);


                        }

                    } else {

                        String error = Objects.requireNonNull (task.getException ()).getMessage();
                        Toast.makeText(getActivity (), "(FIRESTORE Retrieve Error) : " + error, Toast.LENGTH_LONG).show();

                    }

                    //setupProgress.setVisibility(View.INVISIBLE);
                    hideProgressingView();
                    setupBtn.setEnabled(true);

                }
            });


            setupBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    final String user_name = setupName.getText().toString();
                    if(mainImageURI==null) {
                        Toast.makeText (getActivity (), "Profile picture and username are compulsory", Toast.LENGTH_SHORT).show ();
                    }else{
                        if (!TextUtils.isEmpty(user_name) && mainImageURI != null) {

                            // setupProgress.setVisibility (View.VISIBLE);
                            showProgressingView();

                            if (isChanged) {

                                user_id = firebaseAuth.getCurrentUser ().getUid ();

                                File newImageFile = new File (mainImageURI.getPath ());
                                try {

                                    compressedImageFile = new Compressor (getActivity ())
                                            .setMaxHeight (125)
                                            .setMaxWidth (125)
                                            .setQuality (50)
                                            .compressToBitmap (newImageFile);

                                } catch (IOException e) {
                                    e.printStackTrace ();
                                }

                                ByteArrayOutputStream baos = new ByteArrayOutputStream ();
                                compressedImageFile.compress (Bitmap.CompressFormat.JPEG, 100, baos);
                                byte[] thumbData = baos.toByteArray ();

                                UploadTask image_path = storageReference.child ("profile_images").child (user_id + ".jpg").putBytes (thumbData);

                                image_path.addOnCompleteListener (new OnCompleteListener<UploadTask.TaskSnapshot> () {
                                    @Override
                                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                                        if (task.isSuccessful ()) {
                                            storeFirestore (task, user_name);

                                        } else {

                                            String error = task.getException ().getMessage ();
                                            Toast.makeText (getActivity (), "(IMAGE Error) : " + error, Toast.LENGTH_LONG).show ();

                                          //  setupProgress.setVisibility (View.INVISIBLE);
                                            hideProgressingView();

                                        }
                                    }
                                });

                            } else {
                                Toast.makeText (getActivity (), "Please select a profile picture and user name!", Toast.LENGTH_SHORT).show ();
                                storeFirestore (null, user_name);


                            }
                        }
                    }

                }

            });
/*
            setupImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){

                        if(ContextCompat.checkSelfPermission(getActivity (), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){


                            ActivityCompat.requestPermissions(getActivity (), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);

                        } else {

                            BringImagePicker();

                        }

                    } else {

                        BringImagePicker();

                    }

                }

            });

 */

            return view;

    }

    private void storeFirestore(@NonNull Task<UploadTask.TaskSnapshot> task, String user_name) {

        Uri download_uri;

        if(task != null) {

            download_uri = task.getResult().getDownloadUrl();

        } else {

            download_uri = mainImageURI;

        }

        Map<String, String> userMap = new HashMap<> ();
        userMap.put("name", user_name);
        assert download_uri != null;
        userMap.put("image", download_uri.toString());

        firebaseFirestore.collection("Users").document(user_id).set(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if(task.isSuccessful()){

                    Toast.makeText(getActivity (), "The user Settings are updated.", Toast.LENGTH_LONG).show();
                    Intent mainIntent = new Intent(getActivity (), MainActivity.class);
                    startActivity(mainIntent);


                } else {

                    String error = Objects.requireNonNull (task.getException ()).getMessage();
                    Toast.makeText(getActivity (), "(FIRESTORE Error) : " + error, Toast.LENGTH_LONG).show();

                }

               // setupProgress.setVisibility(View.INVISIBLE);
                hideProgressingView();

            }
        });


    }

    private void BringImagePicker() {

        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setAspectRatio(1, 1)
                .start(getActivity ());

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {

                mainImageURI = result.getUri();
                setupImage.setImageURI(mainImageURI);

                isChanged = true;

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {

                Exception error = result.getError();

            }
        }

    }


    public void showProgressingView() {

        if (!isProgressShowing) {
            isProgressShowing = true;
            progressView = (ViewGroup) getLayoutInflater().inflate(R.layout.progressbar_layout, null);
            View v = getActivity ().findViewById(android.R.id.content).getRootView();
            ViewGroup viewGroup = (ViewGroup) v;
            viewGroup.addView(progressView);
        }
    }

    public void hideProgressingView() {
        View v = getActivity ().findViewById(android.R.id.content).getRootView();
        ViewGroup viewGroup = (ViewGroup) v;
        viewGroup.removeView(progressView);
        isProgressShowing = false;
    }
}
