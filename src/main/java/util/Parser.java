package util;

import com.google.gson.Gson;
import mips.Instruction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Parser {

    private static Logger logger = LoggerFactory.getLogger(Parser.class);

    public static List<Instruction> readInstruction(String fileName){
        // read JSON file
        List<String> strList = readJSON(fileName);

        // parse instructions
        List<Instruction> insList = new ArrayList<>();
        logger.info("Parsing instruction:");
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

            Reader reader = Files.newBufferedReader(Paths.get(ClassLoader.getSystemResource(fileName).toURI()));

            // convert JSON file to list
            List<String> list = gson.fromJson(reader, List.class);

            logger.info("Parsing JSON:");
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

//    public static void outputJSON(Storage storage){
//        Gson gson = new Gson();
//        String str = gson.toJson(storage);
//        Storage newStorage = gson.fromJson(str, Storage.class);
//        System.out.println(str);
//    }
}
