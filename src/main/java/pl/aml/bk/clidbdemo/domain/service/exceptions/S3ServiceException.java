package pl.aml.bk.clidbdemo.domain.service.exceptions;

/**
 * Exception thrown when there's an error in the S3 service operations
 */
public class S3ServiceException extends RuntimeException {

    public S3ServiceException(String message) {
        super(message);
    }

    public S3ServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
