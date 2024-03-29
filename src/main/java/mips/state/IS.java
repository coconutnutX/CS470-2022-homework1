package mips.state;

import mips.dataStructure.Instruction;
import mips.dataStructure.IntegerQueueItem;
import mips.Storage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * 3. Issue stage
 */
public class IS {

    private static Logger logger = LoggerFactory.getLogger(IS.class);

    /**
     * issue ready instructions from the Integer Queue
     */
    public static void execute(Storage storage, HashSet<IntegerQueueItem> executing, HashMap<Integer, Long> forwardingPath, List<Instruction> instructions){

        // scans the Integer Queue, picks and issues at most four ready instructions
        // when more than four instructions are ready, the oldest instructions (with smaller PCs) are issued first
        List<IntegerQueueItem> issuedItem = new ArrayList<>();
        for(int i=0; i < storage.IntegerQueue.size(); i++){
            IntegerQueueItem integerQueueItem = storage.IntegerQueue.get(i);
            boolean ready = integerQueueItem.checkReady(storage, forwardingPath, instructions.get(integerQueueItem.PC));
            if(ready && issuedItem.size()<4 && executing.size()<4){
                // issue ready instruction
                executing.add(integerQueueItem);
                logger.info("issue: " + integerQueueItem.PC);

                issuedItem.add(integerQueueItem);
            }
        }

        // delete issued items from Integer Queue
        for(IntegerQueueItem item: issuedItem){
            storage.IntegerQueue.remove(item);
        }
    }

}
