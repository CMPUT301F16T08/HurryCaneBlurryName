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

        Notification n =  new Notification(UserHolder.getInstance().getUser().getUsername(),"A driver is interested in your request!");
        n.setToUser(toUser);

        ElasticSearchRequestController.AddNotifTask addNotifTask = new ElasticSearchRequestController.AddNotifTask();
        addNotifTask.execute(n);
    }

    public void sendConfirmNotification(User toUser) {
        Notification n =  new Notification(UserHolder.getInstance().getUser().getUsername(), "A rider has accepted your interest!");
        n.setToUser(toUser);
    }

    public static ArrayList<String> updateNotifs() {

        ElasticSearchRequestController.GetMyNotifsTask getNotifTask = new ElasticSearchRequestController.GetMyNotifsTask();
        getNotifTask.execute(UserHolder.getInstance().getUser().getUsername());
        ArrayList<Notification> notifsArray = new ArrayList<>();
        try {
            notifsArray = getNotifTask.get();
        } catch (Exception e) {
            Log.i("ErrorNotif", "Getting error in notification manager");
        }

        ArrayList<String> notifStrings = new ArrayList<>();
        for (Notification n : notifsArray) {
            notifStrings.add(n.getMessage());
        }

        // Once notifs received, don't care about it
        // Delete from ElasticSearch

        ElasticSearchRequestController.DeleteNotifsTask deleteNotifTask = new ElasticSearchRequestController.DeleteNotifsTask();
        deleteNotifTask.execute(notifsArray.toArray(new Notification[notifsArray.size()]));

        return notifStrings;
    }

    public static void dismissNotification (Notification n) {


    }
}
