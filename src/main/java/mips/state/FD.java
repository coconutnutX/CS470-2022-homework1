package mips.state;

import mips.Instruction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import mips.Storage;

import java.util.List;

/**
 * 1. Fetch & Decode stage
 */
public class FD {

    private static Logger logger = LoggerFactory.getLogger(FD.class);

    /**
     * fetch instructions and update PC
     */
    public static void execute(Storage storage, List<Instruction> instructions){
        int cnt = 0;
        while(cnt < 4  && storage.PC < instructions.size() && storage.DecodedPCs.size() < 4){
            logger.info("fetch: " + instructions.get(storage.PC));
            storage.DecodedPCs.add(storage.PC);
            storage.PC++;
            cnt++;
        }
    }

}
