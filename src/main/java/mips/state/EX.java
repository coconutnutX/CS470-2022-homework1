package mips.state;

import mips.dataStructure.IntegerQueueItem;
import mips.Storage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.HashSet;

/**
 * 4. Execution stage
 */
public class EX {

    private static Logger logger = LoggerFactory.getLogger(EX.class);

    /**
     * update executingList and forwardingPath
     */
    public static void execute(Storage storage, HashSet<IntegerQueueItem> executing, HashSet<IntegerQueueItem> executing2, HashMap<Integer, Long> forwardingPath){
        // clear forwarding path each cycle
        forwardingPath.clear();

        // instructions in the 2nd cycle
        for(IntegerQueueItem integerQueueItem: executing2){
            int PC = integerQueueItem.PC;
            try{
                long value = calculate(integerQueueItem);
                logger.info("execution complete: " + PC);

                // broadcast results to the forwarding path on the second ALU cycle
                forwardingPath.put(PC, value);

                // mark instruction done
                storage.getActiveListItemByPC(PC).Done = true;

                // update Physical Register File and Busy Bit Table
                long phyReg = integerQueueItem.DestRegister;
                storage.PhysicalRegisterFile.arr[(int)phyReg] = value;
                storage.BusyBitTable[(int)phyReg] = false;
            }catch(Exception e){
                logger.warn("exception in: " + PC);

                // mark exception
                storage.getActiveListItemByPC(PC).Exception = true;
                storage.getActiveListItemByPC(PC).Done = true;
            }
        }
        executing2.clear();

        // instructions in the 1st cycle
        for(IntegerQueueItem integerQueueItem: executing){
            executing2.add(integerQueueItem);
        }
        executing.clear();
    }

    private static long calculate(IntegerQueueItem integerQueueItem){
        long value = 0;
        long a = integerQueueItem.OpAValue;
        long b = integerQueueItem.OpBValue;

        switch (integerQueueItem.OpCode) {
            // add, subtract, and multiply are bit-wise identical if the two operands are regarded as both being signed or both being unsigned
            case "add": value = a + b; break;
            case "sub": value = a - b; break;
            case "mulu": value = a * b; break;
            case "divu": value = Long.divideUnsigned(a, b); break;
            case "remu": value = Long.remainderUnsigned(a, b); break;
            default: logger.warn("unknown OpCode");
        }

        return value;
    }

}
