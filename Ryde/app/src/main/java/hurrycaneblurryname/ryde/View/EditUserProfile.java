package hurrycaneblurryname.ryde.View;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import hurrycaneblurryname.ryde.ElasticSearchRequestController;
import hurrycaneblurryname.ryde.Model.Request.RequestUserHolder;
import hurrycaneblurryname.ryde.Model.User;
import hurrycaneblurryname.ryde.Model.UserHolder;
import hurrycaneblurryname.ryde.R;
/**
 * The type Edit User Profile activity.
 * Author: Chen
 */
public class EditUserProfile extends AppCompatActivity {
    private EditText userEditText;
    private EditText emailEditText;
    private EditText phoneEditText;
    private Button finishButton;
    private Button cancelButton;

    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_user_profile);
        setTitle(R.string.editTitle);

        emailEditText = (EditText)findViewById(R.id.emailEditText);
        phoneEditText = (EditText)findViewById(R.id.phoneEditText);
        finishButton = (Button)findViewById(R.id.finishButton);
        cancelButton = (Button)findViewById(R.id.cancelButton);

        // retrive login user info
        user = UserHolder.getInstance().getUser();

        finishButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                user.setEmail(emailEditText.getText().toString());
                user.setPhone(phoneEditText.getText().toString());
                UserHolder.getInstance().setUser(user);
                // TO-DO
                // elastic request to update user profile
                ElasticSearchRequestController.UpdateUserTask updateUserTask = new ElasticSearchRequestController.UpdateUserTask();
                updateUserTask.execute(user);
                finish();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });

    }

    protected void onStart() {
        super.onStart();
        // retrive clicked user info
        user = RequestUserHolder.getInstance().getUser();

        // display current User info on editText
        emailEditText.setText(user.getEmail(), TextView.BufferType.EDITABLE);
        phoneEditText.setText(user.getPhone(), TextView.BufferType.EDITABLE);

    }
}
