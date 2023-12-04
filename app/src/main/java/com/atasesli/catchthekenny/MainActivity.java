package com.atasesli.catchthekenny;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import androidx.gridlayout.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {
    Button startButton;
    ImageView kenny5;
    GridLayout.LayoutParams layoutParams;
    TextView timerView,scoreView,bestScoreView;
    Random numberGenerator;
    Handler gameHandler;
    Runnable run;
    SharedPreferences sharedPreferences;
    int time,score,highestScore,notClickedCount,miliseconds;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startButton = findViewById(R.id.startButton);
        kenny5 = findViewById(R.id.kenny5);
        timerView = findViewById(R.id.Time);
        scoreView = findViewById(R.id.score);
        bestScoreView = findViewById(R.id.bestScore);
        layoutParams = (GridLayout.LayoutParams) kenny5.getLayoutParams();
        numberGenerator = new Random();
        sharedPreferences = this.getSharedPreferences("com.atasesli.catchthekenny", Context.MODE_PRIVATE);
        score = 0;
        time = 0;
        notClickedCount = 0;
        miliseconds = 1000;
        highestScore = sharedPreferences.getInt("highestScore",0);
        bestScoreView.setText(String.format("Top Score: %s",highestScore));
    }
    public void gameLogic(){
        int randomRow = numberGenerator.nextInt(3);
        int randomColumn = numberGenerator.nextInt(3);
        kenny5.setVisibility(View.INVISIBLE);
        kenny5.setEnabled(false);
        layoutParams.rowSpec = GridLayout.spec(randomRow);
        layoutParams.columnSpec = GridLayout.spec(randomColumn);
        kenny5.setLayoutParams(layoutParams);
        kenny5.setEnabled(true);
        kenny5.setVisibility(View.VISIBLE);
    }
    public void onStartingGame(View view){
        startButton.setVisibility(View.INVISIBLE);
        startButton.setEnabled(false);
        gameHandler = new Handler();
        run = () -> {
            gameLogic();
            time++;
            timerView.setText(String.format("Time: %s",time));
            notClickedCount++;
            if (notClickedCount >= 3) onStoppingGame();
            else {
                if (miliseconds > 300) miliseconds -= 10;
                gameHandler.postDelayed(run,miliseconds);
            }
        };
        gameHandler.post(run);
    }
    public void onStoppingGame(){
        startButton.setEnabled(true);
        startButton.setVisibility(View.VISIBLE);
        kenny5.setEnabled(false);
        gameHandler.removeCallbacks(run);
        if (score > highestScore){
            sharedPreferences.edit().putInt("highestScore",score).apply();
            bestScoreView.setText(String.format("Score: %s",score));
        }
        notClickedCount = 0;
        score = 0;
        time = 0;
        miliseconds = 1000;
    }
    public void onKennyClick(View view){
        notClickedCount = 0;
        score++;
        scoreView.setText(String.format("Score: %s",score));
        kenny5.setEnabled(false);
        kenny5.setVisibility(View.INVISIBLE);
    }
}