package com.hbtipcalc.tipcalculator;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.hbtipcalc.tipcalculator.math.Calculator;
import com.hbtipcalc.tipcalculator.math.TipResult;

public class MainActivity extends AppCompatActivity {

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

        Button btn = findViewById(R.id.CalcButton);
        TextView output = findViewById(R.id.Output);
        EditText billInput = findViewById(R.id.BillAmountInput);
        EditText tipInput = findViewById(R.id.TipAmountInput);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                String inAmount = "100";
//                int tipPercent = 20;

                String inAmount = billInput.getText().toString().trim();
                String tipPercentString = tipInput.getText().toString().trim();

                if (inAmount.length() == 0 || tipPercentString.length() == 0)
                {
                    // instead of returning here, the object should be highlighted
                    // to show that it needs to be filled or something
                    return;
                }

                int tipPercent = Integer.parseInt(tipPercentString);
                TipResult result = Calculator.calculate(inAmount, tipPercent, Calculator.RoundFlag.NONE);

                String outputString = String.format("Bill Amount: \t\t\t%s" +
                        "\nTip Amount: \t\t\t%s" +
                        "\nTotal Amount: \t%s", inAmount, result.getFormattedTip(), result.getFormattedTotal());

                output.setText(outputString);
            }
        });
    }
}