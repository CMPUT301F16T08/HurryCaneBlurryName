package hurrycaneblurryname.ryde;

import android.test.ActivityInstrumentationTestCase2;

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
 * @since 2016-10-12
 */
public class RequestListTest extends ActivityInstrumentationTestCase2<MainActivity> {

    public RequestListTest() {
        super(MainActivity.class);
    }

    public void testAddRequest(){
        RequestList requests = new RequestList();
        Request request = new Request();
        requests.addRequest(request);
        assertTrue("Test Request not contained!",requests.contains(request));
    }


}
