package hurrycaneblurryname.ryde;

import android.location.Location;
import android.test.ActivityInstrumentationTestCase2;

import hurrycaneblurryname.ryde.Model.Request.Request;
import hurrycaneblurryname.ryde.Model.Request.RequestList;
import hurrycaneblurryname.ryde.Model.User;
import hurrycaneblurryname.ryde.View.LoginScreenActivity;


/**
 * <h1>Use Case Tests</h1>
 * Tests Use Cases as Outlined in the Wiki
 *
 * @author Blaz Pocrnja
 * @version 1.1
 * @since 10 /23/2016
 */
public class UseCaseTests extends ActivityInstrumentationTestCase2{
    /**
     * Instantiates a new Use case tests.
     */
    public UseCaseTests() {
        super(LoginScreenActivity.class);
    }

    /**
     * Test rider requests new ride.
     * UC-1
     */
    public void testRiderRequestsNewRide(){
        //Assume User has rider role
        User rider = new User("");
        rider.setRole("rider");

        //1. System prompts Rider for two locations
        //2. Rider submits two locations
        Location from = new Location("from");
        Location to = new Location("to");
        Request request = new Request(rider);
        try{
            request.setLocations(from, to);
        }
        catch(LocationException e){
            assertTrue("LocationException Thrown!" , false);
        }

        assertTrue("From Location Not Set!" , request.getFrom().equals(from));
        assertTrue("To Location Not Set!" , request.getTo().equals(to));

        //3. System prompts Rider for a description
        //4. Rider submits a description (Optional)
        assertTrue("Description was not properly initialized!", request.getDescription().equals(""));

        try {
            request.setDescription("This is a new description.");
        }
        catch(DescriptionTooLongException e){
            assertTrue("DescriptionTooLongException Thrown!" , false);
        }
        assertTrue("Description not set!" , request.getDescription().equals("This is a new description."));

        //5. System provides estimate of trip
        //6. Rider confirms/submits estimate for trip
        request.setEstimate(20.25);
        assertTrue("Estimate not set!", request.getEstimate().equals(20.25));

        //7. System saves new request
        rider.addRequest(request);
        assertTrue("Request not saved with rider!", rider.hasRequest(request));
    }

    /**
     * Test view current requests.
     * UC-2
     */
    public void testViewCurrentRequests(){
        //1. System displays all ride requests and driver acceptances associated with user
        User user1 = new User("1");
        User user2 = new User("2");
        Request request = new Request(user1);
        user1.addRequest(request);
        request.setDriver(user2);
        user2.addRequest(request);

        assertTrue("Driver not associated with request!", user2.hasRequest(request));
        assertTrue("Rider not associated with request!", user1.hasRequest(request));


        //2. User selects to view start and end locations
        //3. System displays ride start and end locations on a map
        Location from = new Location("from");
        Location to = new Location("to");
        try{
            request.setLocations(from, to);
        }
        catch(LocationException e){
            assertTrue("LocationException Thrown!" , false);
        }

        assertTrue("From Location Not Set!" , request.getFrom().equals(from));
        assertTrue("To Location Not Set!" , request.getTo().equals(to));

    }

    /**
     * Test cancel current request.
     * UC-3
     */
    public void testCancelCurrentRequest(){
        //1. System prompts for confirmation from user
        //2. User confirms cancellation
        //3. Ride request is removed from system.
        User user = new User("");
        Request request = new Request(user);
        user.addRequest(request);

        assertTrue("Request was not added to user's list!",user.hasRequest(request));

        user.removeRequest(request);

        assertFalse("Request was not removed from user's list!",user.hasRequest(request));
    }

    /**
     * Test contact request driver.
     * UC-4
     */
    public void testContactRequestDriver(){
        User user = new User("Mr.Information");
        user.setEmail("myemail@ualberta.ca");
        user.setPhone("1-780-555-0001");

        //1. System provides Rider with Driver contact information
        assertTrue("Name not Initialized!", user.getUsername().equals("Mr.Information"));
        assertTrue("Email not Set!", user.getEmail().equals("myemail@ualberta.ca"));
        assertTrue("Phone not Set!", user.getPhone().equals("1-780-555-0001"));
        //2. Rider selects phone method
        //3. System redirects to call app.
        //OR
        //2. Rider selects email method
        //3. System redirects to email app


    }

    /**
     * Test confirm driver acceptance.
     * UC-5
     */
    public void testConfirmDriverAcceptance(){
        User rider = new User("");
        User driver = new User("");
        Request request = new Request(rider);
        rider.addRequest(request);
        request.setDriver(driver);

        assertTrue("Status not open!", request.getStatus().equals("open"));

        //1. System closes the request
        request.setStatus("pending");
        assertTrue("Status not pending!", request.getStatus().equals("pending"));

        //2. System sends notification to the Driver of confirmation
        driver.addRequest(request);
    }

    /**
     * Test confirm completion of request.
     * UC-6
     */
    public void testConfirmCompletionOfRequest(){
        User rider = new User("");
        User driver = new User("");
        Request request = new Request(rider);
        rider.addRequest(request);
        request.setDriver(driver);

        assertTrue("Status not open!", request.getStatus().equals("open"));

        request.setStatus("pending");

        assertTrue("Status not pending!", request.getStatus().equals("pending"));

        driver.addRequest(request);

        //1. System marks the request as complete
        request.setStatus("complete");

        assertTrue("Status not complete!", request.getStatus().equals("complete"));

        //2. System prompts User for payment details
        //Assume user has preferred method of payment on account
        rider.setCardNumber("1234567890");

        assertTrue("Credit card not set!" , rider.getCardNumber().equals("1234567890"));
    }

    /**
     * Test view profile.
     * UC-7
     */
    public void testViewProfile(){
        User user = new User("Mr.Information");
        user.setEmail("myemail@ualberta.ca");
        user.setPhone("1-780-555-0001");

        //1. System displays username and contact information
        assertTrue("Name not Initialized!", user.getUsername().equals("Mr.Information"));
        assertTrue("Email not Set!", user.getEmail().equals("myemail@ualberta.ca"));
        assertTrue("Phone not Set!", user.getPhone().equals("1-780-555-0001"));
    }

    /**
     * Test edit self profile.
     * UC-8
     */
    public void testEditSelfProfile(){
        User user = new User("Mr.Information");
        user.setEmail("myemail@ualberta.ca");
        user.setPhone("1-780-555-0001");

        //1. System displays username and contact information
        assertTrue("Name not Initialized!", user.getUsername().equals("Mr.Information"));
        assertTrue("Email not Set!", user.getEmail().equals("myemail@ualberta.ca"));
        assertTrue("Phone not Set!", user.getPhone().equals("1-780-555-0001"));

        //2. User selects Edit
        //3. System prompts User for changes
        //4. User enters changes to information and selects Save
        user.setEmail("new@ualberta.ca");
        user.setPhone("1-780-555-0002");

        assertTrue("Email not Changed!", user.getEmail().equals("new@ualberta.ca"));
        assertTrue("Phone not Changed!", user.getPhone().equals("1-780-555-0002"));

        //5. System returns to Step 1.


    }

    /**
     * Test search for open rides.
     * UC-9
     */
    public void testSearchForOpenRides(){
        User rider = new User("");
        Request request = new Request(rider);
        try {
            request.setDescription("Need a ride. This is an emergency!");
        }
        catch(DescriptionTooLongException e){
            assertTrue("DescriptionTooLongException Thrown!" , false);
        }
        RequestList requests = new RequestList();
        requests.addRequest(request);

        //1. System prompts for Driver for search term
        //2. Driver submits geo-location or search keyword
        RequestList searched = new RequestList();
        searched.addAllKeyword(requests, "Fun");

        //3. System lists matching ride requests
        assertTrue("'Fun' should not be a keyword!" , searched.getSize() == 0);

        searched.addAllKeyword(requests, "Emergency");
        assertTrue("'Emergency' keyword not found" , searched.getSize() == 1);
    }

    /**
     * Test driver accepts request.
     * UC-10
     */
    public void testDriverAcceptsRequest(){
        User rider = new User("");
        User driver = new User("");
        Request request = new Request(rider);
        rider.addRequest(request);
        request.setDriver(driver);

        assertTrue("Status not open!", request.getStatus().equals("open"));

        //1. System changes ride request status to pending

        request.setStatus("pending");

        assertTrue("Status not pending!", request.getStatus().equals("pending"));

        driver.addRequest(request);

        //2. System sends Rider acceptance notification
    }

    /**
     * Test view accepted request.
     * UC-11
     */
    public void testViewAcceptedRequest(){
        User rider = new User("");
        User driver = new User("");
        Request req1 = new Request(rider);
        Request req2 = new Request(rider);

        req1.setDriver(driver);
        driver.addRequest(req1);

        req2.setDriver(driver);
        driver.addRequest(req2);

        req1.setStatus("complete");
        req2.setStatus("complete");

        //1. System displays list of accepted ride requests
        assertTrue("First Request not added!" , driver.getRequest(0).equals(req1));
        assertTrue("First Request not added!" , driver.getRequest(1).equals(req2));

    }


}
