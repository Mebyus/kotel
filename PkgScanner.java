import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.util.Iterator;

public class PkgScanner implements Iterator<SourceFile> {
    public static PkgScanner from(Path path) {
        File f = path.toFile();
        if (!f.isDirectory()) {
            // probably throw an exception
        }
        return new PkgScanner(filterFiles(f.listFiles())); // need to filter out directories and take only java files
    }

    public static File[] filterFiles(File[] files) {
        if (files == null) {
            return files;
        }
        int validCounter = 0;
        boolean[] valid = new boolean[files.length];
        for (int i = 0; i < files.length; i++) {
            if (!files[i].isDirectory() && files[i].getName().endsWith(".java")) {
                valid[i] = true;
                validCounter++;
            }
        }
        File[] filtered = new File[validCounter];
        int filteredIndex = 0;
        for (int i = 0; i < files.length; i++) {
            if (valid[i]) {
                filtered[filteredIndex] = files[i];
                filteredIndex++;
            }
        }
        return filtered;
    }

    private File[] files;
    private int index;

    PkgScanner(File[] files) {
        this.files = files;
    }

    public boolean hasNext() {
        return this.files != null && this.index < this.files.length;
    }

    public SourceFile next() {
        File f = this.files[this.index];
        this.index++;
        SourceFile sf = null;
        try {
            sf = SourceFile.from(f.toPath());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return sf;
    }
}
