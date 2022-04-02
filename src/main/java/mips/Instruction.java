package mips;

public class Instruction {
    public String instr; // NOTE: opCode of addi is add
    public Integer dest;
    public Integer opA;
    public Integer opB;
    public Integer phyOpA;
    public Integer phyOpB;

    public Instruction(String str){
        String[] split = str.split(" ");
        this.instr = split[0];
        this.dest = getRegId(split[1], 1);
        this.opA = getRegId(split[2], 1);
        if(this.instr.equals("addi")){
            this.opB = Integer.parseInt(split[3]);
        }else{
            this.opB = getRegId(split[3], 0);
        }
    }

    @Override
    public String toString() {
        return this.instr + " " + this.dest + " " + this.opA + " " + this.opB;
    }

    private Integer getRegId(String str, int ending){
        return Integer.parseInt(str.substring(1,str.length()-ending));
    }
}
