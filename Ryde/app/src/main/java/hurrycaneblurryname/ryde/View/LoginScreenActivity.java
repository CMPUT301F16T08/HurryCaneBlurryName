package hurrycaneblurryname.ryde.View;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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
        setContentView(R.layout.activity_login_screen);
        setTitle(R.string.Login);

        this.userEditText = (EditText)findViewById(R.id.userEditText);
        this.passwordEditText = (EditText)findViewById(R.id.userEditText);
        this.loginButton = (Button)findViewById(R.id.loginButton);


        signupTextView = (TextView)findViewById(R.id.reg_text);

        signupTextView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(LoginScreenActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // TO-DOs
                // Run ElasticSearch Query, find if user match
                // Match? get Role, navigate to different main screen
                // unmatch? Hint: User/Password wrong.
                finish();
            }
        });

    }
}
