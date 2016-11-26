package hurrycaneblurryname.ryde;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

/**
 * Created by pocrn_000 on 11/25/2016.
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
