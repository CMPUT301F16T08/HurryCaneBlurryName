package hurrycaneblurryname.ryde;

import android.location.Location;

import junit.framework.TestCase;

import hurrycaneblurryname.ryde.Model.Driver;
import hurrycaneblurryname.ryde.Model.Request.Request;

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
        try{
            request.setLocations(from , to);
        }
        catch(LocationException e){
            assertTrue("LocationException Thrown!" , false);
        }

        assertTrue("From Location Not Set!" , request.getFrom().equals(from));
        assertTrue("To Location Not Set!" , request.getTo().equals(to));
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
        Request request = new Request();
        request.setEstimate("50.00");

        assertTrue("Estimate Not Set!" , request.getEstimate().equals("50.00"));
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

        assertFalse("hasDriver Not Working!" , request.hasDriver());

        Driver driver = new Driver("");
        request.setDriver(driver);

        assertTrue("hasDriver Not Working!" , request.hasDriver());
        assertTrue("Driver Does Not Match!" , request.getDriver().equals(driver));
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
        Request request = new Request();

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
        Request request = new Request();
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