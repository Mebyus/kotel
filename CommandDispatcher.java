import java.util.HashMap;

public class CommandDispatcher {
    private class Pair {
        public CommandTemplate template;
        public CommandExecutor executor;

        Pair(CommandTemplate template, CommandExecutor executor) {
            this.template = template;
            this.executor = executor;
        }
    }

    private HashMap<String, Pair> pairs;

    CommandDispatcher() {
        this.pairs = new HashMap<String, Pair>();
    }

    public void register(CommandTemplate template, CommandExecutor executor) {
        this.pairs.put(template.name, new Pair(template, executor));
    }

    public void dispatch(String[] args) throws Exception {
        String commandName;
        if (args == null || args.length == 0) {
            commandName = "install";
        } else {
            commandName = args[0];
        }
        Pair pair = this.pairs.get(commandName);
        Command command = new Command(pair.template);
        command.parse(args);
        pair.executor.execute(command);
    }
}
