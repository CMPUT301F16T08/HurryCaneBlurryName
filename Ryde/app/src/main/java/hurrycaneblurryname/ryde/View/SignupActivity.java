package hurrycaneblurryname.ryde.View;

import android.content.DialogInterface;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.gson.Gson;

import hurrycaneblurryname.ryde.ElasticSearchRequestController;
import hurrycaneblurryname.ryde.Model.User;
import hurrycaneblurryname.ryde.R;

import static hurrycaneblurryname.ryde.R.string.User;

public class SignupActivity extends AppCompatActivity {
    private EditText userEditText;
    private EditText passwordEditText;
    private EditText emailEditText;
    private EditText phoneEditText;
    private Button signupButton;
    private Button cancelButton;

    private User newUser;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);
        setTitle(R.string.signup);

        userEditText = (EditText) findViewById(R.id.userEditText);
        passwordEditText = (EditText) findViewById(R.id.passwordEditText);
        emailEditText = (EditText) findViewById(R.id.emailEditText);
        phoneEditText = (EditText) findViewById(R.id.phoneEditText);
        signupButton = (Button) findViewById(R.id.signupButton);
        cancelButton = (Button) findViewById(R.id.cancelButton);

        signupButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                int textlength = userEditText.getText().length();
                if (textlength == 0) {
                    emptyDescAlertDialog("Username cannot be empty!");
                    return;
                }
                textlength = passwordEditText.getText().length();
                if (textlength == 0) {
                    emptyDescAlertDialog("Password cannot be empty!");
                    return;
                }
                textlength = phoneEditText.getText().length();
                if (textlength == 0) {
                    emptyDescAlertDialog("Phone Number cannot be empty!");
                    return;
                }
                textlength = emailEditText.getText().length();
                if (textlength == 0) {
                    emptyDescAlertDialog("Email cannot be empty!");
                    return;
                }

                newUser = new User(userEditText.getText().toString());
                newUser.setPassword(passwordEditText.getText().toString());
                newUser.setPhone(phoneEditText.getText().toString());
                newUser.setEmail(emailEditText.getText().toString());

                // TO-DOs
                // save as Gson format
                // do we need to save as a Gson??
                // push to server
                ElasticSearchRequestController.AddUserTask addUserTask = new ElasticSearchRequestController.AddUserTask();
                addUserTask.execute(newUser);


            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });


        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }


    private void emptyDescAlertDialog(String errorMsg) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage(errorMsg);
        alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                // TODO Auto-generated catch block
            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Signup Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }
}
