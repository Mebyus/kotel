import java.nio.file.Path;
import java.util.Iterator;

public class SourceFile {
    public static SourceFile from(Path path) {
        return new SourceFile(path.getFileName().toString(), path.toString(), ImportTokenIterator.from(path));
    }

    public String name;
    public String path;
    public Iterator<ImportToken> tokens;

    SourceFile(String name, String path, Iterator<ImportToken> tokens) {
        this.name = name;
        this.path = path;
        this.tokens = tokens;
    }
}
