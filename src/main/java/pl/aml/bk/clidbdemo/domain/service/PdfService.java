package pl.aml.bk.clidbdemo.domain.service;

import lombok.RequiredArgsConstructor;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.LosslessFactory;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.springframework.stereotype.Service;
import pl.aml.bk.clidbdemo.domain.database.entity.OperationEntity;
import pl.aml.bk.clidbdemo.domain.database.entity.UserEntity;
import pl.aml.bk.clidbdemo.domain.database.repository.OperationEntityRepository;
import pl.aml.bk.clidbdemo.domain.service.exceptions.PdfServiceException;
import pl.aml.bk.clidbdemo.domain.service.exceptions.S3ServiceException;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PdfService {

    private final OperationEntityRepository operationEntityRepository;
    private final S3Service s3Service;

    /**
     * Generates a PDF report with charts for a user's operations
     *
     * @param userId     The ID of the user
     * @param outputPath The path where the PDF will be saved
     * @return A message indicating success or failure
     */
    public String generateUserOperationsPdf(Integer userId, String outputPath) {
        return generateUserOperationsPdf(userId, outputPath, false);
    }

    /**
     * Generates a PDF report with charts for a user's operations
     *
     * @param userId     The ID of the user
     * @param outputPath The path where the PDF will be saved
     * @param uploadToS3 Whether to upload the PDF to S3
     * @return A message indicating success or failure
     */
    public String generateUserOperationsPdf(Integer userId, String outputPath, boolean uploadToS3) {
        try {
            List<OperationEntity> operations = operationEntityRepository.findAllByUser_id(userId);

            if (operations.isEmpty()) {
                return "No operations found for user with ID: " + userId;
            }

            UserEntity user = operations.stream().findFirst().orElseThrow(() -> new PdfServiceException("Operations not found for user")).getUser();

            try (PDDocument document = new PDDocument()) {
                // Create first page with user info and charts
                PDPage firstPage = new PDPage(PDRectangle.A4);
                document.addPage(firstPage);

                try (PDPageContentStream contentStream = new PDPageContentStream(document, firstPage)) {
                    // Add title
                    contentStream.beginText();
                    contentStream.setFont(PDType1Font.HELVETICA_BOLD, 16);
                    contentStream.newLineAtOffset(50, 750);
                    contentStream.showText("Operations Report for " + user.getFirstName() + " " + user.getLastName());
                    contentStream.endText();

                    // Add user info
                    contentStream.beginText();
                    contentStream.setFont(PDType1Font.HELVETICA, 12);
                    contentStream.newLineAtOffset(50, 720);
                    contentStream.showText("User ID: " + user.getId());
                    contentStream.newLineAtOffset(0, -15);
                    contentStream.showText("Email: " + user.getEmail());
                    contentStream.endText();

                    // Generate and add pie chart for operation distribution
                    BufferedImage pieChart = createOperationDistributionChart(operations);
                    PDImageXObject pdImagePie = LosslessFactory.createFromImage(document, pieChart);
                    contentStream.drawImage(pdImagePie, 50, 500, 200, 200);

                    // Generate and add bar chart for operation amounts
                    BufferedImage barChart = createOperationAmountsChart(operations);
                    PDImageXObject pdImageBar = LosslessFactory.createFromImage(document, barChart);
                    contentStream.drawImage(pdImageBar, 300, 500, 250, 200);

                    // Add chart titles
                    contentStream.beginText();
                    contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
                    contentStream.newLineAtOffset(50, 480);
                    contentStream.showText("Operation Distribution");
                    contentStream.newLineAtOffset(250, 0);
                    contentStream.showText("Operation Amounts");
                    contentStream.endText();

                    // Add operations section title
                    contentStream.beginText();
                    contentStream.setFont(PDType1Font.HELVETICA_BOLD, 14);
                    contentStream.newLineAtOffset(50, 450);
                    contentStream.showText("Operations List");
                    contentStream.endText();

                    // Add operations table header
                    contentStream.beginText();
                    contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
                    contentStream.newLineAtOffset(50, 430);
                    contentStream.showText("Operation");
                    contentStream.newLineAtOffset(150, 0);
                    contentStream.showText("Amount");
                    contentStream.endText();

                    // Add operations table content - first page
                    float y = 410;
                    int operationsPerPage = 20; // Maximum operations that fit on a page
                    int operationsOnFirstPage = Math.min(operations.size(), operationsPerPage);

                    for (int i = 0; i < operationsOnFirstPage; i++) {
                        OperationEntity operation = operations.get(i);
                        contentStream.beginText();
                        contentStream.setFont(PDType1Font.HELVETICA, 12);
                        contentStream.newLineAtOffset(50, y);
                        contentStream.showText(operation.getOperationName().getValue());
                        contentStream.newLineAtOffset(150, 0);
                        contentStream.showText(operation.getAmount().toString());
                        contentStream.endText();
                        y -= 20;
                    }
                }

                // Create additional pages if needed
                int operationsPerPage = 30; // More operations can fit on subsequent pages
                int remainingOperations = operations.size() - Math.min(operations.size(), 20);
                int currentIndex = Math.min(operations.size(), 20);

                while (remainingOperations > 0) {
                    PDPage additionalPage = new PDPage(PDRectangle.A4);
                    document.addPage(additionalPage);

                    int operationsOnThisPage = Math.min(remainingOperations, operationsPerPage);

                    try (PDPageContentStream contentStream = new PDPageContentStream(document, additionalPage)) {
                        // Add page header
                        contentStream.beginText();
                        contentStream.setFont(PDType1Font.HELVETICA_BOLD, 14);
                        contentStream.newLineAtOffset(50, 750);
                        contentStream.showText("Operations List (Continued)");
                        contentStream.endText();

                        // Add operations table header
                        contentStream.beginText();
                        contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
                        contentStream.newLineAtOffset(50, 730);
                        contentStream.showText("Operation");
                        contentStream.newLineAtOffset(150, 0);
                        contentStream.showText("Amount");
                        contentStream.endText();

                        // Add operations table content
                        float y = 710;

                        for (int i = 0; i < operationsOnThisPage; i++) {
                            OperationEntity operation = operations.get(currentIndex + i);
                            contentStream.beginText();
                            contentStream.setFont(PDType1Font.HELVETICA, 12);
                            contentStream.newLineAtOffset(50, y);
                            contentStream.showText(operation.getOperationName().getValue());
                            contentStream.newLineAtOffset(150, 0);
                            contentStream.showText(operation.getAmount().toString());
                            contentStream.endText();
                            y -= 20;
                        }
                    }

                    currentIndex += operationsOnThisPage;
                    remainingOperations -= operationsOnThisPage;
                }

                document.save(outputPath);

                // Upload to S3 if requested
                if (uploadToS3) {
                    try {
                        String fileName = "user_" + userId + "_operations.pdf";
                        String s3Url = s3Service.uploadFile(outputPath, fileName);
                        return "PDF report generated successfully at " + outputPath + " and uploaded to S3 at " + s3Url;
                    } catch (S3ServiceException e) {
                        return "PDF report generated successfully at " + outputPath + " but failed to upload to S3: " + e.getMessage();
                    }
                }

                return "PDF report generated successfully at " + outputPath;
            }
        } catch (Exception e) {
            return "Error generating PDF: " + e.getMessage();
        }
    }

    /**
     * Creates a pie chart showing the distribution of operation types
     */
    private BufferedImage createOperationDistributionChart(List<OperationEntity> operations) {
        // Count operations by type
        Map<String, Long> operationCounts = operations.stream()
                .collect(Collectors.groupingBy(op -> op.getOperationName().getValue(), Collectors.counting()));

        // Create a simple pie chart using Java2D
        BufferedImage image = new BufferedImage(400, 400, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = image.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Fill background
        g2.setColor(Color.WHITE);
        g2.fillRect(0, 0, 400, 400);

        // Draw pie chart
        double total = operationCounts.values().stream().mapToLong(Long::longValue).sum();
        double currentAngle = 0.0;
        int centerX = 200;
        int centerY = 200;
        int radius = 150;

        // Colors for pie slices
        Color[] colors = {
                new Color(66, 133, 244),  // Blue
                new Color(219, 68, 55),   // Red
                new Color(244, 180, 0),   // Yellow
                new Color(15, 157, 88),   // Green
                new Color(171, 71, 188),  // Purple
                new Color(255, 112, 67),  // Orange
                new Color(158, 157, 36)   // Olive
        };

        int colorIndex = 0;
        int legendY = 20;

        for (Map.Entry<String, Long> entry : operationCounts.entrySet()) {
            double sliceAngle = 360.0 * entry.getValue() / total;

            // Draw the pie slice
            g2.setColor(colors[colorIndex % colors.length]);
            g2.fillArc(centerX - radius, centerY - radius, 2 * radius, 2 * radius,
                    (int) currentAngle, (int) sliceAngle);

            // Draw the legend
            g2.fillRect(20, legendY, 15, 15);
            g2.setColor(Color.BLACK);
            g2.drawString(entry.getKey() + " (" + entry.getValue() + ")", 40, legendY + 12);

            currentAngle += sliceAngle;
            colorIndex++;
            legendY += 20;
        }

        g2.dispose();
        return image;
    }

    /**
     * Creates a bar chart showing the amounts for each operation type
     */
    private BufferedImage createOperationAmountsChart(List<OperationEntity> operations) {
        // Sum amounts by operation type
        Map<String, BigDecimal> operationAmounts = operations.stream()
                .collect(Collectors.groupingBy(
                        op -> op.getOperationName().getValue(),
                        Collectors.reducing(BigDecimal.ZERO, OperationEntity::getAmount, BigDecimal::add)
                ));

        // Create a simple bar chart using Java2D
        BufferedImage image = new BufferedImage(500, 400, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = image.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Fill background
        g2.setColor(Color.WHITE);
        g2.fillRect(0, 0, 500, 400);

        // Draw axes
        g2.setColor(Color.BLACK);
        g2.drawLine(50, 350, 450, 350);  // X-axis
        g2.drawLine(50, 50, 50, 350);    // Y-axis

        // Find the maximum amount for scaling
        BigDecimal maxAmount = operationAmounts.values().stream()
                .max(BigDecimal::compareTo)
                .orElse(BigDecimal.ONE);

        // Draw bars
        int barWidth = 400 / (operationAmounts.size() * 2);
        int x = 80;

        for (Map.Entry<String, BigDecimal> entry : operationAmounts.entrySet()) {
            // Calculate bar height (scaled)
            int barHeight = (int) (250 * entry.getValue().doubleValue() / maxAmount.doubleValue());

            // Draw the bar
            g2.setColor(new Color(66, 133, 244));
            g2.fillRect(x, 350 - barHeight, barWidth, barHeight);

            // Draw the label
            g2.setColor(Color.BLACK);
            g2.drawString(entry.getKey(), x, 370);

            // Draw the value
            g2.drawString(entry.getValue().toString(), x, 345 - barHeight);

            x += barWidth * 2;
        }

        g2.dispose();
        return image;
    }
}
