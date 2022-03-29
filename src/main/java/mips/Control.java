package mips;

import com.google.gson.Gson;
import mips.state.CM;
import mips.state.EX;
import mips.state.FD;
import mips.state.RD;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Control {
    public boolean isPropagating;
    public List<Instruction> instructions;
    public List<Storage> storageList;

    // for deep copying storage
    private Gson gson;

    // intermediate results
    private Integer backpressure;                     // indicate # fewer instructions to fetch, range(0,4)
    private HashMap<Integer, Integer> executingList;  // <PC, cycle executed>
    private HashMap<Integer, Integer> forwardingPath; // <PC, value>

    private static Logger logger = LoggerFactory.getLogger(Control.class);

    public Control(List<Instruction> instructions){
        this.isPropagating = true;
        this.instructions = instructions;
        this.storageList = new ArrayList<>();
        // initialize storage
        Storage initialStorage = new Storage();
        storageList.add(initialStorage);

        this.gson = new Gson();

        this.backpressure = 0;
    }

    /**
     * propagate one cycle according to state of last Storage
     * deep copy and modify the copied Storage
     */
    public void propagate(){
        logger.info("---------- cycle " + storageList.size() + " ----------");

        // make a copy of all data structures to prepare the next state of the processor
        Storage storage = deepCopyLastStorage();

        // update the value according to the functionality of all units
        EX.execute(instructions, executingList, forwardingPath);
        CM.execute(storage, forwardingPath);
        RD.checkBackpressure(storage, backpressure);
        FD.execute(storage, backpressure, instructions);
        RD.execute(storage, instructions);

        // append current storage to list
        storageList.add(storage);

        // check if finish (no instruction left & active list empty)
        if(storage.PC == instructions.size()-1 && storage.ActiveList.isEmpty()){
            this.isPropagating = false;
        }
    }

    private Storage deepCopyLastStorage(){
        Storage oldStorage = storageList.get(storageList.size() - 1);
        String str = gson.toJson(oldStorage);
        return gson.fromJson(str, Storage.class);
    }
}