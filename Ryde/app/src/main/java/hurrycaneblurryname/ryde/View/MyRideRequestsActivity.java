package hurrycaneblurryname.ryde.View;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;

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
                Request requestToPass = offers.get(position);
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
                Request requestToPass = closedRequests.get(position);
                RequestHolder.getInstance().setRequest(requestToPass);
                Intent info = new Intent(MyRideRequestsActivity.this, RideInfoActivity.class);
                startActivity(info);

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        requestList.clear();
        openRequests.clear();
        offers.clear();
        closedRequests.clear();
        //TODO deleted requests take a while to be reflected in these lists.


        ElasticSearchRequestController.GetRiderRequestsTask getMyRequests = new ElasticSearchRequestController.GetRiderRequestsTask();
        getMyRequests.execute(user.getUsername());
        try {
            requestList = getMyRequests.get();

        } catch (Exception e) {
            Log.i("ErrorGetRequest", "Failed to get open requests");
        }

        // TODO dummy request need to remove
        User fakeuser = new User("dummyDriver");
        fakeuser.setEmail("fake@aaa.com");
        fakeuser.setPhone("1111111");

        Request fake1 = new Request(user);
        fake1.setEstimate(50.0);
        fake1.setId("dummy1");

        Request fake2 = new Request(user);
        fake2.setEstimate(30.0);
        fake2.setDriver(fakeuser);
        fake2.setStatus("accepted");
        fake2.setId("dummy2");

        Request fake3 = new Request(user);
        fake3.setEstimate(70.0);
        fake3.setDriver(fakeuser);
        fake3.setStatus("closed");
        fake3.setId("dummy3");

        try {
            fake1.setDescription("1.no driver");
            fake2.setDescription("2.has driver");
            fake3.setDescription("3.closed");
        } catch (Exception e) {
            Log.i("DescripError", "Description too long");
        }

        openRequests.add(fake1);
        offers.add(fake2);
        closedRequests.add(fake3);

        // factor all lists
        factorLists();

        openViewAdapter.notifyDataSetChanged();
        offerViewAdapter.notifyDataSetChanged();
        closedViewAdapter.notifyDataSetChanged();
        changeTextStatus();
        ListUtils.setDynamicHeight(openView);
        ListUtils.setDynamicHeight(closedView);
        ListUtils.setDynamicHeight(offerView);


    }


    @Override
    protected void onStart() {
        super.onStart();

        //Load in datas
        user = UserHolder.getInstance().getUser();

        openViewAdapter = new ArrayAdapter<Request>(this, R.layout.list_item, openRequests);
        openView.setAdapter(openViewAdapter);

        offerViewAdapter = new ArrayAdapter<Request>(this, R.layout.list_item, offers);
        offerView.setAdapter(offerViewAdapter);

        closedViewAdapter = new ArrayAdapter<Request>(this, R.layout.list_item, closedRequests);
        closedView.setAdapter(closedViewAdapter);

    }

    private void changeTextStatus(){
        openText = (TextView)findViewById(R.id.openText);
        offerText = (TextView)findViewById(R.id.offerText);
        closedText = (TextView)findViewById(R.id.closedText);

        if(openRequests.size()>0)
        {
            openText.setVisibility(View.GONE);
        }

        if (offers.size()>0)
        {
            offerText.setVisibility(View.GONE);
        }

        if (closedRequests.size()>0)
        {
            closedText.setVisibility(View.GONE);
        }
    }

    private void factorLists(){
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
    }

    public static class ListUtils {
        public static void setDynamicHeight(ListView mListView) {
            ListAdapter mListAdapter = mListView.getAdapter();
            if (mListAdapter == null) {
                // when adapter is null
                return;
            }
            int height = 0;
            int desiredWidth = View.MeasureSpec.makeMeasureSpec(mListView.getWidth(), View.MeasureSpec.UNSPECIFIED);
            for (int i = 0; i < mListAdapter.getCount(); i++) {
                View listItem = mListAdapter.getView(i, null, mListView);
                listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
                height += listItem.getMeasuredHeight();
            }
            ViewGroup.LayoutParams params = mListView.getLayoutParams();
            params.height = height + (mListView.getDividerHeight() * (mListAdapter.getCount() - 1));
            mListView.setLayoutParams(params);
            mListView.requestLayout();
        }
    }

}
