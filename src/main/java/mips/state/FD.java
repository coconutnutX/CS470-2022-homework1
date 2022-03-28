package mips.state;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import mips.Storage;

/**
 * 1. Fetch & Decode stage
 */
public class FD {

    private static Logger logger = LoggerFactory.getLogger(FD.class);

    /**
     * fetch instructions and update PC
     */
    public static void execute(Storage storage, Boolean applyBackpressure){
        logger.info("test");
        if(applyBackpressure){
            return;
        }
    }

}
