package me.blf.test.service;

import android.widget.Toast;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import me.blf.test.model.Question;
import me.blf.test.model.TestSet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class JsonQuestionServiceImpl implements QuestionService{
    public TestSet getQuestions(InputStreamReader inputStreamReader) throws Exception {
        var sb = new StringBuilder();
        try {
            var reader = new BufferedReader(inputStreamReader);
            var readLine = reader.readLine();
            while (readLine != null) {
                sb.append(readLine);
                readLine = reader.readLine();
            }
        } catch (IOException e) {
            throw new Exception("Не удалось прочитать файл с тестами");
        }
        if (sb.length() > 0) {
            var mapper = new ObjectMapper();
            return mapper.readValue(sb.toString(), new TypeReference<>(){});
        } else {
            throw new Exception("Файл тестов пустой");
        }
    }
}
