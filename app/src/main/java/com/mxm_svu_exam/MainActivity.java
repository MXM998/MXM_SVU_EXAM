package com.mxm_svu_exam;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private EditText etAssignment, etExam;
    private TextView tvMinWithoutHelp, tvMinWithHelp, tvResult;
    private Button btnCalculateMin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // تهيئة العناصر
        initializeViews();

        // إضافة مستمع للأزرار
        setupListeners();
    }

    private void initializeViews() {
        etAssignment = findViewById(R.id.etAssignment);
        etExam = findViewById(R.id.etExam);
        tvMinWithoutHelp = findViewById(R.id.tvMinWithoutHelp);
        tvMinWithHelp = findViewById(R.id.tvMinWithHelp);
        tvResult = findViewById(R.id.tvResult);
        btnCalculateMin = findViewById(R.id.btnCalculateMin);
    }

    private void setupListeners() {
        // زر حساب الحد الأدنى
        btnCalculateMin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculateMinimumGrades();
            }
        });

        // مستمع لتغيير نص الامتحان لحساب النتيجة تلقائياً
        etExam.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    calculateFinalResult();
                }
            }
        });
    }

    private void calculateMinimumGrades() {
        String assignmentText = etAssignment.getText().toString();

        if (TextUtils.isEmpty(assignmentText)) {
            tvMinWithoutHelp.setText("0");
            tvMinWithHelp.setText("0");
            return;
        }

        try {
            double assignmentGrade = Double.parseDouble(assignmentText);

            // حساب الحد الأدنى للامتحان
            // المعادلة: 60 = (الوظيفة × 0.25) + (الامتحان × 0.75)
            double minWithoutHelp = (60 - (assignmentGrade * 0.25)) / 0.75;
            double minWithHelp = (57 - (assignmentGrade * 0.25)) / 0.75;

            // التأكد من أن القيم لا تكون سالبة
            minWithoutHelp = Math.max(0, minWithoutHelp);
            minWithHelp = Math.max(0, minWithHelp);

            // عرض النتائج مع تقريب لرقمين عشريين
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
            tvResult.setText("أدخل جميع العلامات");
            tvResult.setBackgroundColor(0xFFF0F0F0);
            tvResult.setTextColor(0xFF000000);
            return;
        }

        try {
            double assignmentGrade = Double.parseDouble(assignmentText);
            double examGrade = Double.parseDouble(examText);

            // حساب المحصلة النهائية
            double finalGrade = (assignmentGrade * 0.25) + (examGrade * 0.75);

            // عرض النتيجة مع تقريب لرقمين عشريين
            tvResult.setText(String.format("%.2f", finalGrade));

            // تحديد اللون بناءً على النتيجة
            if (finalGrade >= 60) {
                // نجاح - لون أخضر
                tvResult.setBackgroundColor(0xFF4CAF50);
                tvResult.setTextColor(0xFFFFFFFF);
            } else if (finalGrade >= 57 && finalGrade < 60) {
                // نجاح بمساعدة - لون أصفر
                tvResult.setBackgroundColor(0xFFFFEB3B);
                tvResult.setTextColor(0xFF000000);
            } else {
                // رسوب - لون أحمر
                tvResult.setBackgroundColor(0xFFF44336);
                tvResult.setTextColor(0xFFFFFFFF);
            }

        } catch (NumberFormatException e) {
            tvResult.setText("خطأ في الإدخال");
            tvResult.setBackgroundColor(0xFFF0F0F0);
            tvResult.setTextColor(0xFF000000);
        }
    }
}