package com.example.api;

import com.example.utils.FileUtils;
import com.example.utils.ResourceLoader;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class DictionaryResourceFileService {

    private final ResourceLoader resourceLoader;

    public ByteArrayResource getTemplateWorkbook() throws IOException {
        return FileUtils.createOfficeDocumentResource(
                resourceLoader.loadTemplateWorkbook(),
                "Template",
                ".xlsx"
        );
    }

}
