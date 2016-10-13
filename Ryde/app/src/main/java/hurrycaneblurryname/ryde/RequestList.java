package hurrycaneblurryname.ryde;

import java.util.ArrayList;

/**
 * Created by pocrn_000 on 10/11/2016.
 */
public class RequestList {

    /**
     * The Requests.
     */
    ArrayList<Request> requests = new ArrayList<Request>();

    /**
     * Add request.
     *
     * @param request the request
     */
    public void addRequest(Request request) {
        requests.add(request);
    }

    /**
     * Contains boolean.
     *
     * @param request the request
     * @return the boolean
     */
    public boolean contains(Request request) {
        return requests.contains(request);
    }

    /**
     * Remove request.
     *
     * @param request the request
     */
    public void removeRequest(Request request) {
        requests.remove(request);
    }

    /**
     * Gets request.
     *
     * @param i the
     * @return the request
     */
    public Request getRequest(int i) {
        return requests.get(i);
    }
}
