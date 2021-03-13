import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;

public class ImportTokenIterator implements Iterator<ImportToken> {
    public static ImportTokenIterator from(Path path) throws FileNotFoundException {
        return new ImportTokenIterator(ImportLexer.from(path));
    }

    private ImportLexer lexer;
    private boolean encounteredException;
    private boolean reachedEnd;
    private ImportToken nextToken;

    ImportTokenIterator(ImportLexer lexer) {
        this.lexer = lexer;
    }

    public boolean hasNext() {
        if (this.reachedEnd) {
            return false;
        }
        if (this.encounteredException) {
            return false;
        }

        this.nextToken = this.advance();
        return this.nextToken != null;
    }

    private SourceToken tryGetNextSourceToken() {
        SourceToken token;
        try {
            token = this.lexer.lex();
        } catch (IOException e) {
            this.encounteredException = true;
            e.printStackTrace();
            this.tryCloseLexer();
            return null;
        }
        return token;
    }

    private ImportToken advance() {
        boolean packageToken = false;
        boolean importToken = false;
        boolean staticImport = false;
        boolean asteriskToken = false;

        ArrayList<String> idents = new ArrayList<String>();

        while (true) {
            SourceToken token = this.tryGetNextSourceToken();
            if (token == null) {
                return null;
            }

            switch (token.type) {
            case EOF:
            case END_OF_IMPORTS:
                this.reachedEnd = true;
                return null;
            case KW_IMPORT:
                importToken = true;
                break;
            case KW_PACKAGE:
                packageToken = true;
                break;
            case KW_STATIC:
                staticImport = true;
                break;
            case ASTERISK:
                asteriskToken = true;
                break;
            case IDENT:
                idents.add(token.literal);
                break;
            case SEMI:
                return this.makeToken(importToken, packageToken, staticImport, asteriskToken, idents);
            case ILLEGAL:
                this.encounteredException = true;
                this.tryCloseLexer();
                return null;
            }
        }
    }

    private ImportToken makeToken(boolean importToken, boolean packageToken, boolean staticImport,
            boolean asteriskToken, ArrayList<String> idents) {
        if (packageToken) {
            if (importToken || staticImport || asteriskToken) {
                return new ImportToken(ImportToken.Type.ILLEGAL, String.join(".", idents), "");
            }
            return new ImportToken(ImportToken.Type.PKG, String.join(".", idents), "");
        }
        if (importToken) {
            if (staticImport && asteriskToken) {
                String member = idents.remove(idents.size() - 1);
                return new ImportToken(ImportToken.Type.STATIC_WHOLE, String.join(".", idents), member);
            }
            if (staticImport) {
                String memberLast = idents.remove(idents.size() - 1);
                String memberFirst = idents.remove(idents.size() - 1);
                String member = String.join(".", memberFirst, memberLast);
                return new ImportToken(ImportToken.Type.STATIC_MEMBER, String.join(".", idents), member);
            }
            if (asteriskToken) {
                return new ImportToken(ImportToken.Type.WHOLE, String.join(".", idents), "");
            }
            String member = idents.remove(idents.size() - 1);
            return new ImportToken(ImportToken.Type.MEMBER, String.join(".", idents), member);
        }

        return new ImportToken(ImportToken.Type.ILLEGAL, String.join(".", idents), "");
    }

    private void tryCloseLexer() {
        try {
            this.lexer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ImportToken next() {
        ImportToken token = this.nextToken;
        this.nextToken = null;
        return token;
    }
}