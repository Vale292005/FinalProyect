package com.example.finalproyect.Export;


import com.example.finalproyect.Elements.Task;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class ExportExcel {
    public static void exportToExcel(List<Task> tasks, String filePath) {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Tasks");

        // Crear la fila de encabezados
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("Value");
        headerRow.createCell(1).setCellValue("Description");
        headerRow.createCell(2).setCellValue("Mandatory");
        headerRow.createCell(3).setCellValue("Time");
        headerRow.createCell(4).setCellValue("Children");

        // Escribir los datos de las tareas
        int rowNum = 1;
        for (Task task : tasks) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(task.getValue());
            row.createCell(1).setCellValue(task.getDescription());
            row.createCell(2).setCellValue(task.isMandatory());
            row.createCell(3).setCellValue(task.getTime());
           // row.createCell(4).setCellValue(task.getChildren().size());  // Puedes agregar más detalles de los hijos si lo necesitas
        }

        // Ajustar el tamaño de las columnas
        for (int i = 0; i < 5; i++) {
            sheet.autoSizeColumn(i);
        }

        // Guardar el archivo Excel
        try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
            workbook.write(fileOut);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
