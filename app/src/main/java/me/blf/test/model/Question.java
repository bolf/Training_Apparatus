package me.blf.test.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Question {
    private String question;
    private String[] answers;
    private Integer[] rightAnswersInd;
    private QuestionType questionType;
    private AnswerType answerType;
    private List<Integer> userAnswersIndexesList = new ArrayList<>();
    private String userAnswer;
    private int markImage;
    private int rightAnswer = 0;

    public Question() {
    }

    public Question(String question, String[] answers, Integer[] rightAnswersInd, QuestionType questionType, AnswerType answerType) {
        this.question = question;
        this.answers = answers;
        this.rightAnswersInd = rightAnswersInd;
        this.questionType = questionType;
        this.answerType = answerType;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String[] getAnswers() {
        return answers;
    }

    public void setAnswers(String[] answers) {
        this.answers = answers;
    }

    public Integer[] getRightAnswersInd() {
        return rightAnswersInd;
    }

    public void setRightAnswersInd(Integer[] rightAnswersInd) {
        this.rightAnswersInd = rightAnswersInd;
    }

    public QuestionType getQuestionType() {
        return questionType;
    }

    public void setQuestionType(QuestionType questionType) {
        this.questionType = questionType;
    }

    public AnswerType getAnswerType() {
        return answerType;
    }

    public void setAnswerType(AnswerType answerType) {
        this.answerType = answerType;
    }

    public int getMarkImage() {
        return markImage;
    }

    public void setMarkImage(int markImage) {
        this.markImage = markImage;
    }

    public List<Integer> getUserAnswersIndexesList() {
        return userAnswersIndexesList;
    }

    public String getUserAnswer() {
        return userAnswer;
    }

    public void setUserAnswer(String userAnswer) {
        this.userAnswer = userAnswer;
    }

    public int getRightAnswer() {
        return rightAnswer;
    }

    public void setRightAnswer(int rightAnswer) {
        this.rightAnswer = rightAnswer;
    }
}
