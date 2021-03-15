import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.List;

public class Declaration {
    public static Declaration from(Path path) throws IOException {
        List<String> declarationLines = Files.readAllLines(path.resolve(".kotel"), Charset.forName("utf-8"));
        Iterator<String> linesIterator = declarationLines.iterator();
        String root = "";
        String lang = "";
        String entry = "";
        while (linesIterator.hasNext()) {
            String line = linesIterator.next();
            if (line != null && line.startsWith("root")) {
                root = line.substring(5);
            }
            if (line != null && line.startsWith("lang")) {
                lang = line.substring(5);
            }
            if (line != null && line.startsWith("entry")) {
                entry = line.split("\\x20+")[1];
            }
        }
        if (!entry.startsWith(root)) {
            entry = root + "." + entry;
        }
        return new Declaration(root, path.toString(), lang, entry);
    }

    public String root;
    public String path;
    public String lang;
    public String entry;

    Declaration(String root, String path, String lang, String entry) {
        this.root = root;
        this.path = path;
        this.lang = lang;
        this.entry = entry;
    }
}
