import java.io.InputStream;
import java.nio.file.Path;
import java.util.Iterator;

public class ImportTokenIterator implements Iterator<ImportToken> {
    public static ImportTokenIterator from(Path path) {
        return new ImportTokenIterator(new ImportLexer(InputStream.nullInputStream()));
    }

    private ImportLexer lexer;

    ImportTokenIterator(ImportLexer lexer) {
        this.lexer = lexer;
    }

    public boolean hasNext() {
        return true;
    }

    public ImportToken next() {
        return new ImportToken(ImportToken.Type.PKG, "pkg.name", "MyClass");
    }
}