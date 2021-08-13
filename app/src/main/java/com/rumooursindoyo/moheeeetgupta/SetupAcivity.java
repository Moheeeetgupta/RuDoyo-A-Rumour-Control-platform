package com.rumooursindoyo.moheeeetgupta;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

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


public class SetupAcivity extends AppCompatActivity {

    private CircleImageView setupImage;
    private Uri mainImageURI = null;
    ViewGroup progressView;
    protected boolean isProgressShowing = false;
    private String user_id;

    private boolean isChanged = false;

    private EditText setupName;
    private Button setupBtn;
  //  private ProgressBar setupProgress;

    /**
     * public class StorageReference extends Object.
     * implements Comparable<StorageReference> Represents a reference to a Google Cloud Storage object.
     * Developers can upload and download objects, get/set object metadata, and delete an object at a specified path.
     */
    private StorageReference storageReference;
    private FirebaseAuth firebaseAuth;
    /**
     * In Cloud Firestore, you can use queries to retrieve individual, specific documents or
     * to retrieve all the documents in a collection that match your query parameters.
     * Your queries can include multiple, chained filters and combine filtering and sorting.
     */
    private FirebaseFirestore firebaseFirestore;

    /**
     * In effect, a bitmap is an array of binary data representing the values of pixels
     * in an image or display
     */
    private Bitmap compressedImageFile;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_acivity);

        Toolbar setupToolbar = findViewById(R.id.setupToolbar); // doubt
        setSupportActionBar(setupToolbar);
       getSupportActionBar ();

        firebaseAuth = FirebaseAuth.getInstance();
        /**
         * getUid() :- Returns the unique identifier of the provider type that, this instance corresponds to.
         * abstract "String.getUid()" Returns a user identifier as specified by the authentication provider.
         */
        user_id = Objects.requireNonNull (firebaseAuth.getCurrentUser ()).getUid(); // getting user id of current authenticated user.

        firebaseFirestore = FirebaseFirestore.getInstance(); // getting current instance / state of FirebaseFirestore

        /**
         * FirebaseStorage is a service that supports uploading and downloading large objects to Google Cloud Storage
         */
        storageReference = FirebaseStorage.getInstance().getReference();


        setupImage = findViewById(R.id.setup_image); // image for setting up
        setupName = findViewById(R.id.Setup_name); // name for setting up
        setupBtn = findViewById(R.id.Setup_button); // button for setting up
    //    setupProgress = findViewById(R.id.setup_progress); // progressbar for setting up

      //  setupProgress.setVisibility(View.VISIBLE); // making progressbar visible
        setupBtn.setEnabled(false); // disabling setUp button

        /**
         * here is the code snippet to get and show  profile picture and User name to the setup activity when user has registered and have set their profile picture and name
         *
         * FirebaseFirestore have collection of folders and these are collectively called as collection, here Users is one of folder belongs to collection
         * document is child of collection.
         */
        firebaseFirestore.collection("Users").document(user_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if(task.isSuccessful()){

                    if(task.getResult().exists()){

                        String name = task.getResult().getString("name"); // name will be retrived of corresponding user_id
                        String image = task.getResult().getString("image"); // image url in string format will be retrived from corresponding user_id

                        mainImageURI = Uri.parse(image); // url string will be parsed into Uri class parse function to parse string into url.

                        setupName.setText(name); // retrived name from firestore will be set up to the nameing part of the setup activity.

                        /**
                         * RequestOptions :- An abstract class representing browser-based request parameters.
                         * This class is used to supply options when creating a new credential.
                         * This class is used to supply an authentication request with the data it needs to generate an assertion.
                         */
                        RequestOptions placeholderRequest = new RequestOptions();

                        /**
                         *  Placeholder :- these are Drawables that are shown while a request is in progress.
                         *  When a request completes successfully, the placeholder is replaced with the requested resource.
                         *  If the requested resource is loaded from memory, the placeholder may never be shown.
                         */
                        placeholderRequest.placeholder(R.drawable.default_image);

                        /**
                         * Glide Api will set up default image first with setDefaultRequestOptions to the setup activity and then will load image from " mainImageURI " into profile picture image view.
                         */
                        Glide.with(SetupAcivity.this).setDefaultRequestOptions(placeholderRequest).load(image).into(setupImage); // I have changed image to mainImageURI


                    }

                } else {

                    String error = Objects.requireNonNull (task.getException ()).getMessage();
                    Toast.makeText(SetupAcivity.this, "(FIRESTORE Retrieve Error) : " + error, Toast.LENGTH_LONG).show();

                }

                // progress bar will be invisible after finishing
              //  setupProgress.setVisibility(View.INVISIBLE);
               // hideProgressingView();

                // now setup button will be enabled now
                setupBtn.setEnabled(true);

            }
        });



        // this code snippet is used for  setting up profile picture for the first time when user have created their account.
        setupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String user_name = setupName.getText().toString(); // getting setupName entered in the edittext
                if(mainImageURI==null) { // if mainImageURI have set to null , means have not assigned any url.
                    Toast.makeText (SetupAcivity.this, "Profile picture and username are compulsory", Toast.LENGTH_SHORT).show ();
                }else{
                    // if user_name has  entered and picture has choosen , then enter in the below if block.
                    if (!TextUtils.isEmpty(user_name) && mainImageURI != null) {

                        // progress bar will be visible.
                        showProgressingView();
                       // setupProgress.setVisibility (View.VISIBLE);

                        //
                        if (isChanged) {

                            user_id = firebaseAuth.getCurrentUser ().getUid ();

                            // getting local path of picked image for setting up profile .
                            File newImageFile = new File (mainImageURI.getPath ());
                            try {

                                compressedImageFile = new Compressor (SetupAcivity.this)
                                        .setMaxHeight (125)
                                        .setMaxWidth (125)
                                        .setQuality (50)
                                        .compressToBitmap (newImageFile);

                            } catch (IOException e) {
                                e.printStackTrace ();
                            }

                            /**
                             * ByteArrayOutputStream class creates an Output Stream for writing data into byte array.
                             * The size of buffer grows automatically as data is written to it.
                             * There is no affect of closing the byteArrayOutputStream on the working of it's methods.
                             * They can be called even after closing the class
                             */
                            ByteArrayOutputStream baos = new ByteArrayOutputStream ();

                            // compress the imagefile
                            compressedImageFile.compress (Bitmap.CompressFormat.JPEG, 100, baos);
                            byte[] thumbData = baos.toByteArray (); // conversion of bas to byte array.

                            /**
                             * UploadTask :- public class UploadTask extends StorageTask<UploadTask.TaskSnapshot> An controllable task that uploads and
                             * fires events for success, progress and failure.
                             * It also allows pause and resume to control the upload operation.
                             */
                            UploadTask image_path = storageReference.child ("profile_images").child (user_id + ".jpg").putBytes (thumbData); // uploading image_path to profile_image folder at firebase storage as reference.

                            image_path.addOnCompleteListener (new OnCompleteListener<UploadTask.TaskSnapshot> () {
                                @Override
                                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                                    // if task is successfull then store user_name to Firestore.
                                    if (task.isSuccessful ()) {
                                        storeFirestore (task, user_name);

                                    } else {

                                        String error = task.getException ().getMessage ();
                                        Toast.makeText (SetupAcivity.this, "(IMAGE Error) : " + error, Toast.LENGTH_LONG).show ();

                                      //  setupProgress.setVisibility (View.INVISIBLE);
                                        hideProgressingView();


                                    }
                                }
                            });

                        } else {
                            Toast.makeText (SetupAcivity.this, "Please select a profile picture and user name!", Toast.LENGTH_SHORT).show ();
                            storeFirestore (null, user_name);


                        }
                    }
                }

            }

        });

        setupImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){

                    // if permission is not granted then, request for permission  to read external storage
                    if(ContextCompat.checkSelfPermission(SetupAcivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){


                        ActivityCompat.requestPermissions(SetupAcivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);

                    } else {

                        BringImagePicker();

                    }

                } else {

                    BringImagePicker();

                }

            }

        });


    }

    private void storeFirestore(@NonNull Task<UploadTask.TaskSnapshot> task, String user_name) {

        Uri download_uri;

        if(task != null) {

            download_uri = task.getResult().getDownloadUrl();

        } else {

            download_uri = mainImageURI;

        }

        Map<String, String> userMap = new HashMap<>();
        userMap.put("name", user_name);
        assert download_uri != null;
        userMap.put("image", download_uri.toString());

        // this code snippet is for storing user_name and image reference from storage to the FirebaseFirestore.
        firebaseFirestore.collection("Users").document(user_id).set(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if(task.isSuccessful()){

                    // if profile has set up then mainactivity will be started.
                    Toast.makeText(SetupAcivity.this, "The user Settings are updated.", Toast.LENGTH_LONG).show();
                    Intent mainIntent = new Intent(SetupAcivity.this, MainActivity.class);
                    startActivity(mainIntent);
                    finish();

                } else {

                    String error = Objects.requireNonNull (task.getException ()).getMessage();
                    Toast.makeText(SetupAcivity.this, "(FIRESTORE Error) : " + error, Toast.LENGTH_LONG).show();

                }

             //   setupProgress.setVisibility(View.INVISIBLE);
                hideProgressingView();


            }
        });


    }

    private void BringImagePicker() {

        // code snippet for cropping image picked forsetting it as profile picture
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setAspectRatio(1, 1)
                .start(SetupAcivity.this);

    }

    /**
     * callback function of BringImagePicker() included intent, this callback function is taking imagePicker intent data , means selected image
     * to setupActivity when image has picked up and then setting image to setupImage imageView for profile picture.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {

                mainImageURI = result.getUri();
                setupImage.setImageURI(mainImageURI);

                // now profile image has set up locally.
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
            View v = this.findViewById(android.R.id.content).getRootView();
            ViewGroup viewGroup = (ViewGroup) v;
            viewGroup.addView(progressView);
        }
    }

    public void hideProgressingView() {
        View v = this.findViewById(android.R.id.content).getRootView();
        ViewGroup viewGroup = (ViewGroup) v;
        viewGroup.removeView(progressView);
        isProgressShowing = false;
    }
}