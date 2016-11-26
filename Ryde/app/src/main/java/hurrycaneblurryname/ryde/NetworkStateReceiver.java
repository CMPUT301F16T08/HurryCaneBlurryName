package hurrycaneblurryname.ryde;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Network Listener to Run in the background for any activity.
 * Clears the offline queue of commands if connected to internet.
 * @author blaz
 * @date 11/25/2016.
 * Source: http://stackoverflow.com/questions/25678216/android-internet-connectivity-change-listener
 * Date Accessed: 11/25/2016
 * Author: Cjames
 */
public class NetworkStateReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        int status = NetworkUtil.getConnectivityStatusString(context);
        //Check if online
        if(status!=NetworkUtil.NETWORK_STATUS_NOT_CONNECTED){
            //Send anything in offline queue
            CommandManager commandManager = CommandManager.getInstance();
            commandManager.invokeAll(context);
        }
    }
}
