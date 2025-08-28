package br.com.dougluciano.emissor_documento.services;

import br.com.dougluciano.emissor_documento.entities.Document;
import br.com.dougluciano.emissor_documento.entities.Person;
import br.com.dougluciano.emissor_documento.repository.DocumentRepository;
import br.com.dougluciano.emissor_documento.repository.PersonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DocumentService {

    private final S3Client s3Client;
    private final DocumentRepository repository;
    private final PersonRepository personRepository;

    @Value("${object-storage.bucket-name}")
    private String bucketName;

    @Transactional(readOnly = true)
    public List<Document> findAll() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public Document findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Documento não encontrado com ID: " + id));
    }

    public ResponseBytes<GetObjectResponse> getDocumentContent(Long id) {
        Document document = findById(id);

        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(document.getStoragePath())
                .build();

        return s3Client.getObjectAsBytes(getObjectRequest);
    }

    @Transactional(readOnly = true)
    public List<Document> findByMedicalRecordByPerson(Long id){
        return repository.findAllByPersonIdOrderByMedicalRecordIndexAsc(id);
    }

    @Transactional
    public Document uploadDocument(String title, String htmlContent, Long id){

        Person existingPerson = personRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Pessoa não encontrada"));

        Optional<Document> lastDocument = repository.findByPersonIdOrderByMedicalRecordIndexDesc(id);

        int newDocumentIndex = lastDocument.map(doc -> doc.getMedicalRecordIndex() + 1).orElse(1);

        // creating path, file name and extension
        String fileExtension = ".html";
        String fileName = UUID.randomUUID().toString() + fileExtension;
        String storagePath = "documents/" + fileName;

        //Preparing upload to object storage
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(storagePath)
                .contentType("text/html")
                .build();

        s3Client.putObject(
                putObjectRequest,
                RequestBody.fromBytes(htmlContent.getBytes(StandardCharsets.UTF_8))
                );

        //save metadata into database
        Document document = new Document();
        document.setTitle(title);
        document.setFileType("text/html");
        document.setStoragePath(storagePath);
        document.setPerson(existingPerson);
        document.setMedicalRecordIndex(newDocumentIndex);

        return repository.save(document);
    }

    @Transactional
    public void deleteDocument(Long id) {
        Document document = findById(id);

        // 1. Deletar o objeto no MinIO
        DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                .bucket(bucketName)
                .key(document.getStoragePath())
                .build();
        s3Client.deleteObject(deleteObjectRequest);

        // 2. Deletar o registro de metadados no banco
        repository.delete(document);
    }
}
