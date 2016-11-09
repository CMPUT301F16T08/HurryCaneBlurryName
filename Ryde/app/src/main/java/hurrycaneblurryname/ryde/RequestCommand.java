package hurrycaneblurryname.ryde;

import hurrycaneblurryname.ryde.Model.Request.Request;

/**
 * Created by pocrn_000 on 11/9/2016.
 */

public class RequestCommand implements Command{

    private Request request;

    public RequestCommand(Request request){
        this.request = request;
    }

    @Override
    public void execute(){

    }

    @Override
    public void unexecute(){

    }

    @Override
    public boolean isReversible(){

    }
}
