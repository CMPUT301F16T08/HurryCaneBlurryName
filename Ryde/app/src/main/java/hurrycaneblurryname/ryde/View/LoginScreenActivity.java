package hurrycaneblurryname.ryde.View;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import hurrycaneblurryname.ryde.R;

/**
 * The type Main activity.
 */
public class LoginScreenActivity extends AppCompatActivity {
    private TextView signupTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);
        setTitle(R.string.Login);


        signupTextView = (TextView)findViewById(R.id.reg_text);
        signupTextView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View arg0, MotionEvent arg1) {
                // TODO Auto-generated method stub
                return true;
            }
        });

    }
}