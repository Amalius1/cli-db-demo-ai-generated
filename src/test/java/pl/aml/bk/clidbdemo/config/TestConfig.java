package pl.aml.bk.clidbdemo.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.utility.DockerImageName;
import pl.aml.bk.clidbdemo.domain.service.S3Service;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.CreateBucketRequest;

import java.net.URI;

@TestConfiguration
@Profile("test")
public class TestConfig {

    private static final int MINIO_PORT = 9000;
    private static final String MINIO_ACCESS_KEY = "minioadmin";
    private static final String MINIO_SECRET_KEY = "minioadmin";
    private static final String TEST_BUCKET_NAME = "test-pdfs";

    @Bean(initMethod = "start", destroyMethod = "stop")
    public GenericContainer<?> minioContainer() {
        GenericContainer<?> container = new GenericContainer<>(DockerImageName.parse("minio/minio:latest"))
                .withExposedPorts(MINIO_PORT)
                .withEnv("MINIO_ROOT_USER", MINIO_ACCESS_KEY)
                .withEnv("MINIO_ROOT_PASSWORD", MINIO_SECRET_KEY)
                .withCommand("server /data");

        return container;
    }

    @Bean
    @DependsOn("minioContainer")
    public S3Client s3Client(GenericContainer<?> minioContainer) {
        // Wait for the container to be ready
        if (!minioContainer.isRunning()) {
            minioContainer.start();
        }

        String endpoint = String.format("http://localhost:%d", minioContainer.getMappedPort(MINIO_PORT));

        S3Client s3Client = S3Client.builder()
                .endpointOverride(URI.create(endpoint))
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(MINIO_ACCESS_KEY, MINIO_SECRET_KEY)))
                .region(Region.of("us-east-1"))
                .forcePathStyle(true) // Required for MinIO
                .build();

        // Create the test bucket
        try {
            CreateBucketRequest createBucketRequest = CreateBucketRequest.builder()
                    .bucket(TEST_BUCKET_NAME)
                    .build();
            s3Client.createBucket(createBucketRequest);
        } catch (Exception e) {
            // Bucket might already exist, which is fine
            System.out.println("Note: " + e.getMessage());
        }

        return s3Client;
    }

    @Bean
    @Primary
    @DependsOn("s3Client")
    public S3Service s3Service(GenericContainer<?> minioContainer, S3Client s3Client) {
        String endpoint = String.format("http://localhost:%d", minioContainer.getMappedPort(MINIO_PORT));

        // Create and configure the S3Service to use the MinIO container
        S3Service s3Service = new S3Service();
        s3Service.setS3Endpoint(endpoint);
        s3Service.setS3Region("us-east-1");
        s3Service.setS3AccessKey(MINIO_ACCESS_KEY);
        s3Service.setS3SecretKey(MINIO_SECRET_KEY);
        s3Service.setS3BucketName(TEST_BUCKET_NAME);

        return s3Service;
    }
}
