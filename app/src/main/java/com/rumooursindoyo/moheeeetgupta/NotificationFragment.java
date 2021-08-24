package com.rumooursindoyo.moheeeetgupta;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
// import android.telecom.Call;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import org.tensorflow.lite.examples.textclassification.client.Result;
import org.tensorflow.lite.examples.textclassification.client.TextClassificationClient;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

// import javax.security.auth.callback.Callback;


/**
 * A simple {@link Fragment} subclass.
 *
 * A Fragment represents a reusable portion of your app's UI. A fragment defines and manages its own layout,
 * has its own lifecycle, and can handle its own input events. Fragments cannot live on their own;
 * they must be hosted by an activity or another fragment. The fragment’s view hierarchy becomes part of, or attaches to, the host’s view hierarchy.
 */
public class NotificationFragment extends Fragment {

    private final static String expression = "(?<=watch\\?v=|/videos/|embed\\/|youtu.be\\/|\\/v\\/|\\/e\\/|watch\\?v%3D|watch\\?feature=player_embedded&v=|%2Fvideos%2F|embed%\u200C\u200B2F|youtu.be%2F|%2Fv%2F)[^#\\&\\?\\n]*";
    EditText videiId;

    private  int flag = 0;
    ViewGroup progressView;
    View view;
    protected boolean isProgressShowing = false;

    private static final String TAG = "TextClassificationDemo";

    private TextClassificationClient client;

    private TextView resultTextView;
    private TextView resultCommentTextView;
    private TextView headingToShow;
    private View viewDivider;
    private Handler handler;

    /*
    A Handler allows communicating back with UI thread from other background thread.
    This is useful in android as android doesn’t allow other threads to communicate directly with UI thread.
    A Handler allows you to send and process Message and Runnable objects associated with a thread’s MessageQueue.
    Each Handler instance is associated with a single thread and that thread’s message queue.
     */

    private ScrollView scrollView;
    private String sharedLink=null;





    public NotificationFragment() {
        // Required empty public constructor
    }


    @Override
     /*
    onCreateView() is called by Android once the Fragment should inflate a view.
     */
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.tfe_tc_activity_main, container, false);
        MainActivity mainActivity=(MainActivity)getActivity ();
        sharedLink=mainActivity.getLink ();
        videiId = view.findViewById( R.id.ytid );
        if(sharedLink!=null){
            videiId.setText (sharedLink);
        }

        headingToShow = view.findViewById( R.id.text_heading );
        viewDivider = view.findViewById( R.id.view_divider );
      //  viewDividerBottom = view.findViewById( R.id.view_divider_bottom );
        Log.v(TAG, "onCreate");


        client = new TextClassificationClient(((AppCompatActivity)getActivity()).getApplicationContext());
        handler = new Handler();

        //Button to predict the truth and false probability of youtube link.
        Button classifyButton = view.findViewById(R.id.button);

        /*
        setOnClickListener method helps us to link a listener with certain attributes.
        setOnClickListener is a method in Android basically used with buttons, image buttons etc.
         */
        classifyButton.setOnClickListener( new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                showProgressingView();
                // checking if our editText field is empty or not.
                if (videiId.getText().toString().isEmpty()) {
                    videiId.setError( "Please enter video link..." );
                    hideProgressingView();
                    return;
                }




                if(isYoutubeUrl(videiId.getText ().toString ())==false){
                    Toast.makeText(getActivity (),"Please Enter a valid youtube link...",Toast.LENGTH_LONG).show ();
                    hideProgressingView();
                }else{
                    if(flag == 1){
                        resultTextView.setText( " " );
                        resultCommentTextView.setText( " " );
                        flag = 0;
                    }
                    if(flag == 0) {
                        String id = getVideoId( videiId.getText().toString() );
                        /*
                        getSuperHeroes takes youtube video id  as input and fetches comments(<= 500) on that particular video id
                        and append them as paragraph which further given as input to classify method.
                        */
                        getSuperHeroes( id );
                    }
                }



            }
        });


        resultTextView = view.findViewById(R.id.result_text_view);
        resultCommentTextView = view.findViewById( R.id.result_text_view_cmmnt );
        // inputEditText = findViewById(R.id.input_text);
        scrollView = view.findViewById(R.id.scroll_view);




        return view;
    }





    public static String getVideoId(String videoUrl) {
        if (videoUrl == null || videoUrl.trim().length() <= 0){
            return null;
        }
        Pattern pattern = Pattern.compile(expression);
        Matcher matcher = pattern.matcher(videoUrl);
        try {
            if (matcher.find())
                return matcher.group();
        } catch (ArrayIndexOutOfBoundsException ex) {
            ex.printStackTrace();
        }
        return null;
    }



    private void getSuperHeroes(String id) {
        Call<Results> call = RetrofitClient.getInstance().getMyApi().getsuperHeroes("AIzaSyAbfxyKBnnSKsBho6k8C2OSFdJNk8NAy9w", "plainText", "snippet", 10000, id);
        call.enqueue(new Callback<Results>() {
            @Override
            public void onResponse(Call<Results> call, Response<Results> response) {
                List<ItemsArray> myitemsList = response.body().getUserArray();
                String commentssss = " ";
                String textCommentsShow = "";
                for (int i = 0; i < myitemsList.size(); i++) {
                    Snippet obj = myitemsList.get( i ).getSnippet();
                    TopComments comments = obj.getComment();
                    Snippetii snippetii = comments.getSnippetii();
                    String res = snippetii.getTextDisplay();
                    commentssss =commentssss+"\n"+ res;
                    res = (i+1) + ". " + res;
                    textCommentsShow = textCommentsShow + "\n" + res;
                }
              classify(commentssss);
                resultCommentTextView.append( textCommentsShow );
              //  hideProgressingView();
              //resultTextView.setText( commentssss );


            }

            @Override
            public void onFailure(Call<Results> call, Throwable t) {
                Toast.makeText( ((AppCompatActivity)getActivity()).getApplicationContext(), t.toString(), Toast.LENGTH_LONG ).show();
            }

        });


    }



    public static boolean isYoutubeUrl(String youTubeURl)
    {
        boolean success;
        String pattern = "^(http(s)?:\\/\\/)?((w){3}.)?youtu(be|.be)?(\\.com)?\\/.+";
        if (!youTubeURl.isEmpty() && youTubeURl.matches(pattern))
        {
            success = true;
        }
        else
        {
            // Not Valid youtube URL
            success = false;
        }
        return success;
    }



    /*
    onStart() When activity start getting visible to user then onStart() will be called.
    This calls just after the onCreate() at first time launch of activity. When activity launch,
    first onCreate() method call then onStart() and then onResume(). If the activity is in onPause() condition i.e. not visible to user.
     */


    /*
    Lambda Parameters are parameters to the lambda function passed within opening parenthesis "(" and closing parenthesis ")".
    When more than one parameter is passed, they are separated by commas. To support lambdas,
    Java has introduced a new operator “->”, also known as lambda operator or arrow operator

    The left side specifies the parameters required by the expression, which could also be empty if no parameters are required.
    The right side is the lambda body which specifies the actions of the lambda expression.
    */

    @Override
    public void onStart() {
        super.onStart();
        Log.v(TAG, "onStart");
        handler.post(
                () -> {
                    client.load();
                });
    }


    /*
    When Activity is in background then onPause() method will execute. After a millisecond of that method next
    onStop() method will execute. Means here also Activity is not visible to user when onStop() executed.
    We will use onStop() method to stop Api calls etc.
     */

    @Override
    public void onStop() {
        super.onStop();
        Log.v(TAG, "onStop");
        handler.post(
                () -> {
                    client.unload();
                });
    }

    /** Send input text to TextClassificationClient and get the classify messages. */
    private void classify(final String text) {
        handler.post(
                () -> {
                    // Run text classification with TF Lite.
                    List<Result> results = client.classify(text);
                    hideProgressingView();

                    // Show classification result on screen
                    showResult(text, results);

                });
    }

    /** Show classification result on the screen. */
    private void showResult(final String inputText, final List<Result> results) {
        // Run on UI thread as we'll updating our app UI
        getActivity().runOnUiThread(
                () -> {

                    String textToShow = "\n";
                    String textCommentsShow = "\n";


                    for (int i = 0; i < results.size(); i++) {
                        Result result = results.get(i);
                       // textToShow += String.format("    %s: %s\n", result.getTitle(), result.getConfidence()*100);
                       //  textToShow  = textToShow + result.getTitle() + "   = " + result.getConfidence()*100 + "\n";


                        if(result.getTitle().equals( "Positive" )){
                            textToShow += String.format ("    %s: %.1f%%\n",  "Truth Probability of Video ", result.getConfidence () * 100.0f);
                        }else if(result.getTitle().equals("Negative")){
                            textToShow += String.format ("    %s: %.1f%%\n", "False Probability of Video ", result.getConfidence () * 100.0f );
                        }
                    }






                    resultTextView.append (textToShow);
                    flag = 1;
                    headingToShow.setVisibility( View.VISIBLE );
                    viewDivider.setVisibility( View.VISIBLE );
                    // viewDividerBottom.setVisibility( View.VISIBLE );




                    // Append the result to the UI.
                   // resultTextView.append(textToShow);

                    // Clear the input text.
                    // inputEditText.getText().clear();
                    videiId.getText().clear();

                    // Scroll to the bottom to show latest entry's classification result.
                    scrollView.post(() -> scrollView.fullScroll(View.FOCUS_DOWN));
                });
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