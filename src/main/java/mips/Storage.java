package mips;

import mips.dataStructure.ActiveListItem;
import mips.dataStructure.IntegerQueueItem;
import mips.dataStructure.PhyRegFile;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.*;
import java.util.stream.IntStream;

public class Storage {

    /**
     * - an unsigned integer pointing to the next instruction to fetch
     * - initialized to zero on reset
     */
    public int PC;

    /**
     * - an array with 64 elements
     * - each element is an unsigned 64bit integer
     * - initialized to zero on reset
     */
    public PhyRegFile PhysicalRegisterFile;

    /**
     * - an array that buffers instructions that have been decoded but have not been renamed and dispatched yet
     * - initialized empty since no instruction is waiting for renaming and dispatching
     */
    public Queue<Integer> DecodedPCs;

    /**
     * - an unsigned integer storing the value of the program counter of the instruction that triggers an exception
     * - initialized to zero
     */
    public int ExceptionPC;

    /**
     * - a boolean variable, indicating whether the processor is under the Exception Mode
     * - initialized to false
     */
    public boolean Exception;

    /**
     * - to mark enter exception mode in the next cycle
     * - use transient to exclude this field when serializing
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    public @interface Exclude {}

    @Exclude
    public boolean EnterException;
    @Exclude
    public int EnterExceptionPC;

    /**
     * - records the mapping relationship between architectural and physical registers
     * - an array with 32 elements
     * - on initialization, all architectural registers are mapped to physical registers with the same id
     */
    public int[] RegisterMapTable;

    /**
     * - keeps all physical registers that are available for renaming
     * - a FIFO structure, but we use an array to represent it
     * - on initialization, physical registers from p32 to p63 are free
     * - so the Free List should be reset to an array with 32 elements from 32 to 63
     */
    public Deque<Integer> FreeList;

    /**
     * - indicates whether the value of a specific physical register will be generated from the Execution stage,
     * - a table with 64 boolean values
     * - on initialization, an array with 64 false
     */
    public boolean[] BusyBitTable;

    /**
     * - keeps all instructions renamed and being executed
     * - modeled with an array, and every entry records the information of a specific instruction
     * - it is never possible to see more than 32 entries
     */
    public List<ActiveListItem> ActiveList;

    /**
     * - keeps all instructions awaiting issuing
     * - modeled with an array, and every entry records the operation information of a specific instruction
     * - initialized empty
     * - it is impossible to have more than 32 entries
     */
    public List<IntegerQueueItem> IntegerQueue;

    public Storage() {
        this.PC = 0;
        this.PhysicalRegisterFile = new PhyRegFile();
        this.DecodedPCs = new LinkedList<>();
        this.ExceptionPC = 0;
        this.Exception = false;
        this.EnterException = false;
        this.EnterExceptionPC = 0;
        this.RegisterMapTable = IntStream.range(0, 32).toArray();
        this.FreeList = new LinkedList<>();
        for(int i=32; i<=63; i++){
            FreeList.add(i);
        }
        this.BusyBitTable = new boolean[64];
        this.ActiveList = new ArrayList<>();
        this.IntegerQueue = new ArrayList<>();
    }

    public ActiveListItem getActiveListItemByPC(int PC){
        for(ActiveListItem activeListItem: ActiveList){
            if(activeListItem.PC == PC){
                return activeListItem;
            }
        }
        return null;
    }

}
