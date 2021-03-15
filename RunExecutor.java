import java.nio.file.Path;

public class RunExecutor implements CommandExecutor {
    public void execute(Command command) throws Exception {
        String targetPath = InstallExecutor.getTargetPath(command.targets);
        Declaration declaration = Declaration.from(Path.of(targetPath));
        String[] classPaths = { InstallExecutor.getBuildDestination().toString() };
        JavaRunner.run(declaration.entry, classPaths);
    }
}
