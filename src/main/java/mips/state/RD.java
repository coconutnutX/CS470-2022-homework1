package mips.state;

import mips.Control;
import mips.Instruction;
import mips.Storage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;

/**
 * 2. Rename & Dispatch stage
 */
public class RD {

    private static Logger logger = LoggerFactory.getLogger(RD.class);

    /**
     * rename instructions and check operands
     */
    public static void execute(Storage storage, List<Instruction> instructions){

        // should not exceed 4
        while(storage.DecodedPCs.size() > 0){
            rename(storage, instructions);
        }

    }

    /**
     * if there are enough physical resources,
     * rename the instructions decoded from the previous stage
     * and updated the Register Map Table and Free List accordingly
     */
    private static void rename(Storage storage, List<Instruction> instructions){

        if(storage.FreeList.size() == 0 || storage.ActiveList.size() > 32 || storage.IntegerQueue.size() > 32){
            logger.warn("no enough physical resources");
        }

        int PC = storage.DecodedPCs.remove();
        int phyReg = storage.FreeList.remove();

        Instruction instruction = instructions.get(PC);
        storage.RegisterMapTable[instruction.dest] = phyReg;
        storage.BusyBitTable[phyReg] = true;

        logger.info("rename: " + instruction + " -> " + phyReg);
    }

}
