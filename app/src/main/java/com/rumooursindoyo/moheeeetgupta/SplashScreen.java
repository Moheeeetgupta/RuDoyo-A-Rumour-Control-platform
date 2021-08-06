package com.rumooursindoyo.moheeeetgupta;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

/**
 * This activity is used for completing the purpose to implement the functionality of splash screen
 * in the application
 */


// AppCompatActivity :- Base class for activities that wish to use some of the newer
// platform features on older Android devices.
public class SplashScreen extends AppCompatActivity {

    // time duration in which splashscreen will be shown when user will open the application and
    // after finishing SPLASH_TIME_OUT , another activity will be called
    private  static  int SPLASH_TIME_OUT = 3000;
    @Override
    /** onCreate(Bundle savedInstanceState) Function in Android: onCreate method is used to start/create an activity.
     * After Orientation changed then onCreate(Bundle savedInstanceState) will call and recreate
     * the activity and load all data from savedInstanceState.
     * Basically Bundle class is used to store the data of activity whenever above condition occur in app.
     */
    protected void onCreate(Bundle savedInstanceState) {
        // super is used to call the parent class constructor.
        super.onCreate(savedInstanceState);

        /**  setContentView() is a method part of "android.app.Activity class".
         * It helps to set our content or render our layout on the screen.
         * Based on the value given by the user, views will be inflated and rendered after the measurement of the screen, root view, and its child views.
         * "R" is an Java-class that is auto-generated from your resources by the build process.
         * The "R.layout" member is a auto-generated class that contains all IDs for layouts.
         */
        setContentView(R.layout.activity_splash_screen);


        /** Handler class :-  it is basically a message queue. You post a message to it,
         * and it will eventually process it by calling its run method and passing the message to it.
         * Since these run calls will always occur in the order of messages received on the same thread,
         * it allows you to serialize events.
         *
         * postDelayed :- Causes the Runnable r / new Runnable to be added to the message queue, to be run after the specified amount of time elapses.
         * The runnable will be run on the thread to which this handler is attached. The time-base is SystemClock.uptimeMillis().
         * Time spent in deep sleep will add an additional delay to execution.
         *
         * Runnable :-  This is an interface which should be implemented by any class whose instances are intended to be executed
         * by a thread. The class must define a method of no arguments called run .
         * This interface is designed to provide a common protocol for objects that wish to execute code while
         * they are active.
         */
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Checking whether user is signed out or not .
                // If yes then start loginactivity otherwise start mainactivity
                // "check_login", this variable is declared and set in mainactivity so it is accessed
                // here by calling this variable through mainactivity.
                if(MainActivity.check_login == 0){
                    /**
                     * Android Intent is the message that is passed between components such as activities, content providers,
                     * broadcast receivers, services etc.
                     * It is generally used with startActivity() method to invoke activity, broadcast receivers etc.
                     *
                     * Explicit Intent − It going to connect the internal world of an application such as start
                     * activity or send data between two activities. To start new activity we have to create
                     * Intent object and pass source activity and destination activity like below code.
                     *
                     * Implicit Intents − It connects with out side application such as call, mail,
                     * phone,see any website ..etc. In implicit intent we have to pass an action using setAction().
                     */

                    // This is explicit intent , to start new activity we have to create
                    // Intent object, here is loginIntent as intent object and pass source activity and destination activity like below code.
                    Intent loginIntent = new Intent(SplashScreen.this, loginactivity.class);
                    // loginactivity will be started
                    startActivity(loginIntent);
                    /**
                     * finish :- this is called when your activity is done and should be closed,
                     * when starting an activity calling finish() will close the current activity
                     * and it will not be available in the Activity Stack.
                     * On Clicking the back button from the New Activity, the finish() method is called
                     * and the activity destroys and returns to the home screen.
                     * When calling finish() on an activity, the method onDestroy() is executed.
                     * This method can do things like: Dismiss any dialogs the activity was managing.
                     * Close any cursors the activity was managing
                     */
                    finish();
                }else{
                    Intent mainActivityIntent = new Intent(SplashScreen.this, MainActivity.class);
                    startActivity(mainActivityIntent);
                    finish();
                }
            }
        }, SPLASH_TIME_OUT );
    }
}