package mips;

public class ActiveListItem {
    public boolean Done;
    public boolean Exception;
    public int LogicalDestination;
    public int OldDestination;
    public int PC;

    public ActiveListItem(int logicalDestination, int oldDestination, int PC){
        this.Done = false;
        this.Exception = false;
        this.LogicalDestination = logicalDestination;
        this.OldDestination = oldDestination;
        this.PC = PC;
    }
}