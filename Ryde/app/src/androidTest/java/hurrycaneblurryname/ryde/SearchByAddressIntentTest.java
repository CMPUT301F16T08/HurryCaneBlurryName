package hurrycaneblurryname.ryde;

import android.app.Activity;
import android.test.ActivityInstrumentationTestCase2;
import android.view.Display;
import android.widget.EditText;

import com.robotium.solo.Solo;

import org.junit.internal.runners.statements.ExpectException;

import java.util.Random;

import hurrycaneblurryname.ryde.View.LoginScreenActivity;
import hurrycaneblurryname.ryde.View.MapsActivity;

/**
 * Created by john on 2016-11-26.
 * this test includes use cases:
 *  - SearchByAddress(UC-16)
 *  - PriceEstimate(UC-14)
 *  - RideRequestsNewRide (UC-1)
 */

public class SearchByAddressIntentTest extends ActivityInstrumentationTestCase2<LoginScreenActivity> {
    private Solo solo;
    public SearchByAddressIntentTest(){
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
        solo.enterText((EditText) solo.getView(R.id.userEditText), "test1");
        solo.enterText((EditText) solo.getView(R.id.passwordEditText), "123456");
        solo.clickOnButton("Login");
        solo.assertCurrentActivity("Wrong Activity", MapsActivity.class);

        solo.enterText((EditText) solo.getView(R.id.text_map_search_start), "University of Alberta");
        solo.clickOnView(solo.getView(R.id.button_map_search_start));
        solo.enterText((EditText) solo.getView(R.id.text_map_search_end), "West Edmonton Mall");
        solo.clickOnView(solo.getView(R.id.button_map_search_end));

        solo.clickOnView(solo.getView(R.id.button_map_request));


        //http://stackoverflow.com/questions/6741100/random-numbers-in-java-when-working-with-android
        Random rand=new Random();
        int n=rand.nextInt(1000);
        solo.enterText(0, String.valueOf(n));
        /*
        click confirm, confirm and send from 3 alert dialog
        http://stackoverflow.com/questions/10359192/how-to-select-which-button-to-click-on-robotium-for-an-alert-dialog
         */
        solo.clickOnView(solo.getView(android.R.id.button2));
        solo.sleep(1000);
        solo.clickOnView(solo.getView(android.R.id.button2));
        solo.sleep(1000);
        solo.clickOnView(solo.getView(android.R.id.button2));

        //http://stackoverflow.com/questions/26118480/how-to-open-navigation-drawer-menu-in-robotium-automation-script-in-android/29645959#29645959
        Display display = solo.getCurrentActivity().getWindowManager().getDefaultDisplay();
        int width = display.getWidth();
        int height = display.getHeight();
        float xStart = 0 ;
        float xEnd = width / 2;
        solo.drag(xStart, xEnd, height / 2, height / 2, 1);
        /////////////////
        solo.clickOnText("My Ride Requests");


        //http://stackoverflow.com/questions/5323487/verify-alertdialog-shown-on-invalid-text-input
        boolean correctInput = solo.searchText(String.valueOf(n));
        assertEquals(true, correctInput);

        solo.clickOnText(String.valueOf(n));
        solo.clickOnView(solo.getView(R.id.cancelButton));
        solo.clickOnView(solo.getView(android.R.id.button2));
        solo.goBack();

        boolean correctInput2 = solo.searchText(String.valueOf(n));
        assertEquals(true, !correctInput2);
    }

    @Override
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }
}
