package hurrycaneblurryname.ryde;

import junit.framework.TestCase;

import hurrycaneblurryname.ryde.Model.Request.Request;
import hurrycaneblurryname.ryde.Model.Request.RequestList;
import hurrycaneblurryname.ryde.Model.User;

/**
 * <h1>RequestList Test</h1>
 * Tests RequestList Class
 *
 * @author Blaz Pocrnja
 * @version 1.0
 * @since 10/12/2016
 */
public class RequestListTest extends TestCase{

    /**
     * Test add request.
     * <p>
     *     <b>Involved in</b>
     *     <ul>
     *         <li>UC-1</li>
     *     </ul>
     * </p>
     */
    public void testAddRequest(){

        RequestList requests = new RequestList();
        User user = new User("user");
        Request request = new Request(user);
        requests.addRequest(request);

        //rtEquals(requests.getRequest(0),request);
        int z = requests.getRequest(0).compareTo(request);
        boolean x = false;
        if (z == 1){x = true;}
        assertTrue("Test Request Not Contained!" , x);
    }

    /**
     * Test remove request.
     * <p>
     *     <b>Involved in</b>
     *     <ul>
     *         <li>UC-3</li>
     *     </ul>
     * </p>
     */
    public void testRemoveRequest(){
        RequestList requests = new RequestList();
        User user = new User("user");
        Request request = new Request(user);
        requests.removeRequest(request);

        assertFalse("Test Request Still Contained!" , requests.contains(request));
    }
}
