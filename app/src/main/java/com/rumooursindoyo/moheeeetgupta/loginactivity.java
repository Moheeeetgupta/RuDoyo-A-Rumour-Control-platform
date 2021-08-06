package com.rumooursindoyo.moheeeetgupta;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class loginactivity extends AppCompatActivity {
    private EditText loginEmailText;
    private EditText loginPassText;
    private Button loginBtn;
    private TextView loginRegBtn;
    private FirebaseAuth mAuth;
    private TextView forgotbtn;
    private ConstraintLayout main_layout;
    ViewGroup progressView;
    protected boolean isProgressShowing = false;
    // private LinearLayout linearLayout;

    @Override

    /**
     * savedInstanceState :- this is a reference to a Bundle object that is passed into the onCreate method of
     * every Android Activity. Activities have the ability, under special circumstances,
     * to restore themselves to a previous state using the data stored in this bundle.
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_login );

        // getInstance method is used to get the state of authentication , whether signin or not
        mAuth = FirebaseAuth.getInstance();

        /**
         * findViewById connects your backend code with the User Interface elements, layouts, buttons, etc.
         * Every component in the user interface (xml layout file) has an ID associated with it,
         * which allows you to control its actions from backend.
         * View is a basic building block of UI (User Interface) in android.
         */

        // finding views of corresponding id and inflating and storing it into java EditText object
        loginEmailText = (EditText) findViewById( R.id.Log_Email ); // email edittext
        loginPassText = (EditText) findViewById( R.id.Login_pass ); // password edittext
        loginBtn = (Button) findViewById( R.id.Login_Btn ); // login button
        loginRegBtn = findViewById( R.id.Login_Reg_Btn ); // new user
        main_layout = (ConstraintLayout) findViewById( R.id.main_layout ); // mainLayout = findViewById(R.id.linearLayout);
        forgotbtn = findViewById( R.id.btn_forgot_password ); // forget button


        /**
         * setOnClickListener :- this is the method which helps us to link a listener with certain attributes.
         * While invoking this method a callback function will run.
         * One can also create a class for more than one listener, so this can lead you to code reusability.
         *
         * In Android, the OnClickListener() interface has an onClick(View v) method
         * that is called when the view (component) is clicked. The code for a component's functionality
         * is written inside this method, and the listener is set using the setOnClickListener() method.
         */

        // functinality for implementing forget button in loginactivity
        forgotbtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // function for recovering password
                showRecoverPaswword();
            }
        } );

        // listner is set to recognise if loginRegBtn is clicked by the user and
        // then perform corressponding work , starting registeractivity.
        loginRegBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent regIntent = new Intent( loginactivity.this, RegisterActivity.class );
                startActivity( regIntent );
            }
        } );


        // this button is clicked when user have filled the required fields for login purpose
        loginBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // login email is received in string format from loginEmailText edittext,
                // where user have filled their email.
                String loginEmail = loginEmailText.getText().toString();

                // login password is received in string format from loginPassText edittext,
                // where user have filled their email.
                String loginPass = loginPassText.getText().toString();

                // TextUtils is simply a set of utility functions to do operations on String objects.
                // TextUtils.isemplty() will always return a boolean value. In code, the former simply calls
                // the equivalent of the other, plus a null check. checks if string length is zero only,
                // this may result in NullPointerException if you try to use that string and it is null.
                // isEmpty() Returns true if, and only if, length() is 0

                // if email and password both filleds are filled by the user
                if (!TextUtils.isEmpty( loginEmail ) && !TextUtils.isEmpty( loginPass )) {
                    // show then progressbar, exactly after clicking this button
                    showProgressingView();
                    /**
                     * Firebase Auth :- Firebase Authentication provides backend services, easy-to-use SDKs, and ready-made UI
                     * libraries to authenticate users to your app. It supports authentication using passwords,
                     * phone numbers, popular federated identity providers like Google, Facebook and Twitter, and more
                     *
                     * signInWithEmailAndPassword() :- this method takes in an email address and password, validates them,
                     * and then signs a user .
                     *
                     * addOnCompleteListener :- listning the completion of signInWithEmailAndPassword method and then checking that whether sign in is successfull or not.
                     *
                     *Task :-  The goal of the Task API is to provide an easy, lightweight, and Android-aware framework for Firebase (and Play services)
                     * client APIs to perform work asynchronously. It was introduced in Play services version 9.0. 0 along with Firebase.1.
                     *
                     * AuthResult :- public interface AuthResult implements Parcelable. Result object obtained
                     * from operations that can affect the authentication state. Contains a method that
                     * returns the currently signed-in user after the operation has completed.
                     */
                    mAuth.signInWithEmailAndPassword( loginEmail, loginPass ).addOnCompleteListener( new OnCompleteListener<AuthResult>() {
                                                                                                         @Override
                                                                                                         public void onComplete(@NonNull Task<AuthResult> task) {
                                                                                                             if (task.isSuccessful()) {
                                                                                                                 sendToMain();
                                                                                                             } else {
                                                                                                                 String errorMessage = task.getException().getMessage();
                                                                                                                 /**
                                                                                                                  * A toast provides simple feedback about an operation in a small popup.
                                                                                                                  * It only fills the amount of space required for the message and the current activity remains
                                                                                                                  * visible and interactive. Toasts automatically disappear after a timeout.
                                                                                                                  * here it is showing error message ( exception message ) if signin is not successfull
                                                                                                                  */
                                                                                                                 Toast.makeText( loginactivity.this, "Error :" + errorMessage, Toast.LENGTH_SHORT ).show();
                                                                                                             }
                                                                                                             // hiding progress bar after completion of sign in process
                                                                                                             hideProgressingView();

                                                                                                         }
                                                                                                     }
                    );
                }

            }
        } );

    }

    // function to recover password, forget password implementation
    public void showRecoverPaswword() {
        /**
         * Dialog :- it is a small window that prompts the user to make a decision or enter additional information.
         * A dialog does not fill the screen and is normally used for modal events that require users to take an
         * action before they can proceed. Dialog Design.
         *
         * AlertDialog :- it can be used to display the dialog message with OK and Cancel buttons.
         * It can be used to interrupt and ask the user about his/her choice to continue or discontinue.
         * Android AlertDialog is the subclass of Dialog class.
         * AlertDialog is composed of three regions: title, content area and action buttons.
         *
         * Builder :- Builder is the static inner class inside the AlertDialog class.
         * So to create a Builder class object, you need to call AlertDialog.Builder.
         * It helps in building AlertDialoge.
         */
        AlertDialog.Builder builder = new AlertDialog.Builder( this ); // Context : this , means this should pop up in the loginactivity.
        builder.setTitle( "Recover Password" );  // Uded to set title of AlertDialoge
        LinearLayout linearLayout = new LinearLayout( this ); // LinearLayout is used as parent view to show AlertDialoge.
        final EditText emailEt = new EditText( this ); // EditText to enter email address for password recovery
        emailEt.setHint( "Enter registered email..." ); // setHint function is used to set Hint in the EditText so that user can unserstand what to enter in this field.
        emailEt.setInputType( InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS );
        emailEt.setMinEms( 10 );  // havve to add it
        linearLayout.addView( emailEt ); // addView function is adding emailEt EdditText view to the linearlayout as child view.
        linearLayout.setPadding( 7, 10, 10, 10 );
        builder.setView( linearLayout ); // builder is setting linearlayout view as parent view for showing AlertDialoge

        // builder is setting Recover button which is set through setPositiveButton to the AlertDialoge Screen.
        // Anonymus class,  new DialogInterface.OnClickListener() is used to allow the creator of a dialog to run below code in onClick method when an item which is here Recover button on the dialog is clicked.
        builder.setPositiveButton( "Recover", new DialogInterface.OnClickListener() {
            @Override
            // DialogInterface :- Interface that defines a dialog-type class that can be shown, dismissed, or canceled,
            // and may have buttons that can be clicked.
            public void onClick(DialogInterface dialog, int which) {
                String email = emailEt.getText().toString().trim(); // on clicking Recover Button , email from EditText is extracteed.
                beginRecovery( email ); // function to start recovery of forgotten password

            }
        } );

        // now builder is setting negative button that is of cancellation.
        builder.setNegativeButton( "Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss(); // turn off the AlertDialoge
            }
        } );

        builder.create().show(); // Creates an AlertDialog with the arguments supplied to this builder and then show it to the user's screen.


    }


    // function to start recovery of forgotten password
    private void beginRecovery(String email) {
        mAuth.sendPasswordResetEmail( email ) // sending password reset email link to the filled email.
                .addOnCompleteListener( new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText( loginactivity.this, "Email sent for password recovery...", Toast.LENGTH_SHORT ).show();
                        } else {
                            Toast.makeText( loginactivity.this, "Failed to recover...", Toast.LENGTH_SHORT ).show();
                        }
                    }
                } )
                // failure on sending password recovery link to the user
                .addOnFailureListener( new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText( loginactivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT ).show();
                    }
                } );

    }

    //whenever the app starts ,it  should first verify the real user
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser(); // getting current user
        // if user exists and is loged in then very first on starting loginactivity , MainActivity will be started  instead of starting loginactivity.
        if (currentUser != null) {
            sendToMain();

        }
    }

    private void sendToMain() {
        Intent mainIntent = new Intent( loginactivity.this, MainActivity.class );
        startActivity( mainIntent );
        finish();

    }

// functrion used for showing progressbar when login button is clicked
    public void showProgressingView() {

        // if isProgressShowing = false , means progressbar is not showing .
        if (!isProgressShowing) {
            isProgressShowing = true;

            // inflating progressbar_layout layout which has progressbar to show.
            progressView = (ViewGroup) getLayoutInflater().inflate( R.layout.progressbar_layout, null );

            // "android.R.id.content" gives you the root element of a view of current activity, without having to know its actual name/type/ID but
            //Actually just findViewById(android.R.id.content) is giving the root view.
            // If that is not true in some cases you can get root view from the findViewById(android.R.id.content).getRootView().
            View v = this.findViewById( android.R.id.content ).getRootView();
            ViewGroup viewGroup = (ViewGroup) v; // converting obtained root View object to viewGroup
            viewGroup.addView( progressView ); // now adding progressbar to the logingactivity ViewGroup which is root view obtained from findViewById(android.R.id.content).getRootView().
        }
    }

    // function used to hide progressbar when login process is completed
    public void hideProgressingView() {
        View v = this.findViewById( android.R.id.content ).getRootView();
        ViewGroup viewGroup = (ViewGroup) v;
        viewGroup.removeView( progressView ); // removing progressbar from login layout
        isProgressShowing = false;
    }

}
