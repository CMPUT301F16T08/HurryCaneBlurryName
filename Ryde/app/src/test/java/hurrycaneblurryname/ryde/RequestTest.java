package hurrycaneblurryname.ryde;
import com.google.android.gms.maps.model.LatLng;
import junit.framework.TestCase;
import java.util.Arrays;
import hurrycaneblurryname.ryde.Model.Driver;
import hurrycaneblurryname.ryde.Model.Request.Request;
import hurrycaneblurryname.ryde.Model.User;

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
        LatLng from = new LatLng(-53.8921983, 152.1984271);
        LatLng to = new LatLng(-54.0, 153.130491);
        double[] From = {-53.8921983, 152.1984271};
        double[] To = {-54.0, 153.130491};
        User user = new User("user");
        Request request = new Request(user);
        try{
            request.setLocations(from , to);
        }
        catch(LocationException e){
            assertTrue("LocationException Thrown!" , false);
        }
        double[] x = request.getFrom();
        assertEquals(From[0],x[0]);
        //assertTrue("From Location Not Set!" , Arrays.equals(request.getFrom(),From));
        //assertTrue("To Location Not Set!" , Arrays.equals(request.getTo(),To));
    }

    /**
     * Test estimate setters and getters.
     * <p>
     *     <b>Involved in</b>
     *     <ul>
     *         <li>UC-1</li>
     *     </ul>
     * </p>
     */
    public void testEstimate(){
        User user = new User("user");
        Request request = new Request(user);
        request.setEstimate(50.00);

        assertTrue("Estimate Not Set!" , request.getEstimate().equals(50.00));
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
        User user = new User("user");
        Request request = new Request(user);

        assertFalse("hasDriver Not Working!" , request.hasDriver());

        Driver driver = new Driver("mark");
        request.setDriver(driver);

        assertTrue("hasDriver Not Working!" , request.hasDriver());
        assertEquals("mark",request.getDriver().getUsername());
    }

    /**
     * Test status setters and getters.
     * <p>
     *     <b>Involved in</b>
     *     <ul>
     *         <li>UC-6</li>
     *         <li>UC-10</li>
     *     </ul>
     * </p>
     */
    public void testStatus(){
        User user = new User("user");
        Request request = new Request(user);

        assertTrue("Constructor Did Not Set Status to Open!" , request.getStatus().equals("open"));

        request.setStatus("complete");

        assertTrue("Status Not Set!" , request.getStatus().equals("complete"));
    }

    /**
     * Test keyword search and description setter
     * <p>
     *     <b>Involved in</b>
     *     <ul>
     *         <li>UC-1</li>
     *         <li>UC-9</li>
     *     </ul>
     * </p>
     */
    public void testDescription(){
        User user = new User("user");
        Request request = new Request(user);
        try {
            request.setDescription("I need a ride. This is an eMerGency!");
        }
        catch(DescriptionTooLongException e){
            assertTrue("DescriptionTooLongException Thrown!" , false);
        }
        assertFalse("Keyword Search Broken!" , !request.hasKeyword("emergency"));
        assertTrue("Description Setter Broken!" , request.hasKeyword("eMerGency"));
    }
}