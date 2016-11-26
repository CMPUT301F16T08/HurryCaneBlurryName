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

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

import hurrycaneblurryname.ryde.ElasticSearchRequestController;
import hurrycaneblurryname.ryde.Model.Request.Request;
import hurrycaneblurryname.ryde.Model.Request.RequestHolder;
import hurrycaneblurryname.ryde.Model.Request.RequestUserHolder;
import hurrycaneblurryname.ryde.Model.User;
import hurrycaneblurryname.ryde.Model.UserHolder;
import hurrycaneblurryname.ryde.NotificationManager;
import hurrycaneblurryname.ryde.R;

public class RideInfoFromSearch extends AppCompatActivity {

    private TextView descTextView;
    private TextView riderTextView;
    private TextView fromTextView;
    private TextView toTextView;
    private TextView statusTextView;
    private TextView feeTextView;
    private TextView riderClickTextView;

    private Button interestButton;
    private Button completeButton;
    private TextView awaitText;

    private Request request;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ride_info_from_search);
        setTitle("Ride Detail");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        // set up connection to TextView
        descTextView = (TextView)findViewById(R.id.descTexts);
        riderTextView = (TextView)findViewById(R.id.riderUsernameText);
        fromTextView = (TextView)findViewById(R.id.fromTexts);
        toTextView = (TextView)findViewById(R.id.toTexts);
        statusTextView = (TextView)findViewById(R.id.statusTexts);
        feeTextView = (TextView)findViewById(R.id.estTexts);

        riderClickTextView = (TextView)findViewById(R.id.riderClickText);
        interestButton = (Button)findViewById(R.id.interestButton);
        completeButton = (Button)findViewById(R.id.completeButton);
        awaitText = (TextView)findViewById(R.id.awaitText);

        request = RequestHolder.getInstance().getRequest();

        if (request.getOffers().contains(UserHolder.getInstance().getUser())) {
            interestButton.setText(R.string.interestCancel);
        } else {
            interestButton.setText(R.string.interestRequest);
        }


        riderClickTextView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                RequestUserHolder.getInstance().setUser(request.getRider());
                Intent intent = new Intent(RideInfoFromSearch.this, ProfileInfoActivity.class);
                startActivity(intent);
            }
        });

        interestButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (request.getOffers().contains(UserHolder.getInstance().getUser())){
                    Toast.makeText(RideInfoFromSearch.this, "You are no longer interested in this request.", Toast.LENGTH_SHORT).show();
                    request.removeOffer(UserHolder.getInstance().getUser());
                    interestButton.setText(R.string.interestRequest);



                } else {
                    request.addOffer(UserHolder.getInstance().getUser());
                    Toast.makeText(RideInfoFromSearch.this, "You're interested in this request!", Toast.LENGTH_SHORT).show();
                    interestButton.setText(R.string.interestCancel);

                    NotificationManager.sendAcceptNotification(request.getRider());

                }

                ElasticSearchRequestController.UpdateRequestsTask updateRequestsTask = new ElasticSearchRequestController.UpdateRequestsTask();
                updateRequestsTask.execute(request);


                finish();
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
        buttonTextViewControl(request);
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
        fromTextView.setText(Arrays.toString(request.getFrom()));
        toTextView.setText(Arrays.toString(request.getTo()));
        statusTextView.setText(request.getStatus());
        feeTextView.setText("$"+request.getEstimate().toString());

    }

    private void buttonTextViewControl(Request request){
        if (request.getStatus().equals("open")) {
            completeButton.setVisibility(View.GONE);
        }
        if (request.getStatus().equals("accepted")){
            interestButton.setVisibility(View.GONE);
        }
        if (!request.getDriverComplete()) {
            awaitText.setVisibility(View.GONE);
        }
        else {
            completeButton.setVisibility(View.GONE);
        }
        if (request.getStatus().equals("closed")){
            completeButton.setVisibility(View.GONE);
            interestButton.setVisibility(View.GONE);
            awaitText.setVisibility(View.GONE);
        }
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
                // Update request using elasticsearch query
                RequestHolder.getInstance().getRequest().setDriverComplete();
                ElasticSearchRequestController.UpdateRequestsTask updateRequestsTask = new ElasticSearchRequestController.UpdateRequestsTask();
                updateRequestsTask.execute(RequestHolder.getInstance().getRequest());
                Toast.makeText(RideInfoFromSearch.this, "Success!", Toast.LENGTH_SHORT).show();
                finish();
            }

        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}
