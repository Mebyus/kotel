import java.nio.file.Path;

public class InstallExecutor implements CommandExecutor {
    public static Path getBuildDestination() {
        Path destination;
        String kotelHome = System.getenv("KOTEL_HOME");
        if (kotelHome != null && kotelHome != "") {
            System.out.println("KOTEL_HOME branch");
            destination = Path.of(kotelHome, "jbin");
        } else {
            String home = System.getProperty("user.home");
            destination = Path.of(home, "kotel", "jbin");
        }
        return destination;
    }

    private static String getTargetPath(String[] targets) {
        String targetPath;
        if (targets.length == 0) {
            targetPath = ".";
        } else {
            targetPath = targets[0];
        }
        return targetPath;
    }

    public void execute(Command command) throws Exception {
        String targetPath = getTargetPath(command.targets);
        Declaration declaration = Declaration.from(Path.of(targetPath));
        FSPkgScannerFactory f = new FSPkgScannerFactory();
        DepWalker w = new DepWalker(declaration.root, declaration.path, f);
        w.walk();
        JavaBuilder.build(w.getStructure(), getBuildDestination());
    }
}
