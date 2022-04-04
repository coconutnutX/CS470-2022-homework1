package util;

import com.google.gson.*;
import mips.Instruction;
import mips.PhyRegFile;
import mips.Storage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileWriter;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Parser {

    private static Logger logger = LoggerFactory.getLogger(Parser.class);

    // for custom serialization (int to unsigned)
    private static JsonSerializer<PhyRegFile> serializer = new JsonSerializer<PhyRegFile>() {
        @Override
        public JsonElement serialize(PhyRegFile src, Type typeOfSrc, JsonSerializationContext context) {
            JsonArray ret = new JsonArray();

            for(int value: src.arr){
                String unsignedIntString = Integer.toUnsignedString(value);
                ret.add(Long.valueOf(unsignedIntString));
            }

            return ret;
        }
    };

    public static List<Instruction> readInstruction(String fileName){
        // read JSON file
        List<String> strList = readJSON(fileName);

        // parse instructions
        List<Instruction> insList = new ArrayList<>();
        logger.info("parsing instruction:");
        for(String str: strList){
            Instruction instruction = new Instruction(str);
            insList.add(instruction);

            logger.info(instruction.toString());
        }
        return insList;
    }

    public static List<String> readJSON(String fileName){
        try {
            Gson gson = new Gson();

            Reader reader = Files.newBufferedReader(Paths.get(fileName));

            // convert JSON file to list
            List<String> list = gson.fromJson(reader, List.class);

            logger.info("parsing JSON:");
            for (String str: list) {
                logger.info(str);
            }

            reader.close();

            return list;
        } catch (Exception ex) {
            ex.printStackTrace();

            return null;
        }
    }

    public static void outputJSON(List<Storage> storageList, String filename){
        try {
            GsonBuilder gsonBuilder = new GsonBuilder();
            gsonBuilder.registerTypeAdapter(PhyRegFile.class, serializer);

            Gson customGson = gsonBuilder.create();

            Writer writer = new FileWriter(filename);

            logger.info("output JSON");
            String str = customGson.toJson(storageList);
            writer.write(str);

            writer.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
