package hurrycaneblurryname.ryde.View;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import hurrycaneblurryname.ryde.Model.Request.RequestUserHolder;
import hurrycaneblurryname.ryde.Model.User;
import hurrycaneblurryname.ryde.R;

/**
 * The type Rider Confirm Driver activity.
 * Author: Chen
 */

public class RiderConfirmDriverActivity extends AppCompatActivity {

    private TextView userTextView;
    private TextView emailTextView;
    private TextView phoneTextView;
    private Button callButton;
    private Button emailButton;
    private Button confirmButton;// TODO

    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rider_confirm_driver);
        setTitle("Profile Detail");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        userTextView = (TextView)findViewById(R.id.userTexts);
        emailTextView = (TextView)findViewById(R.id.emailTexts);
        phoneTextView = (TextView)findViewById(R.id.phoneTexts);

        callButton = (Button)findViewById(R.id.callButton);
        emailButton = (Button)findViewById(R.id.emailButton);
        confirmButton = (Button)findViewById(R.id.confirmButton);

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

        confirmButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // TODO
            }
        });

    }

    protected void onStart() {
        super.onStart();
        // retrive clicked user info
        user = RequestUserHolder.getInstance().getUser();

        // display current User info on editText
        userTextView.setText(user.getUsername());
        emailTextView.setText(user.getEmail());
        phoneTextView.setText(user.getPhone());

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

