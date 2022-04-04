package mips;

/**
 * an array of 64 unsigned 64bit integers
 * this class is used for custom serialization
 */
public class PhyRegFile {
    public int[] arr;

    public PhyRegFile(){
        this.arr = new int[64];
    }
}
