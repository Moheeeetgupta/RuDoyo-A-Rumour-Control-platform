package com.rumooursindoyo.moheeeetgupta;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {
    private EditText reg_email_field;
    private EditText reg_pass_field;
    private EditText reg_confirm_pass_field;
    private Button reg_btn;
    ViewGroup progressView_reg;
    protected boolean isProgressShowing_reg = false;
    private LinearLayout linearLayout;
    private TextView reg_login_btn;
  // private ProgressBar reg_progress;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAuth =FirebaseAuth.getInstance();


        reg_email_field = findViewById(R.id.Reg_email);
        reg_pass_field = findViewById(R.id.Reg_pass);
        reg_confirm_pass_field= findViewById(R.id.Reg_cnfirm_pass);
        reg_btn = findViewById(R.id.Login_Btn);
        reg_login_btn= findViewById(R.id.Login_Reg_Btn);
        // reg_progress= findViewById(R.id.Regprogress);

        // if user is not new, then by clicking on this view , it will go on loginactivity.
        reg_login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent regIntent = new Intent(RegisterActivity.this,loginactivity.class);
                startActivity(regIntent);
            }
        });

        // button for registration of new user.
        reg_btn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                String email = reg_email_field.getText().toString(); // getting email  entered by the new user
                String pass = reg_pass_field.getText().toString(); // getting password entered by new user
                String confirm_pass = reg_confirm_pass_field.getText().toString(); // getting confirm password entered by new user

                // Checking email, password and confirm password fields are empty or not, if not then enter into below if block
                if(!TextUtils.isEmpty(email) && !TextUtils.isEmpty(pass) && !TextUtils.isEmpty(confirm_pass)) {

                    // checking entered password is equal to confirm password entered, if yes then enter into below if block
                    if(pass.equals(confirm_pass)){

                        // setting up progressbar to signup activity when signup button is clicked and showing it
                        showProgressingView();

                        /**
                         * using " createUserWithEmailAndPassword " , this function , new user is created with entered email id and password
                         */
                        mAuth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                // if new user account is created.
                                if (task.isSuccessful()) {

                                    // then sending link on registered email. to verify account through this function
                                    UserVerification ();

                                    /**
                                     * Objects.requireNonNull :- Checks that the specified object reference is not null.
                                     *
                                     * mAuth.getCurrentUser ()).isEmailVerified () :- checking whether the new user's account
                                     * is verified or nor if yes then enter into the below if block
                                     */
                                    if(Objects.requireNonNull (mAuth.getCurrentUser ()).isEmailVerified ()){

                                        // if user is verified then SetUpActivity will be started to set up personal account like picture and name
                                        Intent setupIntent = new Intent( RegisterActivity.this, SetupAcivity.class);
                                        startActivity(setupIntent);
                                        finish();
                                    }else{
                                        // email is not verified, means user has not verified their account yet, then show below toast
                                        Toast.makeText (RegisterActivity.this, "Please verify your email first!", Toast.LENGTH_SHORT).show ();
                                    }

                                } else {
                                    // if account is not created and some error occures then show toast containing corresponding error
                                    String errorMessage = Objects.requireNonNull (task.getException ()).getMessage();
                                    Toast.makeText(RegisterActivity.this, "Error :" + errorMessage, Toast.LENGTH_SHORT).show();
                                }

                                // this function will remove progressbar from signup activity after completion of signup process.
                                hideProgressingView();
                            }
                        });

                    }else{
                        // if password and confirm password doesn't match then the below toast given will be shown
                        Toast.makeText (RegisterActivity.this, "Password and confirm password should match!", Toast.LENGTH_SHORT).show ();
                    }


                }else {
                    // if any of email, password and confirm password is empty then show the below given text
                    Toast.makeText(RegisterActivity.this, "Please fill the required field first!", Toast.LENGTH_SHORT).show();
                }

                {

                }

            }
        });



    }

    // this function is used for sending verification link on email
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void UserVerification() {
        Objects.requireNonNull (mAuth.getCurrentUser ()).sendEmailVerification ().addOnCompleteListener (new OnCompleteListener<Void> () {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful ()){
                      showadialog();

                }else{
                    String errorMessage = Objects.requireNonNull (task.getException ()).getMessage();
                    Toast.makeText(RegisterActivity.this, "Error :" + errorMessage, Toast.LENGTH_SHORT).show();
                }


            }
        });
    }

    // this function is used to build and show dialog after successfull sending of verification link on bew user's email address
    private void showadialog() {
        AlertDialog.Builder builder =new AlertDialog.Builder (this);
        builder.setTitle ("A verification link has been sent to your email address")
                .setMessage ("Please verify your email first");

        // button to cancel showing dialog box
        builder.setNegativeButton ("Ok", new DialogInterface.OnClickListener () {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss ();
            }
        });
        builder.create ().show ();
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        // if current user exists and logged in, then send it to mainactivity
        if (currentUser != null) {
            sendToMain();
            // User is signed in
        }
    }
    private void sendToMain(){
        Intent mainIntent = new Intent( RegisterActivity.this, MainActivity.class);
        startActivity(mainIntent);
        finish();
    }

    public void showProgressingView() {

        if (!isProgressShowing_reg) {
            isProgressShowing_reg = true;
            progressView_reg = (ViewGroup) getLayoutInflater().inflate(R.layout.progressbar_layout, null);
            View v = this.findViewById(android.R.id.content).getRootView();
            ViewGroup viewGroup = (ViewGroup) v;
            viewGroup.addView(progressView_reg);
        }
    }

    public void hideProgressingView() {
        View v = this.findViewById(android.R.id.content).getRootView();
        ViewGroup viewGroup = (ViewGroup) v;
        viewGroup.removeView(progressView_reg);
        isProgressShowing_reg = false;
    }
}


