package hurrycaneblurryname.ryde;

import junit.framework.TestCase;

/**
 * Created by pocrn_000 on 10/11/2016.
 */

public class RequestListTest extends TestCase{

    public void testAddRequest(){
        RequestList requests = new RequestList();
        Request request = new Request();
        requests.addRequest(request);
        assertTrue("Test Request not contained!",requests.contains(request));
    }
}
