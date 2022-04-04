package mips;

import com.google.gson.Gson;
import mips.state.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class Control {
    public boolean isPropagating;
    public List<Instruction> instructions;
    public List<Storage> storageList;

    // for deep copying storage
    private Gson gson;

    // intermediate results
    private HashSet<IntegerQueueItem> executing;          // instructions being executed in the 1st cycle
    private HashSet<IntegerQueueItem> executing2;         // instructions being executed in the 2nd cycle
    private HashMap<Integer, Integer> forwardingPath;     // <PC, value>

    private static Logger logger = LoggerFactory.getLogger(Control.class);

    public Control(List<Instruction> instructions){
        this.isPropagating = true;
        this.instructions = instructions;
        this.storageList = new ArrayList<>();

        // initialize storage
        Storage initialStorage = new Storage();
        storageList.add(initialStorage);

        this.gson = new Gson();
        this.executing = new HashSet<>();
        this.executing2 = new HashSet<>();
        this.forwardingPath = new HashMap<>();
    }

    /**
     * propagate one cycle according to state of last Storage
     * deep copy and modify the copied Storage
     */
    public void propagate(){
        logger.info("---------- cycle " + storageList.size() + " ----------");

        // make a copy of all data structures to prepare the next state of the processor
        Storage storage = deepCopyLastStorage();

        if(!storage.Exception){
            // update the value according to the functionality of all units
            CM.execute(storage);
            EX.execute(storage, executing, executing2, forwardingPath);
            IS.execute(storage, executing, forwardingPath, instructions);
            RD.execute(storage, instructions, forwardingPath);
            FD.execute(storage, instructions);
        }else{
            CM.executeExceptionMode(storage, executing, executing2, instructions);
        }

        // append current storage to list
        storageList.add(storage);

        if(!storage.Exception){
            // check if finish (no instruction left & active list empty)
            if(storage.PC == instructions.size() && storage.ActiveList.isEmpty() && storage.DecodedPCs.isEmpty()){
                this.isPropagating = false;
            }
        }else{
            if(storage.ActiveList.isEmpty()){
                this.isPropagating = false;
            }
        }
    }

    private Storage deepCopyLastStorage(){
        Storage oldStorage = storageList.get(storageList.size() - 1);
        String str = gson.toJson(oldStorage);
        return gson.fromJson(str, Storage.class);
    }
}