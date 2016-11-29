package hurrycaneblurryname.ryde.View;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import hurrycaneblurryname.ryde.ElasticSearchRequestController;
import hurrycaneblurryname.ryde.Model.Request.Request;
import hurrycaneblurryname.ryde.Model.Request.RequestHolder;
import hurrycaneblurryname.ryde.Model.Request.RequestUserHolder;
import hurrycaneblurryname.ryde.Model.User;
import hurrycaneblurryname.ryde.NotificationManager;
import hurrycaneblurryname.ryde.R;

/**
 * The type Rider Confirm Driver activity.
 * Author: Chen
 */

public class RiderConfirmDriverActivity extends AppCompatActivity {

    private TextView userTextView;
    private TextView emailTextView;
    private TextView phoneTextView;
    private Button callButton;
    private Button emailButton;
    private Button confirmButton;

    private TextView carInfoTextView;

    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rider_confirm_driver);
        setTitle("Profile Detail");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        userTextView = (TextView)findViewById(R.id.userTexts);
        emailTextView = (TextView)findViewById(R.id.emailTexts);
        phoneTextView = (TextView)findViewById(R.id.phoneTexts);
        carInfoTextView = (TextView)findViewById(R.id.Info);

        callButton = (Button)findViewById(R.id.callButton);
        emailButton = (Button)findViewById(R.id.emailButton);
        confirmButton = (Button)findViewById(R.id.confirmButton);

        user = RequestUserHolder.getInstance().getUser();

        callButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:"+user.getPhone()));
                startActivity(callIntent);
            }
        });

        emailButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent email = new Intent(Intent.ACTION_SEND);
                email.putExtra(Intent.EXTRA_EMAIL, new String[]{user.getEmail()});
                email.putExtra(Intent.EXTRA_SUBJECT, "Ryde: ");
                email.putExtra(Intent.EXTRA_TEXT, "");
                email.setType("message/rfc822");
                startActivity(Intent.createChooser(email, "Choose an Email client :"));
            }
        });

        confirmButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // TODO
                confirmDriverAlertDialog();
            }
        });

    }

    protected void onStart() {
        super.onStart();
        // retrive clicked user info
        user = RequestUserHolder.getInstance().getUser();

        // display current User info on editText
        userTextView.setText(user.getUsername());
        emailTextView.setText(user.getEmail());
        phoneTextView.setText(user.getPhone());
        carInfoTextView.setText(user.getVehicleYear() + "\n" + user.getVehicleMake() + "\n" + user.getVehicleModel());
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

    private void confirmDriverAlertDialog() {
        // dialog show detail of habit when selected
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Confirm Driver");
        alertDialogBuilder.setMessage("Are you sure you want to confirm "+user.getUsername()+" as your driver?");
        alertDialogBuilder.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                // TODO Auto-generated catch block
                finish();
            }
        });
        alertDialogBuilder.setNegativeButton("Yes",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                Request request = RequestHolder.getInstance().getRequest();
                if (!request.getDriver().getUsername().equals("")) {
                    Toast.makeText(RiderConfirmDriverActivity.this, "You have confirmed a driver for this request!", Toast.LENGTH_SHORT).show();

                    return;
                }
                RequestHolder.getInstance().getRequest().setDriver(user);
                RequestHolder.getInstance().getRequest().setStatus("accepted");
                ArrayList<User> updatedOffers = RequestHolder.getInstance().getRequest().getOffers();
                updatedOffers.remove(user);
                RequestHolder.getInstance().getRequest().setOffers(updatedOffers);
                // Update request using elasticsearch query
                ElasticSearchRequestController.UpdateRequestsTask updateRequestsTask = new ElasticSearchRequestController.UpdateRequestsTask();
                updateRequestsTask.execute(RequestHolder.getInstance().getRequest());

                Toast.makeText(RiderConfirmDriverActivity.this, "Success!", Toast.LENGTH_SHORT).show();
                NotificationManager.sendConfirmNotification(user, request.getDescription());
                finish();
            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }


}

