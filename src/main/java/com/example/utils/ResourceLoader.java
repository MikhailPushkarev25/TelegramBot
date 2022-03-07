package com.example.utils;

import com.example.constants.resources.DictionaryResourcePathEnum;
import com.example.constants.resources.TemplateResourcePathEnum;
import lombok.Getter;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

//Класс загружает шаблоны из resources
@Component
public class ResourceLoader {
    @Getter
    private final Map<String, XSSFWorkbook> defaultDictionaries;

    public ResourceLoader() throws IOException {
        this.defaultDictionaries = loadAllDefaultDictionaryWorkBooks();
    }

    public XWPFDocument loadTemplateDocument() throws IOException {
        return new XWPFDocument(
                Objects.requireNonNull(
                        getClass()
                                .getClassLoader()
                                .getResourceAsStream(TemplateResourcePathEnum.TEMPLATE_TASKS.getFilePath())
                )
        );
    }

    public XSSFWorkbook loadTemplateWorkbook() throws IOException {
        return loadWorkbook(TemplateResourcePathEnum.TEMPLATE_DICTIONARY.getFilePath());
    }

    public XSSFWorkbook loadDefaultDictionaryWorkbook(DictionaryResourcePathEnum dictionaryResourcePath) throws IOException {
        return loadWorkbook(dictionaryResourcePath.getFilePath());
    }

    private Map<String, XSSFWorkbook> loadAllDefaultDictionaryWorkBooks() throws IOException {
        Map<String, XSSFWorkbook> defaultDictionaries = new HashMap<>();
        for (DictionaryResourcePathEnum path : DictionaryResourcePathEnum.values()) {
            defaultDictionaries.put(path.name(), loadWorkbook(path.getFilePath()));
        }
        return defaultDictionaries;
    }

    private XSSFWorkbook loadWorkbook(String filePath) throws IOException {
        return new XSSFWorkbook(
                Objects.requireNonNull(
                        getClass()
                                .getClassLoader()
                                .getResourceAsStream(filePath)
                )
        );
    }
}
