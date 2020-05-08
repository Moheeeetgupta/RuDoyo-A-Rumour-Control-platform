package com.rumooursindoyo.moheeeetgupta;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
    private Button loginRegBtn;
    private FirebaseAuth mAuth;
    private ProgressBar loginProg;
    private Button forgotbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();
        loginEmailText = (EditText) findViewById(R.id.Log_Email);
        loginPassText =  (EditText) findViewById(R.id.Login_pass);
        loginBtn =(Button)findViewById(R.id.Login_Btn);
        loginRegBtn =(Button)findViewById(R.id.Login_Reg_Btn);
        loginProg= (ProgressBar) findViewById(R.id.Regprogress);
        forgotbtn =(Button)findViewById (R.id.btn_forgot_password);
        forgotbtn.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                showRecoverPaswword();
            }
        });

        loginRegBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent regIntent = new Intent(loginactivity.this,RegisterActivity.class);
                startActivity(regIntent);
            }
        });


        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String loginEmail = loginEmailText.getText().toString();
                String loginPass = loginPassText.getText().toString();
                if(!TextUtils.isEmpty(loginEmail) && !TextUtils.isEmpty(loginPass))
                {
                    loginProg.setVisibility(View.VISIBLE);
                    mAuth.signInWithEmailAndPassword(loginEmail,loginPass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                                                                                         @Override
                                                                                                         public void onComplete(@NonNull Task<AuthResult> task) {
                                                                                                             if(task.isSuccessful()){
                                                                                                                 sendToMain();
                                                                                                             }else{
                                                                                                                 String errorMessage = task.getException().getMessage();
                                                                                                                 Toast.makeText(loginactivity.this, "Error :" + errorMessage, Toast.LENGTH_SHORT).show();
                                                                                                             }
                                                                                                             loginProg.setVisibility(View.INVISIBLE);
                                                                                                         }
                                                                                                     }
                    );
                }

            }
        });

    }

public void showRecoverPaswword() {
        AlertDialog.Builder builder =new AlertDialog.Builder (this);
        builder.setTitle ("Recover Password");
    LinearLayout linearLayout =new LinearLayout (this);
    final EditText emailEt = new EditText (this);
    emailEt.setHint ("Enter registered email...");
    emailEt.setInputType (InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
    emailEt.setMinEms (10);
    linearLayout.addView (emailEt);
    linearLayout.setPadding (7,10,10,10);
    builder.setView (linearLayout);
    builder.setPositiveButton ("Recover", new DialogInterface.OnClickListener () {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            String email=emailEt.getText ().toString ().trim ();
            beginRecovery(email);

        }
    });
    builder.setNegativeButton ("Cancel", new DialogInterface.OnClickListener () {
        @Override
        public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss ();
        }
    });

    builder.create ().show ();


    }

    private void beginRecovery(String email) {
        mAuth.sendPasswordResetEmail (email)
                .addOnCompleteListener (new OnCompleteListener<Void> () {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful ()){
                    Toast.makeText (loginactivity.this, "Email sent for password recovery...", Toast.LENGTH_SHORT).show ();
                }else{
                    Toast.makeText (loginactivity.this, "Failed to recover...", Toast.LENGTH_SHORT).show ();
                }
            }
        })
                .addOnFailureListener (new OnFailureListener () {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText (loginactivity.this, ""+e.getMessage (), Toast.LENGTH_SHORT).show ();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            sendToMain();

        }
    }
    private void sendToMain(){
        Intent mainIntent = new Intent(loginactivity.this, MainActivity.class);
        startActivity(mainIntent);
        finish();

    }
}
