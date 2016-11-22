package hurrycaneblurryname.ryde;

import hurrycaneblurryname.ryde.Model.Request.Request;

/**
 * Implementation of the Command Interface to add a request.
 */

public class AddRequestCommand extends Command{

    private Request request;

    public AddRequestCommand(Request request){
        this.request = request;
        isA = "AddRequestCommand";
    }

    @Override
    public void execute(){
        ElasticSearchRequestController.AddRequestsTask addRequestsTask =
                new ElasticSearchRequestController.AddRequestsTask();
        addRequestsTask.execute(request);
    }

}
