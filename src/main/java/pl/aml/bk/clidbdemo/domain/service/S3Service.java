package pl.aml.bk.clidbdemo.domain.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pl.aml.bk.clidbdemo.domain.service.exceptions.S3ServiceException;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;

import java.io.File;
import java.net.URI;

@Service
@RequiredArgsConstructor
public class S3Service {

    @Value("${s3.endpoint}")
    private String s3Endpoint;

    @Value("${s3.region}")
    private String s3Region;

    @Value("${s3.access-key}")
    private String s3AccessKey;

    @Value("${s3.secret-key}")
    private String s3SecretKey;

    @Value("${s3.bucket-name}")
    private String s3BucketName;

    /**
     * Uploads a file to the S3 bucket
     *
     * @param filePath The path to the file to upload
     * @param key      The key (name) to use for the file in the S3 bucket
     * @return The URL of the uploaded file
     * @throws S3ServiceException if there's an error uploading the file
     */
    public String uploadFile(String filePath, String key) {
        try {
            S3Client s3Client = createS3Client();

            File file = new File(filePath);
            if (!file.exists()) {
                throw new S3ServiceException("File not found: " + filePath);
            }

            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(s3BucketName)
                    .key(key)
                    .build();

            s3Client.putObject(putObjectRequest, RequestBody.fromFile(file));

            return String.format("%s/%s/%s", s3Endpoint, s3BucketName, key);
        } catch (S3Exception e) {
            throw new S3ServiceException("Error uploading file to S3: " + e.getMessage(), e);
        }
    }

    /**
     * Creates an S3 client configured with the application's S3 settings
     *
     * @return A configured S3 client
     */
    protected S3Client createS3Client() {
        AwsBasicCredentials credentials = AwsBasicCredentials.create(s3AccessKey, s3SecretKey);

        return S3Client.builder()
                .region(Region.of(s3Region))
                .endpointOverride(URI.create(s3Endpoint))
                .credentialsProvider(StaticCredentialsProvider.create(credentials))
                .forcePathStyle(true) // Required for MinIO
                .build();
    }

    // Setter methods for testing
    public void setS3Endpoint(String s3Endpoint) {
        this.s3Endpoint = s3Endpoint;
    }

    public void setS3Region(String s3Region) {
        this.s3Region = s3Region;
    }

    public void setS3AccessKey(String s3AccessKey) {
        this.s3AccessKey = s3AccessKey;
    }

    public void setS3SecretKey(String s3SecretKey) {
        this.s3SecretKey = s3SecretKey;
    }

    public void setS3BucketName(String s3BucketName) {
        this.s3BucketName = s3BucketName;
    }
}
