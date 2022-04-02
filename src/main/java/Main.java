import java.util.List;

import mips.Control;
import mips.Instruction;
import util.Parser;

public class Main {
    public static void main(String[] args) {
        // read and parse instruction
        List<Instruction> instructions = Parser.readInstruction("test1.json");

        // initialize control
        Control control = new Control(instructions);

        int cnt = 0;
        while(control.isPropagating){
            control.propagate();
            cnt++;
            if(cnt == 100){
                break;
            }
        }

        // dump JSON
        Parser.outputJSON(control.storageList, "test_output1.json");
    }
}