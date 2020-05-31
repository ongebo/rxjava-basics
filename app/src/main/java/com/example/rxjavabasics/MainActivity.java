package com.example.rxjavabasics;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    private List<Disposable> disposables = new ArrayList<>();
    private TextView evenNumberTextView;
    private TextView oddNumberTextView;
    private TextView primeNumberTextView;
    private Button startStreamButton;
    private Button endStreamButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
        startStream();

        startStreamButton.setOnClickListener(view -> startStream());
        endStreamButton.setOnClickListener(view -> RandomIntegers.runStream = false);
    }

    private void init() {
        evenNumberTextView = findViewById(R.id.tvEvenNumber);
        oddNumberTextView = findViewById(R.id.tvOddNumber);
        primeNumberTextView = findViewById(R.id.tvPrimeNumber);
        startStreamButton = findViewById(R.id.buttonStartStream);
        endStreamButton = findViewById(R.id.buttonEndStream);
    }

    private void startStream() {
        RandomIntegers.runStream = true;

        Disposable disposable = RandomIntegers.getIntegerStream()
                .subscribeOn(Schedulers.newThread()) // Use a new thread for the data source.
                .observeOn(AndroidSchedulers.mainThread()) // Consume integers on the UI thread.
                .subscribe(this::handleInteger, this::handleError, this::handleCompletion);

        disposables.add(disposable);
    }

    @SuppressLint("DefaultLocale")
    private void handleInteger(int integer) {
        String integerAsString = String.format("%d", integer);
        if (integerIsPrime(integer)) {
            primeNumberTextView.setText(integerAsString);
        } else if (integer % 2 == 0) {
            evenNumberTextView.setText(integerAsString);
        } else {
            oddNumberTextView.setText(integerAsString);
        }
    }

    private boolean integerIsPrime(int integer) {
        for (int c = 2; c < integer / 2; c++) {
            if (integer % c == 0) {
                return false;
            }
        }
        return true;
    }

    private void handleError(Throwable throwable) {
        Toast.makeText(this, throwable.getMessage(), Toast.LENGTH_SHORT).show();
    }

    private void handleCompletion() {
        Toast.makeText(this, R.string.message_stream_complete, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        for (Disposable disposable : disposables) {
            disposable.dispose();
        }

        super.onDestroy();
    }
}
