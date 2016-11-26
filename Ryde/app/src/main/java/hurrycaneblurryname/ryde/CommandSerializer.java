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
 * Serializes an ArrayList of Commands to Json
 * Source: http://stackoverflow.com/questions/19588020/gson-serialize-a-list-of-polymorphic-objects
 * Date Accessed: 11/20/2016
 * Author:giampaolo
 */
public class CommandSerializer implements JsonSerializer<ArrayList<Command>> {

    private static Map<String, Class> map = new TreeMap<String, Class>();

    static {
        map.put("Command", Command.class);
        map.put("AddRequestCommand", AddRequestCommand.class);
        map.put("AcceptRequestCommand", AcceptRequestCommand.class);
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
