import java.nio.file.Path;
import java.util.Iterator;

public class PkgScanner implements Iterator<SourceFile> {
    public static PkgScanner from(Path path) {
        return new PkgScanner();
    }

    public boolean hasNext() {
        return true;
    }

    public SourceFile next() {
        return SourceFile.from(Path.of("example/MyClass.java"));
    }
}
