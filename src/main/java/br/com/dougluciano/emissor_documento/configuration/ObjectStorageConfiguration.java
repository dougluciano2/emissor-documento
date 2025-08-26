package br.com.dougluciano.emissor_documento.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

import java.net.URI;

@Configuration
public class ObjectStorageConfiguration {

    @Value("${object-storage.endpoint}")
    private String objectStorageEndpoint;

    @Value("${object-storage.region}")
    private String objectStorageRegion;

    @Value("${object-storage.access-key}")
    private String accessKey;

    @Value("${object-storage.secret-key}")
    private String secretKey;

    @Bean
    public S3Client s3Client(){
        StaticCredentialsProvider credentialsProvider = StaticCredentialsProvider.create(
                AwsBasicCredentials.create(accessKey, secretKey)
        );

        return S3Client.builder()
                .endpointOverride(URI.create(objectStorageEndpoint))
                .region(Region.of(objectStorageRegion))
                .credentialsProvider(credentialsProvider)
                .forcePathStyle(true)
                .build();
    }
}
