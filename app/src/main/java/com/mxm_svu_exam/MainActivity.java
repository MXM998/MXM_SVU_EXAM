package com.mxm_svu_exam;

import android.animation.ObjectAnimator;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class MainActivity extends AppCompatActivity {

    private EditText etAssignment, etExam;
    private TextView tvMinWithoutHelp, tvMinWithHelp, tvResult, tvResultMessage;
    private Button btnCalculateMin, btnCalculateFinal;
    private View indicatorSuccess, indicatorWarning, indicatorError;
    private CardView cardResult;


    private int developerClickCount = 0;
    private long lastDeveloperClickTime = 0;
    private static final int REQUIRED_CLICKS = 3;
    private static final long MAX_CLICK_INTERVAL = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }

        setContentView(R.layout.activity_main);

        initializeViews();
        setupListeners();
        setupDeveloperEasterEgg();
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

    private void setupDeveloperEasterEgg() {
        TextView tvDeveloper = findViewById(R.id.tvDeveloper);

        tvDeveloper.setOnClickListener(v -> {
            long currentTime = System.currentTimeMillis();


            if (currentTime - lastDeveloperClickTime > MAX_CLICK_INTERVAL) {
                developerClickCount = 0;
            }

            lastDeveloperClickTime = currentTime;
            developerClickCount++;

            if (developerClickCount >= REQUIRED_CLICKS) {
                showHelloWorldEffect(tvDeveloper);
                developerClickCount = 0;
            }
        });
    }

    private void showHelloWorldEffect(TextView developerView) {
        Toast.makeText(this, "@Mxm_mystery_bot", Toast.LENGTH_LONG).show();

        developerView.animate()
                .rotationBy(360f)
                .setDuration(800)
                .start();

        ObjectAnimator shakeX = ObjectAnimator.ofFloat(developerView, "translationX", 0, 25, -25, 25, -25, 15, -15, 6, -6, 0);
        shakeX.setDuration(500);
        shakeX.start();

        int originalColor = developerView.getCurrentTextColor();
        developerView.setTextColor(Color.parseColor("#FF00FF"));

        developerView.postDelayed(() -> {
            developerView.setTextColor(originalColor);
        }, 1000);
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

            if (assignmentGrade >= 0 && assignmentGrade < 40) {
                tvMinWithoutHelp.setText("لن تتأهل للفحص");
                tvMinWithHelp.setText("لن تتأهل للفحص");
                return;
            }

            double minWithoutHelp = ((59.01 - (assignmentGrade * 0.25)) / 0.75) +0.01;
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
        RestShine(cardResult);

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

        if (finalGrade <= 100 && finalGrade >= 99.01) {
            indicatorSuccess.setAlpha(1f);
            cardResult.setCardBackgroundColor(Color.parseColor("#2AFFE100"));
            applyShineAnimation(cardResult);
        }
        else if (finalGrade >= 90) {
            indicatorSuccess.setAlpha(1f);
            cardResult.setCardBackgroundColor(Color.parseColor("#2A9112BC"));
            applyShineAnimation(cardResult);
        }
        else if (finalGrade >= 80) {
            indicatorSuccess.setAlpha(1f);
            cardResult.setCardBackgroundColor(Color.parseColor("#2AB6F500"));
            applyShineAnimation(cardResult);
        }
        else if (finalGrade >= 59.01) {
            indicatorSuccess.setAlpha(1f);
            cardResult.setCardBackgroundColor(Color.parseColor("#1A7ADAA5"));
        } else if (finalGrade >= 57.001 && finalGrade < 59.01) {
            indicatorWarning.setAlpha(1f);
            cardResult.setCardBackgroundColor(Color.parseColor("#1AE1AA36"));
            if(finalGrade < 57.01)
            {
                tvResult.setText(String.format("%.3f", finalGrade));
            }
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
        if (finalGrade <= 100 && finalGrade >= 99.01) {
            return "🌟🌟🌟";
        }
        else if (finalGrade >= 90) {
            return "أسطوري معدل ممتاز 🤩";
        }
        else if (finalGrade >= 80) {
            return "وحش معدل عالي 😎";
        } else if (finalGrade >= 60.01) {
            return "مبروك! لقد نجحت 🎉";
        }
        else if (finalGrade >= 59.01) {
            return "ال 60 أحلى من 100 🌚";
        } else if (finalGrade >= 57.001 ) {
            return "نجاح بمساعدة - أحسنت العمل!";
        } else if (finalGrade > 0) {
            return "لم تحقق النجاح - حاول مرة أخرى 💪";
        } else {
            return "أدخل العلامات لحساب النتيجة";
        }
    }
    private void applyShineAnimation(CardView vv) {

        ObjectAnimator alphaAnim = ObjectAnimator.ofFloat(
                vv,
                "alpha",
                1.0f,
                0.6f,
                1.0f
        );
        alphaAnim.setDuration(1500);
        alphaAnim.setRepeatCount(ObjectAnimator.INFINITE);
        alphaAnim.setRepeatMode(ObjectAnimator.REVERSE);
        vv.setTag(R.id.shine_animator_tag, alphaAnim);
        alphaAnim.start();

    }
    private void RestShine(CardView vv) {
        vv.clearAnimation();
        vv.setAlpha(1.0f);
        ObjectAnimator animator = (ObjectAnimator) vv.getTag(R.id.shine_animator_tag);
        if (animator != null && animator.isRunning()) {
            animator.cancel();
            vv.setTag(R.id.shine_animator_tag, null);
        }
    }
}