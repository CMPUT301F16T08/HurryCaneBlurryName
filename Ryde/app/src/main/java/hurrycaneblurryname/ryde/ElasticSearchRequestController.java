package hurrycaneblurryname.ryde;

import android.os.AsyncTask;
import android.util.Log;

import com.searchly.jestdroid.DroidClientConfig;
import com.searchly.jestdroid.JestClientFactory;
import com.searchly.jestdroid.JestDroidClient;

import java.util.ArrayList;
import java.util.List;

import hurrycaneblurryname.ryde.Model.Request.Request;
import hurrycaneblurryname.ryde.Model.User;
import hurrycaneblurryname.ryde.Model.UserHolder;
import io.searchbox.core.Delete;
import io.searchbox.core.DocumentResult;
import io.searchbox.core.Index;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;

/**
 * Controller for handling querying for request object from elasticsearch server
 * @author cho on 2016-11-08.
 * @version 1.0
 */
public class ElasticSearchRequestController {
    private static JestDroidClient client;

    /**
     * Task handling getting requests for the Driver with given search parameters from elasticsearch
     *
     * TODO: handle searching by geolocation (coordinates and landmarks) and current location
     */
    public static class GetRequestsTask extends AsyncTask<String, Void, ArrayList<Request>> {

        /**
         * Querying for requests starting at the current geolocation
         * @param searchParam query parameters.
         *                          [0] should be current geolocation lat
         *                          [1] should be current geolocation lon
         *
         * @return array of requests that are closest to current geolocation
         * @usage Declare and initialize a ElasticSearchRequestController.GetRequestsTask object
         *        object.execute("search parameter");
         */
        @Override
        protected ArrayList<Request> doInBackground(String... searchParam) {
            verifySettings();

            ArrayList<Request> requests = new ArrayList<Request>();

            // search for first 10 requests with geolocation
            // Default to 500m
            // "{"from": 0, "size":10000, "filter" : {"geo_distance" : { "distance" : "500m", "location" :  [ -113.49026, 53.54565 ]}}}";
            String search_string = "{\"from\": 0, \"size\":10000, \"filter\" : {\"geo_distance\" : { \"distance\" : \"500m\", \"location\" :  [ "+ searchParam[1] +"," + searchParam[0] +"]}}}";

            // assume that search_parameters[0] is the only search term we are interested in using
            Search search = new Search.Builder(search_string)
                    .addIndex("f16t08")
                    .addType("requests")
                    .build();

            try {
                SearchResult result = client.execute(search);
                if (result.isSucceeded()) {
                    List<Request> foundTweets = result.getSourceAsObjectList(Request.class);
                    requests.addAll(foundTweets);
                }
                else {
                    Log.i("ErrorGetRequest", "The search query failed to find any requests that matched.");
                }
            }
            catch (Exception e) {
                Log.i("ErrorGetRequest", "Something went wrong when we tried to communicate with the elasticsearch server!");
            }

            return requests;
        }
    }

    public static class GetOpenRequestsTask extends AsyncTask<String, Void, ArrayList<Request>> {

        /**
         * Querying for requests starting at the current geolocation
         * @param searchParam query parameters.
         *                          [0] should be current geolocation lat
         *                          [1] should be current geolocation lon
         *
         * @return array of requests that are closest to current geolocation
         * @usage Declare and initialize a ElasticSearchRequestController.GetRequestsTask object
         *        object.execute("search parameter");
         */
        @Override
        protected ArrayList<Request> doInBackground(String... searchParam) {
            verifySettings();

            ArrayList<Request> requests = new ArrayList<Request>();

            // TODO filter for own username only!!!!
            // Default to 500m
            //{"size" : 10, "query" : { "match" : { "status" : "open" }}, "filter" : {"geo_distance" : { "distance" : "500m", "from" :  { "lat": 53.56838158542664, "lon": -113.4578289091587}}}}
            String search_string = "{\"size\" : 10, \"query\" : { \"match\" : { \"status\" : \"open\" }}, \"filter\" : {\"geo_distance\" : { \"distance\" : \"500m\", \"from\" :  { \"lat\": 53.56838158542664, \"lon\": -113.4578289091587}}}}";

            Search search = new Search.Builder(search_string)
                    .addIndex("f16t08")
                    .addType("requests")
                    .build();

            try {
                SearchResult result = client.execute(search);
                if (result.isSucceeded()) {
                    List<Request> foundTweets = result.getSourceAsObjectList(Request.class);
                    requests.addAll(foundTweets);
                }
                else {
                    Log.i("ErrorGetRequest", "The search query failed to find any open requests that matched.");
                }
            }
            catch (Exception e) {
                Log.i("ErrorGetRequest", "Something went wrong when we tried to communicate with the elasticsearch server!");
            }

            return requests;
        }
    }

    /**
     * Delete a request by its unique ID from ElasticSearch.
     *
     */

    public static class DeleteRequestsTask extends AsyncTask<String, Void, Void> {

        /**
         * Delete the request information from ElasticSearch, usually after a request is completed.
         * @param search_id search_id[0] is the unique ID stored with each Request object
         * @return null
         * @usage Declare and initialize a ElasticSearchRequestController.DeleteRequestsTask object
         *        object.execute("IDparameter");
         */
        @Override
        protected Void doInBackground(String... search_id) {
            verifySettings();

            Delete delete = new Delete.Builder(search_id[0])
                    .index("f16t08")
                    .type("requests")
                    .build();

            try {
                DocumentResult result = client.execute(delete);
                if (result.isSucceeded()) {
                    //TODO find out what documentResult holds
                }
                else {
                    Log.i("ErrorDeleteRequest", result.getErrorMessage() );
                }
            }
            catch (Exception e) {
                Log.i("ErrorDeleteRequest", "Something went wrong when we tried to communicate with the elasticsearch server!");
            }

            return null;
        }
    }

    /**
     * Add a new request to the elasticsearch database.
     */
    public static class AddRequestsTask extends AsyncTask<Request, Void, Void> {

        /**
         * Add a request object to the elastic search database
         * @param requests Request object(s) to be built and added to ElasticSearch
         * @return null
         * @usage Declare and initialize a ElasticSearchRequestController.AddRequestsTask object
         *        object.execute(requestObject);
         */
        @Override
        protected Void doInBackground(Request... requests) {
            verifySettings();

            for (Request request: requests) {
                Index index = new Index.Builder(request).index("f16t08").type("requests").build();

                try {
                    DocumentResult result = client.execute(index);
                    if (result.isSucceeded()) {
                        // TODO: setID of the request from elasticsearch
//                        request.setId(result.getId());
                    }
                    else {
                        Log.i("ErrorAddRequest", "Elastic search was not able to add the request.");
                    }
                }
                catch (Exception e) {
                    Log.i("ErrorAddRequest", "Failed to add a request to elastic search!");
                    e.printStackTrace();
                }
            }

            return null;
        }
    }

    /**
     *  Get User information according to search criteria.
     *
     *  TODO: handle searching by userid
     */
    public static class GetUsersTask extends AsyncTask<String, Void, User> {

        /**
         * Search and get a User with a specific userID string. Pulls all matched and returns only one.
         * @param search_parameters unique userID string
         * @return Single User object
         * @usage Declare and initialize a ElasticSearchRequestController.GetUsersTask object
         *        object.execute("search parameter");
         */
        @Override
        protected User doInBackground(String... search_parameters) {
            verifySettings();

            ArrayList<User> users = new ArrayList<User>();

            // "{ "query": {"term": {"username": "search_parameters[0] "}}}";

            String search_string = String.format(
                    "{\n" + "    \"query\": {\n" +
                            "       \"term\" : { \"username\" : \"%s\" }\n" +
                            "    }\n" +
                            "}", search_parameters[0]);

            //if input is empty, pull all tweets
            if (search_parameters[0].equals("")) {
                search_string = "";
            }
            // assume that search_parameters[0] is the only search term we are interested in using
            Search search = new Search.Builder(search_string)
                    .addIndex("f16t08")
                    .addType("users")
                    .build();

            try {
                SearchResult result = client.execute(search);
                if (result.isSucceeded()) {
                    List<User> foundUsers = result.getSourceAsObjectList(User.class);
                    users.addAll(foundUsers);
                }
                else {
                    Log.i("ErrorGetUser", "The search query failed to find any users that matched.");
                }

            } catch (Exception e) {
                Log.i("ErrorGetUser", "Something went wrong when we tried to communicate with the elasticsearch server!");
                e.printStackTrace();
            }

            if (users.isEmpty()) {
                return new User("");
            }
            return users.get(0);
        }
    }


    // FIX!!! username unique check!!!
    /**
     * Is called after a user creates a new account.
     * Add a new user to the elasticsearch database.
     */
    public static class AddUserTask extends AsyncTask<User, Void, Void> {

        /**
         * Add a new user's information to elasticsearch
         * @param users User object to build elasticsearch entry
         * @return null
         * @usage Declare and initialize a ElasticSearchRequestController.AddUsersTask object
         *        object.execute(userObject);
         */

        @Override
        protected Void doInBackground(User... users) {
            verifySettings();

            for (User user: users) {
                Index index = new Index.Builder(user).index("f16t08").type("users").build();

                try {
                    DocumentResult result = client.execute(index);
                    if (result.isSucceeded()) {
                        user.setId(result.getId());
                        Log.i("Debug", "Successful create user");
                    }
                    else {
                        Log.i("ErrorAddUser", "Elastic search was not able to add the user.");
                    }
                }
                catch (Exception e) {
                    Log.i("ErrorAddUser", "Failed to add a user to elastic search!");
                    e.printStackTrace();
                }
            }

            return null;
        }
    }



    /**
     * Is called after a user updates his profile.
     * Update user's profile
     */

    public static class UpdateUserTask extends AsyncTask<User, Void, Void> {

        /**
         * Update a new user's information to elasticsearch
         * @param users User object to build elasticsearch entry
         * @return null
         * @usage Declare and initialize a ElasticSearchRequestController.UpdateUserTask object
         *        object.execute(userObject);
         */

        // TODO look into updating stuff on elasticsearch
        @Override
        protected Void doInBackground(User... users) {
            verifySettings();

            for (User user: users) {
                Index index = new Index.Builder(user).index("f16t08").type("users").build();

                try {
                    DocumentResult result = client.execute(index);
                    if (result.isSucceeded()) {
                        user.setId(result.getId());
                        Log.i("Debug", "Successful upgrade user profile");
                    }
                    else {
                        Log.i("ErrorUpgradeUser", "Elastic search was not able to upgrade user profile.");
                    }
                }
                catch (Exception e) {
                    Log.i("ErrorUpgradeUser", "Failed to upgrade a user to elastic search!");
                    e.printStackTrace();
                }
            }

            return null;
        }
    }

    private static void verifySettings() {
        // if the client hasn't been initialized then we should make it!
        if (client == null) {
            DroidClientConfig.Builder builder = new DroidClientConfig.Builder("http://cmput301.softwareprocess.es:8080");
            DroidClientConfig config = builder.build();

            JestClientFactory factory = new JestClientFactory();
            factory.setDroidClientConfig(config);
            client = (JestDroidClient) factory.getObject();
        }
    }

}
