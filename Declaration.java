import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.List;

public class Declaration {
    public static String[] fields(String s) {
        return s.split("\\x20+");
    }

    public static Declaration from(Path path) throws IOException {
        List<String> declarationLines = Files.readAllLines(path.resolve(".kotel"), Charset.forName("utf-8"));
        Iterator<String> linesIterator = declarationLines.iterator();
        Declaration res = new Declaration("", path.toString(), "", "");
        while (linesIterator.hasNext()) {
            String line = linesIterator.next();
            String[] fields = Declaration.fields(line);
            if (fields.length > 1) {
                String attribute = fields[0];
                String value = fields[1];
                res.process(attribute, value);
            }
        }
        if (!res.entry.startsWith(res.root)) {
            res.entry = res.root + "." + res.entry;
        }
        return res;
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

    private void process(String attribute, String value) {
        switch (attribute) {
        case "root":
            this.root = value;
            break;
        case "lang":
            this.lang = value;
            break;
        case "entry":
            this.entry = value;
            break;
        default:
        }
    }
}
