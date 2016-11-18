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
import hurrycaneblurryname.ryde.View.RideInfoActivity;

/**
 * Created by Zone on 2016/11/17.
 */
public class TabFragment3 extends Fragment {

    private User user;
    //Arrays
    private ArrayList<Request> requestList = new ArrayList<Request>();
    private ArrayList<Request> closedRequests = new ArrayList<Request>();
    //ListViews
    private ListView closedView;
    //Adapters
    private ArrayAdapter<Request> closedViewAdapter;
    // Status TextView
    private TextView closedText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab_fragment_3, container, false);

        closedText = (TextView) view.findViewById(R.id.closedText);
        closedView = (ListView) view.findViewById(R.id.closedView);
        closedViewAdapter = new ArrayAdapter<Request>(getActivity(), R.layout.list_item, closedRequests);
        closedView.setAdapter(closedViewAdapter);
        closedView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Get request to show and start RideInfo
                Request requestToPass = closedRequests.get(position);
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
        closedRequests.clear();
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
        closedViewAdapter.notifyDataSetChanged();
    }

    private void factorLists() {
        for (Request r : requestList) {
            String status = r.getStatus();
            if (status.equals("closed")) {
                closedRequests.add(r);
            }
        }
    }

    private void changeTextStatus() {
        if (closedRequests.size()>0)
        {
            closedText.setVisibility(View.GONE);
        }
    }
}