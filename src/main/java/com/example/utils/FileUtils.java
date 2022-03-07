package com.example.utils;

import org.apache.poi.POIXMLDocument;
import org.springframework.core.io.ByteArrayResource;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
// Класс создает документ с именем и разрешением
public class FileUtils {

    private FileUtils() {
    }

    public static ByteArrayResource createOfficeDocumentResource(POIXMLDocument document, String name, String suffix)
            throws IOException {
        return new ByteArrayResource(Files.readAllBytes(createOfficeDocumentFile(document, name, suffix)));
    }

    private static Path createOfficeDocumentFile(POIXMLDocument document, String name, String suffix) throws IOException {
        File file = File.createTempFile(name, suffix);

        try (FileOutputStream out = new FileOutputStream(file)) {
            document.write(out);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file.toPath();
    }
}
