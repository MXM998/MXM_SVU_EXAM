package com.mxm_svu_exam;

import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class MainActivity extends AppCompatActivity {

    private EditText etAssignment, etExam;
    private TextView tvMinWithoutHelp, tvMinWithHelp, tvResult, tvResultMessage;
    private Button btnCalculateMin, btnCalculateFinal;
    private View indicatorSuccess, indicatorWarning, indicatorError;
    private CardView cardResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeViews();
        setupListeners();
    }

    private void initializeViews() {
        etAssignment = findViewById(R.id.etAssignment);
        etExam = findViewById(R.id.etExam);
        tvMinWithoutHelp = findViewById(R.id.tvMinWithoutHelp);
        tvMinWithHelp = findViewById(R.id.tvMinWithHelp);
        tvResult = findViewById(R.id.tvResult);
        tvResultMessage = findViewById(R.id.tvResultMessage);
        btnCalculateMin = findViewById(R.id.btnCalculateMin);
        btnCalculateFinal = findViewById(R.id.btnCalculateFinal);
        indicatorSuccess = findViewById(R.id.indicatorSuccess);
        indicatorWarning = findViewById(R.id.indicatorWarning);
        indicatorError = findViewById(R.id.indicatorError);
        cardResult = findViewById(R.id.cardResult);
    }

    private void setupListeners() {
        btnCalculateMin.setOnClickListener(v -> calculateMinimumGrades());
        btnCalculateFinal.setOnClickListener(v -> calculateFinalResult());
    }

    private void calculateMinimumGrades() {
        String assignmentText = etAssignment.getText().toString();

        if (TextUtils.isEmpty(assignmentText)) {
            tvMinWithoutHelp.setText("0.00");
            tvMinWithHelp.setText("0.00");
            return;
        }

        try {
            double assignmentGrade = Double.parseDouble(assignmentText);

            if (assignmentGrade < 0 || assignmentGrade > 100) {
                tvMinWithoutHelp.setText("خطأ");
                tvMinWithHelp.setText("خطأ");
                return;
            }

            double minWithoutHelp = (60 - (assignmentGrade * 0.25)) / 0.75;
            double minWithHelp = ((57.01 - (assignmentGrade * 0.25)) / 0.75)-0.01;

            minWithoutHelp = Math.max(0, Math.min(100, minWithoutHelp));
            minWithHelp = Math.max(0, Math.min(100, minWithHelp));

            tvMinWithoutHelp.setText(String.format("%.2f", minWithoutHelp));
            tvMinWithHelp.setText(String.format("%.2f", minWithHelp));

        } catch (NumberFormatException e) {
            tvMinWithoutHelp.setText("خطأ");
            tvMinWithHelp.setText("خطأ");
        }
    }

    private void calculateFinalResult() {
        String assignmentText = etAssignment.getText().toString();
        String examText = etExam.getText().toString();

        if (TextUtils.isEmpty(assignmentText) || TextUtils.isEmpty(examText)) {
            showResult(0, "أدخل جميع العلامات أولاً");
            return;
        }

        try {
            double assignmentGrade = Double.parseDouble(assignmentText);
            double examGrade = Double.parseDouble(examText);

            if (assignmentGrade < 0 || assignmentGrade > 100 || examGrade < 0 || examGrade > 100) {
                showResult(0, "العلامات يجب أن تكون بين 0 و 100");
                return;
            }

            double finalGrade = (assignmentGrade * 0.25) + (examGrade * 0.75);
            showResult(finalGrade, getResultMessage(finalGrade));

        } catch (NumberFormatException e) {
            showResult(0, "خطأ في إدخال البيانات");
        }
    }

    private void showResult(double finalGrade, String message) {
        tvResult.setText(String.format("%.2f", finalGrade));
        tvResultMessage.setText(message);

        resetIndicators();

        if (finalGrade >= 60) {
            indicatorSuccess.setAlpha(1f);
            cardResult.setCardBackgroundColor(Color.parseColor("#1A7ADAA5"));
        } else if (finalGrade >= 57.001 && finalGrade < 60) {
            indicatorWarning.setAlpha(1f);
            cardResult.setCardBackgroundColor(Color.parseColor("#1AE1AA36"));
        } else if (finalGrade > 0) {
            indicatorError.setAlpha(1f);
            cardResult.setCardBackgroundColor(Color.parseColor("#1A239BA7"));
        } else {
            cardResult.setCardBackgroundColor(Color.parseColor("#1E1E2E"));
        }
    }

    private void resetIndicators() {
        indicatorSuccess.setAlpha(0.3f);
        indicatorWarning.setAlpha(0.3f);
        indicatorError.setAlpha(0.3f);
    }

    private String getResultMessage(double finalGrade) {
        if (finalGrade >= 60) {
            return "مبروك! لقد نجحت 🎉";
        } else if (finalGrade >= 57.001 && finalGrade < 60) {
            return "نجاح بمساعدة - أحسنت العمل!";
        } else if (finalGrade > 0) {
            return "لم تحقق النجاح - حاول مرة أخرى 💪";
        } else {
            return "أدخل العلامات لحساب النتيجة";
        }
    }
}