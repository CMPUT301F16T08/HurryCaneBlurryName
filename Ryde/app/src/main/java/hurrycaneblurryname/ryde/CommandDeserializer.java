package hurrycaneblurryname.ryde;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

/**
 * Deserializes an ArrayList of Commands from Json
 * Source: http://stackoverflow.com/questions/19588020/gson-serialize-a-list-of-polymorphic-objects
 * Date Accessed: 11/20/2016
 * Author:giampaolo
 */

public class CommandDeserializer implements JsonDeserializer<ArrayList<Command>> {

    private static Map<String, Class> map = new TreeMap<String, Class>();

    static {
        map.put("Command", Command.class);
        map.put("AddRequestCommand", AddRequestCommand.class);
        map.put("UpdateRequestCommand", UpdateRequestCommand.class);
    }

    @Override
    public ArrayList<Command> deserialize(JsonElement json, Type typeOfT,
                                          JsonDeserializationContext context) throws JsonParseException {

        ArrayList list = new ArrayList<Command>();
        JsonArray ja = json.getAsJsonArray();

        for (JsonElement je : ja) {

            String type = je.getAsJsonObject().get("isA").getAsString();
            Class c = map.get(type);
            if (c == null)
                throw new RuntimeException("Unknown class: " + type);
            list.add(context.deserialize(je, c));
        }

        return list;

    }

}
