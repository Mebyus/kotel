import java.util.HashSet;

public class CommandTemplate {
    public HashSet<String> boolArgs;
    public HashSet<String> valArgs;
    public String name;

    CommandTemplate(String name, String[] boolArgsKeys, String[] valArgsKeys) {
        this.name = name;
        this.boolArgs = new HashSet<String>();
        this.valArgs = new HashSet<String>();

        if (boolArgsKeys != null) {
            for (int i = 0; i < boolArgsKeys.length; i++) {
                this.boolArgs.add(boolArgsKeys[i]);
            }
        }

        if (valArgsKeys != null) {
            for (int i = 0; i < valArgsKeys.length; i++) {
                this.valArgs.add(valArgsKeys[i]);
            }
        }
    }
}
