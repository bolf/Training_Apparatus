package me.blf.test;

import static java.lang.String.format;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import me.blf.test.model.Question;
import me.blf.test.model.QuestionType;
import me.blf.test.model.TestSet;
import me.blf.test.service.JsonQuestionServiceImpl;

import java.io.*;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

public class MainActivity extends AppCompatActivity {
    private TestSet testSet;
    private RecyclerView recyclerView;
    private Button buttonFinish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Тренажер знаний");
        buttonFinish = findViewById(R.id.buttonFinish);
        buttonFinish.setVisibility(View.GONE);
    }

    private static final int FILE_REQUEST_CODE = 2;

    private void openFile(Uri pickerInitialUri) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("application/json");
        startActivityForResult(intent, FILE_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent resultData) {
        super.onActivityResult(requestCode, resultCode, resultData);
        if (requestCode == FILE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            try {
                testSet = new JsonQuestionServiceImpl().getQuestions(new InputStreamReader(getContentResolver().openInputStream(resultData.getData())));
            } catch (FileNotFoundException e) {
                Toast.makeText(this,"Файл не найден",Toast.LENGTH_LONG).show();
                return;
            } catch (Exception e) {
                Toast.makeText(this,e.getMessage(),Toast.LENGTH_LONG).show();
                return;
            }

            this.setTitle(testSet.getTitle());

            recyclerView = findViewById(R.id.questionsRecyclerView);
            var adapter = new QuestionsRecyclerViewAdapter(this,testSet.getQuestionList());
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));

            findViewById(R.id.buttonChooseFile).setVisibility(View.GONE);
            buttonFinish.setVisibility(View.VISIBLE);
        }
    }

    public void pickTestFileBtnClickListener(View view) {
        openFile(Uri.fromFile(new File(".")));
    }

    public void finishTestBtnClickListener(View view) {
        if (testSet.getQuestionList().stream()
                .anyMatch(question -> question.getUserAnswer() == null &&
                        question.getUserAnswersIndexesList().isEmpty())) {
            Toast.makeText(view.getContext(), "Для завершения ответь на все вопросы", Toast.LENGTH_LONG).show();
            return;
        }
        testSet.getQuestionList()
                .forEach(question -> {
                    int mark = R.drawable.ic_right_answer;
                    question.setRightAnswer(1);
                    if (question.getQuestionType() == QuestionType.SINGLE) {
                        if (!Objects.equals(question.getUserAnswersIndexesList().get(0), question.getRightAnswersInd()[0])) {
                            mark = R.drawable.ic_wrong_answer;
                            question.setRightAnswer(0);
                        }
                    } else if (question.getQuestionType() == QuestionType.MULTI) {
                        if (!question.getUserAnswersIndexesList().stream().map(Object::toString).sorted().collect(Collectors.joining())
                                .equals(Arrays.stream(question.getRightAnswersInd()).map(Object::toString).sorted().collect(Collectors.joining()))) {
                            mark = R.drawable.ic_wrong_answer;
                            question.setRightAnswer(0);
                        }

                    } else if (question.getQuestionType() == QuestionType.INPUT) {
                        if (!question.getUserAnswer().trim()
                                .equals(question.getAnswers()[question.getRightAnswersInd()[0]])) {
                            mark = R.drawable.ic_wrong_answer;
                            question.setRightAnswer(0);
                        }
                    }
                    question.setMarkImage(mark);
                });
            testSet.setRightAnswersQuantity(testSet.getQuestionList().stream().mapToInt(Question::getRightAnswer).sum());
            testSet.setRightAnswersPercent(testSet.getQuestionList().size()/100f);
            ((TextView)findViewById(R.id.resultTV)).setText("Правильно: " + testSet.getRightAnswersQuantity() +
                " Неправильно: " + (testSet.getQuestionList().size() - testSet.getRightAnswersQuantity()) + "  (" +
                    (new DecimalFormat("0.##")).format(testSet.getRightAnswersQuantity() / testSet.getRightAnswersPercent()) + "%)");

        recyclerView.getAdapter().notifyDataSetChanged();
    }
}
