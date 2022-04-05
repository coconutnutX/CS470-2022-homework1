package mips.dataStructure;

public class Instruction {
    public String instr; // NOTE: opCode of addi is add
    public long dest;
    public long opA;
    public long opB;
    public long phyOpA;
    public long phyOpB;

    public Instruction(String str){
        String[] split = str.split(" ");
        this.instr = split[0];
        this.dest = getRegId(split[1], 1);
        this.opA = getRegId(split[2], 1);
        if(this.instr.equals("addi")){
            this.opB = Long.parseLong(split[3]);
        }else{
            this.opB = getRegId(split[3], 0);
        }
    }

    @Override
    public String toString() {
        return this.instr + " " + this.dest + " " + this.opA + " " + this.opB;
    }

    private Long getRegId(String str, int ending){
        return Long.parseLong(str.substring(1,str.length()-ending));
    }
}
