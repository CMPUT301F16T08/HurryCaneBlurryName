package hurrycaneblurryname.ryde.Model.Request;

import android.util.Log;

import java.util.ArrayList;

import hurrycaneblurryname.ryde.ElasticSearchRequestController;
import hurrycaneblurryname.ryde.Model.User;
import io.searchbox.annotations.JestId;

/**
 * Created by cho on 2016-11-21.
 */

public class ESRequest implements Requestable {

    @JestId
    private String id;
    private double[] from;
    private double[] to;
    private User rider;
    private User driver;
    private Double estimate;
    private String status;
    private String description;
    private ArrayList<User> offers;

    @Override
    public void refresh(String id) {
        ElasticSearchRequestController.GetRequestTask refreshRequestTask = new ElasticSearchRequestController.GetRequestTask();
        refreshRequestTask.execute(id);
        try {
//            this = refreshRequestTask.get().get(0);
        } catch (Exception e) {
            Log.i("RefreshError", "Unable to get copy from ElasticSearch");
        }
    }
}
