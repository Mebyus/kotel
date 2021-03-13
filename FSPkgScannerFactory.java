import java.nio.file.Path;
import java.util.Iterator;

public class FSPkgScannerFactory implements PkgScannerFactory {
    public Iterator<SourceFile> make(Path path) {
        return PkgScanner.from(path);
    }
}
