package me.blf.test;

import android.content.Context;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import me.blf.test.model.AnswerType;
import me.blf.test.model.Question;
import me.blf.test.model.QuestionType;

import java.util.List;

public class QuestionsRecyclerViewAdapter extends RecyclerView.Adapter<QuestionsRecyclerViewAdapter.QViewHolder> {
    public Context context;
    List<Question> questionList;

    public QuestionsRecyclerViewAdapter(Context context, List<Question> questionList) {
        this.questionList = questionList;
        this.context = context;
    }

    @NonNull
    @Override
    public QViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        var layoutInflater = LayoutInflater.from(context);
        var view = layoutInflater.inflate(R.layout.question_recycler_view_row, parent, false);
        return new QuestionsRecyclerViewAdapter.QViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QViewHolder holder, int position) {
        var curQuestion = questionList.get(position);
        holder.questionTV.setText(curQuestion.getQuestion());
        holder.answerHintTV.setText(QuestionType.getQuestionHintByType(curQuestion.getQuestionType()));
        //заполнение вариантов ответов
        if (curQuestion.getQuestionType() == QuestionType.INPUT) {
            if (curQuestion.getAnswerType() == AnswerType.STRING) {
                holder.answerET.setInputType(EditorInfo.TYPE_CLASS_TEXT);
            } else if (curQuestion.getAnswerType() == AnswerType.DIGIT) {
                holder.answerET.setInputType(EditorInfo.TYPE_CLASS_NUMBER);
            }
            setAnswerCheckBoxVisibility(holder, View.GONE);
            setAnswerRadioButtonsVisibility(holder, View.GONE);
            holder.answerET.setVisibility(View.VISIBLE);

            //восстановление состояния
            if (curQuestion.getUserAnswer() != null) {
                holder.answerET.setText(curQuestion.getUserAnswer());
            }

        } else if (curQuestion.getQuestionType() == QuestionType.SINGLE) {
            setAnswerRadioButtonsVisibility(holder, View.GONE);
            setAnswerCheckBoxVisibility(holder, View.GONE);
            holder.answerET.setVisibility(View.GONE);
            for (int i = 0; i < curQuestion.getAnswers().length; i++) {
                holder.answerRbArr[i].setText(curQuestion.getAnswers()[i]);
                holder.answerRbArr[i].setVisibility(View.VISIBLE);
                holder.answerRbArr[i].setTag(i);
            }

            //восстановление состояния
            if (!curQuestion.getUserAnswersIndexesList().isEmpty()) {
                holder.answerRbArr[curQuestion.getUserAnswersIndexesList().get(0)].setChecked(true);
            }

        } else if (curQuestion.getQuestionType() == QuestionType.MULTI) {
            setAnswerRadioButtonsVisibility(holder, View.GONE);
            setAnswerCheckBoxVisibility(holder, View.GONE);
            holder.answerET.setVisibility(View.GONE);
            for (int i = 0; i < curQuestion.getAnswers().length; i++) {
                holder.answerCBArr[i].setText(curQuestion.getAnswers()[i]);
                holder.answerCBArr[i].setVisibility(View.VISIBLE);
                holder.answerCBArr[i].setTag(i);
            }
        }
        holder.question = curQuestion;
        if (holder.question.getMarkImage() > 0) {
            holder.markImage.setImageResource(holder.question.getMarkImage());
        }
        //восстановление состояния
        curQuestion.getUserAnswersIndexesList().forEach(integer -> holder.answerCBArr[integer].setSelected(true));
    }

    private void setAnswerRadioButtonsVisibility(QViewHolder holder, int visibility) {
        for(var rB : holder.answerRbArr) {
            rB.setVisibility(visibility);
        }
    }

    private void setAnswerCheckBoxVisibility(QViewHolder holder, int visibility) {
        for(var cB : holder.answerCBArr) {
            cB.setVisibility(visibility);
        }
    }

    @Override
    public int getItemCount() {
        return questionList.size();
    }

    public static class QViewHolder extends RecyclerView.ViewHolder {
        TextView questionTV;
        TextView answerHintTV;
        RadioButton[] answerRbArr = new RadioButton[5];
        CheckBox[] answerCBArr = new CheckBox[5];
        EditText answerET;
        Question question;
        RadioGroup radioGroup;
        ImageView markImage;

        public QViewHolder(@NonNull View itemView) {
            super(itemView);
            questionTV = itemView.findViewById(R.id.questionTextTextView);
            answerHintTV = itemView.findViewById(R.id.answerHint);

            radioGroup = itemView.findViewById(R.id.answerRadioGroup);
            answerRbArr[0] = itemView.findViewById(R.id.answerRadioButton);
            answerRbArr[1] = itemView.findViewById(R.id.answerRadioButton1);
            answerRbArr[2] = itemView.findViewById(R.id.answerRadioButton2);
            answerRbArr[3] = itemView.findViewById(R.id.answerRadioButton3);
            answerRbArr[4] = itemView.findViewById(R.id.answerRadioButton4);

            answerCBArr[0] = itemView.findViewById(R.id.answerCheckBox);
            answerCBArr[1] = itemView.findViewById(R.id.answerCheckBox1);
            answerCBArr[2] = itemView.findViewById(R.id.answerCheckBox2);
            answerCBArr[3] = itemView.findViewById(R.id.answerCheckBox3);
            answerCBArr[4] = itemView.findViewById(R.id.answerCheckBox4);

            answerET = itemView.findViewById(R.id.answerEditText);

            for (var radBtn : answerRbArr) {
                radBtn.setOnClickListener(view -> {
                    question.getUserAnswersIndexesList().clear();
                    question.getUserAnswersIndexesList().add((Integer)view.getTag());
                });
            }

            for (var checkBox : answerCBArr) {
                checkBox.setOnClickListener(view -> {
                    if (((CheckBox)view).isChecked()) {
                        question.getUserAnswersIndexesList().add((Integer) view.getTag());
                    } else {
                        question.getUserAnswersIndexesList().removeIf(val -> val == view.getTag());
                    }
                });
            }

            answerET.setOnEditorActionListener((textView, actionId, keyEvent) -> {
                if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                        actionId == EditorInfo.IME_ACTION_DONE ||
                        keyEvent != null &&
                                keyEvent.getAction() == KeyEvent.ACTION_DOWN &&
                                keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                    if (keyEvent == null || !keyEvent.isShiftPressed()) {
                        question.setUserAnswer(textView.getText().toString());
                    }
                }
                return false;
            });
            markImage = itemView.findViewById(R.id.markImageView);
        }
    }
}
