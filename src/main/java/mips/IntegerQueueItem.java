package mips;

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

    public IntegerQueueItem(int destRegister, String opCode, int PC) {
        this.DestRegister = destRegister;
        this.OpCode = opCode;
        this.PC = PC;
    }

    public String printOpA() {
        return "ready=" + OpAIsReady + " reg=" + OpARegTag + " value=" + OpAValue;
    }

    public String printOpB() {
        return "ready=" + OpBIsReady + " reg=" + OpBRegTag + " value=" + OpBValue;
    }
}