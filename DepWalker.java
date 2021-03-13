import java.nio.file.Path;
import java.util.Iterator;

public class DepWalker {
    private DepStructure structure;
    private PkgScannerFactory factory;

    DepWalker(String root, String path, PkgScannerFactory factory) {
        this.structure = new DepStructure(root, path);
        this.factory = factory;
    }

    public void walk() {
        String next = this.structure.next();
        while (next != null) {
            Iterator<SourceFile> pkgScanner = this.factory.make(Path.of(next));
            this.scan(pkgScanner);
            next = this.structure.next();
        }
    }

    private void scan(Iterator<SourceFile> pkgScanner) {
        while (pkgScanner.hasNext()) {
            SourceFile file = pkgScanner.next();
            this.scan(file);
        }
    }

    private void scan(SourceFile file) {
        this.structure.addFile(file.name, file.path);
        while (file.tokens.hasNext()) {
            ImportToken token = file.tokens.next();
            this.handle(token);
        }
    }

    private void handle(ImportToken token) {
        if (token.type == ImportToken.Type.PKG) {
            // check pkg name with expected name
        } else {
            this.structure.addImport(token.pkgName);
        }
    }
}
