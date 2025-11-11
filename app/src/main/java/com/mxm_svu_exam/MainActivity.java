package com.mxm_svu_exam;



import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
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
    private CardView resultCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeViews();
        setupListeners();
        setupAnimations();
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
    }

    private void setupListeners() {
        btnCalculateMin.setOnClickListener(v -> {
            animateButton(btnCalculateMin);
            calculateMinimumGrades();
        });

        btnCalculateFinal.setOnClickListener(v -> {
            animateButton(btnCalculateFinal);
            calculateFinalResult();
        });
    }

    private void setupAnimations() {
        // تأثير توهج متقطع للعناصر
        startPulseAnimation(tvResult);
    }

    private void animateButton(Button button) {
        ValueAnimator animator = ValueAnimator.ofFloat(1f, 0.8f, 1f);
        animator.setDuration(200);
        animator.addUpdateListener(animation -> {
            float scale = (float) animation.getAnimatedValue();
            button.setScaleX(scale);
            button.setScaleY(scale);
        });
        animator.start();
    }

    private void startPulseAnimation(View view) {
        ValueAnimator animator = ValueAnimator.ofFloat(1f, 1.1f, 1f);
        animator.setDuration(2000);
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setRepeatMode(ValueAnimator.REVERSE);
        animator.addUpdateListener(animation -> {
            float scale = (float) animation.getAnimatedValue();
            view.setScaleX(scale);
            view.setScaleY(scale);
        });
        animator.start();
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
                tvMinWithoutHelp.setText("ERROR");
                tvMinWithHelp.setText("ERROR");
                return;
            }

            double minWithoutHelp = (60 - (assignmentGrade * 0.25)) / 0.75;
            double minWithHelp = (57 - (assignmentGrade * 0.25)) / 0.75;

            minWithoutHelp = Math.max(0, Math.min(100, minWithoutHelp));
            minWithHelp = Math.max(0, Math.min(100, minWithHelp));

            tvMinWithoutHelp.setText(String.format("%.2f", minWithoutHelp));
            tvMinWithHelp.setText(String.format("%.2f", minWithHelp));

            animateTextChange(tvMinWithoutHelp);
            animateTextChange(tvMinWithHelp);

        } catch (NumberFormatException e) {
            tvMinWithoutHelp.setText("ERROR");
            tvMinWithHelp.setText("ERROR");
        }
    }

    private void calculateFinalResult() {
        String assignmentText = etAssignment.getText().toString();
        String examText = etExam.getText().toString();

        if (TextUtils.isEmpty(assignmentText) || TextUtils.isEmpty(examText)) {
            tvResult.setText("0.00");
            tvResultMessage.setText("Please enter all marks first");
            resetIndicators();
            return;
        }

        try {
            double assignmentGrade = Double.parseDouble(assignmentText);
            double examGrade = Double.parseDouble(examText);

            if (assignmentGrade < 0 || assignmentGrade > 100 || examGrade < 0 || examGrade > 100) {
                tvResult.setText("ERROR");
                tvResultMessage.setText("Marks must be between 0 and 100");
                resetIndicators();
                return;
            }

            double finalGrade = (assignmentGrade * 0.25) + (examGrade * 0.75);

            tvResult.setText(String.format("%.2f", finalGrade));
            animateTextChange(tvResult);

            if (finalGrade >= 60) {
                // Success - Green
                tvResultMessage.setText("SUCCESS! You passed without help");
                setActiveIndicator(indicatorSuccess, Color.GREEN);
                animateColorChange(tvResult, Color.parseColor("#00FF88"));
            } else if (finalGrade >= 57 && finalGrade < 60) {
                // Warning - Pink
                tvResultMessage.setText("SUCCESS WITH HELP! Congratulations");
                setActiveIndicator(indicatorWarning, Color.MAGENTA);
                animateColorChange(tvResult, Color.parseColor("#FF00FF"));
            } else {
                // Error - Blue
                tvResultMessage.setText("FAILED! Better luck next time");
                setActiveIndicator(indicatorError, Color.CYAN);
                animateColorChange(tvResult, Color.parseColor("#00D4FF"));
            }

        } catch (NumberFormatException e) {
            tvResult.setText("ERROR");
            tvResultMessage.setText("Input error - check your marks");
            resetIndicators();
        }
    }

    private void resetIndicators() {
        indicatorSuccess.setAlpha(0.3f);
        indicatorWarning.setAlpha(0.3f);
        indicatorError.setAlpha(0.3f);
    }

    private void setActiveIndicator(View indicator, int color) {
        resetIndicators();
        indicator.setAlpha(1f);

        // تأثير توهج للمؤشر النشط
        ValueAnimator animator = ValueAnimator.ofFloat(0.7f, 1f);
        animator.setDuration(1000);
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setRepeatMode(ValueAnimator.REVERSE);
        animator.addUpdateListener(animation -> {
            float alpha = (float) animation.getAnimatedValue();
            indicator.setAlpha(alpha);
        });
        animator.start();
    }

    private void animateTextChange(TextView textView) {
        ValueAnimator animator = ValueAnimator.ofFloat(1f, 1.3f, 1f);
        animator.setDuration(300);
        animator.addUpdateListener(animation -> {
            float scale = (float) animation.getAnimatedValue();
            textView.setScaleX(scale);
            textView.setScaleY(scale);
        });
        animator.start();
    }

    private void animateColorChange(TextView textView, int newColor) {
        ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(),
                textView.getCurrentTextColor(), newColor);
        colorAnimation.setDuration(500);
        colorAnimation.addUpdateListener(animator ->
                textView.setTextColor((Integer) animator.getAnimatedValue()));
        colorAnimation.start();
    }
}
