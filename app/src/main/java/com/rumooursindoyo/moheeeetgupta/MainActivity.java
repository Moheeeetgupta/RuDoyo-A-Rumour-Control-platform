package com.rumooursindoyo.moheeeetgupta;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {

    private Toolbar mainToolbar;
    // instance of firebase auth for authentication purpose
    private FirebaseAuth mAuth;

    // instance of FirebaseFirestore used to store and sync data for client and server-side development.
    private FirebaseFirestore firebaseFirestore;

    /**
    This variable is used in splashScreen activity to check whether
     the user is login or not if user is login then after showing splashScreen HomeFragmet will be shown and if user is logout
     then after showing splashScreen loginactivity will be shown
     */
    public static int check_login = 0;


    private String current_user_id;

    // FloatingActionButton button for adding new posts
    private FloatingActionButton addPostBtn;

    private BottomNavigationView mainbottomNav;

    private HomeFragment homeFragment;

    // fragment used for prediction of truth probability of any youtube video
    private NotificationFragment notificationFragment;

    // fragment used for managing personal account section of user after registration
    private AccountFragment accountFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // no need , check it
        setTheme(R.style.AppTheme);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // obtaining an instance of this class by calling getInstance() method, like initialising mAuth variable.
        mAuth = FirebaseAuth.getInstance();


        // obtaining an instance of this class by calling getInstance() method, like initialising firebaseFirestore variable.
        firebaseFirestore = FirebaseFirestore.getInstance();

        mainToolbar = (Toolbar) findViewById(R.id.MainToolbar);
        setSupportActionBar(mainToolbar);

        // setting title to actionbar of mainactivity
        getSupportActionBar().setTitle("RuDoYo");

        // if current user exists
        if(mAuth.getCurrentUser() != null) {


            mainbottomNav = findViewById(R.id.mainBottomNav);


            // FRAGMENTS
            homeFragment = new HomeFragment();   // fragment which shows list of posts
            notificationFragment = new NotificationFragment(); // Fragment which shows youtube  video verification feature
            accountFragment = new AccountFragment(); //  Fragment which will show user's personal profile.

            initializeFragment(); // Fragments are initialized.

            // when bottom navigation items/ fragments will be tapped by the user and then , navigation item means fragment will be selected.
            // and corresponding id of the fragment will be received and corresponding fragments will be displayed on the screen.

            mainbottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                    Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.main_container);

                    // if any of three fragments bottom navigation will be tapped by the user then, their corresponding fragments
                    // id will be received, and according to id's corresponding fragments will be opened.
                    switch (item.getItemId()) {

                        case R.id.bottom_action_home:

                            addPostBtn.show(); // addpost button will be shown in this fragment.
                            replaceFragment(homeFragment, currentFragment); // currentFragmet will be replaced with homeFragment when homeFragment's id's will be gotten.
                            return true;

                        case R.id.bottom_action_account:

                            addPostBtn.hide(); // addpost button will be hidden in this fragment.
                            replaceFragment(accountFragment, currentFragment); // currentFragmet will be replaced with accountFragment when accountFragment's id's will be gotten.
                            return true;

                        case R.id.bottom_action_notif:

                            addPostBtn.hide();
                            replaceFragment(notificationFragment, currentFragment);

                            return true;

                        default:
                            return false;


                    }

                }
            });


            // this is the button for adding new posts
            addPostBtn = findViewById(R.id.add_post_btn);
            addPostBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent newPostIntent = new Intent(MainActivity.this,NewPostActivity.class); // on tapping addPostBtn, NewPostActivity for adding new posts will be started.
                    startActivity(newPostIntent);

                }
            });

        }



    }


    @Override
    protected void onStart() {
        super.onStart();
        // getting current state of user
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        // if user is not signed in then send them to SignIn page.
        if(currentUser == null){

            sendToLogin();

        } else { // if user is signed in

            // get user id
            current_user_id = mAuth.getCurrentUser().getUid();

            firebaseFirestore.collection("Users").document(current_user_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                    if(task.isSuccessful()){

                        if(!task.getResult().exists()){

                            Intent setupIntent = new Intent(MainActivity.this, SetupAcivity.class);
                            startActivity(setupIntent);
                            finish();


                        }

                    } else {

                        String errorMessage = task.getException().getMessage();
                        Toast.makeText(MainActivity.this, "Error : " + errorMessage, Toast.LENGTH_LONG).show();


                    }

                }
            });

        }

    }


    @Override
    // main menu means logout button has created in actionbar.
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
      return true;

    }

    @Override
    // for performing logout operation when logout menu button in actionbar has tapped.
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.action_logout_btn:
                logOut();
                return true;


            default:
                return false;


        }

    }



    // logout operation has done and loginactivity has started.
    private void logOut() {


        mAuth.signOut();
        sendToLogin();
        check_login = 0;
    }

    // login activity has started.
    private void sendToLogin() {

        Intent loginIntent = new Intent(MainActivity.this, loginactivity.class);
        startActivity(loginIntent);
        finish();
        check_login = 1;

    }

    // function for initialising fragments.
    private void initializeFragment(){

        // FragmentManager is class provided by the framework which is used to create transactions for adding,
        // removing or replacing fragments.
        // getSupportFragmentManager is associated with Activity consider it as a FragmentManager for your Activity.

        /**
         * beginTransaction :- public abstract FragmentTransaction beginTransaction () Start a series of edit operations on the Fragments
         * associated with this FragmentManager. Note: A fragment transaction can only be created/committed prior to an activity
         * saving its state. If you try to commit a transaction after Activity.
         */

        /**
         * At runtime, a FragmentManager can add, remove, replace, and perform other actions with fragments in response to user
         * interaction. Each set of fragment changes that you commit is called a transaction, and you can specify what to do inside
         * the transaction using the APIs provided by the FragmentTransaction class.
         */
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

        /**
         * add() :- To add a fragment to a FragmentManager, add() function is called on the transaction.
         * This method receives the ID of the container for the fragment, as well as the class name of the fragment
         * you wish to add. The added fragment is moved to the RESUMED state.
         * It is strongly recommended that the container is a FragmentContainerView that is part of the view hierarchy.
         */

        // all the fragments are added to the container.
        fragmentTransaction.add(R.id.main_container, homeFragment);
        fragmentTransaction.add(R.id.main_container, notificationFragment);
        fragmentTransaction.add(R.id.main_container, accountFragment);

        /**
         * Hide() and Show() :- Use the FragmentTransaction methods show() and hide() to show and hide the view of fragments that have been added
         * to a container. These methods set the visibility of the fragment's views without affecting the lifecycle of the fragment.
         *
         * While you don't need to use a fragment transaction to toggle the visibility of the views within a fragment,
         * these methods are useful for cases where you want changes to the visibility state to be associated with
         * transactions on the back stack.
         */

        // only homefragment will be show at the time of intitilisation.
        fragmentTransaction.hide(notificationFragment);
        fragmentTransaction.hide(accountFragment);

        /**
         * Commit :- The final call on each FragmentTransaction must commit the transaction.
         * The commit() call signals to the FragmentManager that all operations have been added to the transaction.
         */

        /**
         * Commit is asynchrono :-
         * Calling commit() doesn't perform the transaction immediately. Rather, the transaction is scheduled to run on the main UI
         * thread as soon as it is able to do so. If necessary, however, you can call commitNow() to run the fragment transaction on
         * your UI thread immediately.
         *
         * Note that commitNow is incompatible with addToBackStack. Alternatively, you can execute all pending FragmentTransactions
         * submitted by commit() calls that have not yet run by calling executePendingTransactions(). This approach is compatible
         * with addToBackStack.
         */
        fragmentTransaction.commit();

    }

    // function for replacing fragments according to user interactions.
    private void replaceFragment(Fragment fragment, Fragment currentFragment) {

        // transaction has begun.
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

        // if homefragment
        if (fragment == homeFragment) {

            // then hide accountfragment and notificationfragment only show homefragment.
            fragmentTransaction.hide(accountFragment);
            fragmentTransaction.hide(notificationFragment);

        }

        if (fragment == accountFragment) {

            fragmentTransaction.hide(homeFragment);
            fragmentTransaction.hide(notificationFragment);

        }

        if (fragment == notificationFragment) {

            fragmentTransaction.hide(homeFragment);
            fragmentTransaction.hide(accountFragment);

        }
        fragmentTransaction.show(fragment);

        //fragmentTransaction.replace(R.id.main_container, fragment);
        fragmentTransaction.commit();

    }

}
