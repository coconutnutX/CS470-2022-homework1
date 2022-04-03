package mips.state;

import mips.Control;
import mips.Instruction;
import mips.IntegerQueueItem;
import mips.Storage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * 3. Issue stage
 */
public class IS {

    private static Logger logger = LoggerFactory.getLogger(IS.class);

    /**
     * issue ready instructions from the Integer Queue
     */
    public static void execute(Storage storage, HashMap<IntegerQueueItem, Integer> executing, HashMap<Integer, Integer> forwardingPath, List<Instruction> instructions){

        // scans the Integer Queue, picks and issues at most four ready instructions
        // when more than four instructions are ready, the oldest instructions (with smaller PCs) are issued first
        List<IntegerQueueItem> issuedItem = new ArrayList<>();
        for(int i=0; i < storage.IntegerQueue.size(); i++){
            IntegerQueueItem integerQueueItem = storage.IntegerQueue.get(i);
            boolean ready = integerQueueItem.checkReady(storage, forwardingPath, instructions.get(integerQueueItem.PC));
            if(ready && issuedItem.size()<4 && executing.size()<4){
                // issue ready instruction
                executing.put(integerQueueItem, 0);
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
