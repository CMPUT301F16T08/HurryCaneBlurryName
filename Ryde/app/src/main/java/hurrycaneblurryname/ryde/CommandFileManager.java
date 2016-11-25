package hurrycaneblurryname.ryde;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Loads and saves commands from an arraylist and file.
 */
public class CommandFileManager<Command>{

    protected Context context;

    CommandFileManager(Context context){
        this.context = context;
    }

    protected ArrayList<Command> loadFromFile(String FILENAME){

        ArrayList<Command> list = new ArrayList<Command>();

        try {
            FileInputStream fis = context.openFileInput(FILENAME);
            BufferedReader in = new BufferedReader(new InputStreamReader(fis));

            GsonBuilder gb = new GsonBuilder();
            gb.registerTypeAdapter(new TypeToken<ArrayList<Command>>(){}.getType(), new CommandDeserializer());
            Gson gson = gb.create();

            // Code from http://stackoverflow.com/questions/12384064/gson-convert-from-json-to-a-typed-arraylistt
            Type listType = new TypeToken<ArrayList<Command>>(){}.getType();

            list = gson.fromJson(in,listType);

            if(list == null){
                list = new ArrayList<Command>();
            }

        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            list = new ArrayList<Command>();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            throw new RuntimeException();
        }

        return list;
    }

    protected void saveInFile(ArrayList<Command> list, String FILENAME) {
        try {
            FileOutputStream fos = context.openFileOutput(FILENAME, 0);

            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(fos));

            GsonBuilder gb = new GsonBuilder();
            gb.registerTypeAdapter(new TypeToken<ArrayList<Command>>(){}.getType(), new CommandSerializer());
            Gson gson = gb.create();

            gson.toJson(list, out);
            out.flush();

            fos.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            throw new RuntimeException();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            throw new RuntimeException();
        }
    }



}
