package com.hbtipcalc.tipcalculator;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.hbtipcalc.tipcalculator.math.Calculator;
import com.hbtipcalc.tipcalculator.math.TipResult;

public class MainActivity extends AppCompatActivity {

    TextView output;
    TextView tipLabel;
    EditText billInput;
    SeekBar tipInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        this.output = findViewById(R.id.Output);
        this.tipLabel = findViewById(R.id.TipLabel);

        this.billInput = findViewById(R.id.BillAmountInput);
        this.tipInput = findViewById(R.id.TipSeekBar);

        tipLabel.setText(String.format("Tip Amount: %d%%", tipInput.getProgress()));

        tipInput.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
            {
                tipLabel.setText(String.format("Tip Amount: %d%%", progress));
                temp();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        billInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Runs before text changes (rarely needed)
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                temp();
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Runs after the text has changed
                String currentText = s.toString();
                // Do something with currentText
            }
        });

        // make a app class to contain some internal vars
        // make a settings class which the app contains. store default tip, etc.
        // make a info class which stores like activeBill, tip, etc...
        // instead of a calculate button, have a on change listener for the text input and the slider
        // then call updateInfo on that (update the info in the app)
        // then call displayInfo and pass it the updated info (displays the apps info)

        // implement the split bill at the end
    }

    private void temp()
    {
        String inAmount = billInput.getText().toString().trim();

        if (inAmount.length() == 0)
        {
            output.setText("");
            // instead of returning here, the object should be highlighted
            // to show that it needs to be filled or something
            return;
        }

        int tipPercent = tipInput.getProgress();
        TipResult result = Calculator.calculate(inAmount, tipPercent, Calculator.RoundFlag.NONE);

        String outputString = String.format("Tip Amount: %s" +
                "\n Total Amount: %s", result.getFormattedTip(), result.getFormattedTotal());

        output.setText(outputString);
    }
}