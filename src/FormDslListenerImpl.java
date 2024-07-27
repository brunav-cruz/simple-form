import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FormDslListenerImpl extends FormDslBaseListener {
    private String formName;
    private List<Field> fields = new ArrayList<>();
    private String buttonText = "Submit";
    private String buttonColor = "#FFA500";
    private String titleColor = "#FFA500";

    @Override
    public void enterForm(FormDslParser.FormContext ctx) {
        formName = ctx.ID().getText();
    }

    @Override
    public void enterField(FormDslParser.FieldContext ctx) {
        String fieldName = ctx.ID().getText();
        String fieldType = ctx.type().getText();
        List<String> options = new ArrayList<>();
        if (ctx.options() != null) {
            for (FormDslParser.OptionContext optionCtx : ctx.options().option()) {
                options.add(optionCtx.ID().getText());
            }
        }
        fields.add(new Field(fieldName, fieldType, options));
    }

    @Override
    public void enterButton(FormDslParser.ButtonContext ctx) {
        buttonText = ctx.STRING(0).getText().replace("\"", "");
        buttonColor = ctx.STRING(1).getText().replace("\"", "");
    }

    @Override
    public void enterTitle(FormDslParser.TitleContext ctx) {
        titleColor = ctx.STRING().getText().replace("\"", "");
    }

    private String darkenColor(String color) {
        // Remover o símbolo # se presente
        color = color.replace("#", "");

        // Converter a cor para inteiros
        int r = Integer.parseInt(color.substring(0, 2), 16);
        int g = Integer.parseInt(color.substring(2, 4), 16);
        int b = Integer.parseInt(color.substring(4, 6), 16);

        // Escurecer a cor em 20%
        r = (int) Math.max(0, r * 0.8);
        g = (int) Math.max(0, g * 0.8);
        b = (int) Math.max(0, b * 0.8);

        // Converter de volta para hexadecimal e retornar
        return String.format("#%02x%02x%02x", r, g, b);
    }

    public String getGeneratedHtml() {
        String hoverColor = darkenColor(buttonColor);
        StringBuilder sb = new StringBuilder();
        sb.append("<!DOCTYPE html>\n");
        sb.append("<html>\n");
        sb.append("<head>\n");
        sb.append("    <meta charset=\"UTF-8\">\n");
        sb.append("    <title>").append(formName).append("</title>\n");
        sb.append("    <style>\n");
        sb.append("        body { font-family: Arial, sans-serif; margin: 0; padding: 0; background-color: #f4f4f4; }\n");
        sb.append("        .container { max-width: 600px; margin: 50px auto; padding: 20px; background: white; box-shadow: 0 0 10px rgba(0, 0, 0, 0.1); }\n");
        sb.append("        h1 { text-align: center; color: ").append(titleColor).append("; }\n");
        sb.append("        form div { margin-bottom: 15px; }\n");
        sb.append("        label { display: block; margin-bottom: 5px; color: #333; }\n");
        sb.append("        input, select { width: 100%; padding: 8px; box-sizing: border-box; border: 1px solid #ccc; }\n");
        sb.append("        button { display: block; width: 100%; padding: 10px; background: ").append(buttonColor).append("; color: white; border: none; cursor: pointer; }\n");
        sb.append("        button:hover { background: ").append(hoverColor).append("; }\n");
        sb.append("    </style>\n");
        sb.append("    <script>\n");
        sb.append("        function handleSubmit(event) {\n");
        sb.append("            event.preventDefault();\n");
        sb.append("            const data = new FormData(event.target);\n");
        sb.append("            const value = Object.fromEntries(data.entries());\n");
        sb.append("            fetch('http://localhost:3000/submit', {\n");
        sb.append("                method: 'POST',\n");
        sb.append("                headers: { 'Content-Type': 'application/json' },\n");
        sb.append("                body: JSON.stringify(value)\n");
        sb.append("            }).then(response => response.text()).then(data => {\n");
        sb.append("                alert(data);\n");
        sb.append("            }).catch(error => {\n");
        sb.append("                console.error('Error:', error);\n");
        sb.append("            });\n");
        sb.append("        }\n");
        sb.append("    </script>\n");
        sb.append("</head>\n");
        sb.append("<body>\n");
        sb.append("    <div class=\"container\">\n");
        sb.append("        <h1>").append(formName).append("</h1>\n");
        sb.append("        <form onsubmit=\"handleSubmit(event)\">\n");
        for (Field field : fields) {
            sb.append("            <div>\n");
            sb.append("                <label>").append(field.name).append("</label>\n");
            if (field.type.equals("text")) {
                sb.append("                <input type=\"text\" name=\"").append(field.name).append("\" />\n");
            } else if (field.type.equals("select")) {
                sb.append("                <select name=\"").append(field.name).append("\">\n");
                for (String option : field.options) {
                    sb.append("                    <option value=\"").append(option).append("\">").append(option).append("</option>\n");
                }
                sb.append("                </select>\n");
            }
            sb.append("            </div>\n");
        }
        sb.append("            <button type=\"submit\">").append(buttonText).append("</button>\n");
        sb.append("        </form>\n");
        sb.append("    </div>\n");
        sb.append("</body>\n");
        sb.append("</html>\n");
        return sb.toString();
    }

    public String getGeneratedServerJs() {
        StringBuilder sb = new StringBuilder();
        sb.append("const express = require('express');\n");
        sb.append("const bodyParser = require('body-parser');\n");
        sb.append("const fs = require('fs');\n");
        sb.append("const path = require('path');\n");
        sb.append("const cors = require('cors');\n\n");
        sb.append("const app = express();\n");
        sb.append("const PORT = 3000;\n\n");
        sb.append("app.use(cors());\n");
        sb.append("app.use(bodyParser.json());\n");
        sb.append("app.use(bodyParser.urlencoded({ extended: true }));\n\n");
        sb.append("app.get('/', (req, res) => {\n");
        sb.append("    res.sendFile(path.join(__dirname, '../output/form.html'));\n");
        sb.append("});\n\n");
        sb.append("app.post('/submit', (req, res) => {\n");
        sb.append("    const data = req.body;\n");
        sb.append("    const formFields = JSON.parse(fs.readFileSync(path.join(__dirname, 'fields.json'), 'utf8'));\n\n");
        sb.append("    for (const field of formFields) {\n");
        sb.append("        if (!(field.name in data)) {\n");
        sb.append("            return res.status(400).send(`Campo ausente: ${field.name}`);\n");
        sb.append("        }\n");
        sb.append("        if (field.type === 'text' && typeof data[field.name] !== 'string') {\n");
        sb.append("            return res.status(400).send(`Campo inválido: ${field.name} deve ser texto`);\n");
        sb.append("        }\n");
        sb.append("        if (field.type === 'select' && !field.options.includes(data[field.name])) {\n");
        sb.append("            return res.status(400).send(`Campo inválido: ${field.name} deve ser uma das opções válidas`);\n");
        sb.append("        }\n");
        sb.append("    }\n\n");
        sb.append("    const filePath = path.join(__dirname, 'data.json');\n");
        sb.append("    fs.readFile(filePath, 'utf8', (err, jsonString) => {\n");
        sb.append("        if (err) {\n");
        sb.append("            console.log('File read failed:', err);\n");
        sb.append("            return res.status(500).send('Internal server error');\n");
        sb.append("        }\n");
        sb.append("        let jsonData = [];\n");
        sb.append("        if (jsonString) {\n");
        sb.append("            jsonData = JSON.parse(jsonString);\n");
        sb.append("        }\n");
        sb.append("        jsonData.push(data);\n\n");
        sb.append("        fs.writeFile(filePath, JSON.stringify(jsonData, null, 2), err => {\n");
        sb.append("            if (err) {\n");
        sb.append("                console.log('Error writing file:', err);\n");
        sb.append("                return res.status(500).send('Internal server error');\n");
        sb.append("            }\n");
        sb.append("            res.send('Data received and stored');\n");
        sb.append("        });\n");
        sb.append("    });\n");
        sb.append("});\n\n");
        sb.append("app.listen(PORT, () => {\n");
        sb.append("    console.log(`Server is running on http://localhost:${PORT}`);\n");
        sb.append("});\n");
        return sb.toString();
    }

    public void saveFieldsToFile(String filePath) {
        List<Field> fields = getFields();
        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write("[\n");
            for (int i = 0; i < fields.size(); i++) {
                Field field = fields.get(i);
                writer.write("  {\n");
                writer.write("    \"name\": \"" + field.name + "\",\n");
                writer.write("    \"type\": \"" + field.type + "\",\n");
                writer.write("    \"options\": " + optionsToString(field.options) + "\n");
                writer.write("  }" + (i < fields.size() - 1 ? "," : "") + "\n");
            }
            writer.write("]\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

     private String optionsToString(List<String> options) {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < options.size(); i++) {
            sb.append("\"").append(options.get(i)).append("\"");
            if (i < options.size() - 1) {
                sb.append(", ");
            }
        }
        sb.append("]");
        return sb.toString();
    }

    private List<Field> getFields() {
        return fields;
    }

    private static class Field {
        String name;
        String type;
        List<String> options;

        Field(String name, String type, List<String> options) {
            this.name = name;
            this.type = type;
            this.options = options;
        }
    }
}
