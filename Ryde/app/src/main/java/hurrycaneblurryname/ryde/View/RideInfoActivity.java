package hurrycaneblurryname.ryde.View;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;


import org.w3c.dom.Text;

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
    private TextView riderClickTextView;
    private TextView driverClickTextView;

    private Request request;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ride_info);
        setTitle("Ride Detail");

        // set up connection to TextView
        descTextView = (TextView)findViewById(R.id.descTexts);
        riderTextView = (TextView)findViewById(R.id.riderUsernameText);
        driverTextView = (TextView)findViewById(R.id.driverUsernameText);
        fromTextView = (TextView)findViewById(R.id.fromTexts);
        toTextView = (TextView)findViewById(R.id.toTexts);
        statusTextView = (TextView)findViewById(R.id.statusTexts);
        feeTextView = (TextView)findViewById(R.id.estTexts);

        riderClickTextView = (TextView)findViewById(R.id.riderClickText);
        driverClickTextView = (TextView)findViewById(R.id.driverClickText);

        riderClickTextView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                RequestUserHolder.getInstance().setUser(request.getRider());
                Intent intent = new Intent(RideInfoActivity.this, ProfileInfoActivity.class);
                startActivity(intent);
            }
        });

        driverClickTextView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                RequestUserHolder.getInstance().setUser(request.getDriver());
                Intent intent = new Intent(RideInfoActivity.this, ProfileInfoActivity.class);
                startActivity(intent);
            }
        });
    }

    protected void onStart() {
        super.onStart();
        // set up TextView Contents according to Clicked request
        request = RequestHolder.getInstance().getRequest();
        setTextViewContent(request);
    }

    private void setTextViewContent(Request request){
        descTextView.setText(request.getDescription());
        riderTextView.setText(request.getRider().getUsername());
        driverTextView.setText(request.getDriver().getUsername());
        //fromTextView.setText(request.getFrom().toString());
        //toTextView.setText(request.getTo().toString());
        statusTextView.setText(request.getStatus());
        feeTextView.setText(request.getEstimate().toString());
    }
}
