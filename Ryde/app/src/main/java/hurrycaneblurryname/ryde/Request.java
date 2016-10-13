package hurrycaneblurryname.ryde;

import android.location.Location;

/**
 * Created by pocrn_000 on 10/11/2016.
 */

public class Request {
    
    private Location from;
    private Location to;
    private Driver driver;
    private String estimate;
    private String status;

    public Request(){
        this.status = "open";
    }

    public void setLocations(Location from, Location to) {
        this.from = from;
        this.to = to;
    }

    public Location getFrom() {
        return from;
    }

    public Location getTo() {
        return to;
    }

    public boolean hasDriver() {
        if(driver == null){
            return false;
        }
        return true;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }

    public Driver getDriver() {
        return driver;
    }

    public void setEstimate(String estimate) {
        this.estimate = estimate;
    }

    public String getEstimate() {
        return estimate;
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
