package hurrycaneblurryname.ryde;

import java.util.LinkedList;

/**
 * Singleton/Command Pattern.
 * Keeps track of commands executed in a linked list.
 */

public class CommandManager {
    private LinkedList<Command> historyList;
    private LinkedList<Command> redoList;

    // CommandManager is a singleton
    private static final CommandManager instance = new CommandManager();

    private CommandManager() {
        historyList = new LinkedList<Command>();
        redoList = new LinkedList<Command>();
    }

    // invoke a command and add it to history list
    public void invokeCommand( Command command ) {
        command.execute();
        if (command.isReversible()) {
            historyList.addFirst( command );
        }
        else {
            historyList.clear();
        }
        if (redoList.size() > 0) {
            redoList.clear();
        }
    }

    public void undo() {
        if (historyList.size() > 0) {
            Command command = historyList.removeFirst();
            command.unexecute();
            redoList.addFirst( command );
        }
    }

    public void redo() {
        if (redoList.size() > 0) {
            Command command = redoList.removeFirst();
            command.execute();
            historyList.addFirst( command );
        }
    }

    public static CommandManager getInstance() {
        return instance;
    }


}
