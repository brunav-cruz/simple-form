import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;

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

        String generatedHtml = listener.getGeneratedHtml();
        String generatedServerJs = listener.getGeneratedServerJs();

        // Escrever o código gerado em um arquivo HTML
        try (FileWriter fileWriter = new FileWriter("../output/form.html")) {
            fileWriter.write(generatedHtml);
        }

        // Escrever o código gerado em um arquivo server.js
        try (FileWriter fileWriter = new FileWriter("../server/server.js")) {
            fileWriter.write(generatedServerJs);
        }

        // Salvar os campos do formulário em um arquivo JSON
        listener.saveFieldsToFile("../server/fields.json");

        // Criar o arquivo data.json vazio
        createEmptyDataJson("../server/data.json");

        // Abrir o arquivo HTML no navegador
        openHtmlInBrowser("../output/form.html");

        // Executar o servidor Node.js
        runServer();
    }

    private static void openHtmlInBrowser(String filePath) throws IOException {
        // Método para abrir o arquivo HTML no navegador
        java.awt.Desktop.getDesktop().browse(java.nio.file.Paths.get(filePath).toUri());
    }

    private static void runServer() throws IOException {
        // Método para executar o servidor Node.js
        String[] command = {"node", "../server/server.js"};
        ProcessBuilder pb = new ProcessBuilder(command);
        pb.directory(Paths.get(".").toFile());
        pb.inheritIO();
        pb.start();
    }

    private static void createEmptyDataJson(String filePath) throws IOException {
        try (FileWriter fileWriter = new FileWriter(filePath)) {
            fileWriter.write("[]");
        }
    }
}
