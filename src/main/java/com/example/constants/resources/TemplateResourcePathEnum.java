package com.example.constants.resources;

//Расположение файлов шаблонов в resources
public enum TemplateResourcePathEnum {

    TEMPLATE_TASKS("templates/Template.docx"),
    TEMPLATE_DICTIONARY("templates/Template.xlsx");

    private final String filePath;


    TemplateResourcePathEnum(String filePath) {
        this.filePath = filePath;
    }

    public String getFilePath() {
        return filePath;
    }
}
