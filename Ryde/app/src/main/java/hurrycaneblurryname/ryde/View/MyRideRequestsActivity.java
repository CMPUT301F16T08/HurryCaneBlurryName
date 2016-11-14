package hurrycaneblurryname.ryde.View;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import hurrycaneblurryname.ryde.R;

/**
 * The type My Ride Info activity.
 * Author: Chen
 * Storyboard by: Blaz
 * Code Reuse from: asg1-Blaz
 */

public class MyRideRequestsActivity extends AppCompatActivity {

    //Arrays
    //TO-DO
    /*
    private ArrayList<> requestList = new ArrayList<>();
    private ArrayList<> openRequests = new ArrayList<>();
    private ArrayList<> offers = new ArrayList<>();
    private ArrayList<> closedRequests = new ArrayList<>();
    */

    //ListViews
    private ListView openView;
    private ListView offerView;
    private ListView closedView;

    //Adapters
    /*
    private ArrayAdapter<Request> openViewAdapter;
    private ArrayAdapter<float> offerViewAdapter;
    private ArrayAdapter<Request> closedViewAdapter;
    */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_ride_requests);
        setTitle("My Ride Request");

        openView = (ListView) findViewById(R.id.openView);
        openView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Get habit name
                String str = parent.getItemAtPosition(position).toString();

            }
        });

        offerView = (ListView) findViewById(R.id.offerView);
        offerView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Get habit name
                String str = parent.getItemAtPosition(position).toString();

            }
        });

        closedView = (ListView) findViewById(R.id.closedView);
        closedView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Get habit name
                String str = parent.getItemAtPosition(position).toString();

            }
        });

    }

    @Override

    protected void onStart() {
        super.onStart();

        //Load in datas
        /*
        openViewAdapter = new ArrayAdapter<Habit>(this, R.layout.list_item, openRequests);
        openView.setAdapter(openViewAdapter);

        offerViewAdapter = new ArrayAdapter<Habit>(this, R.layout.list_item, offers);
        offerView.setAdapter(offerViewAdapter);

        closedViewAdapter = new ArrayAdapter<Habit>(this, R.layout.list_item, closedRequests);
        closedView.setAdapter(closedViewAdapter);

        ListUtils.setDynamicHeight(openView);
        ListUtils.setDynamicHeight(offerView);
        ListUtils.setDynamicHeight(closedView);
        */

    }

}
