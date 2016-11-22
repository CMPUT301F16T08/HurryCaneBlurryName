package hurrycaneblurryname.ryde.View;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import hurrycaneblurryname.ryde.ElasticSearchRequestController;
import hurrycaneblurryname.ryde.Model.Request.Request;
import hurrycaneblurryname.ryde.Model.Request.RequestHolder;
import hurrycaneblurryname.ryde.Model.Request.RequestUserHolder;
import hurrycaneblurryname.ryde.Model.User;
import hurrycaneblurryname.ryde.Model.UserHolder;
import hurrycaneblurryname.ryde.R;

/**
 * The type Available Drivers activity.
 * Author: Chen
 */

public class AvailableDriversActivity extends AppCompatActivity {

    //Arrays
    private ArrayList<User> drivers = new ArrayList<User>();
    //ListViews
    private ListView driversView;
    //Adapters
    private ArrayAdapter<User> driversViewAdapter;
    // Status TextView
    private TextView driversText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.available_drivers);
        setTitle("Available Drivers");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        driversText = (TextView) findViewById(R.id.driversText);
        driversView = (ListView) findViewById(R.id.driversView);
        driversViewAdapter = new ArrayAdapter<User>(this, R.layout.list_item, drivers);
        driversView.setAdapter(driversViewAdapter);
        driversView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                RequestUserHolder.getInstance().setUser(drivers.get(position));
                Intent intent = new Intent(AvailableDriversActivity.this, RiderConfirmDriverActivity.class);
                startActivity(intent);
            }
        });

    }


    @Override
    protected void onResume() {
        super.onResume();
        drivers.clear();
        drivers.addAll(RequestHolder.getInstance().getRequest().getOffers());
        changeDriversStatus();
        driversViewAdapter.notifyDataSetChanged();
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

    private void changeDriversStatus(){
        if(!drivers.isEmpty())
        {
            driversText.setVisibility(View.GONE);
        }
    }
}
