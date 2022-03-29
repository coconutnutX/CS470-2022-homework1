package mips.state;

import mips.ActiveListItem;
import mips.Storage;

import java.util.HashMap;

/**
 * 5. Commit stage
 */
public class CM {

    /**
     * 1. update active list
     * 2. retiring instructions
     * 3. recycling physical registers
     */
    public static void execute(Storage storage, HashMap<Integer, Integer> forwardingPath){
        // scans the Active list in the program order and picks instructions for retirement
        for(int i=0; i<storage.ActiveList.size(); i++){
            ActiveListItem activeListItem = storage.ActiveList.get(i);
            if(!activeListItem.Done){
                break;
            }

            // retire instruction

        }

        // picks instructions for retirement until any of the following happens:
        //• four instructions are already picked,
        //• an instruction is met that is not completed yet, or
        //• an instruction is met that is completed but triggers an exception. In this case, the processor will enter the Exception mode in the next cycle.

        // recycling physical registers and push them back to the Free List.
    }

}
