import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class DepStructure {
    public static class Pkg {
        public String name;
        public String path;
        private HashSet<String> imports;

        Pkg(String name, String path) {
            this.name = name;
            this.path = path;
            this.imports = new HashSet<String>();
        }

        public void addImport(String name) {
            this.imports.add(name);
        }

        public boolean contains(String name) {
            return this.imports.contains(name);
        }

        public void addFile(String name, String path) {

        }
    }

    private String root;
    private String path;
    private int currentIndex;
    private DepStructure.Pkg currentPkg;
    private ArrayList<Integer> currentVertice;
    private ArrayList<DepStructure.Pkg> pkgs;
    private HashMap<String, Integer> pkgIndexes;
    private ArrayList<ArrayList<Integer>> graph;

    DepStructure(String root, String path) {
        this.root = root;
        this.path = Path.of(path).toString();
        this.pkgs = new ArrayList<DepStructure.Pkg>();
        this.pkgIndexes = new HashMap<String, Integer>();
        this.graph = new ArrayList<ArrayList<Integer>>();
    }

    public String getPath(String name) {
        if (this.isExternal(name)) {
            return null;
        }
        String localPath = this.toLocalName(name).replace(".", "/");
        return Path.of(this.path, localPath).toString();
    }

    public boolean isExternal(String name) {
        return !name.startsWith(this.root);
    }

    public String toLocalName(String name) {
        return name.substring(this.root.length());
    }

    private DepStructure.Pkg addPkg(String name, String path) {
        this.pkgIndexes.put(name, this.pkgs.size());
        DepStructure.Pkg newPkg = new Pkg(name, path);
        this.graph.add(new ArrayList<Integer>());
        this.pkgs.add(newPkg);
        return newPkg;
    }

    public void addFile(String name, String path) {
        this.currentPkg.addFile(name, path);
    }

    public void addImport(String name) {
        if (this.isExternal(name)) {
            return;
        }
        if (!this.pkgIndexes.containsKey(name)) {
            this.addPkg(name, this.getPath(name));
        }
        if (!this.currentPkg.contains(name)) {
            this.currentVertice.add(this.pkgIndexes.get(name));
        }
        this.currentPkg.addImport(name);
    }

    public String next() {
        if (this.currentPkg == null) {
            this.currentPkg = this.addPkg(this.root, this.path);
            this.currentVertice = this.graph.get(0);
            return this.currentPkg.path;
        }
        this.currentIndex++;
        if (this.currentIndex >= this.pkgs.size()) {
            return null;
        }
        this.currentVertice = this.graph.get(this.currentIndex);
        this.currentPkg = this.pkgs.get(this.currentIndex);
        return this.pkgs.get(this.currentIndex).path;
    }

    public String name(int pkgIndex) {
        return this.pkgs.get(pkgIndex).name;
    }

    public int[][] graph() {
        int[][] g = new int[this.graph.size()][];
        for (int i = 0; i < g.length; i++) {
            ArrayList<Integer> a = this.graph.get(i);
            if (a.size() > 0) {
                g[i] = new int[a.size()];
                for (int j = 0; j < a.size(); j++) {
                    g[i][j] = a.get(j);
                }
            }
        }
        return g;
    }
}
