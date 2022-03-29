package mips.state;

import mips.ActiveListItem;
import mips.Instruction;
import mips.IntegerQueueItem;
import mips.Storage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;

/**
 * 2. Rename & Dispatch stage
 */
public class RD {

    private static Logger logger = LoggerFactory.getLogger(RD.class);

    /**
     * rename instructions and check operands
     */
    public static void execute(Storage storage, List<Instruction> instructions, HashMap<Integer, Integer> forwardingPath){

        // should not exceed 4
        while(storage.DecodedPCs.size() > 0){
            // check if there are enough physical resources
            if(storage.FreeList.size() == 0 || storage.ActiveList.size() > 32 || storage.IntegerQueue.size() > 32){
                logger.warn("no enough physical resources");
                break;
            }

            renameAndAllocate(storage, instructions, forwardingPath);
        }
    }

    private static void renameAndAllocate(Storage storage, List<Instruction> instructions, HashMap<Integer, Integer> forwardingPath){

        // rename the instructions decoded from the previous stage
        int PC = storage.DecodedPCs.remove();
        int phyReg = storage.FreeList.remove();

        // updated the Register Map Table and Free List accordingly
        Instruction instruction = instructions.get(PC);
        int oldDestination = storage.RegisterMapTable[instruction.dest];
        storage.RegisterMapTable[instruction.dest] = phyReg;
        storage.BusyBitTable[phyReg] = true;

        logger.info("rename: " + instruction + " -> " + phyReg);

        // allocate newly renamed entries in the Active List
        storage.ActiveList.add(new ActiveListItem(instruction.dest, oldDestination, PC));

        // allocate newly renamed entries in the Integer Queue
        IntegerQueueItem integerQueueItem = new IntegerQueueItem(phyReg, instruction.instr, PC);

        // determine the state of the operands
        integerQueueItem.checkReady(storage, forwardingPath, instruction);

        storage.IntegerQueue.add(integerQueueItem);
    }



}
