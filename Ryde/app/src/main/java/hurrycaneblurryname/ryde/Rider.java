package hurrycaneblurryname.ryde;

/**
 * Created by pocrn_000 on 10/12/2016.
 */
public class Rider extends User{

    protected RequestList rideRequests;

    /**
     * Instantiates a new Rider.
     *
     * @param username the username
     */
    public Rider(String username){
        super(username);
        rideRequests = new RequestList();
    }

    public void addRideRequest(Request request) {
        rideRequests.addRequest(request);
    }

    public boolean hasRideRequest(Request request) {
        return rideRequests.contains(request);
    }
}
