package com.example.api;

import com.example.constants.resources.DictionaryResourcePathEnum;
import com.example.exceptions.UserDictionaryNotFoundException;
import com.example.model.Dictionary;
import com.example.model.Word;
import com.example.repository.DictionaryRepository;
import com.example.utils.FileUtils;
import com.example.utils.ResourceLoader;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class TasksService {

    DictionaryRepository repository;
    WordService wordService;
    ResourceLoader resourceLoader;

    public ByteArrayResource getAllDefaultDictionaryTaskDocument() throws IOException {
        List<Dictionary> defaultDictionaryList = Arrays.stream(DictionaryResourcePathEnum.values())
                .map(resourcePath -> repository.findById(resourcePath.name()).orElseThrow(UserDictionaryNotFoundException::new))
                .collect(Collectors.toList());
        return createDocumentByteArray(wordService.getDictionariesWordList(defaultDictionaryList), "Task (all grades)");
    }

    public ByteArrayResource getTaskDocument(String dictionaryId, String fileName) throws IOException {
        Dictionary dictionary = repository.findById(dictionaryId).orElseThrow(UserDictionaryNotFoundException::new);
        return createDocumentByteArray(dictionary.getWordList(), MessageFormat.format("Tasks ({0})", fileName));
    }

    private ByteArrayResource createDocumentByteArray(List<Word> wordList, String fileName) throws IOException {
        XWPFDocument document = resourceLoader.loadTemplateDocument();
        setTaskToDocument(document, wordList);
        return FileUtils.createOfficeDocumentResource(document, fileName, ".docx");
    }

    private void setTaskToDocument(XWPFDocument document, List<Word> wordList) {
        Collections.shuffle(wordList);

        List<XWPFParagraph> paragraphs = document.getParagraphs();
        int i = 0;
        for (Word word : wordList) {
            XWPFParagraph paragraph = paragraphs.get(i);
            setValue(paragraph.createRun(), String.join(",", getVariants(word)));
            i = i + 2;
        }

        while (document.getParagraphs().size() > i) {
            document.removeBodyElement(document.getPosOfParagraph(document.getLastParagraph()));
        }
    }

    private List<String> getVariants(Word word) {
        List<String> mistakes = new ArrayList<>(word.getMistakes());
        mistakes.add(word.getWord());
        Collections.shuffle(mistakes);
        return mistakes;
    }

    private void setValue(XWPFRun run, String text) {
        run.setFontSize(14);
        run.setFontFamily("Times New Roman");
        run.setText(text);
    }
}
