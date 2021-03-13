public class ImportToken {
    public static enum Type {
        ILLEGAL, PKG, MEMBER, WHOLE, STATIC_MEMBER, STATIC_WHOLE
    }

    public Type type;
    public String pkgName;
    public String member;

    ImportToken(Type type, String pkgName, String member) {
        this.type = type;
        this.pkgName = pkgName;
        this.member = member;
    }

    @Override
    public String toString() {
        return String.format("%15s   %30s   %s", this.type.name(), this.pkgName, this.member);
    }
}
