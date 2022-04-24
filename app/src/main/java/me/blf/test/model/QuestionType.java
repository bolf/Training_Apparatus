package me.blf.test.model;

public enum QuestionType {
    SINGLE, MULTI, INPUT;

    public static String getQuestionHintByType(QuestionType questionType) {
        var hint = "";
        switch (questionType) {
            case SINGLE: {hint = "Выберите один правильный ответ"; break;}
            case MULTI: {hint = "Выберите один или более правильных ответов"; break;}
            case INPUT: {hint = "Введите правильный ответ"; break;}
        }
        return hint;
    }
}
