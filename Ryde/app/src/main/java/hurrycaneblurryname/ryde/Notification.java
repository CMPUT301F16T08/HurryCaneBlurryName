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
    String requestString;

    /**
     * Instantiates a new Notification.
     *
     * @param fromUser the user
     */
    public Notification(String fromUser, String requestString) {
        this.fromUser = fromUser;
        this.requestString = requestString;
        this.message = "";
    }

    public void setToUser(User user) {
        this.toUser = user.getUsername();
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void compileMessage(String s) {
        if (s.equals("accept")) {
            this.message = fromUser + " is interested:\n" + requestString;

        } else if (s.equals("confirm")) {
            this.message = fromUser + " confirmed your offer:\n" + requestString;
        }

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



