package hurrycaneblurryname.ryde.View;

import android.content.Intent;
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

        request = RequestHolder.getInstance().getRequest();
        riderClickTextView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                RequestUserHolder.getInstance().setUser(request.getRider());
                Intent intent = new Intent(RideInfoFromSearch.this, ProfileInfoActivity.class);
                startActivity(intent);
            }
        });

        interestButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ArrayList<User> drivers = request.getOffers();
                if (drivers.contains(UserHolder.getInstance().getUser())){
                    Toast.makeText(RideInfoFromSearch.this, "You are already interested in this request!", Toast.LENGTH_SHORT).show();
                    return;
                }
                else{
                    drivers.add(UserHolder.getInstance().getUser());
                    request.setOffers(drivers);
                    // update request elasticsearch query
                    ElasticSearchRequestController.UpdateRequestsTask updateRequestsTask = new ElasticSearchRequestController.UpdateRequestsTask();
                    updateRequestsTask.execute(request);
                    Toast.makeText(RideInfoFromSearch.this, "Success!", Toast.LENGTH_SHORT).show();
                    finish();
                }
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
        fromTextView.setText(Arrays.toString(request.getFrom()));
        toTextView.setText(Arrays.toString(request.getTo()));
        statusTextView.setText(request.getStatus());
        feeTextView.setText(request.getEstimate().toString());

    }
}
