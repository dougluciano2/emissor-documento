package br.com.dougluciano.emissor_documento.services;

import br.com.dougluciano.emissor_documento.entities.DocumentField;
import br.com.dougluciano.emissor_documento.repository.DocumentFieldRepository;
import br.com.dougluciano.emissor_documento.repository.PersonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class TemplateGenerationService {

    private final PersonRepository personRepository;
    private final TemplateService templateService;
    private final DocumentFieldRepository fieldRepository;
    private final SpelExpressionParser expressionParser = new SpelExpressionParser();

    // Regex para encontrar qualquer texto entre colchetes, ex: [Placeholder]
    private static final Pattern PLACEHOLDER_PATTERN = Pattern.compile("\\[(.*?)\\]");

    public String generateDocumentFromTemplate(Long templateId, Long personId){

        //busca pessoa e o template
        var person = personRepository.findById(personId)
                .orElseThrow(() -> new IllegalArgumentException("Pessoa não encontrada com ID: " + personId));

        ResponseBytes<GetObjectResponse> templateContentBytes = templateService.getTemplateContent(templateId);

        String templateHtml = templateContentBytes.asUtf8String();

        // prepara o SpEL
        // pega a expressão, por ex. #person no spel e ve se existe correspondencia
        StandardEvaluationContext context = new StandardEvaluationContext();
        context.setVariable("person", person);

        Matcher matcher = PLACEHOLDER_PATTERN.matcher(templateHtml);
        StringBuilder finalHtml = new StringBuilder();

        while (matcher.find()){
            String placeholder = matcher.group(0); // O placeholder completo, ex: "[NomeCompleto]"

            String expressionString = fieldRepository.findByPlaceholder(placeholder)
                    .map(DocumentField::getExpression)
                    .orElse(null);

            if (expressionString != null){
                // executa a expressão SpEL
                Expression expression = expressionParser.parseExpression(expressionString);

                String resolvedValue = expression.getValue(context, String.class);

                matcher.appendReplacement(finalHtml, Matcher.quoteReplacement(resolvedValue != null ? resolvedValue : "[Sem informação no banco de dados]"));
            } else {
                matcher.appendReplacement(finalHtml, Matcher.quoteReplacement(placeholder));
            }

        }

        matcher.appendTail(finalHtml);

        return finalHtml.toString();
    }
}
