package hurrycaneblurryname.ryde.View;

import android.content.Intent;
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

import hurrycaneblurryname.ryde.ElasticSearchRequestController;
import hurrycaneblurryname.ryde.Model.Request.Request;
import hurrycaneblurryname.ryde.Model.Request.RequestHolder;
import hurrycaneblurryname.ryde.Model.User;
import hurrycaneblurryname.ryde.Model.UserHolder;
import hurrycaneblurryname.ryde.R;
import hurrycaneblurryname.ryde.TabFragment;

/**
 * Created by Zone on 2016/11/17.
 */
public class RiderTabFragment1 extends TabFragment {

    private User user;
    //ListViews
    private ListView openView;
    //Adapters


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab_fragment_1, container, false);
        filteredText = (TextView) view.findViewById(R.id.openText);
        openView = (ListView) view.findViewById(R.id.openView);
        filteredViewAdapter = new ArrayAdapter<Request>(getActivity(), R.layout.list_item, filteredRequests);
        openView.setAdapter(filteredViewAdapter);

        openView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Get request to show and start RideInfo
                Request requestToPass = filteredRequests.get(position);
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
        user = UserHolder.getInstance().getUser();
        requestList= new ArrayList<>(user.getRequestList());

        ElasticSearchRequestController.GetRiderRequestsTask getMyRequests = new ElasticSearchRequestController.GetRiderRequestsTask();
        getMyRequests.execute(user.getUsername());
        ArrayList newList;
        try {
            newList = getMyRequests.get();

            if (newList != null) {
                Log.i("newListGet", "Got a new List!!");
                requestList.clear();
                requestList.addAll(newList);
                user.setRequestList(requestList);

                ElasticSearchRequestController.UpdateUserTask updateUserTask =  new ElasticSearchRequestController.UpdateUserTask();
                updateUserTask.execute(user);


            } else {
                Log.i("NullListError", "Got a null list from ES");
            }

        } catch (Exception e) {
            Log.i("ErrorGetRequest", "Failed to get open requests");
        }
        factorLists("open");
        filteredViewAdapter.notifyDataSetChanged();
        changeTextStatus();
    }

}
