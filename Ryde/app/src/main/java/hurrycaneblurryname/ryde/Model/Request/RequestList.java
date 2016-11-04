package hurrycaneblurryname.ryde.Model.Request;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by pocrn_000 on 10/11/2016.
 */
public class RequestList implements Iterable<Request>{

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

    public void addAllKeyword(RequestList requests, String keyword) {
        for(Request i : requests){
            if(i.hasKeyword(keyword)){
                this.requests.add(i);
            }
        }
    }

    @Override
    public Iterator<Request> iterator() {
        return requests.iterator();
    }

    public int getSize(){
        return requests.size();
    }
}
