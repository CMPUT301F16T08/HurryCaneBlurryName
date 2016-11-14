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
import android.widget.Toast;

import hurrycaneblurryname.ryde.ElasticSearchRequestController;
import hurrycaneblurryname.ryde.Model.Rider;
import hurrycaneblurryname.ryde.Model.User;
import hurrycaneblurryname.ryde.Model.UserHolder;
import hurrycaneblurryname.ryde.R;

/**
 * The type Main activity.
 * Author: Chen
 * Modified by: Blaz, Cho
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
        passwordEditText = (EditText)findViewById(R.id.passwordEditText);
        loginButton = (Button)findViewById(R.id.loginButton);

        signupTextView = (TextView)findViewById(R.id.reg_text);

        signupTextView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                signUp();
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                login();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        userEditText.getText().clear();
        passwordEditText.getText().clear();
    }

    private void signUp() {
        Intent intent = new Intent(LoginScreenActivity.this, SignupActivity.class);
        startActivity(intent);
    }

    private void login() {
        // check if username/password are empty
        int textlength = userEditText.getText().length();
        if (textlength == 0) {
            Toast.makeText(LoginScreenActivity.this, "Please provide a Username!", Toast.LENGTH_SHORT).show();
            return;
        }
        textlength = passwordEditText.getText().length();
        if (textlength == 0) {
            Toast.makeText(LoginScreenActivity.this, "Password cannot be empty!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Run ElasticSearch Query, find if user match
        ElasticSearchRequestController.GetUsersTask getUsersTask = new ElasticSearchRequestController.GetUsersTask();
        getUsersTask.execute(userEditText.getText().toString());

        // Match? get Role, navigate to different main screen
        User user;
        try {
            user = getUsersTask.get();
            UserHolder.getInstance().setUser(user);
            // intent to MainActivity
            // http://stackoverflow.com/questions/4878159/whats-the-best-way-to-share-data-between-activities
            // authored by Cristian
            // Accessed November 4, 2016


            if (user.getPassword().equals(passwordEditText.getText().toString())) {
                Intent map = new Intent(LoginScreenActivity.this, MapsActivity.class);
                startActivity(map);
            }
            else{
                Toast.makeText(LoginScreenActivity.this, "Wrong password!", Toast.LENGTH_SHORT).show();
            }
            passwordEditText.getText().clear();

        } catch (Exception e) {
            // no user was found
            Toast.makeText(LoginScreenActivity.this, "Username not found!", Toast.LENGTH_SHORT).show();
        }

    }
}
