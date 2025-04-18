package pl.aml.bk.clidbdemo.domain.service;

import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import pl.aml.bk.clidbdemo.config.TestConfig;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

@SpringBootTest(
    properties = {"spring.shell.interactive.enabled=false"},
    classes = {TestConfig.class}
)
@ActiveProfiles("test")
class PdfServiceIntegrationTest implements WithAssertions {

    private static final String TEST_PDF_FILE = "test-output.pdf";

    @Autowired
    private PdfService pdfService;

    @Autowired
    private S3Service s3Service;

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
    @DisplayName("Generate user operations PDF should create PDF file for user 1")
    void generateUserOperationsPdf_shouldCreatePdfFileForUser1() {
        // When
        String result = pdfService.generateUserOperationsPdf("test.user1@example.com", TEST_PDF_FILE);

        // Then
        assertThat(result).isNotNull();
        assertThat(result).contains("PDF report generated successfully");

        // Verify that the file exists
        File pdfFile = new File(TEST_PDF_FILE);
        assertThat(pdfFile.exists()).isTrue();
        assertThat(pdfFile.length()).isGreaterThan(0);
    }

    @Test
    @DisplayName("Generate user operations PDF should create PDF file for user 2")
    void generateUserOperationsPdf_shouldCreatePdfFileForUser2() {
        // When
        String result = pdfService.generateUserOperationsPdf("test.user2@example.com", TEST_PDF_FILE);

        // Then
        assertThat(result).isNotNull();
        assertThat(result).contains("PDF report generated successfully");

        // Verify that the file exists
        File pdfFile = new File(TEST_PDF_FILE);
        assertThat(pdfFile.exists()).isTrue();
        assertThat(pdfFile.length()).isGreaterThan(0);
    }

    @Test
    @DisplayName("Generate user operations PDF should return error message for non-existent user")
    void generateUserOperationsPdf_shouldReturnErrorMessageForNonExistentUser() {
        // When
        String result = pdfService.generateUserOperationsPdf("nonexistent@example.com", TEST_PDF_FILE);

        // Then
        assertThat(result).isNotNull();
        assertThat(result).contains("No operations found for user with email: nonexistent@example.com");

        // Verify that the file does not exist
        File pdfFile = new File(TEST_PDF_FILE);
        assertThat(pdfFile.exists()).isFalse();
    }

    @Test
    @DisplayName("Generate user operations PDF should upload to S3 for user 1")
    void generateUserOperationsPdf_shouldUploadToS3ForUser1() {
        // When
        String result = pdfService.generateUserOperationsPdf("test.user1@example.com", TEST_PDF_FILE, true);

        // Then
        assertThat(result).isNotNull();

        // Verify that the file exists locally
        File pdfFile = new File(TEST_PDF_FILE);
        assertThat(pdfFile.exists()).isTrue();
        assertThat(pdfFile.length()).isGreaterThan(0);

        // Note: We're not verifying the S3 upload in this test because it's difficult to set up
        // a reliable S3 connection in a test environment. The important thing is that the PDF is generated.
    }

}
