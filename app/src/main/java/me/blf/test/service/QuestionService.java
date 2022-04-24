package me.blf.test.service;

import me.blf.test.model.Question;
import me.blf.test.model.TestSet;

import java.io.InputStreamReader;
import java.util.List;

public interface QuestionService {
    TestSet getQuestions(InputStreamReader inputStreamReader) throws Exception;
}
