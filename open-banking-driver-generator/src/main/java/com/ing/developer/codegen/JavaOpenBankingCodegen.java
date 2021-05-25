package com.ing.developer.codegen;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.parameters.Parameter;
import org.openapitools.codegen.languages.JavaClientCodegen;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class JavaOpenBankingCodegen extends JavaClientCodegen {

    private static final List<String> excludedParams = Arrays.asList("Signature", "Digest", "Date");

    public JavaOpenBankingCodegen() {
        super();
        this.embeddedTemplateDir = this.templateDir = "Java";
    }

    public String getName() {
        return "Java";
    }

    public String getHelp() {
        return "Generates a Java client for ING Open Banking APIs";
    }

    public void preprocessOpenAPI(OpenAPI openAPI) {
        if (openAPI != null && openAPI.getPaths() != null) {
            for (Map.Entry<String, PathItem> entry : openAPI.getPaths().entrySet()) {
                for (Operation operation : entry.getValue().readOperations()) {
                    if (operation.getParameters() != null) {
                        operation.getParameters().forEach(this::ifAuthorizationThenNotRequired);
                        operation.getParameters().removeIf(this::isAnExcludedParameter);
                    }
                }
            }
        }
        super.preprocessOpenAPI(openAPI);
    }

    private void ifAuthorizationThenNotRequired(Parameter p) {
        if ("Authorization".equals(p.getName())) p.required(false);
    }

    private boolean isAnExcludedParameter(Parameter p) {
        return excludedParams.contains(p.getName());
    }

}
