package hurrycaneblurryname.ryde;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by pocrn_000 on 11/19/2016.
 */

public class CommandSerializer implements JsonSerializer<ArrayList<Command>> {

    private static Map<String, Class> map = new TreeMap<String, Class>();

    static {
        map.put("Command", Command.class);
        map.put("AddRequestCommand", AddRequestCommand.class);
        //TODO Second Command Type
    }

    @Override
    public JsonElement serialize(ArrayList<Command> src, Type typeOfSrc,
                                 JsonSerializationContext context) {
        if (src == null)
            return null;
        else {
            JsonArray ja = new JsonArray();
            for (Command cmd : src) {
                Class c = map.get(cmd.isA);
                if (c == null)
                    throw new RuntimeException("Unknown class: " + cmd.isA);
                ja.add(context.serialize(cmd, c));

            }
            return ja;
        }
    }
}
