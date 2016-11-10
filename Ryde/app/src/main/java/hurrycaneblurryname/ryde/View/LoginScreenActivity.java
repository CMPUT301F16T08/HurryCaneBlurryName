package hurrycaneblurryname.ryde.View;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import hurrycaneblurryname.ryde.ElasticSearchRequestController;
import hurrycaneblurryname.ryde.Model.User;
import hurrycaneblurryname.ryde.R;

/**
 * The type Main activity.
 */
public class LoginScreenActivity extends AppCompatActivity {
    private TextView signupTextView;
    private EditText userEditText;
    private EditText passwordEditText;
    private Button loginButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_screen);
        setTitle(R.string.Login);

        userEditText = (EditText)findViewById(R.id.userEditText);
        passwordEditText = (EditText)findViewById(R.id.userEditText);
        loginButton = (Button)findViewById(R.id.loginButton);


        signupTextView = (TextView)findViewById(R.id.reg_text);

        signupTextView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(LoginScreenActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // check if username/password are empty
                int textlength = userEditText.getText().length();
                if (textlength == 0) {
                    emptyDescAlertDialog("Please fill up the Username!");
                    return;
                }
                textlength = passwordEditText.getText().length();
                if (textlength == 0) {
                    emptyDescAlertDialog("Password cannot be empty!");
                    return;
                }
                // TODO
                // Run ElasticSearch Query, find if user match
                ElasticSearchRequestController.GetUsers Task getUserTask = new ElasticSearchRequestController.GetUsersTask();
                getUserTask.execute(userEditText.getText().toString());

                // Match? get Role, navigate to different main screen
                User user;
                try {
                    user = getUserTask.get();
                } catch (Exception e) {
                    Log.i("LoginError", "Couldn't get user");
                    e.printStackTrace();
                }
                // unmatch? Hint: User/Password wrong.

                finish();
            }
        });

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

}
