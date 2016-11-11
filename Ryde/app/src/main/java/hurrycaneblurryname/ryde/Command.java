package hurrycaneblurryname.ryde;

/**
 * Interface for classes that use a command pattern.
 * @author blaz
 */
public interface Command {
    public void execute();
    public void unexecute();
    public boolean isReversible();
}
