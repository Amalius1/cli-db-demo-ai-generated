package pl.aml.bk.clidbdemo.commands;

import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.AfterEach;
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
class PrintUserOperationsCommandIntegrationTest implements WithAssertions {

    private static final String TEST_TEXT_FILE = "test-output.txt";
    private static final String TEST_PDF_FILE = "test-output.pdf";

    @Autowired
    private PrintUserOperationsCommand printUserOperationsCommand;

    @AfterEach
    void cleanup() {
        // Delete the test files after each test
        try {
            Files.deleteIfExists(Paths.get(TEST_TEXT_FILE));
            Files.deleteIfExists(Paths.get(TEST_PDF_FILE));
        } catch (Exception e) {
            // Ignore exceptions during cleanup
        }
    }

    @Test
    void printUserOperations_shouldReturnFormattedOperationsForUser1() {
        // When
        String result = printUserOperationsCommand.printUserOperations("test.user1@example.com");

        // Then
        assertThat(result).isNotNull();
        assertThat(result).contains("Operation: Deposit");
        assertThat(result).contains("Operation: Withdrawal");
        assertThat(result).contains("Operation: Transfer");
        assertThat(result).contains("Operation: Purchase");
    }

    @Test
    void saveUserOperations_shouldSaveOperationsToFile() {
        // When
        String result = printUserOperationsCommand.saveUserOperations("test.user1@example.com", TEST_TEXT_FILE);

        // Then
        assertThat(result).isNotNull();
        assertThat(result).contains("Operations saved successfully");

        // Verify that the file exists
        File textFile = new File(TEST_TEXT_FILE);
        assertThat(textFile.exists()).isTrue();
        assertThat(textFile.length()).isGreaterThan(0);
    }

    @Test
    void generatePdfReport_shouldCreatePdfFile() {
        // When
        String result = printUserOperationsCommand.generatePdfReport("test.user1@example.com", TEST_PDF_FILE, false);

        // Then
        assertThat(result).isNotNull();
        assertThat(result).contains("PDF report generated successfully");

        // Verify that the file exists
        File pdfFile = new File(TEST_PDF_FILE);
        assertThat(pdfFile.exists()).isTrue();
        assertThat(pdfFile.length()).isGreaterThan(0);
    }
}
