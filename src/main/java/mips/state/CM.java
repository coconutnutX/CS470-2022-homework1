package mips.state;

import mips.ActiveListItem;
import mips.Storage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
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
            if(!activeListItem.Done){
                break;
            }

            retiredItems.add(activeListItem);
            logger.info("retire: " + activeListItem.PC);

            // recycling physical registers and push them back to the Free List
            storage.FreeList.add(activeListItem.OldDestination);
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

}
