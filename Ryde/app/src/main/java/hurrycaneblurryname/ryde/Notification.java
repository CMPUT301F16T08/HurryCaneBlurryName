package hurrycaneblurryname.ryde;

import java.util.ArrayList;

import hurrycaneblurryname.ryde.Model.Request.Request;
import hurrycaneblurryname.ryde.Model.User;
import hurrycaneblurryname.ryde.Model.UserHolder;


public class Notification {


    String message;
    String toUser;

    String fromUser;

    /**
     * Instantiates a new Notification.
     *
     * @param user the user
     */
    public Notification(String user, String message) {
        this.fromUser = user;
        this.message = message;
    }

    public void setToUser(User user) {
        this.toUser = user.getUsername();
    }

    public void setMessage(String message) {
        this.message = message;
    }

}



