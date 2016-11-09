package hurrycaneblurryname.ryde.View;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import hurrycaneblurryname.ryde.Model.User;
import hurrycaneblurryname.ryde.R;

public class SignupActivity extends AppCompatActivity {
    private EditText userEditText;
    private EditText passwordEditText;
    private EditText emailEditText;
    private EditText phoneEditText;
    private Button signupButton;
    private Button cancelButton;

    private User newUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        setTitle(R.string.signup);

        userEditText = (EditText)findViewById(R.id.userEditText);
        passwordEditText = (EditText)findViewById(R.id.passwordEditText);
        emailEditText = (EditText)findViewById(R.id.emailEditText);
        phoneEditText = (EditText)findViewById(R.id.phoneEditText);
        signupButton = (Button)findViewById(R.id.signupButton);
        cancelButton = (Button)findViewById(R.id.cancelButton);

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
                // push to server

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
}
