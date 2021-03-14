import java.io.IOException;
import java.nio.file.Path;

public class JavaBuilder {
    public static void build(DepStructure dependencyStructure, Path destination)
            throws IOException, InterruptedException {
        String[] srcPaths = dependencyStructure.listPaths();
        String[] compilerArgs = prepareCompilerArgs(srcPaths, destination);
        Process p = new ProcessBuilder(compilerArgs).start();
        System.out.println(String.format("Compiler exited with code: %d", p.waitFor()));
    }

    private static String[] prepareCompilerArgs(String[] srcPaths, Path destination) {
        String[] compilerArgs = new String[srcPaths.length + 3];
        compilerArgs[0] = "javac";
        compilerArgs[1] = "-d";
        compilerArgs[2] = destination.toString();
        for (int i = 0; i < srcPaths.length; i++) {
            compilerArgs[i + 3] = srcPaths[i];
        }
        return compilerArgs;
    }
}
