package pl.aml.bk.clidbdemo.domain.service;

import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.test.context.ActiveProfiles;
import pl.aml.bk.clidbdemo.domain.service.exceptions.S3ServiceException;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Test configuration for simulating S3 upload errors
 */
@TestConfiguration
class ErrorS3Config {

    @Bean
    @Primary
    public S3Service errorS3Service() {
        return new S3Service() {
            @Override
            public String uploadFile(String filePath, String key) {
                throw new S3ServiceException("Test S3 upload error");
            }
        };
    }
}

@SpringBootTest(
    properties = {"spring.shell.interactive.enabled=false"},
    classes = {ErrorS3Config.class}
)
@ActiveProfiles("test")
class PdfServiceErrorHandlingTest implements WithAssertions {

    private static final String TEST_PDF_FILE = "test-output.pdf";

    @Autowired
    private PdfService pdfService;

    @AfterEach
    void cleanup() {
        // Delete the test PDF file after each test
        try {
            Files.deleteIfExists(Paths.get(TEST_PDF_FILE));
        } catch (Exception e) {
            // Ignore exceptions during cleanup
        }
    }

    @Test
    @DisplayName("Generate user operations PDF should handle S3 upload error gracefully")
    void generateUserOperationsPdf_shouldHandleS3UploadError() {
        // When
        String result = pdfService.generateUserOperationsPdf("test.user1@example.com", TEST_PDF_FILE, true);

        // Then
        assertThat(result).isNotNull();
        assertThat(result).contains("PDF report generated successfully");
        assertThat(result).contains("but failed to upload to S3");

        // Verify that the file exists locally
        File pdfFile = new File(TEST_PDF_FILE);
        assertThat(pdfFile.exists()).isTrue();
        assertThat(pdfFile.length()).isGreaterThan(0);
    }
}
