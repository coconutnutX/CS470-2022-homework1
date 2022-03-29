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
        
        // observe the results of forwarding paths
        // and update the Physical Register File as well as the Busy Bit Table
        // TODO
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

        // determine the state of operandA
        int[] opA = checkOperandReady(storage, instruction.opA, forwardingPath);
        integerQueueItem.OpARegTag = instruction.opA;
        if(opA[0] != 0){
            integerQueueItem.OpAIsReady = true;
            integerQueueItem.OpAValue = opA[1];
        }
        logger.info("opA: [" + opA[0] + "] " + integerQueueItem.printOpA());

        // determine the state of operandB
        if(instruction.instr.equals("addi")){
            integerQueueItem.OpCode = "add";

            integerQueueItem.OpBIsReady = true;
            integerQueueItem.OpBValue = instruction.opB;
            logger.info("opB: [3] " + integerQueueItem.printOpB());
        }else{
            int[] opB = checkOperandReady(storage, instruction.opB, forwardingPath);
            integerQueueItem.OpBRegTag = instruction.opB;
            if(opB[0] == 1){
                integerQueueItem.OpBIsReady = true;
                integerQueueItem.OpBValue = opB[1];
            }
            logger.info("opB: [" + opB[0] + "] " + integerQueueItem.printOpB());
        }

        storage.IntegerQueue.add(integerQueueItem);
    }

    /**
     * return: int[0]
     * entry 1:
     *  - 0-not ready
     *  - 1-in physical register
     *  - 2-from forwarding path
     * entry 2: if ready, value of the operand
     */
    private static int[] checkOperandReady(Storage storage, int arcReg, HashMap<Integer, Integer> forwardingPath){
        int ready = 0;
        int value = 0;

        int phyReg = storage.RegisterMapTable[arcReg];
        if(!storage.BusyBitTable[phyReg]){
            // (a) ready in the physical register file
            ready = 1;
            value = storage.PhysicalRegisterFile[phyReg];
        }else{
            // (b) ready from the forwarding path
            if(forwardingPath.containsKey(phyReg)){
                ready = 2;
                value = forwardingPath.get(phyReg);
            }
        }
        // (c) not produced yet

        return new int[]{ready, value};
    }

}
