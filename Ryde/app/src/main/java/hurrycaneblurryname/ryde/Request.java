package hurrycaneblurryname.ryde;

import android.location.Location;

import java.util.ArrayList;

/**
 * Created by pocrn_000 on 10/11/2016.
 */

public class Request {
    
    private Location from;
    private Location to;
    private Driver driver;
    private String estimate;
    private String status;
    private String description;

    public Request(){
        this.status = "open";
        this.description = "";
    }

    public void setLocations(Location from, Location to) throws LocationException{
        this.from = from;
        this.to = to;

        //TODO Determine when to throw location exception
        if(false){
            throw new LocationException();
        }
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

    public boolean hasKeyword(String keyword) {
        return description.toLowerCase().contains(keyword.toLowerCase());
    }

    public void setDescription(String description) throws DescriptionTooLongException{
        this.description = description;

        //TODO Determine max length of description
        if(false){
            throw new DescriptionTooLongException();
        }
    }
}
