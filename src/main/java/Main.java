import java.util.List;

import mips.Control;
import mips.Instruction;
import util.Parser;

public class Main {
    public static void main(String[] args) {
        // read and parse instruction
        List<Instruction> instructions = Parser.readInstruction("test6.json");

        // initialize control
        Control control = new Control(instructions);

        while(control.isPropagating){
            control.propagate();
        }

        // dump JSON
        Parser.outputJSON(control.storageList, "test6_output.json");
    }
}