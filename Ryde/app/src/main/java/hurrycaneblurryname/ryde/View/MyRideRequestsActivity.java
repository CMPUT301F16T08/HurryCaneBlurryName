package hurrycaneblurryname.ryde.View;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import hurrycaneblurryname.ryde.ElasticSearchRequestController;
import hurrycaneblurryname.ryde.Model.Request.Request;
import hurrycaneblurryname.ryde.Model.Request.RequestHolder;
import hurrycaneblurryname.ryde.Model.User;
import hurrycaneblurryname.ryde.Model.UserHolder;
import hurrycaneblurryname.ryde.R;

/**
 * The type My Ride Info activity.
 * Author: Chen
 * Storyboard by: Blaz
 * Code Reuse from: asg1-Blaz
 */

public class MyRideRequestsActivity extends AppCompatActivity {

    private User user;

    //Arrays
    private ArrayList<Request> requestList = new ArrayList<Request>();
    private ArrayList<Request> openRequests = new ArrayList<Request>();
    private ArrayList<Request> offers = new ArrayList<Request>();
    private ArrayList<Request> closedRequests = new ArrayList<Request>();


    //ListViews
    private ListView openView;
    private ListView offerView;
    private ListView closedView;

    //Adapters
    private ArrayAdapter<Request> openViewAdapter;
    private ArrayAdapter<Request> offerViewAdapter;
    private ArrayAdapter<Request> closedViewAdapter;

    // Status TextView
    private TextView openText;
    private TextView offerText;
    private TextView closedText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_ride_requests);
        setTitle("My Ride Request");

        openView = (ListView) findViewById(R.id.openView);
        openView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Get request to show and start RideInfo
                Request requestToPass = openRequests.get(position);
                RequestHolder.getInstance().setRequest(requestToPass);
                Intent info = new Intent(MyRideRequestsActivity.this, RideInfoActivity.class);
                startActivity(info);

            }
        });

        offerView = (ListView) findViewById(R.id.offerView);
        offerView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Get request to show and start RideInfo
                Request requestToPass = openRequests.get(position);
                RequestHolder.getInstance().setRequest(requestToPass);
                Intent info = new Intent(MyRideRequestsActivity.this, RideInfoActivity.class);
                startActivity(info);

            }
        });

        closedView = (ListView) findViewById(R.id.closedView);
        closedView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Get request to show and start RideInfo
                Request requestToPass = openRequests.get(position);
                RequestHolder.getInstance().setRequest(requestToPass);
                Intent info = new Intent(MyRideRequestsActivity.this, RideInfoActivity.class);
                startActivity(info);

            }
        });

    }

    @Override

    protected void onStart() {
        super.onStart();

        //Load in datas
        user = UserHolder.getInstance().getUser();

        ElasticSearchRequestController.GetRiderRequestsTask getMyRequests = new ElasticSearchRequestController.GetRiderRequestsTask();
        getMyRequests.execute(user.getUsername());
        try {
            requestList = getMyRequests.get();

        } catch (Exception e) {
            Log.i("ErrorGetRequest", "Failed to get open requests");
        }

        for (Request r : requestList ) {
            String status = r.getStatus();
            if(status.equals("open")) {
                openRequests.add(r);
            } else if (status.equals("accepted")) {
                offers.add(r);
            } else if (status.equals("closed")) {
                closedRequests.add(r);
            }
        }

        openViewAdapter = new ArrayAdapter<Request>(this, R.layout.list_item, openRequests);
        openView.setAdapter(openViewAdapter);

        offerViewAdapter = new ArrayAdapter<Request>(this, R.layout.list_item, offers);
        offerView.setAdapter(offerViewAdapter);

        closedViewAdapter = new ArrayAdapter<Request>(this, R.layout.list_item, closedRequests);
        closedView.setAdapter(closedViewAdapter);

        changeTextStatus();

    }

    private void changeTextStatus(){
        openText = (TextView)findViewById(R.id.openText);
        offerText = (TextView)findViewById(R.id.offerText);
        closedText = (TextView)findViewById(R.id.closedText);

        if((!openRequests.isEmpty()) && (!requestList.isEmpty()))
        {
            openText.setVisibility(View.GONE);
        }

        if (!offers.isEmpty())
        {
            offerText.setVisibility(View.GONE);
        }

        if (!closedRequests.isEmpty())
        {
            closedText.setVisibility(View.GONE);
        }
    }

}
