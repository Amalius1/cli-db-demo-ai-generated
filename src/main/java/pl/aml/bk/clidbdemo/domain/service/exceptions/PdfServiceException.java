package pl.aml.bk.clidbdemo.domain.service.exceptions;

/**
 * Custom exception for PDF service operations.
 * This exception is thrown when there are issues with PDF generation or processing.
 */
public class PdfServiceException extends RuntimeException {

    /**
     * Constructs a new PdfServiceException with the specified detail message.
     *
     * @param message the detail message
     */
    public PdfServiceException(String message) {
        super(message);
    }

    /**
     * Constructs a new PdfServiceException with the specified detail message and cause.
     *
     * @param message the detail message
     * @param cause the cause of the exception
     */
    public PdfServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
