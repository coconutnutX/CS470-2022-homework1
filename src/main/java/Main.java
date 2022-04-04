import java.util.List;

import mips.Control;
import mips.Instruction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.Parser;

public class Main {

    private static Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        // String testFileName = args[0];
        String testFileName = "test_2.json";
        logger.info("input file: " + testFileName);

        // read and parse instruction
        List<Instruction> instructions = Parser.readInstruction(testFileName);

        // initialize control
        Control control = new Control(instructions);

        logger.info("start simulating...");
        while(control.isPropagating){
            control.propagate();
        }

        // dump JSON
        String outputFileName = testFileName.split("\\.")[0] + "_output.json";
        logger.info("output file: " + outputFileName);
        Parser.outputJSON(control.storageList, outputFileName);
    }
}