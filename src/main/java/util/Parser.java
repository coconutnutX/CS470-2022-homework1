package util;

import com.google.gson.Gson;
import mips.Instruction;
import mips.Storage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileWriter;
import java.io.Reader;
import java.io.Writer;
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

            // Reader reader = Files.newBufferedReader(Paths.get(ClassLoader.getSystemResource(fileName).toURI()));
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
            Gson gson = new Gson();

            Writer writer = new FileWriter(filename);

            logger.info("output JSON");
            String str = gson.toJson(storageList);
            writer.write(str);

            writer.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
