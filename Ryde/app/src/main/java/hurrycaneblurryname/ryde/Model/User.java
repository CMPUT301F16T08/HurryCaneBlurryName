package hurrycaneblurryname.ryde.Model;

import android.util.Log;

import java.util.ArrayList;

import hurrycaneblurryname.ryde.Model.Request.Request;
import hurrycaneblurryname.ryde.Model.Request.RequestList;
import io.searchbox.annotations.JestId;

/**
 * Created by pocrn_000 on 10/12/2016.
 */
public class User {


    @JestId
    private String id;

    private String username;
    private String password;
    private String phone;
    private String email;
    private String role;
    private String cardNumber;
    protected ArrayList<Request> requests;
    private String vehicleMake;
    private String vehicleModel;
    private int vehicleYear;

    /**
     * Instantiates a new User.
     *
     * @param username the username
     */
    public User(String username){
        requests = new ArrayList<>();
        this.username = username;
    }

    /**
     * Gets id to be used in elasticsearch
     *
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * Sets id to be used in elasticsearch
     *
     * @param id the id
     */
    public void setId(String id) {
        this.id = id;
    }


    /**
     * Add request.
     *
     * @param request the request
     */
    public void addRequest(Request request) {
        requests.add(request);
    }

    /**
     * Has request boolean.
     *
     * @param request the request
     * @return the boolean
     */
    public boolean hasRequest(Request request) {
        return requests.contains(request);
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

    /**
     * Remove request.
     *
     * @param request the request
     */
    public void removeRequest(Request request) {
        requests.remove(request);
    }

    /**
     * Gets username.
     *
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets phone.
     *
     * @param phone the phone
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * Gets phone.
     *
     * @return the phone
     */
    public String getPhone() {
        return phone;
    }

    /**
     * Sets email.
     *
     * @param email the email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Gets email.
     *
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public ArrayList<Request> getRequestList() {
        return requests;
    }

    public void setRequestList(ArrayList<Request> requestList) {
        this.requests = requestList;
    }

    /**
     * Gets vehicle year.
     *
     * @return the vehicleYear
     */
    public int getVehicleYear(){return vehicleYear;}
    /**
     * Gets vehicle Model.
     *
     * @return the vehicleModel
     */
    public String getVehicleModel(){return vehicleModel;}
    /**
     * Gets vehicle Make.
     *
     * @return the vehicleMake
     */
    public String getVehicleMake(){return vehicleMake;}

    /**
     * Set user Model.
     *
     * @param year the year of the vehicle
     */
    public void setVehicleYear(int year){
        this.vehicleYear = year;
    }
    /**
     * Set user Model.
     *
     * @param Model the Model of the vehicle
     */
    public void setVehicleModel(String Model){
        this.vehicleModel = Model;
    }
    /**
     * Set user Model.
     *
     * @param Make the Make of the vehicle
     */
    public void setVehicleMake(String Make){
        this.vehicleMake = Make;
    }


    @Override
    public String toString() {
        return username;
    }

    @Override
    public boolean equals(Object other)
    {
        boolean sameSame = false;
        Log.i("Equals", "comparing");

        if (other != null && other instanceof User)
        {
            sameSame = this.id.equals(((User) other).getId());
        }

        return sameSame;
    }
}

