import java.nio.file.Path;
import java.util.Iterator;

public interface PkgScannerFactory {
    Iterator<SourceFile> make(Path path);
}
