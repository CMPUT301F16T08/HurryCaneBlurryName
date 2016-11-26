package hurrycaneblurryname.ryde.View;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import org.w3c.dom.Text;
import java.util.Arrays;

import hurrycaneblurryname.ryde.ElasticSearchRequestController;
import hurrycaneblurryname.ryde.Model.Request.Request;
import hurrycaneblurryname.ryde.Model.Request.RequestHolder;
import hurrycaneblurryname.ryde.Model.Request.RequestUserHolder;
import hurrycaneblurryname.ryde.R;

/**
 * The type Rider Ride Info activity.
 * Author: Chen
 */

public class RideInfoActivity extends AppCompatActivity {
    private TextView descTextView;
    private TextView riderTextView;
    private TextView driverTextView;
    private TextView fromTextView;
    private TextView toTextView;
    private TextView statusTextView;
    private TextView feeTextView;
    private TextView driverClickTextView;
    private TextView driversClickTextView;

    private Button completeButton;      //TODO
    private Button cancelButton;

    private Request request;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ride_info);
        setTitle("Ride Detail");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        // set up connection to TextView
        descTextView = (TextView)findViewById(R.id.descTexts);
        riderTextView = (TextView)findViewById(R.id.riderUsernameText);
        driverTextView = (TextView)findViewById(R.id.driverUsernameText);
        fromTextView = (TextView)findViewById(R.id.fromTexts);
        toTextView = (TextView)findViewById(R.id.toTexts);
        statusTextView = (TextView)findViewById(R.id.statusTexts);
        feeTextView = (TextView)findViewById(R.id.estTexts);

        driverClickTextView = (TextView)findViewById(R.id.driverClickText);
        driversClickTextView = (TextView)findViewById(R.id.driversClickText);

        completeButton = (Button)findViewById(R.id.completeButton);
        cancelButton = (Button)findViewById(R.id.cancelButton);
        request = RequestHolder.getInstance().getRequest();
        driverClickTextView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (request.getDriver().getUsername().equals(""))
                {
                    Toast.makeText(RideInfoActivity.this, "No driver has accepted this request!", Toast.LENGTH_SHORT).show();
                    return;
                }
                RequestUserHolder.getInstance().setUser(request.getDriver());
                Intent intent = new Intent(RideInfoActivity.this, ProfileInfoActivity.class);
                startActivity(intent);
            }
        });

        driversClickTextView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(RideInfoActivity.this, AvailableDriversActivity.class);
                startActivity(intent);
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                cancelAlertDialog();
            }
        });

        completeButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                completeAlertDialog();
            }
        });

    }

    protected void onStart() {
        super.onStart();
        // set up TextView Contents according to Clicked request
        request = RequestHolder.getInstance().getRequest();
        setTextViewContent(request);
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

    private void setTextViewContent(Request request){
        descTextView.setText(request.getDescription());
        riderTextView.setText(request.getRider().getUsername());
        if (!request.getDriver().getUsername().equals(""))
        {
            driverTextView.setText(request.getDriver().getUsername());
            driversClickTextView.setVisibility(View.GONE);
        }
        else{
            driverClickTextView.setVisibility(View.GONE);
        }
        fromTextView.setText(Arrays.toString(request.getFrom()));
        toTextView.setText(Arrays.toString(request.getTo()));
        statusTextView.setText(request.getStatus());
        feeTextView.setText("$"+request.getEstimate().toString());

        if(request.getStatus().equals("open"))
        {
            completeButton.setVisibility(View.GONE);
        }

        if (request.getStatus().equals("closed")) {
            completeButton.setVisibility(View.GONE);
            cancelButton.setVisibility(View.GONE);
            TextView feeText = (TextView)findViewById(R.id.estTextView);
            feeText.setText("Fee:");
        }


    }

    private void cancelAlertDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Cancel a request");
        alertDialogBuilder.setMessage("Are you sure you want to cancel this request?");
        alertDialogBuilder.setPositiveButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                // TODO Auto-generated catch block
            }
        });

        alertDialogBuilder.setNegativeButton("Yes",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                // delete from server
                ElasticSearchRequestController.DeleteRequestsTask deleteRequestTask = new ElasticSearchRequestController.DeleteRequestsTask();
                deleteRequestTask.execute(request);
                Toast.makeText(RideInfoActivity.this, "Success!", Toast.LENGTH_SHORT).show();
                finish();
            }

        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void completeAlertDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Complete a request");
        alertDialogBuilder.setMessage("Fee: " + request.getEstimate().toString()+"\nMark request as Complete?");
        alertDialogBuilder.setPositiveButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                // TODO Auto-generated catch block
            }
        });

        alertDialogBuilder.setNegativeButton("Yes",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                if (RequestHolder.getInstance().getRequest().getDriverComplete()) {
                    // Update request using elasticsearch query
                    RequestHolder.getInstance().getRequest().setRiderComplete();
                    RequestHolder.getInstance().getRequest().setStatus("closed");
                    ElasticSearchRequestController.UpdateRequestsTask updateRequestsTask = new ElasticSearchRequestController.UpdateRequestsTask();
                    updateRequestsTask.execute(RequestHolder.getInstance().getRequest());
                    Toast.makeText(RideInfoActivity.this, "Success!", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(RideInfoActivity.this, "Please wait for Driver to complete!", Toast.LENGTH_SHORT).show();
                }
            }

        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

}
