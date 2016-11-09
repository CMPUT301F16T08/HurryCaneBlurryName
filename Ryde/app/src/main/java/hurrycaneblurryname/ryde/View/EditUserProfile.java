package hurrycaneblurryname.ryde.View;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import hurrycaneblurryname.ryde.R;

public class EditUserProfile extends AppCompatActivity {
    private EditText userEditText;
    private EditText emailEditText;
    private EditText phoneEditText;
    private Button finishButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_user_profile);
        setTitle(R.string.editTitle);

        userEditText = (EditText)findViewById(R.id.userEditText);
        emailEditText = (EditText)findViewById(R.id.emailEditText);
        phoneEditText = (EditText)findViewById(R.id.phoneEditText);
        finishButton = (Button)findViewById(R.id.cancelButton);
    }
}
