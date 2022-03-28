package mips.state;

import mips.Storage;

/**
 * 2. Rename & Dispatch stage
 */
public class RD {

    /**
     * check if there are enough resources to dispatch new instructions
     * if not, apply backpressure
     */
    public static void checkBackpressure(Storage storage, Integer backpressure){
        backpressure = 0;
        // TODO
    }

}
