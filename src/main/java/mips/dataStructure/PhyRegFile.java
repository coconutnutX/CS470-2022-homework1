package mips.dataStructure;

/**
 * an array of 64 unsigned 64bit integers
 * this class is used for custom serialization
 */
public class PhyRegFile {
    public long[] arr;

    public PhyRegFile(){
        this.arr = new long[64];
    }
}
