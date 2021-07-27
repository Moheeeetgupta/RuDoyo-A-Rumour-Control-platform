package com.rumooursindoyo.moheeeetgupta;

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
 */
public class NotificationFragment extends Fragment {

    private final static String expression = "(?<=watch\\?v=|/videos/|embed\\/|youtu.be\\/|\\/v\\/|\\/e\\/|watch\\?v%3D|watch\\?feature=player_embedded&v=|%2Fvideos%2F|embed%\u200C\u200B2F|youtu.be%2F|%2Fv%2F)[^#\\&\\?\\n]*";
    EditText videiId;


    private static final String TAG = "TextClassificationDemo";

    private TextClassificationClient client;

    private TextView resultTextView;
    // private EditText inputEditText;
    private Handler handler;
    private ScrollView scrollView;



    public NotificationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.tfe_tc_activity_main, container, false);





        videiId = view.findViewById( R.id.ytid );

        Log.v(TAG, "onCreate");

        client = new TextClassificationClient(((AppCompatActivity)getActivity()).getApplicationContext());
        handler = new Handler();
        Button classifyButton = view.findViewById(R.id.button);
        classifyButton.setOnClickListener( new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                // checking if our editText field is empty or not.
                if (videiId.getText().toString().isEmpty()) {
                    videiId.setError( "Please enter search query" );
                    return;
                }

                // if listView will be shown then, below code will be used to hide actionbar
                //    if (getSupportActionBar() != null) {
                //        getSupportActionBar().hide();
                //    }

                String id = getVideoId( videiId.getText().toString() );
                getSuperHeroes( id );


            }
        });


        resultTextView = view.findViewById(R.id.result_text_view);
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
        Call<Results> call = RetrofitClient.getInstance().getMyApi().getsuperHeroes("AIzaSyAbfxyKBnnSKsBho6k8C2OSFdJNk8NAy9w", "plainText", "snippet", 500, id);
        call.enqueue(new Callback<Results>() {
            @Override
            public void onResponse(Call<Results> call, Response<Results> response) {
                List<ItemsArray> myitemsList = response.body().getUserArray();
                String commentssss = null;
                for (int i = 0; i < myitemsList.size(); i++) {
                    Snippet obj = myitemsList.get( i ).getSnippet();
                    TopComments comments = obj.getComment();
                    Snippetii snippetii = comments.getSnippetii();
                    String res = snippetii.getTextDisplay();
                    commentssss += res;
                }
                classify(commentssss);


            }

            @Override
            public void onFailure(Call<Results> call, Throwable t) {
                Toast.makeText( ((AppCompatActivity)getActivity()).getApplicationContext(), t.toString(), Toast.LENGTH_LONG ).show();
            }

        });


    }


    @Override
    public void onStart() {
        super.onStart();
        Log.v(TAG, "onStart");
        handler.post(
                () -> {
                    client.load();
                });
    }

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

                    // Show classification result on screen
                    showResult(text, results);
                });
    }

    /** Show classification result on the screen. */
    private void showResult(final String inputText, final List<Result> results) {
        // Run on UI thread as we'll updating our app UI
        getActivity().runOnUiThread(
                () -> {
                    String textToShow = "Output:\n";
                    for (int i = 0; i < results.size(); i++) {
                        Result result = results.get(i);
                        textToShow += String.format("    %s: %s\n", result.getTitle(), result.getConfidence());
                    }
                    textToShow += "---------\n";

                    // Append the result to the UI.
                    resultTextView.append(textToShow);

                    // Clear the input text.
                    // inputEditText.getText().clear();
                    videiId.getText().clear();

                    // Scroll to the bottom to show latest entry's classification result.
                    scrollView.post(() -> scrollView.fullScroll(View.FOCUS_DOWN));
                });
    }
}