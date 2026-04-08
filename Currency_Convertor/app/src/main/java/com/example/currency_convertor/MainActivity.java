package com.example.currency_convertor;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

import javax.xml.transform.Result;

public class MainActivity extends AppCompatActivity {

    EditText AmountInput;
    Button Convertbtn;
    Button Settingsbtn;
    TextView ResultView;
    Spinner SpinnerFrom;
    Spinner SpinnerTo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        SharedPreferences sp = getSharedPreferences("Theme", MODE_PRIVATE);
        boolean isDark = sp.getBoolean("dark", false);

        if (isDark) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AmountInput = findViewById(R.id.AmountInput);
        Convertbtn = findViewById(R.id.convertBtn);
        ResultView = findViewById(R.id.resultView);
        SpinnerFrom = findViewById(R.id.spinnerFrom);
        SpinnerTo = findViewById(R.id.spinnerTo);

        // we need to create an adapter which connects our array with currencies to these spinners

        ArrayList<String> curr = new ArrayList<>();
        curr.add("INR");
        curr.add("USD");
        curr.add("JPY");
        curr.add("EUR");

        ArrayAdapter<String> adp = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,curr);
        // sent currencies array into our adapter

        // drop down
        adp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        SpinnerFrom.setAdapter(adp);
        SpinnerTo.setAdapter(adp);

        Convertbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // getting text in string from AmountInput
                String amountStr = AmountInput.getText().toString();
                if(amountStr.isEmpty()){
                    ResultView.setText("Enter a Valid Amount");
                    return;
                }
                // changing in to a decimal value
                double amount = Double.parseDouble(amountStr);
                String from = SpinnerFrom.getSelectedItem().toString();
                String to = SpinnerTo.getSelectedItem().toString();
                // if from and to is same, then we will just return the same amount in the result
                if(from.equals(to)){
                    ResultView.setText("Result : " + amount + " " + to);
                    return;
                }
                double amountInUSD = 0;

                if(from.equals("INR")) amountInUSD = amount * 0.0107886;
                else if (from.equals("USD")) amountInUSD = amount;
                else if (from.equals("EUR")) amountInUSD = amount * 1.15421;
                else if (from.equals("JPY")) amountInUSD = amount * 0.0062653;

                double finalAmount = 0;

                if (to.equals("INR")) finalAmount = amountInUSD * 92.6895;
                else if (to.equals("USD")) finalAmount = amountInUSD;
                else if (to.equals("EUR")) finalAmount = amountInUSD * 0.866398;
                else if (to.equals("JPY")) finalAmount = amountInUSD * 159.6;

                ResultView.setText("Result : " + String.format("%.2f", finalAmount) + " " + to);
            }
        });


        // implementing the settings btn
        Settingsbtn = findViewById(R.id.settingsBtn);
        Settingsbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent GotoSettingsActivity = new Intent(MainActivity.this,SettingsActivity.class);
                startActivity(GotoSettingsActivity);
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}