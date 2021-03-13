public class ImportToken {
    public static enum Type {
        PKG, MEMBER, WHOLE, STATIC_MEMBER, STATIC_WHOLE
    }

    public Type type;
    public String pkgName;
    public String member;

    ImportToken(Type type, String pkgName, String member) {
        this.type = type;
        this.pkgName = pkgName;
        this.member = member;
    }
}
