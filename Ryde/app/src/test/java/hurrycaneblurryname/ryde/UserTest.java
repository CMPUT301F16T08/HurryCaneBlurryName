package hurrycaneblurryname.ryde;

import junit.framework.TestCase;

import hurrycaneblurryname.ryde.Model.Request.Request;
import hurrycaneblurryname.ryde.Model.User;

/**
 * <h1>User Test</h1>
 * Tests User Class
 *
 * @author Blaz Pocrnja
 * @version 1.0
 * @since 10/12/2016
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
        User user = new User("");
        Request request = new Request(user);
        user.addRequest(request);
        assertTrue("Request Was Not Added to User Request List!" , user.hasRequest(request));
    }

    /**
     * Test get request.
     * <p>
     *     <b>Involved in</b>
     *     <ul>
     *         <li>UC-2</li>
     *     </ul>
     * </p>
     */
    public void testGetRequest(){
        User user = new User("");
        Request request = new Request(user);
        user.addRequest(request);

        assertTrue("Request Was Not Retrieved At Index!" , user.getRequest(0).equals(request));
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
        User user = new User("");
        Request request = new Request(user);
        user.addRequest(request);
        user.removeRequest(request);

        assertFalse("Request Not Removed!" , user.hasRequest(request));
    }

    /**
     * Test get Username.
     * <p>
     *     <b>Involved in</b>
     *     <ul>
     *         <li>UC-4</li>
     *         <li>UC-7</li>
     *         <li>UC-8</li>
     *     </ul>
     * </p>
     */
    public void testGetUsername(){
        User user = new User("UniqueUsername");

        assertTrue("Username Not Retrieved!" , user.getUsername().equals("UniqueUsername"));
    }

    /**
     * Test phone setters and getters.
     * <p>
     *     <b>Involved in</b>
     *     <ul>
     *         <li>UC-4</li>
     *         <li>UC-7</li>
     *         <li>UC-8</li>
     *     </ul>
     * </p>
     */
    public void testPhone(){
        User user = new User("");
        user.setPhone("7805550001");

        assertTrue("Phone Not Set!" , user.getPhone().equals("7805550001"));
    }

    /**
     * Test email setters and getters.
     * <p>
     *     <b>Involved in</b>
     *     <ul>
     *         <li>UC-4</li>
     *         <li>UC-7</li>
     *         <li>UC-8</li>
     *     </ul>
     * </p>
     */
    public void testEmail(){
        User user = new User("");
        user.setEmail("exampleemail@ualberta.ca");

        assertTrue("Email Not Set!" , user.getEmail().equals("exampleemail@ualberta.ca"));
    }

    public void testVehicle(){
        User user = new User("");
        user.setVehicleYear(1995);
        user.setVehicleMake("Dodge");
        user.setVehicleModel("Dakota");

        assertEquals(1995 , user.getVehicleYear());
        assertEquals("Dodge" , user.getVehicleMake());
        assertEquals("Dakota" , user.getVehicleModel());
    }
}
