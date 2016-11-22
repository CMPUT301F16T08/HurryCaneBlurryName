package hurrycaneblurryname.ryde;

import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

import hurrycaneblurryname.ryde.Model.Request.Request;

import static hurrycaneblurryname.ryde.R.id.openText;

/**
 * Created by cho on 2016-11-21.
 * Refactor of Tab fragments on MyRideRequests
 */
public class TabFragment extends Fragment {

    protected ArrayList<Request> filteredRequests = new ArrayList<Request>();
    protected TextView filteredText;
    protected ArrayList<Request> requestList = new ArrayList<Request>();

    protected void factorLists(String searchStatus){
        filteredRequests.clear();
        for (Request r : requestList ) {
            String status = r.getStatus();
            if(status.equals(searchStatus)) {
                filteredRequests.add(r);
            }
        }
    }

    protected void changeTextStatus(){
        if(filteredRequests.size()>0)
        {
            filteredText.setVisibility(View.GONE);
        }
    }
}
