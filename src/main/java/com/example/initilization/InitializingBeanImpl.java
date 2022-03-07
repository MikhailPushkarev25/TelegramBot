package com.example.initilization;

import com.example.api.DictionaryAdditionService;
import com.example.utils.ResourceLoader;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class InitializingBeanImpl implements InitializingBean {
    DictionaryAdditionService dictionaryAdditionService;
    ResourceLoader resourceLoader;

    @Override
    public void afterPropertiesSet() {
        Map<String, XSSFWorkbook> defaultDictionaryMap = resourceLoader.getDefaultDictionaries();
        for (Map.Entry<String, XSSFWorkbook> pair : defaultDictionaryMap.entrySet()) {
            dictionaryAdditionService.addDefaultDictionary(pair.getKey(), pair.getValue());
        }
    }
}
