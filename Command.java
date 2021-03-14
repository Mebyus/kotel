import java.util.HashMap;
import java.util.Iterator;

public class Command {
    public String name;
    public String[] targets;
    public HashMap<String, Boolean> boolFlags;
    public HashMap<String, String> valFlags;

    Command(CommandTemplate template) {
        this.name = template.name;
        this.boolFlags = new HashMap<String, Boolean>();
        this.valFlags = new HashMap<String, String>();

        Iterator<String> boolIter = template.boolArgs.iterator();
        while (boolIter.hasNext()) {
            this.boolFlags.put(boolIter.next(), false);
        }

        Iterator<String> valIter = template.valArgs.iterator();
        while (valIter.hasNext()) {
            this.valFlags.put(valIter.next(), "");
        }
    }

    public void parse(String[] args) {
        if (args == null || args.length < 2) {
            this.targets = new String[0];
            return;
        }
        this.targets = new String[args.length - 1];
        for (int i = 0; i < args.length - 1; i++) {
            this.targets[i] = args[i + 1];
        }
    }
}
