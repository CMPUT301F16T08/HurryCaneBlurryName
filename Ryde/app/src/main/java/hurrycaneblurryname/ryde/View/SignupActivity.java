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


import com.google.gson.Gson;

import hurrycaneblurryname.ryde.ElasticSearchRequestController;
import hurrycaneblurryname.ryde.Model.User;
import hurrycaneblurryname.ryde.Model.UserHolder;
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

                //check if username exists already
                ElasticSearchRequestController.GetUsersTask getUsersTask = new ElasticSearchRequestController.GetUsersTask();
                getUsersTask.execute(userEditText.getText().toString());

                User user = new User("");
                try {
                    user = getUsersTask.get();


                } catch (Exception e) {
                    Log.i("ErrorGetUser", "Something went wrong when getting user at sign up");
                    e.printStackTrace();
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
                        emptyDescAlertDialog("Role selection cannot be empty!");
                        return;
                    }

                    // push to server
                    ElasticSearchRequestController.AddUserTask addUserTask = new ElasticSearchRequestController.AddUserTask();
                    addUserTask.execute(newUser);
                    finish();

                } else {

                    emptyDescAlertDialog("Username already exists");
                }

            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
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
