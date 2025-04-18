package pl.aml.bk.clidbdemo.commands;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import pl.aml.bk.clidbdemo.domain.service.PdfService;
import pl.aml.bk.clidbdemo.domain.service.UserService;

import java.nio.file.Files;
import java.nio.file.Path;

@ShellComponent
@RequiredArgsConstructor
class PrintUserOperationsCommand {

    private final UserService userService;
    private final PdfService pdfService;

    @ShellMethod(value = "print-operations")
    public String printUserOperations(@ShellOption String email) {
        return userService.printUserOperationsByEmail(email);
    }

    @ShellMethod(value = "save-operations")
    public String saveUserOperations(@ShellOption String email, @ShellOption String fileName) {
        String operations = userService.printUserOperationsByEmail(email);
        try {
            Files.writeString(Path.of(fileName), operations);
            return "Operations saved successfully to " + fileName;
        } catch (Exception e) {
            return "Error saving operations: " + e.getMessage();
        }
    }

    @ShellMethod(value = "generate-pdf-report")
    public String generatePdfReport(
            @ShellOption String email,
            @ShellOption String fileName,
            @ShellOption(defaultValue = "false") boolean uploadToS3) {
        return pdfService.generateUserOperationsPdf(email, fileName, uploadToS3);
    }
}
