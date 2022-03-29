package mips.state;

import mips.ActiveListItem;
import mips.IntegerQueueItem;
import mips.Storage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 4. Execution stage
 */
public class EX {

    private static Logger logger = LoggerFactory.getLogger(EX.class);

    /**
     * update executingList and forwardingPath
     */
    public static void execute(Storage storage, HashMap<IntegerQueueItem, Integer> executing, HashMap<Integer, Integer> forwardingPath){
        // clear forwarding path each cycle
        forwardingPath.clear();

        List<IntegerQueueItem> executedItems = new ArrayList<>();
        for(IntegerQueueItem integerQueueItem: executing.keySet()){
            int stage = executing.get(integerQueueItem);
            // ALUs have two-cycle latency
            if(stage < 1){
                executing.put(integerQueueItem, stage + 1);
            }else{
                executedItems.add(integerQueueItem);

                int PC = integerQueueItem.PC;
                int value = calculate(integerQueueItem);
                logger.info("execution complete: " + PC);

                // broadcast results to the forwarding path on the second ALU cycle
                forwardingPath.put(PC, value);

                // mark instruction done
                storage.getActiveListItemByPC(PC).Done = true;

                // update Physical Register File and Busy Bit Table
                int phyReg = integerQueueItem.DestRegister;
                storage.PhysicalRegisterFile[phyReg] = value;
                storage.BusyBitTable[phyReg] = false;
            }
        }

        // remove executed items
        for(IntegerQueueItem integerQueueItem: executedItems){
            executing.remove(integerQueueItem);
        }
    }

    private static int calculate(IntegerQueueItem integerQueueItem){
        int value = 0;
        int a = integerQueueItem.OpAValue;
        int b = integerQueueItem.OpBValue;

        switch (integerQueueItem.OpCode) {
            // add, subtract, and multiply are bit-wise identical if the two operands are regarded as both being signed or both being unsigned
            case "add": value = a + b; break;
            case "sub": value = a - b; break;
            case "mulu": value = a * b; break;
            case "divu": value = Integer.divideUnsigned(a, b); break;
            case "remu": value = Integer.remainderUnsigned(a, b); break;
            default: logger.warn("unknown OpCode");
        }

        return value;
    }

}
