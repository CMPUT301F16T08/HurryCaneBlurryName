package hurrycaneblurryname.ryde.View;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
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
    private EditText MakeEditText;
    private EditText ModelEditText;
    private EditText YearEditText;
    private RelativeLayout vehicleInfo;
    private Button finishButton;
    private Button cancelButton;

    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_user_profile);
        setTitle(R.string.editTitle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        emailEditText = (EditText)findViewById(R.id.emailEditText);
        phoneEditText = (EditText)findViewById(R.id.phoneEditText);

        //vehicle fields
        MakeEditText = (EditText)findViewById(R.id.vehicleMake);
        ModelEditText = (EditText)findViewById(R.id.vehicleModel);
        YearEditText = (EditText)findViewById(R.id.vehicleYear);
        vehicleInfo = (RelativeLayout)findViewById(R.id.vehicleInfoPanel);

        // get old info
        //MakeEditText.setText( user.getVehicleModel() );
        //ModelEditText.setText( user.getVehicleModel() );
        //YearEditText.setText( user.getVehicleModel() );


        finishButton = (Button)findViewById(R.id.finishButton);
        cancelButton = (Button)findViewById(R.id.cancelButton);

        // retrive login user info
        user = UserHolder.getInstance().getUser();

        finishButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                user.setEmail(emailEditText.getText().toString());
                user.setPhone(phoneEditText.getText().toString());
                user.setVehicleMake(MakeEditText.getText().toString());
                user.setVehicleModel(ModelEditText.getText().toString());
                user.setVehicleYear(Integer.valueOf(YearEditText.getText().toString()));
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
        user = UserHolder.getInstance().getUser();

        // display current User info on editText
        emailEditText.setText(user.getEmail(), TextView.BufferType.EDITABLE);
        phoneEditText.setText(user.getPhone(), TextView.BufferType.EDITABLE);
        MakeEditText.setText( user.getVehicleMake(), TextView.BufferType.EDITABLE );
        ModelEditText.setText( user.getVehicleModel(), TextView.BufferType.EDITABLE );
        YearEditText.setText( Integer.valueOf(user.getVehicleYear()).toString(), TextView.BufferType.EDITABLE );
        if (user.getRole().equals("rider")){
            vehicleInfo.setVisibility(View.GONE);
        }

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
