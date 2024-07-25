import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

import java.io.FileWriter;
import java.io.IOException;

public class FormGenerator {
    public static void main(String[] args) throws IOException {
        CharStream input = CharStreams.fromFileName("input.formdsl");
        FormDslLexer lexer = new FormDslLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        FormDslParser parser = new FormDslParser(tokens);
        ParseTree tree = parser.form();

        ParseTreeWalker walker = new ParseTreeWalker();
        FormDslListenerImpl listener = new FormDslListenerImpl();
        walker.walk(listener, tree);

        String generatedCode = listener.getGeneratedCode();

        try (FileWriter fileWriter = new FileWriter("form.html")) {
            fileWriter.write(generatedCode);
        }

        openHtmlInBrowser("form.html");
    }

    private static void openHtmlInBrowser(String filePath) throws IOException {
        java.awt.Desktop.getDesktop().browse(java.nio.file.Paths.get(filePath).toUri());
    }
}
