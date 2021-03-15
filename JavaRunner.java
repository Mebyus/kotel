import java.io.IOException;

public class JavaRunner {
    public static void run(String target, String[] classPaths) throws IOException, InterruptedException {
        String[] jvmArgs = prepareJVMArgs(target, classPaths);
        ProcessBuilder b = new ProcessBuilder(jvmArgs);
        b.inheritIO();
        Process p = b.start();

        System.out.println(String.format("JVM exited with code: %d", p.waitFor()));
    }

    private static String joinClassPaths(String[] classPaths) {
        String delimeter = System.getProperty("os.name").startsWith("Windows") ? ";" : ":";
        return String.join(delimeter, classPaths);
    }

    private static String[] prepareJVMArgs(String target, String[] classPaths) {
        String[] compilerArgs = new String[4];
        compilerArgs[0] = "java";
        compilerArgs[1] = "-cp";
        compilerArgs[2] = joinClassPaths(classPaths);
        compilerArgs[3] = target;
        return compilerArgs;
    }
}
