package hurrycaneblurryname.ryde.Model;

import android.util.Log;

import hurrycaneblurryname.ryde.ElasticSearchRequestController;

/**
 * Singleton class to temporary hold login user
 * Author: Chen
 * Reference: http://stackoverflow.com/questions/4878159/whats-the-best-way-to-share-data-between-activities
 */

public class UserHolder {
    private User user;
    public User getUser(){
        return user;
    }

    public void setUser(User userToStore){
        this.user = userToStore;
    }

    private static final UserHolder holder = new UserHolder();
    public static UserHolder getInstance() {return holder;}


}
