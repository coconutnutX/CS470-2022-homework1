package mips.state;

import mips.dataStructure.ActiveListItem;
import mips.dataStructure.IntegerQueueItem;
import mips.Storage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * 5. Commit stage
 */
public class CM {

    private static Logger logger = LoggerFactory.getLogger(CM.class);

    /**
     * retiring instructions and recycling physical registers
     */
    public static void execute(Storage storage){
        // scans the Active list in the program order and picks instructions for retirement
        List<ActiveListItem> retiredItems = new ArrayList<>();
        int cnt = 0;
        for(int i=0; i<storage.ActiveList.size(); i++){
            ActiveListItem activeListItem = storage.ActiveList.get(i);

            if(activeListItem.Exception){
                // enter exception mode in the next cycle
                storage.EnterException = true;
                storage.EnterExceptionPC = activeListItem.PC;
                break;
            }

            if(!activeListItem.Done){
                break;
            }

            retiredItems.add(activeListItem);
            logger.info("retire: " + activeListItem.PC);

            // recycling physical registers and push them back to the Free List
            storage.FreeList.addLast(activeListItem.OldDestination);
            logger.info("recycle physical register " + activeListItem.OldDestination);

            cnt++;
            if(cnt == 4){
                break;
            }
        }

        // remove retired items from Active List
        for(ActiveListItem activeListItem: retiredItems){
            storage.ActiveList.remove(activeListItem);
        }
    }

    public static void executeExceptionMode(Storage storage, HashSet<IntegerQueueItem> executing, HashSet<IntegerQueueItem> executing2){
        // first cycle in exception recovery
        if(!storage.Exception){
            // record PC and set exception flag
            storage.Exception = true;
            storage.ExceptionPC = storage.EnterExceptionPC;

            // reset other structures
            storage.PC = 0x10000;
            storage.DecodedPCs.clear();
            storage.IntegerQueue.clear();
            executing.clear();
            executing2.clear();
        }

        // scans the Active list in reversed program order and picks instructions to recover
        List<ActiveListItem> recoveredItems = new ArrayList<>();
        int cnt = 0;
        for(int i=storage.ActiveList.size()-1; i>=0; i--){
            ActiveListItem activeListItem = storage.ActiveList.get(i);

            // recover Register Map Table, Free List, and Busy Bit Table
            int phyDest = storage.RegisterMapTable[activeListItem.LogicalDestination];
            storage.RegisterMapTable[activeListItem.LogicalDestination] = activeListItem.OldDestination;

            storage.FreeList.addLast(phyDest);
            storage.BusyBitTable[phyDest] = false;

            recoveredItems.add(activeListItem);
            logger.info("recover: " + activeListItem.PC);

            cnt++;
            if(cnt == 4){
                break;
            }
        }

        // remove recovered items from Active List
        for(ActiveListItem activeListItem: recoveredItems){
            storage.ActiveList.remove(activeListItem);
        }
    }

}
