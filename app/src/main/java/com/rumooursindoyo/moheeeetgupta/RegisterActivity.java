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
    private ProgressBar reg_progress;
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
        reg_login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent regIntent = new Intent(RegisterActivity.this,loginactivity.class);
                startActivity(regIntent);
            }
        });
        reg_btn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                String email = reg_email_field.getText().toString();
                String pass = reg_pass_field.getText().toString();
                String confirm_pass = reg_confirm_pass_field.getText().toString();
                if(!TextUtils.isEmpty(email) && !TextUtils.isEmpty(pass) && !TextUtils.isEmpty(confirm_pass)) {
                    if(pass.equals(confirm_pass)){
                       // reg_progress.setVisibility(View.VISIBLE);
                        showProgressingView();
                        mAuth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {

                                    UserVerification ();
                                    if(Objects.requireNonNull (mAuth.getCurrentUser ()).isEmailVerified ()){
                                        Intent setupIntent = new Intent( RegisterActivity.this, SetupAcivity.class);
                                        startActivity(setupIntent);
                                        finish();
                                    }else{
                                        Toast.makeText (RegisterActivity.this, "Please verify your email first!", Toast.LENGTH_SHORT).show ();
                                    }

                                } else {
                                    String errorMessage = Objects.requireNonNull (task.getException ()).getMessage();
                                    Toast.makeText(RegisterActivity.this, "Error :" + errorMessage, Toast.LENGTH_SHORT).show();
                                }
                               // reg_progress.setVisibility(View.INVISIBLE);
                                hideProgressingView();
                            }
                        });

                    }else{
                        Toast.makeText (RegisterActivity.this, "Password and confirm password should match!", Toast.LENGTH_SHORT).show ();
                    }


                }else {
                    Toast.makeText(RegisterActivity.this, "Please fill the required field first!", Toast.LENGTH_SHORT).show();
                }

                {

                }

            }
        });



    }

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

    private void showadialog() {
        AlertDialog.Builder builder =new AlertDialog.Builder (this);
        builder.setTitle ("A verification link has been sent to your email address")
                .setMessage ("Please verify your email first...");

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
        View v = this.findViewById (android.R.id.content).getRootView ();
        ViewGroup viewGroup = (ViewGroup) v;
        viewGroup.removeView(progressView_reg);
        isProgressShowing_reg = false;
    }
}


