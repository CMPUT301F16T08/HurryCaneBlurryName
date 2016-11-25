package hurrycaneblurryname.ryde;

import android.util.Log;

import java.util.ArrayList;

import hurrycaneblurryname.ryde.Model.User;
import hurrycaneblurryname.ryde.Model.UserHolder;

/**
 * Created by cho on 2016-11-24.
 */

public class NotificationManager {

    public static final void sendAcceptNotification(User toUser) {
        Notification n =  new Notification(toUser.getUsername(), "A driver is interested in your request!");
        n.setToUser(toUser);

        ElasticSearchRequestController.AddNotifTask addNotifTask = new ElasticSearchRequestController.AddNotifTask();
        addNotifTask.execute(n);
    }

    public void sendConfirmNotification(User toUser) {
        Notification n =  new Notification(toUser.getUsername(), "A rider has accepted your interest!");
        n.setToUser(toUser);
    }

    public static final ArrayList<Notification> getUpdate() {

        ElasticSearchRequestController.GetMyNotifsTask getNotifTask = new ElasticSearchRequestController.GetMyNotifsTask();
        getNotifTask.execute(UserHolder.getInstance().getUser());
        ArrayList<Notification> notifs = new ArrayList<>();
        try {
            notifs= getNotifTask.get();
        } catch (Exception e) {
            Log.i("ErrorNotif", "Getting error in notification manager");
        }

        return notifs;
    }
}
