package  com.example.stopwatch;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private int seconds = 0;
    private boolean running;
    private boolean wasRunning;
    private ImageView playButton;
    private ImageView pauseButton;
    private LinearLayout lapLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        playButton = findViewById(R.id.imageView);
        pauseButton = findViewById(R.id.imageViewPause);
        lapLayout = findViewById(R.id.lap_layout);

        if (savedInstanceState != null) {
            seconds = savedInstanceState.getInt("seconds");
            running = savedInstanceState.getBoolean("running");
            wasRunning = savedInstanceState.getBoolean("wasRunning");
        }

        runTimer();
        updateButtonVisibility();
    }

    private void runTimer() {
        final TextView timeView = findViewById(R.id.time_view);
        final Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                int hours = seconds / 3600;
                int minutes = (seconds % 3600) / 60;
                int secs = seconds % 60;

                String time = String.format(Locale.getDefault(),
                        "%d:%02d:%02d", hours, minutes, secs);

                timeView.setText(time);

                if (running) {
                    seconds++;
                }

                handler.postDelayed(this, 1000);
            }
        });
    }

    private void updateButtonVisibility() {
        if (running) {
            playButton.setVisibility(View.GONE);
            pauseButton.setVisibility(View.VISIBLE);
        } else {
            playButton.setVisibility(View.VISIBLE);
            pauseButton.setVisibility(View.GONE);
        }
    }

    public void onClickStart(View view) {
        running = true;
        updateButtonVisibility();
    }

    public void onClickStop(View view) {
        running = false;
        updateButtonVisibility();
    }

    public void onClickReset(View view) {
        running = false;
        seconds = 0;
        lapLayout.removeAllViews(); // Clear laps UI
        updateButtonVisibility();
    }

    public void onClickLap(View view) {
        if (running) {
            TextView lapTextView = new TextView(this);
            int lapNumber = lapLayout.getChildCount() + 1;
            int lapSeconds = seconds;

            String lapTime = String.format(Locale.getDefault(),
                    "Lap %d: %d:%02d:%02d", lapNumber,
                    lapSeconds / 3600, (lapSeconds % 3600) / 60, lapSeconds % 60);

            lapTextView.setText(lapTime);
            lapTextView.setTextSize(20);
            lapTextView.setTextColor(Color.BLACK); // Set text color to black
            lapLayout.addView(lapTextView, 0); // Add lap at the top
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        wasRunning = running;
        running = false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (wasRunning) {
            running = true;
        }
        updateButtonVisibility();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("seconds", seconds);
        outState.putBoolean("running", running);
        outState.putBoolean("wasRunning", wasRunning);
    }
}
