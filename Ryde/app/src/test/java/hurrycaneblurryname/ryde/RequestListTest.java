package hurrycaneblurryname.ryde;

import junit.framework.TestCase;

/**
 * <h1>RequestList Test</h1>
 * Tests RequestList Class
 * <p>
 *     <b>Use Cases</b>
 *     <ul>
 *         <li>UC-1</li>
 *     </ul>
 * </p>
 * @author Blaz Pocrnja
 * @version 1.0
 * @since 10/12/2016
 */
public class RequestListTest extends TestCase{

    public void testAddRequest(){
        RequestList requests = new RequestList();
        Request request = new Request();
        requests.addRequest(request);
        assertTrue("Test Request Not Contained!",requests.contains(request));
    }
}
