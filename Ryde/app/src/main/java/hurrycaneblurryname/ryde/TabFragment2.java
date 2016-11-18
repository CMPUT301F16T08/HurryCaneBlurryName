package hurrycaneblurryname.ryde;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import hurrycaneblurryname.ryde.Model.Request.Request;
import hurrycaneblurryname.ryde.Model.Request.RequestHolder;
import hurrycaneblurryname.ryde.Model.User;
import hurrycaneblurryname.ryde.Model.UserHolder;
import hurrycaneblurryname.ryde.R;
import hurrycaneblurryname.ryde.View.RideInfoActivity;

/**
 * Created by Zone on 2016/11/17.
 */
public class TabFragment2 extends Fragment {

    private User user;
    //Arrays
    private ArrayList<Request> requestList = new ArrayList<Request>();
    private ArrayList<Request> offers = new ArrayList<Request>();
    //ListViews
    private ListView offerView;
    //Adapters
    private ArrayAdapter<Request> offerViewAdapter;
    // Status TextView
    private TextView offerText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab_fragment_2, container, false);

        offerText = (TextView) view.findViewById(R.id.offerText);
        offerView = (ListView) view.findViewById(R.id.offerView);
        offerViewAdapter = new ArrayAdapter<Request>(getActivity(), R.layout.list_item, offers);
        offerView.setAdapter(offerViewAdapter);
        offerView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Get request to show and start RideInfo
                Request requestToPass = offers.get(position);
                RequestHolder.getInstance().setRequest(requestToPass);
                Intent info = new Intent(getActivity(), RideInfoActivity.class);
                startActivity(info);
            }
        });


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        requestList.clear();
        offers.clear();
        user = UserHolder.getInstance().getUser();

        ElasticSearchRequestController.GetRiderRequestsTask getMyRequests = new ElasticSearchRequestController.GetRiderRequestsTask();
        getMyRequests.execute(user.getUsername());
        try {
            requestList = getMyRequests.get();

        } catch (Exception e) {
            Log.i("ErrorGetRequest", "Failed to get open requests");
        }

        factorLists();
        changeTextStatus();
        offerViewAdapter.notifyDataSetChanged();
    }

    private void factorLists() {
        for (Request r : requestList) {
            String status = r.getStatus();
            if (status.equals("accepted")) {
                offers.add(r);
            }
        }
    }

    private void changeTextStatus() {
        if (offers.size()>0)
        {
            offerText.setVisibility(View.GONE);
        }
    }
}