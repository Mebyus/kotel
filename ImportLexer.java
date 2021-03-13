import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Path;

public class ImportLexer {
    public static ImportLexer from(Path path) throws FileNotFoundException {
        return new ImportLexer(new BufferedInputStream(new FileInputStream(path.toFile())));
    }

    private int line;
    private int column;
    private int endOfImportsStartColumn;
    private int backup;
    private boolean hasBackup;
    private boolean previousTokenWasSemicolon;
    private boolean reachedEOF;
    private boolean reachedEndOfImports;
    private InputStreamReader stream;

    ImportLexer(InputStream stream) {
        this.line = 1;
        this.stream = new InputStreamReader(stream, Charset.forName("utf-8"));
    }

    ImportLexer(InputStreamReader stream) {
        this.line = 1;
        this.stream = stream;
    }

    public void close() throws IOException {
        this.stream.close();
    }

    public SourceToken lex() throws IOException {
        if (this.reachedEndOfImports) {
            return this.endOfImports();
        }
        if (this.reachedEOF) {
            return this.eof();
        }

        SourceToken token = this.lexAnyToken();
        if (token.isEOF()) {
            this.reachedEOF = true;
            this.close();
            return token;
        }

        if (token.isSemi()) {
            this.previousTokenWasSemicolon = true;
            return token;
        }

        if (this.previousTokenWasSemicolon) {
            if (token.isImportKeyword()) {
                this.previousTokenWasSemicolon = false;
                return token;
            }
            this.reachedEndOfImports = true;
            this.endOfImportsStartColumn = token.column;
            this.close();
            return this.endOfImports();
        }

        this.previousTokenWasSemicolon = false;
        return token;
    }

    private SourceToken lexAnyToken() throws IOException {
        while (true) {
            int c = this.advance();
            if (c == -1) {
                return this.eof();
            }

            this.column++;

            switch (c) {
            case '\n':
                this.nextLine();
                break;
            case ';':
                return this.semi();
            case '.':
                return this.dot();
            case '*':
                return this.asterisk();
            default:
                if (Character.isWhitespace(c)) {
                    continue;
                } else {
                    SourceToken token = this.lexMultipleCharacters(c);
                    if (token == null) {
                        // comments are skipped and
                        // not represented by tokens
                        continue;
                    }
                    return token;
                }
            }
        }
    }

    private int advance() throws IOException {
        if (this.hasBackup) {
            this.hasBackup = false;
            return this.backup;
        }
        return this.stream.read();
    }

    private void backup(int c) {
        this.hasBackup = true;
        this.backup = c;
        this.column--;
    }

    private SourceToken lexMultipleCharacters(int startingCharacter) throws IOException {
        if (Character.isJavaIdentifierStart(startingCharacter)) {
            return lexKeywordOrIdentifier(startingCharacter);
        } else if (startingCharacter == '/') {
            return lexComment();
        }
        return illegal(startingCharacter, this.column);
    }

    private SourceToken lexComment() throws IOException {
        int startColumn = this.column;
        int c = this.advance();
        if (c == -1) {
            return this.illegal('/', startColumn);
        }

        this.column++;
        if (c == '/') {
            this.advanceSingleLineComment();
            return null;
        }
        if (c == '*') {
            this.advanceMultiLineComment();
            return null;
        }
        this.backup(c);
        return this.illegal('/', startColumn);
    }

    private void advanceSingleLineComment() throws IOException {
        while (true) {
            int c = this.advance();
            if (c == -1) {
                return;
            }

            this.column++;
            if (c == '\n') {
                this.nextLine();
                return;
            }
        }
    }

    private void advanceMultiLineComment() throws IOException {
        boolean previousCharacterWasAsterisk = false;
        while (true) {
            int c = this.advance();
            if (c == -1) {
                return;
            }

            this.column++;
            switch (c) {
            case '*':
                previousCharacterWasAsterisk = true;
                break;
            case '/':
                if (previousCharacterWasAsterisk) {
                    return;
                }
                break;
            case '\n':
                this.nextLine();
            default:
                previousCharacterWasAsterisk = false;
            }
        }
    }

    private SourceToken lexKeywordOrIdentifier(int startingCharacter) throws IOException {
        String lit = Character.toString(startingCharacter);
        int startColumn = this.column;
        while (true) {
            int c = this.advance();
            if (c == -1) {
                return this.keywordOrIdentifier(lit, startColumn);
            }

            this.column++;

            if (Character.isJavaIdentifierPart(c)) {
                lit += Character.toString(c);
            } else {
                this.backup(c);
                return this.keywordOrIdentifier(lit, startColumn);
            }
        }
    }

    private SourceToken keywordOrIdentifier(String literal, int startColumn) {
        switch (literal) {
        case "package":
            return this.kwPackage(startColumn);
        case "import":
            return this.kwImport(startColumn);
        case "static":
            return this.kwStatic(startColumn);
        default:
            return this.ident(literal, startColumn);
        }
    }

    private SourceToken ident(String literal, int startColumn) {
        return new SourceToken(SourceToken.Type.IDENT, literal, this.line, startColumn);
    }

    private SourceToken kwPackage(int startColumn) {
        return new SourceToken(SourceToken.Type.KW_PACKAGE, "package", this.line, startColumn);
    }

    private SourceToken kwImport(int startColumn) {
        return new SourceToken(SourceToken.Type.KW_IMPORT, "import", this.line, startColumn);
    }

    private SourceToken kwStatic(int startColumn) {
        return new SourceToken(SourceToken.Type.KW_STATIC, "static", this.line, startColumn);
    }

    private SourceToken semi() {
        return new SourceToken(SourceToken.Type.SEMI, ";", this.line, this.column);
    }

    private SourceToken illegal(int c, int startColumn) {
        return new SourceToken(SourceToken.Type.ILLEGAL, Character.toString(c), this.line, startColumn);
    }

    private SourceToken eof() {
        return new SourceToken(SourceToken.Type.EOF, "", this.line, this.column);
    }

    private SourceToken endOfImports() {
        return new SourceToken(SourceToken.Type.END_OF_IMPORTS, "", this.line, this.endOfImportsStartColumn);
    }

    private SourceToken dot() {
        return new SourceToken(SourceToken.Type.DOT, ".", this.line, this.column);
    }

    private SourceToken asterisk() {
        return new SourceToken(SourceToken.Type.ASTERISK, "*", this.line, this.column);
    }

    private void nextLine() {
        this.line++;
        this.column = 0;
    }
}