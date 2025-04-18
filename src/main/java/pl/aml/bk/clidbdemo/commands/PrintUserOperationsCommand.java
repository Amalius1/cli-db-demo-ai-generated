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
    public String printUserOperations(@ShellOption Integer userId) {
        return userService.printUserOperations(userId);
    }

    @ShellMethod(value = "save-operations")
    public String saveUserOperations(@ShellOption Integer userId, @ShellOption String fileName) {
        String operations = userService.printUserOperations(userId);
        try {
            Files.writeString(Path.of(fileName), operations);
            return "Operations saved successfully to " + fileName;
        } catch (Exception e) {
            return "Error saving operations: " + e.getMessage();
        }
    }

    @ShellMethod(value = "generate-pdf-report")
    public String generatePdfReport(
            @ShellOption Integer userId,
            @ShellOption String fileName,
            @ShellOption(defaultValue = "false") boolean uploadToS3) {
        return pdfService.generateUserOperationsPdf(userId, fileName, uploadToS3);
    }
}
