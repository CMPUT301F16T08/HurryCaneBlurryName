package hurrycaneblurryname.ryde.Model.Request;

import hurrycaneblurryname.ryde.Model.User;

/**
 * Singleton class to temporary hold request related user
 * Author: Chen
 * Reference: http://stackoverflow.com/questions/4878159/whats-the-best-way-to-share-data-between-activities
 */

public class RequestUserHolder {
    private User user;
    public User getUser(){
        return user;
    }

    public void setUser(User userToStore){
        this.user = userToStore;
    }

    private static final RequestUserHolder holder = new RequestUserHolder();
    public static RequestUserHolder getInstance() {return holder;}
}
