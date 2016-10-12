package hurrycaneblurryname.ryde;

import android.location.Location;

/**
 * Created by pocrn_000 on 10/11/2016.
 */

public class Request {
    
    private Location from;
    private Location to;

    public void setLocations(Location from, Location to) {
        this.from = from;
        this.to = to;
    }

    public Location getFrom() {
        return from;
    }

    public Location getTo() {
        return to;
    }
}
