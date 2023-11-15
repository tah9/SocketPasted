package socket;

public interface MachinesOnlineChanged {
    void addMachine(ClientMachine machine);

    void removeMachine(ClientMachine machine);
}
