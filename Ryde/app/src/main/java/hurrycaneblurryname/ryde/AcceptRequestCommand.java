package hurrycaneblurryname.ryde;

import hurrycaneblurryname.ryde.Model.Request.Request;

/**
 * Implementation of the Command Interface to accept requests as a driver
 * @author blaz
 * @date 11/25/2016
 */

public class AcceptRequestCommand extends Command{

    private Request request;

    public AcceptRequestCommand(Request request){
    }

    @Override
    public void execute(){
    }

}

