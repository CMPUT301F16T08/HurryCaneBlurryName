package hurrycaneblurryname.ryde;

import android.util.Log;

import java.util.ArrayList;

import hurrycaneblurryname.ryde.Model.User;
import hurrycaneblurryname.ryde.Model.UserHolder;

/**
 * Created by cho on 2016-11-24.
 */

public class NotificationManager {


    public static final void sendAcceptNotification(User toUser, String requestString) {

        Notification n =  new Notification(UserHolder.getInstance().getUser().getUsername(), requestString);
        n.setToUser(toUser);
        n.compileMessage("accept");

        ElasticSearchRequestController.AddNotifTask addNotifTask = new ElasticSearchRequestController.AddNotifTask();
        addNotifTask.execute(n);
    }

    public static final void sendConfirmNotification(User toUser, String requestString) {
        Notification n =  new Notification(UserHolder.getInstance().getUser().getUsername(), requestString);
        n.setToUser(toUser);
        n.compileMessage("confirm");
        ElasticSearchRequestController.AddNotifTask addNotifTask = new ElasticSearchRequestController.AddNotifTask();
        addNotifTask.execute(n);
    }

    public static ArrayList<Notification> updateNotifs() {

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


        return notifsArray;
    }

    public static void dismissNotification (Notification n) {
        ElasticSearchRequestController.DeleteNotifsTask deleteNotifTask = new ElasticSearchRequestController.DeleteNotifsTask();
        deleteNotifTask.execute(n);

    }
}
