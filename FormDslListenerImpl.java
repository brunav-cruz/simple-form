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

    public String getGeneratedCode() {
        StringBuilder sb = new StringBuilder();
        sb.append("<!DOCTYPE html>\n");
        sb.append("<html>\n");
        sb.append("<head>\n");
        sb.append("    <meta charset=\"UTF-8\">\n");
        sb.append("    <title>").append(formName).append("</title>\n");
        sb.append("    <style>\n");
        sb.append("        body { font-family: Arial, sans-serif; margin: 0; padding: 0; background-color: #f4f4f4; }\n");
        sb.append("        .container { max-width: 600px; margin: 50px auto; padding: 20px; background: white; box-shadow: 0 0 10px rgba(0, 0, 0, 0.1); }\n");
        sb.append("        h1 { text-align: center; color: ").append(titleColor).append("; }\n"); // cor do título
        sb.append("        form div { margin-bottom: 15px; }\n");
        sb.append("        label { display: block; margin-bottom: 5px; color: #333; }\n");
        sb.append("        input, select { width: 100%; padding: 8px; box-sizing: border-box; border: 1px solid #ccc; }\n");
        sb.append("        button { display: block; width: 100%; padding: 10px; background: ").append(buttonColor).append("; color: white; border: none; cursor: pointer; }\n"); // cor do botão
        sb.append("        button:hover { background: #cc8400; }\n");
        sb.append("    </style>\n");
        sb.append("    <script>\n");
        sb.append("        function handleSubmit(event) {\n");
        sb.append("            event.preventDefault();\n");
        sb.append("            const data = new FormData(event.target);\n");
        sb.append("            const value = Object.fromEntries(data.entries());\n");
        sb.append("            console.log(value);\n");
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
