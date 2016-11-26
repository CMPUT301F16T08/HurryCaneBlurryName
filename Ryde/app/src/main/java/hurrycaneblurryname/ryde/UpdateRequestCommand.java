package hurrycaneblurryname.ryde;

import hurrycaneblurryname.ryde.Model.Request.Request;

/**
 * Implementation of the Command Interface to update a specific request when online.
 * @author blaz
 * @date 11/25/2016
 */

public class UpdateRequestCommand extends Command{

    private Request request;

    public UpdateRequestCommand(Request request){
        this.request = request;
        isA = "UpdateRequestCommand";
    }

    @Override
    public void execute(){
        ElasticSearchRequestController.UpdateRequestsTask updateRequestsTask =
                new ElasticSearchRequestController.UpdateRequestsTask();
        updateRequestsTask.execute(request);
    }

}

