package hurrycaneblurryname.ryde;

/**
 * Created by pocrn_000 on 10/12/2016.
 */

public class User {

    private RequestList requests;

    public User(){
        requests = new RequestList();
    }

    public void addRequest(Request request) {
        requests.addRequest(request);
    }

    public boolean hasRequest(Request request) {
        return requests.contains(request);
    }
}
