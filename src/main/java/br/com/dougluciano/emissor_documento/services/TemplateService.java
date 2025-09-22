package br.com.dougluciano.emissor_documento.services;

import br.com.dougluciano.emissor_documento.entities.Template;
import br.com.dougluciano.emissor_documento.entities.dtos.TemplateRequestDTO;
import br.com.dougluciano.emissor_documento.repository.TemplateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.nio.charset.StandardCharsets;
import java.text.Normalizer;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class TemplateService {

    private final TemplateRepository repository;
    private final S3Client objectStorage;

    @Value("${object-storage.bucket-name}")
    private String bucketName;

    private static final Pattern NONLATIN = Pattern.compile("[^\\w-]");
    private static final Pattern WHITESPACE = Pattern.compile("[\\s]");

    @Transactional(readOnly = true)
    public List<Template> findAll(){
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public Template findById(Long id){
        return repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Modelo não encontrado!"));
    }


    @Transactional
    public Template create(TemplateRequestDTO requestDTO){

        String slug = slugify(requestDTO.name());
        String storagePath = "templates/" + slug + ".html";

        objectStorage.putObject(
                PutObjectRequest.builder()
                        .bucket(bucketName)
                        .key(storagePath)
                        .contentType("text/html")
                        .build(),
                RequestBody.fromBytes(requestDTO.htmlContent().getBytes(StandardCharsets.UTF_8))
        );

        Template template = new Template();
        template.setName(requestDTO.name());
        template.setStoragePath(storagePath);



        return repository.save(template);
    }

    public ResponseBytes<GetObjectResponse> getTemplateContent(Long id){
        Template template = findById(id);
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(template.getStoragePath())
                .build();

        return objectStorage.getObjectAsBytes(getObjectRequest);
    }

    private String slugify(String input){
        String noWhiteSpace = WHITESPACE.matcher(input).replaceAll("-");
        String normalized = Normalizer.normalize(noWhiteSpace, Normalizer.Form.NFD);
        String slug = NONLATIN.matcher(normalized).replaceAll("");

        return slug.toLowerCase(Locale.ENGLISH);
    }
}
