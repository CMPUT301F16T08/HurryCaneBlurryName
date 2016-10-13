package hurrycaneblurryname.ryde;

import android.location.Location;

import junit.framework.TestCase;

/**
 * <h1>Request Test</h1>
 * Tests Request Class
 *
 * @author Blaz Pocrnja
 * @version 1.0
 * @since 10/12/2016
 */
public class RequestTest extends TestCase{

    /**
     * Test set locations.
     * <p>
     *     <b>Involved in</b>
     *     <ul>
     *         <li>UC-1</li>
     *     </ul>
     * </p>
     */
    public void testSetLocations(){
        Location from = new Location("from");
        Location to = new Location("to");
        Request request = new Request();
        request.setLocations(from,to);

        assertTrue("From Location Not Set!", request.getFrom().equals(from));
        assertTrue("To Location Not Set!", request.getTo().equals(to));
    }

    /**
     * Test driver setters and getters.
     * <p>
     *     <b>Involved in</b>
     *     <ul>
     *         <li>UC-5</li>
     *     </ul>
     * </p>
     */
    public void testDriver(){
        Request request = new Request();

        assertFalse("There Is a Driver!", request.hasDriver());

        Driver driver = new Driver();
        request.addDriver(driver);

        assertTrue("No Driver Added!", request.hasDriver());
        assertTrue("Driver Does Not Match!",request.getDriver().equals(driver));
    }
}