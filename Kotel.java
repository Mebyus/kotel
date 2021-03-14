public class Kotel {
    public static void main(String[] args) throws Exception {
        CommandDispatcher dispatcher = new CommandDispatcher();

        String[] installBools = {};
        String[] installVals = {};
        dispatcher.register(new CommandTemplate("install", installBools, installVals), new InstallExecutor());

        dispatcher.dispatch(args);
    }
}
