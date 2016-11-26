package hurrycaneblurryname.ryde;

import java.util.ArrayList;

import hurrycaneblurryname.ryde.Model.Request.Request;
import hurrycaneblurryname.ryde.Model.User;
import hurrycaneblurryname.ryde.Model.UserHolder;
import io.searchbox.annotations.JestId;


public class Notification {

    @JestId
    String id;
    String message;
    String toUser;
    String fromUser;

    /**
     * Instantiates a new Notification.
     *
     * @param fromUser the user
     */
    public Notification(String fromUser, String message) {
        this.fromUser = fromUser;
        this.message = message;
    }

    public void setToUser(User user) {
        this.toUser = user.getUsername();
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() { return this.message; }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() { return this.message; }
}



