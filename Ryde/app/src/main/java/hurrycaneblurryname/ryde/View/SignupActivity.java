package hurrycaneblurryname.ryde.View;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import hurrycaneblurryname.ryde.R;

public class SignupActivity extends AppCompatActivity {
    private EditText userEditText;
    private EditText passwordEditText;
    private EditText emailEditText;
    private EditText phoneEditText;
    private Button signupButton;
    private Button cancelButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        setTitle(R.string.signup);

        this.userEditText = (EditText)findViewById(R.id.userEditText);
        this.passwordEditText = (EditText)findViewById(R.id.passwordEditText);
        this.emailEditText = (EditText)findViewById(R.id.emailEditText);
        this.phoneEditText = (EditText)findViewById(R.id.phoneEditText);
        this.signupButton = (Button)findViewById(R.id.signupButton);
        this.cancelButton = (Button)findViewById(R.id.cancelButton);

        signupButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });


    }
}
