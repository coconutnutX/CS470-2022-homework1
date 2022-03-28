package mips.state;

import mips.Storage;

import java.util.logging.Logger;

/**
 * 1. Fetch & Decode stage
 */
public class FD {

    static Logger logger =  java.util.logging.Logger.getLogger("logger");

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
