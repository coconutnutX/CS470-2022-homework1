import java.util.List;

import mips.Instruction;
import mips.Storage;
import util.Parser;

public class Main {
    public static void main(String[] args) {
        List<Instruction> instructions = Parser.readInstruction("test.json");

        Storage storage = new Storage();
        Parser.outputJSON(storage);

    }
}