import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.antlr.v4.runtime.tree.TerminalNode;
import java.util.ArrayList;
import java.util.List;

public class FormDslListenerImpl extends FormDslBaseListener {
    private String formName;
    private List<String> fields = new ArrayList<>();

    @Override
    public void enterForm(FormDslParser.FormContext ctx) {
        formName = ctx.ID().getText();
    }

    @Override
    public void enterField(FormDslParser.FieldContext ctx) {
        fields.add(ctx.ID().getText());
    }

    public String getGeneratedCode() {
        StringBuilder sb = new StringBuilder();
        sb.append("import React from 'react';\n");
        sb.append("import { useForm } from 'react-hook-form';\n\n");
        sb.append("function ").append(formName).append("() {\n");
        sb.append("  const { register, handleSubmit } = useForm();\n");
        sb.append("  const onSubmit = data => console.log(data);\n\n");
        sb.append("  return (\n");
        sb.append("    <form onSubmit={handleSubmit(onSubmit)}>\n");
        for (String field : fields) {
            sb.append("      <div>\n");
            sb.append("        <label>").append(field).append("</label>\n");
            sb.append("        <input {...register('").append(field).append("')} />\n");
            sb.append("      </div>\n");
        }
        sb.append("      <button type='submit'>Submit</button>\n");
        sb.append("    </form>\n");
        sb.append("  );\n");
        sb.append("}\n\n");
        sb.append("export default ").append(formName).append(";\n");
        return sb.toString();
    }
}
