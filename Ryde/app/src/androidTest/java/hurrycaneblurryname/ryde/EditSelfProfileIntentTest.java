package hurrycaneblurryname.ryde;

import android.app.Activity;
import android.test.ActivityInstrumentationTestCase2;
import android.view.Display;
import android.widget.EditText;

import com.robotium.solo.Solo;

import java.util.Random;

import hurrycaneblurryname.ryde.View.LoginScreenActivity;
import hurrycaneblurryname.ryde.View.MapsActivity;

import static android.support.test.InstrumentationRegistry.getInstrumentation;

/**
 * Created by john on 2016-11-27.
 */

public class EditSelfProfileIntentTest extends ActivityInstrumentationTestCase2<LoginScreenActivity> {
    private Solo solo;

    public EditSelfProfileIntentTest() {
        super(hurrycaneblurryname.ryde.View.LoginScreenActivity.class);
    }

    public void testStart() throws Exception{
        Activity activity=getActivity();
    }

    public void setUp() throws Exception{
        //Log.d("TAG1", "setUp()");
        solo = new Solo(getInstrumentation(),getActivity());
    }

    public void testStep(){
        solo.assertCurrentActivity("Wrong Activity", LoginScreenActivity.class);
        solo.enterText((EditText) solo.getView(R.id.userEditText), "test1");
        solo.enterText((EditText) solo.getView(R.id.passwordEditText), "123456");
        solo.clickOnButton("Login");
        solo.assertCurrentActivity("Wrong Activity", MapsActivity.class);
        solo.setNavigationDrawer(Solo.OPENED);

        //http://stackoverflow.com/questions/26118480/how-to-open-navigation-drawer-menu-in-robotium-automation-script-in-android/29645959#29645959
        Display display = solo.getCurrentActivity().getWindowManager().getDefaultDisplay();
        int width = display.getWidth();
        int height = display.getHeight();
        float xStart = 0 ;
        float xEnd = width / 2;
        solo.drag(xStart, xEnd, height / 2, height / 2, 1);
        /////////////////
        solo.clickOnText("Edit Profile");
        solo.clearEditText(1);
        //http://stackoverflow.com/questions/6741100/random-numbers-in-java-when-working-with-android
        int iteration=0;
        String value="";
        while (iteration<10){
            Random rand=new Random();
            int n=rand.nextInt(10);
            value=value+String.valueOf(n);
            iteration=iteration+1;
        }

        //
        solo.enterText((EditText) solo.getView(R.id.phoneEditText), value);
        solo.clickOnText("Save");

        //http://stackoverflow.com/questions/26118480/how-to-open-navigation-drawer-menu-in-robotium-automation-script-in-android/29645959#29645959

        solo.drag(xStart, xEnd, height / 2, height / 2, 1);
        /////////////////
        solo.clickOnText("Edit Profile");
        boolean correctInput = solo.searchText(value);
        assertEquals(true, correctInput);
    }

    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }
}
