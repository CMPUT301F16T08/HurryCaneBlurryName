package hurrycaneblurryname.ryde;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.util.ArrayList;

/**
 * Singleton/Command Pattern.
 * Keeps track of commands executed in a linked list.
 * Stores commands in a file to execute later if offline
 */

public class CommandManager {
    private ArrayList<Command> offlineList;
    private static final String FILENAME = "commands.sav";

    // CommandManager is a singleton
    private static final CommandManager instance = new CommandManager();

    private CommandManager() {
        offlineList = new ArrayList<Command>();
    }

    // invoke a command if online/otherwise add to list
    public void invokeCommand(Context context, Command command ) {
        CommandFileManager commandFileManager = new CommandFileManager(context);
        offlineList = commandFileManager.loadFromFile(FILENAME);

        if(isNetworkAvailable(context)){

            //Check if there are any other commands in queue
            if(!offlineList.isEmpty()){
                invokeAll(context);
            }
            command.execute();
        }
        else{
            offlineList.add(command);
        }

        //Save List
        commandFileManager.saveInFile(offlineList,FILENAME);
    }

    public void invokeAll(Context context){
        CommandFileManager commandFileManager = new CommandFileManager(context);
        offlineList = commandFileManager.loadFromFile(FILENAME);
        if(isNetworkAvailable(context)) {
            while (!offlineList.isEmpty()) {
                offlineList.get(0).execute();
                offlineList.remove(0);
            }


        }

        //Save List
        commandFileManager.saveInFile(offlineList, FILENAME);
    }

    //Check if offline
    //Source: http://stackoverflow.com/questions/4238921/detect-whether-there-is-an-internet-connection-available-on-android
    //Date Accessed: 11/12/2016
    //Author: Alexandre Jasmin
    //TODO MAKE NETWORK CHECKING CLASS
    private boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


    public static CommandManager getInstance() {
        return instance;
    }


}
