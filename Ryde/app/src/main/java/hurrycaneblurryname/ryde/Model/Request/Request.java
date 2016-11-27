package hurrycaneblurryname.ryde.Model.Request;

import android.location.Location;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

import hurrycaneblurryname.ryde.DescriptionTooLongException;
import hurrycaneblurryname.ryde.LocationException;
import hurrycaneblurryname.ryde.Model.User;
import io.searchbox.annotations.JestId;

/**
 * Created by blaz on 10/11/2016.
 * Modified by cho8  11/21/2016
 * Version 1.2
 */
public class Request {

    @JestId
    private String id;
    private double[] from;
    private double[] to;
    private User rider;
    private User driver;
    private Double estimate;
    private Double distance;
    private String status;
    private String description;
    private ArrayList<User> offers;
    private Boolean DriverComplete;
    private Boolean RiderComplete;

    /**
     * Instantiates a new Request.
     *
     * @param rider the rider
     */
    public Request(User rider){
        this.rider = rider;
        this.status = "open";
        this.description = "";
        this.driver = new User("");
        this.estimate = 0.0;
        this.from = new double[] {0.0, 0.0};
        this.to = new double[] {0.0, 0.0};
        this.offers = new ArrayList<User>();
        this.DriverComplete = false;
        this.RiderComplete = false;
        this.distance = 0.0;
    }

    /**
     * Sets locations from LatLng objects used in map activity.
     *
     * @param from LatLng object
     * @param to   LatLng object
     * @throws LocationException when invalid location
     */
    public void setLocations(LatLng from, LatLng to) throws LocationException{
        this.from[0] = from.longitude;
        this.from[1] = from.latitude;
        this.to[0] = to.longitude;
        this.to[1] = to.latitude;

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
    public double[] getFrom() {
        return from;
    }

    /**
     * Gets to.
     *
     * @return the to
     */
    public double[] getTo() {
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
        if(driver.getUsername().isEmpty()){
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
     * Sets estimate.
     *
     * @param distance the distance between from and to
     */
    public void setDistance(Double distance) {
        this.distance = distance;
    }

    /**
     * Gets estimate.
     *
     * @return the distance
     */
    public Double getDistance() {
        return this.distance;
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
     * @return description Gets description.
     */
    public String getDescription() {
        return this.description;
    }

    /**
     * Gets Jest/ElasticSearch id. Do not print this to the app.
     *
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * Sets Jest/ElasticSearch id. String Id should be what is returned by the JestClient result.
     * Do not set this to anything else
     *
     * @param id the JestId
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Gets list of drivers who have issued a ride offer for this request
     *
     * @return the offers
     */
    public ArrayList<User> getOffers() {
        return offers;
    }

    /**
     * Set list of drivers who have issued a ride offer for this request
     *
     * @param offers the offers
     */
    public void setOffers(ArrayList<User> offers) {
        this.offers = offers;
    }

    public void addOffer(User newOffer) {
        this.offers.add(newOffer);
    }

    public void removeOffer(User offer) {
        this.offers.remove(offer);
    }

    /**
     * Gets Driver Complete.
     *
     * @return the DriverComplete
     */
    public Boolean getDriverComplete() {
        return DriverComplete;
    }

    /**
     * Gets Rider Complete.
     *
     * @return the RiderComplete
     */
    public Boolean getRiderComplete() {
        return RiderComplete;
    }

    /**
     * Sets Driver Complete.
     *
     */
    public void setDriverComplete() {
        this.DriverComplete = true;
    }

    /**
     * Sets Rider Complete.
     *
     */
    public void setRiderComplete() {
        this.RiderComplete = true;
    }

    @Override
    public String toString() {
        return this.description;
    }

    /**
     * compare requests for testing requests.
     *
     * @param other the other request
     */
    public int compareTo(Request other) {
        boolean x = (driver == other.getDriver());
        int i = 0;
        if (x){i = 1;}
        if (i != 0) return i;

        x = estimate == other.getEstimate();
        if (x){i = 1;}
        if (i != 0) return i;

        x = id == other.getId();
        if (x){i = 1;}

        return i;
    }

    @Override
    public boolean equals(Object other)
    {
        boolean sameSame = false;
        Log.i("Equals", "comparing");

        if (other != null && other instanceof Request)
        {
            sameSame = this.id.equals(((Request) other).getId());
        }

        return sameSame;
    }

}
