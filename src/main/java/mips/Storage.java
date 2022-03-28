package mips;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
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
    public int[] PhysicalRegisterFile;

    /**
     * - an array that buffers instructions that have been decoded but have not been renamed and dispatched yet
     * - initialized empty since no instruction is waiting for renaming and dispatching
     */
    public List<Integer> DecodedPCs;

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
    public List<Integer> FreeList;

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

    public class ActiveListItem {
        public boolean Done;
        public boolean Exception;
        public int LogicalDestination;
        public int OldDestination;
        public int PC;
    }

    /**
     * - keeps all instructions awaiting issuing
     * - modeled with an array, and every entry records the operation information of a specific instruction
     * - initialized empty
     * - it is impossible to have more than 32 entries
     */
    public List<IntegerQueueItem> IntegerQueue;

    public class IntegerQueueItem {
        public int DestRegister;
        public boolean OpAIsReady;
        public int OpARegTag;
        public int OpAValue;
        public boolean OpBIsReady;
        public int OpBRegTag;
        public int OpBValue;
        public String OpCode;
        public int PC;
    }

    public Storage() {
        this.PC = 0;
        this.PhysicalRegisterFile = new int[64];
        Arrays.fill(PhysicalRegisterFile, 0);
        this.DecodedPCs = new ArrayList<>();
        this.ExceptionPC = 0;
        this.Exception = false;
        this.RegisterMapTable = IntStream.range(0, 32).toArray();
        this.FreeList = new LinkedList<>();
        for(int i=32; i<=63; i++){
            FreeList.add(i);
        }
        this.BusyBitTable = new boolean[64];
        this.ActiveList = new ArrayList<>();
        this.IntegerQueue = new ArrayList<>();
    }

}
