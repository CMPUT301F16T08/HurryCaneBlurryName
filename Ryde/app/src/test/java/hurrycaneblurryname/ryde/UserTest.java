package hurrycaneblurryname.ryde;

import junit.framework.TestCase;

/**
 * <h1>User Test</h1>
 * Tests User Class
 *
 * @author Blaz Pocrnja
 * @version 1.0
 * @since 10 /12/2016
 */
public class UserTest extends TestCase{

    /**
     * Test add request.
     * <p>
     *     <b>Involved in</b>
     *     <ul>
     *         <li>UC-1</li>
     *         <li>UC-2</li>
     *     </ul>
     * </p>
     */
    public void testAddRequest(){
        User user = new User();
        Request request = new Request();
        user.addRequest(request);

        assertTrue("Request Was Not Added to User Request List!",user.hasRequest(request));
    }
}
