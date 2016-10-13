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

    /**
     * Instantiates a new Request.
     */
    public Request(){
        this.status = "open";
        this.description = "";
    }

    /**
     * Sets locations.
     *
     * @param from the from
     * @param to   the to
     * @throws LocationException the location exception
     */
    public void setLocations(Location from, Location to) throws LocationException{
        this.from = from;
        this.to = to;

        //TODO Determine when to throw location exception
        if(false){
            throw new LocationException();
        }
    }

    /**
     * Gets from.
     *
     * @return the from
     */
    public Location getFrom() {
        return from;
    }

    /**
     * Gets to.
     *
     * @return the to
     */
    public Location getTo() {
        return to;
    }

    /**
     * Has driver boolean.
     *
     * @return the boolean
     */
    public boolean hasDriver() {
        if(driver == null){
            return false;
        }
        return true;
    }

    /**
     * Sets driver.
     *
     * @param driver the driver
     */
    public void setDriver(Driver driver) {
        this.driver = driver;
    }

    /**
     * Gets driver.
     *
     * @return the driver
     */
    public Driver getDriver() {
        return driver;
    }

    /**
     * Sets estimate.
     *
     * @param estimate the estimate
     */
    public void setEstimate(String estimate) {
        this.estimate = estimate;
    }

    /**
     * Gets estimate.
     *
     * @return the estimate
     */
    public String getEstimate() {
        return estimate;
    }


    /**
     * Gets status.
     *
     * @return the status
     */
    public String getStatus() {
        return status;
    }

    /**
     * Sets status.
     *
     * @param status the status
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * Has keyword boolean.
     *
     * @param keyword the keyword
     * @return the boolean
     */
    public boolean hasKeyword(String keyword) {
        return description.toLowerCase().contains(keyword.toLowerCase());
    }

    /**
     * Sets description.
     *
     * @param description the description
     * @throws DescriptionTooLongException the description too long exception
     */
    public void setDescription(String description) throws DescriptionTooLongException{
        this.description = description;

        //TODO Determine max length of description
        if(false){
            throw new DescriptionTooLongException();
        }
    }
}
