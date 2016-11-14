package hurrycaneblurryname.ryde.View;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import hurrycaneblurryname.ryde.ElasticSearchRequestController;
import hurrycaneblurryname.ryde.Model.Request.Request;
import hurrycaneblurryname.ryde.Model.User;
import hurrycaneblurryname.ryde.R;

/**
 * The type Search Requests activity.
 * Author: Chen
 * Storyboard by: Blaz
 */

public class SearchRequestsActivity extends AppCompatActivity {
    private Button searchButton;
    private EditText SearchEditText;

    private ListView searchView;
    private ArrayAdapter<Request> searchViewAdapter;

    private ArrayList<Request> searchResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_requests);
        setTitle("Search");

        SearchEditText = (EditText)findViewById(R.id.SearchEditText);
        searchButton = (Button)findViewById(R.id.searchButton);
        searchView = (ListView) findViewById(R.id.SearchResultListView);

        searchButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (SearchEditText.getText().length() == 0) {
                    Toast.makeText(SearchRequestsActivity.this, "Please enter your search keyword!", Toast.LENGTH_SHORT).show();
                    return;
                }
                String[] searchText =  SearchEditText.getText().toString().split(",");

                ElasticSearchRequestController.GetRequestsTask getRequestsTask = new ElasticSearchRequestController.GetRequestsTask();
                getRequestsTask.execute(searchText);

                try {
                    searchResult = getRequestsTask.get();
                } catch (Exception e) {
                    Toast.makeText(SearchRequestsActivity.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
                    searchResult = new ArrayList<Request>();
                    //Log.i("ErrorGetUser", "Something went wrong when getting user at sign up");
                    //e.printStackTrace();
                }
                if (searchResult.isEmpty())
                {
                    Toast.makeText(SearchRequestsActivity.this, "No results!", Toast.LENGTH_SHORT).show();
                }
                searchViewAdapter = new ArrayAdapter<Request>(SearchRequestsActivity.this, R.layout.list_item, searchResult);
                searchView.setAdapter(searchViewAdapter);

            }
        });

        searchView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Get habit name
                String str = parent.getItemAtPosition(position).toString();

            }
        });
    }
}
