package hurrycaneblurryname.ryde.View;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import hurrycaneblurryname.ryde.ElasticSearchRequestController;
import hurrycaneblurryname.ryde.Model.User;
import hurrycaneblurryname.ryde.Model.UserHolder;
import hurrycaneblurryname.ryde.R;

/**
 * The type Edit User Profile activity.
 * Author: Chen
 */
public class AddDriverInfo extends AppCompatActivity {
    private EditText userEditText;
    private TextView emailEditText;
    private TextView phoneEditText;
    private EditText vehicleEditText;
    private Button finishButton;
    private Button cancelButton;

    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_driver_info);
        setTitle(R.string.addDriverInfo);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        vehicleEditText = (EditText)findViewById(R.id.vehicleEditText);

        emailEditText = (TextView)findViewById(R.id.userEmailTextView);
        phoneEditText = (TextView)findViewById(R.id.userPhoneTextView);

        finishButton = (Button)findViewById(R.id.finishButton);
        cancelButton = (Button)findViewById(R.id.cancelButton);

        // retrive login user info
        user = UserHolder.getInstance().getUser();

        finishButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if (vehicleEditText.getText().toString().isEmpty()) {
                    Toast.makeText(AddDriverInfo.this, "Please enter your vehicle description", Toast.LENGTH_SHORT).show();
                    return;
                }
                user.setVehicle(vehicleEditText.getText().toString());
                user.setRole("driver");
                UserHolder.getInstance().setUser(user);


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
        user = UserHolder.getInstance().getUser();

        // display current User info on editText
        emailEditText.setText(user.getEmail());
        phoneEditText.setText(user.getPhone());

    }

    // Back Navigation Handle
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
