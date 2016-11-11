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
import hurrycaneblurryname.ryde.Model.Rider;
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

    private Button testButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_screen);
        setTitle(R.string.Login);

        userEditText = (EditText)findViewById(R.id.userEditText);
        passwordEditText = (EditText)findViewById(R.id.userEditText);
        loginButton = (Button)findViewById(R.id.loginButton);

        testButton = (Button)findViewById(R.id.testButton);

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
                ElasticSearchRequestController.GetUsersTask getUserTask = new ElasticSearchRequestController.GetUsersTask();
                getUserTask.execute(userEditText.getText().toString());

                // Match? get Role, navigate to different main screen
                User user;
                try {
                    user = getUserTask.get();
                    if (user.getRole().equals("rider")) {
                        // intent to RiderMainActivity
                        // http://stackoverflow.com/questions/2736389/how-to-pass-an-object-from-one-activity-to-another-on-android
                        Intent RiderMain = new Intent(LoginScreenActivity.this, RiderMainActivity.class);
                        RiderMain.putExtra("username",user.getUsername());
                        RiderMain.putExtra("password",user.getPassword());
                        RiderMain.putExtra("phone",user.getPhone());
                        RiderMain.putExtra("email",user.getEmail());
                        RiderMain.putExtra("cardnumber",user.getCardNumber());

                        startActivity(RiderMain);
                    } else if (user.getRole().equals("driver")) {
                        // intent to DriverMainActivity
                        Intent DriverMain = new Intent(LoginScreenActivity.this, DriverMainActivity.class);
                        DriverMain.putExtra("username",user.getUsername());
                        DriverMain.putExtra("password",user.getPassword());
                        DriverMain.putExtra("phone",user.getPhone());
                        DriverMain.putExtra("email",user.getEmail());
                        DriverMain.putExtra("cardnumber",user.getCardNumber());

                        startActivity(DriverMain);
                    }

                } catch (Exception e) {
                    Log.i("ErrorLogin", "Couldn't get user");
                    e.printStackTrace();
                }
                //finish();
            }
        });

        testButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // test to different activities
                Intent intent = new Intent(LoginScreenActivity.this, RiderMainActivity.class);
                startActivity(intent);
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
