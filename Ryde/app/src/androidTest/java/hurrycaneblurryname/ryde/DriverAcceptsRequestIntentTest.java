package hurrycaneblurryname.ryde;

import android.app.Activity;
import android.test.ActivityInstrumentationTestCase2;
import android.view.Display;
import android.widget.EditText;

import com.robotium.solo.Solo;

import hurrycaneblurryname.ryde.View.LoginScreenActivity;
import hurrycaneblurryname.ryde.View.MapsActivity;

/**
 * Created by john on 2016-11-26.
 */

public class DriverAcceptsRequestIntentTest extends ActivityInstrumentationTestCase2<LoginScreenActivity> {
    private Solo solo;
    public DriverAcceptsRequestIntentTest(){
        super(hurrycaneblurryname.ryde.View.LoginScreenActivity.class);
    }
    public void testStart()throws Exception{
        Activity activity=getActivity();
    }
    public void setUp() throws Exception{
        solo=new Solo(getInstrumentation(), getActivity());
    }

    public void test(){
        solo.assertCurrentActivity("Wrong Activity", LoginScreenActivity.class);
        solo.enterText((EditText) solo.getView(R.id.userEditText), "test3");
        solo.enterText((EditText) solo.getView(R.id.passwordEditText), "123456");
        solo.clickOnButton("Login");
        solo.assertCurrentActivity("Wrong Activity", MapsActivity.class);

        //http://stackoverflow.com/questions/26118480/how-to-open-navigation-drawer-menu-in-robotium-automation-script-in-android/29645959#29645959
        Display display = solo.getCurrentActivity().getWindowManager().getDefaultDisplay();
        int width = display.getWidth();
        int height = display.getHeight();
        float xStart = 0 ;
        float xEnd = width / 2;
        solo.drag(xStart, xEnd, height / 2, height / 2, 1);
        /////////////////
        solo.clickOnText("Search Requests");
        solo.clickOnButton("by keyword");
        solo.clickOnView(solo.getView(R.id.searchButton));
        solo.clickInList(1);
        solo.clickOnView(solo.getView(R.id.interestButton));
        solo.goBack();

        solo.drag(xStart, xEnd, height / 2, height / 2, 1); //open menu again to logout
        solo.clickOnText("Logout");

    }
    /*
    Testing User COnfirmDriverAcceptane
     */
    public void test2(){
        solo.assertCurrentActivity("Wrong Activity", LoginScreenActivity.class);
        solo.enterText((EditText) solo.getView(R.id.userEditText), "test1");
        solo.enterText((EditText) solo.getView(R.id.passwordEditText), "123456");
        solo.clickOnButton("Login");
        solo.assertCurrentActivity("Wrong Activity", MapsActivity.class);

        //sees notification
        solo.clickOnView(solo.getView(R.id.menu_hotlist));
        solo.clickInList(1);
        //http://stackoverflow.com/questions/26118480/how-to-open-navigation-drawer-menu-in-robotium-automation-script-in-android/29645959#29645959
        Display display = solo.getCurrentActivity().getWindowManager().getDefaultDisplay();
        int width = display.getWidth();
        int height = display.getHeight();
        float xStart = 0 ;
        float xEnd = width / 2;
        solo.drag(xStart, xEnd, height / 2, height / 2, 1);
        /////////////////
        /*
        solo.clickOnText("My Ride Requests");
        solo.clickOnText("Emergency");
        solo.clickOnText("Check to see available Divers");
        solo.clickOnText("Test3");
        solo.clickOnText("CONFIRM DRIVER");
        solo.clickOnText("SEND");

        solo.goBack();
        solo.goBack();
        solo.goBack();
        */
    }

    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }
}
