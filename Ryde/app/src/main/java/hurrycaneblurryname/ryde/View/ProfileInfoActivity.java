package hurrycaneblurryname.ryde.View;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import hurrycaneblurryname.ryde.Model.Request.RequestUserHolder;
import hurrycaneblurryname.ryde.Model.User;
import hurrycaneblurryname.ryde.R;

/**
 * The type Driver Profile Info activity.
 * Author: Chen
 * Reference: https://www.mkyong.com/android/how-to-make-a-phone-call-in-android/
 *            https://www.mkyong.com/android/how-to-send-email-in-android/
 */

public class ProfileInfoActivity extends AppCompatActivity {
    private TextView userTextView;
    private TextView emailTextView;
    private TextView phoneTextView;
    private TextView carInfoTextView;

    private RelativeLayout carInfoPanel;
    private String show;

    private Button callButton;
    private Button emailButton;

    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_info);
        setTitle("Profile Detail");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        userTextView = (TextView)findViewById(R.id.userTexts);
        emailTextView = (TextView)findViewById(R.id.emailTexts);
        phoneTextView = (TextView)findViewById(R.id.phoneTexts);
        carInfoTextView = (TextView)findViewById(R.id.Info);

        carInfoPanel = (RelativeLayout)findViewById(R.id.carInfoPanel);

        callButton = (Button)findViewById(R.id.callButton);
        emailButton = (Button)findViewById(R.id.emailButton);

        user = RequestUserHolder.getInstance().getUser();

        callButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:"+user.getPhone()));
                startActivity(callIntent);
            }
        });

        emailButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent email = new Intent(Intent.ACTION_SEND);
                email.putExtra(Intent.EXTRA_EMAIL, new String[]{user.getEmail()});
                email.putExtra(Intent.EXTRA_SUBJECT, "Ryde: ");
                email.putExtra(Intent.EXTRA_TEXT, "");
                email.setType("message/rfc822");
                startActivity(Intent.createChooser(email, "Choose an Email client :"));
            }
        });

        Intent intent= getIntent();
        Bundle extras = intent.getExtras();
        if(extras != null) {
            show = extras.getString("SHOW_VEHICLE");
        }

    }

    protected void onStart() {
        super.onStart();
        // retrive clicked user info
        user = RequestUserHolder.getInstance().getUser();

        // display current User info on editText
        userTextView.setText(user.getUsername());
        emailTextView.setText(user.getEmail());
        phoneTextView.setText(user.getPhone());
        if (show.equals("y")) {
            carInfoTextView.setText(user.getVehicleYear() + "\n" + user.getVehicleMake() + "\n" + user.getVehicleModel());
        }else {
            carInfoPanel.setVisibility(View.GONE);
        }
    }

    // Back Navigation Handle
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
