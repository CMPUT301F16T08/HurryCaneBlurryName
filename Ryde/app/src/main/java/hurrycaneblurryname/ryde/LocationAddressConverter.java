package hurrycaneblurryname.ryde;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;

import java.util.Arrays;

/**
 * Created by cho on 2016-11-27.
 */

/**
 *  Class for converting geolocation double[] to printable address strings
 *  Uses the Adapter design pattern to convert from double[] to string
 */
public class LocationAddressConverter {

    /**
     * Converts from and to geolocations to closest address by Geocoder
     * @param context context of activity
     * @param geoloc double[], [lon, lat]
     * @return a String[] containing the from Address and to Address respectively
     */
    public static String getLocationAddress(Context context, double[] geoloc) {
        // Display address of locations if possible
        // Reminder: geolocation format is [lon, lat] in request
        // Address displayed is street + city + prov + country
        Geocoder geocoder = new Geocoder(context);
        Address geoAddr;
        String addrString = "";
        try {
            geoAddr = geocoder.getFromLocation(geoloc[1], geoloc[0], 1)
                    .get(0);

            addrString = "\t"+geoAddr.getAddressLine(0) +"\n\t"
                        +geoAddr.getLocality()+"\n\t"
                        +geoAddr.getAdminArea()+"\n\t"
                        +geoAddr.getCountryCode();

        } catch (Exception e) {
            Log.i("AddressError", "Failed to convert from latlon to address");
            addrString = Arrays.toString(geoloc);

        }
        return addrString;
    }
}
