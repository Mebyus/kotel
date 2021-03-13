public class SourceToken {
    public static enum Type {
        EOF, ILLEGAL, END_OF_IMPORTS, SEMI, DOT, ASTERISK, KW_PACKAGE, KW_IMPORT, KW_STATIC, IDENT
    }

    public SourceToken.Type type;
    public String literal;
    public int line;
    public int column;

    SourceToken(SourceToken.Type type, String literal, int line, int column) {
        this.type = type;
        this.literal = literal;
        this.line = line;
        this.column = column;
    }

    public boolean isEOF() {
        return this.type == SourceToken.Type.EOF;
    }

    public boolean isEnd() {
        return this.type == SourceToken.Type.END_OF_IMPORTS || this.type == SourceToken.Type.EOF;
    }

    public boolean isImportKeyword() {
        return this.type == SourceToken.Type.KW_IMPORT;
    }

    public boolean isPackageKeyword() {
        return this.type == SourceToken.Type.KW_PACKAGE;
    }

    public boolean isStaticKeyword() {
        return this.type == SourceToken.Type.KW_STATIC;
    }

    public boolean isAsterisk() {
        return this.type == SourceToken.Type.ASTERISK;
    }

    public boolean isSemi() {
        return this.type == SourceToken.Type.SEMI;
    }

    @Override
    public String toString() {
        return String.format("%3d:%3d   %15s   %s", this.line, this.column, this.type.name(), this.literal);
    }
}
