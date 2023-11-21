package socket;

import java.util.List;

public interface MachinesOnlineChanged {
    void addMachine(ClientMachine machine);

    void removeMachine(ClientMachine machine);

    void updateMachine(List<ClientMachine> list);
}
