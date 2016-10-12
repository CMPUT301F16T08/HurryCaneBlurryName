package hurrycaneblurryname.ryde;

import java.util.ArrayList;

/**
 * Created by pocrn_000 on 10/11/2016.
 */

public class RequestList {

    ArrayList<Request> requests = new ArrayList<Request>();

    public void addRequest(Request request) {
        requests.add(request);
    }

    public boolean contains(Request request) {
        return requests.contains(request);
    }

    public void removeRequest(Request request) {
        requests.remove(request);
    }
}
