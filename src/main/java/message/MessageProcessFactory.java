package message;

import java.util.HashMap;
import java.util.Map;

public class MessageProcessFactory {
    private Map<Character, MessageProcess> process = new HashMap<>();

    public MessageProcessFactory() {
        process.put('T', new TextMessageProcess());
        process.put('I', new ImageMessageProcess());
        process.put('C', new ConnectMessageProcess());
        process.put('S', new SelectedMessageProcess());
        process.put('K', new KeyMessageProcess());
        process.put('M', new MouseMessageProcess());
        // 添加更多的handler
    }

    public MessageProcess getProcess(char type) {
        return process.get(type);
    }
}
