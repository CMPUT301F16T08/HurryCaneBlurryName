package hurrycaneblurryname.ryde.View;

import android.content.DialogInterface;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;


import com.google.gson.Gson;

import hurrycaneblurryname.ryde.ElasticSearchRequestController;
import hurrycaneblurryname.ryde.Model.User;
import hurrycaneblurryname.ryde.Model.UserHolder;
import hurrycaneblurryname.ryde.R;

import static hurrycaneblurryname.ryde.R.string.User;

/**
 * The type Signup activity.
 * Author: Chen
 * Modified by: Cho
 */
public class SignupActivity extends AppCompatActivity {
    private EditText userEditText;
    private EditText passwordEditText;
    private EditText emailEditText;
    private EditText phoneEditText;
    private Button signupButton;
    private Button cancelButton;

    private User newUser;
    int isDriver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);
        setTitle(R.string.signup);


        userEditText = (EditText)findViewById(R.id.userEditText);
        passwordEditText = (EditText)findViewById(R.id.passwordEditText);
        emailEditText = (EditText)findViewById(R.id.emailEditText);
        phoneEditText = (EditText)findViewById(R.id.phoneEditText);
        signupButton = (Button)findViewById(R.id.signupButton);
        cancelButton = (Button)findViewById(R.id.cancelButton);

        isDriver = -1;

        signupButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                int textlength = userEditText.getText().length();
                if (textlength == 0) {
                    Toast.makeText(SignupActivity.this, "Username cannot be empty!", Toast.LENGTH_SHORT).show();
                    return;
                }
                textlength = passwordEditText.getText().length();
                if (textlength == 0) {
                    Toast.makeText(SignupActivity.this, "Password cannot be empty!", Toast.LENGTH_SHORT).show();
                    return;
                }
                textlength = phoneEditText.getText().length();
                if (textlength == 0) {
                    Toast.makeText(SignupActivity.this, "Phone Number cannot be empty!", Toast.LENGTH_SHORT).show();
                    return;
                }
                textlength = emailEditText.getText().length();
                if (textlength == 0) {
                    Toast.makeText(SignupActivity.this, "Email cannot be empty!", Toast.LENGTH_SHORT).show();
                    return;
                }

                //check if username exists already
                ElasticSearchRequestController.GetUsersTask getUsersTask = new ElasticSearchRequestController.GetUsersTask();
                getUsersTask.execute(userEditText.getText().toString());

                User user = new User("");
                try {
                    user = getUsersTask.get();


                } catch (Exception e) {
                    // fix for ErrorGetUser?
                    Toast.makeText(SignupActivity.this, "Something went wrong when getting user at sign up", Toast.LENGTH_SHORT).show();
                    //Log.i("ErrorGetUser", "Something went wrong when getting user at sign up");
                    //e.printStackTrace();
                }

                if (user.getUsername().isEmpty()) {
                    newUser = new User(userEditText.getText().toString());
                    newUser.setPassword(passwordEditText.getText().toString());
                    newUser.setPhone(phoneEditText.getText().toString());
                    newUser.setEmail(emailEditText.getText().toString());

                    if (isDriver == 1){
                        newUser.setRole("driver");
                    }
                    else if(isDriver == 0){
                        newUser.setRole("rider");
                    }
                    else{
                        Toast.makeText(SignupActivity.this, "Role selection cannot be empty!", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // push to server
                    ElasticSearchRequestController.AddUserTask addUserTask = new ElasticSearchRequestController.AddUserTask();
                    addUserTask.execute(newUser);
                    finish();

                } else {
                    Toast.makeText(SignupActivity.this, "Username already exists!", Toast.LENGTH_SHORT).show();
                }

            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });

    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.radio_yes:
                if (checked)
                    isDriver=1;
                    break;
            case R.id.radio_no:
                if (checked)
                    isDriver=0;
                    break;
        }
    }

}
