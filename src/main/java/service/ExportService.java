package service;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;
import exception.DownloadPermissionException;
import exception.StorageException;
import model.WeatherData;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ExportService {
    private final boolean simulateLowStorage;
    private final boolean simulateNoPermission;
    private final NotificationService notificationService; // 新增：注入提示服务


    public ExportService(boolean simulateLowStorage, boolean simulateNoPermission) {
        this.simulateLowStorage = simulateLowStorage;
        this.simulateNoPermission = simulateNoPermission;
        this.notificationService = new NotificationService(); // 初始化提示服务
    }


    public void exportAsPdf(WeatherData data)
            throws StorageException, DownloadPermissionException {
        check();

        // 1. 指定保存到桌面（避免路径/权限问题）
        String savePath = getDesktopPath() + File.separator + "weather_" + data.getLocation().getName() + ".pdf";
        File pdfFile = new File(savePath);
        System.out.println("PDF will be saved to：" + pdfFile.getAbsolutePath());

        try {
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(pdfFile));
            document.open();
            document.add(new Paragraph("Weather Data Export"));
            document.add(new Paragraph("-------------------------"));
            document.add(new Paragraph(data.getSummary()));
            document.close();

            // 2. 导出成功提示
            notificationService.showSuccess("PDF has saved to：\n" + pdfFile.getAbsolutePath());
        } catch (DocumentException | IOException e) {
            throw new RuntimeException("Fail to generate PDF：" + e.getMessage(), e);
        }
    }


    public void exportAsExcel(WeatherData data)
            throws StorageException, DownloadPermissionException {
        check();

        // 1. 指定保存到桌面
        String savePath = getDesktopPath() + File.separator + "weather_" + data.getLocation().getName() + ".xlsx";
        File excelFile = new File(savePath);
        System.out.println("Excel will be saved to：" + excelFile.getAbsolutePath());

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Weather Data");

            // 表头
            Row headRow = sheet.createRow(0);
            headRow.createCell(0).setCellValue("Location");
            headRow.createCell(1).setCellValue("Weather Summary");

            // 数据行
            Row dataRow = sheet.createRow(1);
            dataRow.createCell(0).setCellValue(data.getLocation().getName());
            dataRow.createCell(1).setCellValue(data.getSummary());

            // 自动调整列宽
            for (int i = 0; i < 2; i++) {
                sheet.autoSizeColumn(i);
            }

            // 写入文件
            try (FileOutputStream out = new FileOutputStream(excelFile)) {
                workbook.write(out);
            }

            // 2. 导出成功提示
            notificationService.showSuccess("Excel has saved to：\n" + excelFile.getAbsolutePath());
        } catch (IOException e) {
            throw new RuntimeException("Fail to generate Excel：" + e.getMessage(), e);
        }
    }


    // 工具方法：获取桌面路径
    private String getDesktopPath() {
        String desktop = System.getProperty("user.home") + File.separator + "Desktop";
        File desktopDir = new File(desktop);
        if (!desktopDir.exists()) {
            desktopDir.mkdirs(); // 若桌面目录不存在则创建
        }
        return desktop;
    }


    // 原有检查方法
    private void check() throws StorageException, DownloadPermissionException {
        if (simulateLowStorage) {
            throw new StorageException("Insufficient storage space.Please free up space and try again.");
        }
        if (simulateNoPermission) {
            throw new DownloadPermissionException("Download permission not enabled.Please check your browser settings.");
        }
    }
}