package hurrycaneblurryname.ryde.Model.Request;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

import hurrycaneblurryname.ryde.DescriptionTooLongException;
import hurrycaneblurryname.ryde.LocationException;
import hurrycaneblurryname.ryde.Model.User;
import io.searchbox.annotations.JestId;

/**
 * Created by pocrn_000 on 10/11/2016.
 */
public class Request {

    @JestId
    private String id;
    private LatLng from;
    private LatLng to;
    private User rider;
    private User driver;
    private Double estimate;
    private String status;
    private String description;

    /**
     * Instantiates a new Request.
     */
    public Request(User rider){
        this.rider = rider;
        this.status = "open";
        this.description = "";
        this.driver = new User("");
    }

    /**
     * Sets locations.
     *
     * @param from the from
     * @param to   the to
     * @throws LocationException the location exception
     */
    public void setLocations(LatLng from, LatLng to) throws LocationException{
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
    public LatLng getFrom() {
        return from;
    }

    /**
     * Gets to.
     *
     * @return the to
     */
    public LatLng getTo() {
        return to;
    }

    /**
     * get Driver.
     *
     * @return rider the rider
     */
    public User getRider() {
        return rider;
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
    public void setDriver(User driver) {
        this.driver = driver;
    }

    /**
     * Gets driver.
     *
     * @return the driver
     */
    public User getDriver() {
        return driver;
    }

    /**
     * Sets estimate.
     *
     * @param estimate the estimate
     */
    public void setEstimate(Double estimate) {
        this.estimate = estimate;
    }

    /**
     * Gets estimate.
     *
     * @return the estimate
     */
    public Double getEstimate() {
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
     * Sets status in lowercase.
     *
     * @param status the status
     */
    public void setStatus(String status) {
        this.status = status.toLowerCase();
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

    /**
     * Get description.
     *
     * @return description
     * Gets description.
     *
     * @return the description
     */
    public String getDescription() {
        return this.description;
    }

    /**
     * toString method
     * Gets Jest/ElasticSearch id.
     *
     * @return the id
     */
    @Override
    public String toString() {
        return id;
    }

    public String getId() {
        return id;
    }

    /**
     * Sets Jest/ElasticSearch id. String Id should be what is returned by the JestClient result.
     *
     * @param id the id
     */
    public void setId(String id) {
        this.id = id;
    }

}
