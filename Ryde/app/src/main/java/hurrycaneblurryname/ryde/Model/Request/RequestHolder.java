package hurrycaneblurryname.ryde.Model.Request;

import java.util.ArrayList;

/**
 * Singleton class to temporary hold login user
 * Author: Chen
 * Reference: http://stackoverflow.com/questions/4878159/whats-the-best-way-to-share-data-between-activities
 * November 14, 2016
 * Cristian, John Difool
 */
public class RequestHolder {
    private Request request;
    public Request  getRequest(){
            return request;
        }

    public void setRequest(Request requestToStore){
            this.request = requestToStore;
        }

    private static final RequestHolder holder = new RequestHolder();
    public static RequestHolder getInstance() {return holder;}

}
