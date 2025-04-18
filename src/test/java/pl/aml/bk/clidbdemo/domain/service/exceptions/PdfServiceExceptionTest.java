package pl.aml.bk.clidbdemo.domain.service.exceptions;

import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for the PdfServiceException class
 */
class PdfServiceExceptionTest implements WithAssertions {

    @Test
    @DisplayName("Constructor with message should set message correctly")
    void constructor_withMessage_shouldSetMessage() {
        // Given
        String errorMessage = "Test error message";

        // When
        PdfServiceException exception = new PdfServiceException(errorMessage);

        // Then
        assertThat(exception.getMessage()).isEqualTo(errorMessage);
        assertThat(exception.getCause()).isNull();
    }

    @Test
    @DisplayName("Constructor with message and cause should set both correctly")
    void constructor_withMessageAndCause_shouldSetMessageAndCause() {
        // Given
        String errorMessage = "Test error message";
        Throwable cause = new RuntimeException("Cause exception");

        // When
        PdfServiceException exception = new PdfServiceException(errorMessage, cause);

        // Then
        assertThat(exception.getMessage()).isEqualTo(errorMessage);
        assertThat(exception.getCause()).isEqualTo(cause);
    }
}
