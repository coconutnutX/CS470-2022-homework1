package mips;

public class Instruction {
    public String instr; // NOTE: opCode of addi is add
    public Integer dest;
    public Integer opA;
    public Integer opB;

    public Instruction(String str){
        String[] split = str.split(" ");
        this.instr = split[0];
        this.dest = getRegId(split[1]);
        this.opA = getRegId(split[2]);
        if(this.instr.equals("addi")){
            this.opB = Integer.parseInt(split[3]);
        }else{
            this.opB = getRegId(split[3]);
        }
    }

    @Override
    public String toString() {
        return this.instr + " " + this.dest + " " + this.opA + " " + this.opB;
    }

    private Integer getRegId(String str){
        return Integer.parseInt(str.substring(1,str.length()-1));
    }
}
