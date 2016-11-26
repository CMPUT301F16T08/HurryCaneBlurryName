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
import io.searchbox.core.Update;

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
     * TODO: handle searching by current location, takes in keyword as destination
     */
    public static class GetOpenRequestsGeoTask extends AsyncTask<String, Void, ArrayList<Request>> {

        /**
         * Querying for requests by geolocation
         * @param searchParam query parameters.
         *                          [0] should be current geolocation lat
         *                          [1] should be current geolocation lon
         *
         * @return array of requests
         * @usage Declare and initialize a ElasticSearchRequestController.GetRequestsTask object
         *        object.execute("search parameter");
         */
        @Override
        protected ArrayList<Request> doInBackground(String... searchParam) {
            verifySettings();

            ArrayList<Request> requests = new ArrayList<Request>();
            String search_string;
            // search for first 10 requests with geolocation
            // Default to 500m
            // "{"from": 0, "size":10, "filter" : {"geo_distance" : { "distance" : "10km", "location" :  [ -113.49026, 53.54565 ]}}}";
            if (searchParam.length == 2) {
                search_string = "{\"from\": 0, \"size\":10, \"filter\" : {\"geo_distance\" : { \"distance\" : \"10km\", \"from\" :  [ "+ searchParam[1] +"," + searchParam[0] +"]}}}";
            } else {
                search_string = "";
            }


            // assume that search_parameters[0] is the only search term we are interested in using
            Search search = new Search.Builder(search_string)
                    .addIndex("f16t08")
                    .addType("crequests")   //TODO after geolocation conflict sorted out, change to requests
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
                return null;

            }
            return requests;

        }
    }

    /**
     * Get requests associated with rider task.
     */
    public static class GetOpenRequestsDescTask extends AsyncTask<String, Void, ArrayList<Request>> {

        /**
         * Querying for open requests by keyword in description
         * @param searchParam search paramater. searchParam[0] should be keyword in description
         * @return array of requests that are closest to current geolocation
         * @usage Declare and initialize a ElasticSearchRequestController.GetRequestsTask object
         *        object.execute("search parameter");
         */
        @Override
        protected ArrayList<Request> doInBackground(String... searchParam) {
            verifySettings();

            ArrayList<Request> requests = new ArrayList<>();

            String search_string;
            if (searchParam[0].isEmpty() ) {
                return requests;
            } else {
                search_string = "{\"size\" : 10, \"query\" : { \"match\" : { \"description\" : \""+ searchParam[0] +"\" }}}";
            }
            Log.i("Debug", search_string);


            Search search = new Search.Builder(search_string)
                    .addIndex("f16t08")
                    .addType("crequests")
                    .build();

            try {
                SearchResult result = client.execute(search);
                if (result.isSucceeded()) {
                    requests = new ArrayList<Request>();
                    List<Request> foundTweets = result.getSourceAsObjectList(Request.class);
                    requests.addAll(foundTweets);


                }
                else {
                    Log.i("ErrorGetRequest", "The search query failed to find request that matched ID.");
                }
            }
            catch (Exception e) {
                Log.i("ErrorGetRequest", "Something went wrong when we tried to communicate with the elasticsearch server!");
            }

            return requests;
        }
    }


    /**
     * Get requests associated with rider task.
     */
    public static class GetDriverRequestsTask extends AsyncTask<String, Void, ArrayList<Request>> {

        /**
         * Querying for all of a rider's created requests that haven't been opened.
         * @param searchParam query parameters.
         *                                     searchParam[0] should be the username
         * @return array of requests that are closest to current geolocation
         * @usage Declare and initialize a ElasticSearchRequestController.GetRequestsTask object
         *        object.execute("search parameter");
         */
        @Override
        protected ArrayList<Request> doInBackground(String... searchParam) {
            verifySettings();

            ArrayList<Request> requests;;

            //{"size" : 10, "query" : { "match" : { "username" : "username", "status" : "open" }}}
            String search_string = "{\"size\" : 10, \"query\" : { \"match\" : { \"driver.username\" : \""+ searchParam[0] +"\" }}}";

            Log.i("Debug", search_string);
            Search search = new Search.Builder(search_string)
                    .addIndex("f16t08")
                    .addType("crequests")
                    .build();

            try {
                SearchResult result = client.execute(search);
                if (result.isSucceeded()) {
                    requests = new ArrayList<Request>();
                    List<Request> foundTweets = result.getSourceAsObjectList(Request.class);
                    requests.addAll(foundTweets);

                    return requests;
                }
                else {
                    Log.i("ErrorGetRequest", "The search query failed to find any open requests that matched.");
                }
            }
            catch (Exception e) {
                Log.i("ErrorGetRequest", "Something went wrong when we tried to communicate with the elasticsearch server!");
            }

            return null;
        }
    }

    /**
     * Get requests associated with rider task.
     */
    public static class GetRiderRequestsTask extends AsyncTask<String, Void, ArrayList<Request>> {

        /**
         * Querying for all of a rider's created requests that haven't been opened.
         * @param searchParam query parameters.
         *                                     searchParam[0] should be the username
         * @return array of requests that are closest to current geolocation
         * @usage Declare and initialize a ElasticSearchRequestController.GetRequestsTask object
         *        object.execute("search parameter");
         */
        @Override
        protected ArrayList<Request> doInBackground(String... searchParam) {
            verifySettings();

            ArrayList<Request> requests;;

            //{"size" : 10, "query" : { "match" : { "username" : "username", "status" : "open" }}}
            String search_string = "{\"size\" : 10, \"query\" : { \"match\" : { \"rider.username\" : \""+ searchParam[0] +"\" }}}";

            Log.i("Debug", search_string);
            Search search = new Search.Builder(search_string)
                    .addIndex("f16t08")
                    .addType("crequests") //TODO after geolocation conflict sorted out, change to requests
                    .build();

            try {
                SearchResult result = client.execute(search);
                if (result.isSucceeded()) {
                    requests = new ArrayList<Request>();
                    List<Request> foundTweets = result.getSourceAsObjectList(Request.class);
                    requests.addAll(foundTweets);

                    return requests;
                }
                else {
                    Log.i("ErrorGetRequest", "The search query failed to find any open requests that matched.");
                }
            }
            catch (Exception e) {
                Log.i("ErrorGetRequest", "Something went wrong when we tried to communicate with the elasticsearch server!");
            }

            return null;
        }
    }



    /**
     * Update a request by its unique ID from ElasticSearch.
     */
    public static class UpdateRequestsTask extends AsyncTask<Request, Void, Void> {

        /**
         * Update the request information from ElasticSearch with the request object.
         * @param requests request objects to be removed from elasticsearch
         * @return null
         * @usage Declare and initialize a ElasticSearchRequestController.DeleteRequestsTask object
         *        object.execute("IDparameter");
         */
        @Override
        protected Void doInBackground(Request... requests) {
            verifySettings();

            for (Request r : requests) {
                Index index = new Index.Builder(r).index("f16t08").type("crequests").id(r.getId()).build();

                try {
                    DocumentResult result = client.execute(index);
                    if (result.isSucceeded()) {
                        //TODO find out what documentResult holds
                        Log.i("UpdatedRequest", "Updated request with ID " + r.getId());
                    } else {
                        Log.i("ErrorUpdateRequest", "could not update request with ID " + r.getId());
                    }
                } catch (Exception e) {
                    Log.i("ErrorUpdateRequest", "Something went wrong when we tried to communicate with the elasticsearch server!");
                }
            }

            return null;
        }
    }

    /**
     * Delete a request by its unique ID from ElasticSearch.
     */
    public static class DeleteRequestsTask extends AsyncTask<Request, Void, Void> {

        /**
         * Delete the request information from ElasticSearch, usually after a request is completed.
         * @param requests request objects to be removed from elasticsearch
         * @return null
         * @usage Declare and initialize a ElasticSearchRequestController.DeleteRequestsTask object
         *        object.execute("IDparameter");
         */
        @Override
        protected Void doInBackground(Request... requests) {
            verifySettings();

            for (Request r : requests) {
                Delete delete = new Delete.Builder(r.getId())
                        .index("f16t08")
                        .type("crequests")  //TODO after geolocation conflict sorted out, change to requests
                        .build();

                try {
                    DocumentResult result = client.execute(delete);
                    if (result.isSucceeded()) {
                        //TODO find out what documentResult holds
                    } else {
                        Log.i("ErrorDeleteRequest", "could not delete request with ID " + r.getId());
                    }
                } catch (Exception e) {
                    Log.i("ErrorDeleteRequest", "Something went wrong when we tried to communicate with the elasticsearch server!");
                }
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
                Index index = new Index.Builder(request)
                        .index("f16t08")
                        .type("crequests")  //TODO after geolocation conflict sorted out, change to requests
                        .build();

                try {
                    DocumentResult result = client.execute(index);
                    if (result.isSucceeded()) {
                        request.setId(result.getId());
                    }
                    else {
                        Log.i("ErrorAddRequest", "Elastic search was not able to add the request.");
                    }
                }
                catch (Exception e) {
                    Log.i("AddRequestException", "Failed to add a request to elastic search!");
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
         * Search and get a User with a specific userID string. Pulls all matched and returns only top.
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
                Index index = new Index.Builder(user).index("f16t08").type("users").id(user.getId()).build();

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
