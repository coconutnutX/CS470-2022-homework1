package mips;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;

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

    private static Logger logger = LoggerFactory.getLogger(IntegerQueueItem.class);

    public IntegerQueueItem(int destRegister, String opCode, int PC) {
        this.DestRegister = destRegister;
        this.OpCode = opCode;
        this.PC = PC;
    }

    public boolean checkReady(Storage storage, HashMap<Integer, Integer> forwardingPath, Instruction instruction) {
        // determine the state of operandA
        if(!OpAIsReady){
            int[] opA = checkOperandReady(storage, instruction.phyOpA, forwardingPath);
            OpARegTag = instruction.phyOpA;
            if(opA[0] != 0){
                OpAIsReady = true;
                OpAValue = opA[1];
            }
            logger.info("opA: [" + opA[0] + "] " + printOpA());
        }

        // determine the state of operandB
        if(!OpBIsReady){
            if(instruction.instr.equals("addi")){
                OpCode = "add";

                OpBIsReady = true;
                OpBValue = instruction.phyOpB;
                logger.info("opB: [3] " + printOpB());
            }else{
                int[] opB = checkOperandReady(storage, instruction.phyOpB, forwardingPath);
                OpBRegTag = instruction.phyOpB;
                if(opB[0] == 1){
                    OpBIsReady = true;
                    OpBValue = opB[1];
                }
                logger.info("opB: [" + opB[0] + "] " + printOpB());
            }
        }

        return OpAIsReady && OpBIsReady;
    }

    /**
     * return: int[0]
     * entry 1:
     *  - 0-not ready
     *  - 1-in physical register
     *  - 2-from forwarding path
     * entry 2: if ready, value of the operand
     */
    private static int[] checkOperandReady(Storage storage, int phyReg, HashMap<Integer, Integer> forwardingPath){
        int ready = 0;
        int value = 0;

        if(!storage.BusyBitTable[phyReg]){
            // (a) ready in the physical register file
            ready = 1;
            value = storage.PhysicalRegisterFile[phyReg];
        }else{
            // (b) ready from the forwarding path
            if(forwardingPath.containsKey(phyReg)){
                ready = 2;
                value = forwardingPath.get(phyReg);
            }
        }
        // (c) not produced yet

        return new int[]{ready, value};
    }

    public String printOpA() {
        return "ready=" + OpAIsReady + " reg=" + OpARegTag + " value=" + OpAValue;
    }

    public String printOpB() {
        return "ready=" + OpBIsReady + " reg=" + OpBRegTag + " value=" + OpBValue;
    }

    @Override
    public String toString() {
        // PC	Dest ID	OpA Ready	OpA Tag	OpA Value	OpB Ready	OpB Tag	OpB Value	OpCode
        return PC + " " + DestRegister + " " + OpAIsReady + " " + OpARegTag + " " + OpAValue + " " + OpBIsReady + " " + OpBRegTag + " " + OpBValue + " " + OpCode;
    }
}