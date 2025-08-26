package br.com.dougluciano.emissor_documento.configuration;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.CreateBucketRequest;
import software.amazon.awssdk.services.s3.model.HeadBucketRequest;
import software.amazon.awssdk.services.s3.model.NoSuchBucketException;

import java.net.URI;

@Configuration
public class ObjectStorageConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(ObjectStorageConfiguration.class);

    @Value("${object-storage.endpoint}")
    private String objectStorageEndpoint;

    @Value("${object-storage.region}")
    private String objectStorageRegion;

    @Value("${object-storage.access-key}")
    private String accessKey;

    @Value("${object-storage.secret-key}")
    private String secretKey;

    @Value("${object-storage.bucket-name}")
    private String bucketName;

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

    @Bean
    CommandLineRunner commandLineRunner(S3Client s3Client){
        return args -> {
            HeadBucketRequest headBucketRequest = HeadBucketRequest.builder()
                    .bucket(bucketName)
                    .build();
            try {
                s3Client.headBucket(headBucketRequest);
                logger.info("Bucket '{} already exists", bucketName);
            } catch (NoSuchBucketException e){
                logger.warn("Bucket '{}' not found! Creating bucket with name '{}'", bucketName, bucketName);
                CreateBucketRequest bucketRequest = CreateBucketRequest.builder()
                        .bucket(bucketName)
                        .build();
                s3Client.createBucket(bucketRequest);
                logger.info("Bucket with name '{}' was successfully created!", bucketName);
            }
        };
    }
}
