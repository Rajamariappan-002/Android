package com.trm.a2023178056;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

public class CalculatorFragment extends Fragment implements View.OnClickListener {

    // Declare variables for TextView and buttons
    private TextView display;
    private Button button2;
    private Button button3;
    private Button button4;
    private Button button5;
    private Button button6;
    private Button button7;
    private Button button8;
    private Button button9;
    private Button buttonClear, buttonEqual, buttonPlus, buttonMinus, buttonMultiply, buttonDivide;

    // Variables to hold the current calculation state
    private StringBuilder currentNumber;
    private double operand1, operand2;
    private char operator;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calculator, container, false);

        // Initialize TextView and buttons
        display = view.findViewById(R.id.display);
        Button button0 = view.findViewById(R.id.button_0);
        Button button1 = view.findViewById(R.id.button_1);
        button2 = view.findViewById(R.id.button_2);
        button3 = view.findViewById(R.id.button_3);
        button4 = view.findViewById(R.id.button_4);
        button5 = view.findViewById(R.id.button_5);
        button6 = view.findViewById(R.id.button_6);
        button7 = view.findViewById(R.id.button_7);
        button8 = view.findViewById(R.id.button_8);
        button9 = view.findViewById(R.id.button_9);
        buttonClear = view.findViewById(R.id.button_clear);
        buttonEqual = view.findViewById(R.id.button_equal);
        buttonPlus = view.findViewById(R.id.button_plus);
        buttonMinus = view.findViewById(R.id.button_minus);
        buttonMultiply = view.findViewById(R.id.button_multiply);
        buttonDivide = view.findViewById(R.id.button_divide);

        // Set click listeners for buttons
        button0.setOnClickListener(this);
        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
        button3.setOnClickListener(this);
        button4.setOnClickListener(this);
        button5.setOnClickListener(this);
        button6.setOnClickListener(this);
        button7.setOnClickListener(this);
        button8.setOnClickListener(this);
        button9.setOnClickListener(this);
        buttonClear.setOnClickListener(this);
        buttonEqual.setOnClickListener(this);
        buttonPlus.setOnClickListener(this);
        buttonMinus.setOnClickListener(this);
        buttonMultiply.setOnClickListener(this);
        buttonDivide.setOnClickListener(this);

        // Initialize variables
        currentNumber = new StringBuilder();
        operand1 = 0;
        operand2 = 0;
        operator = ' ';

        return view;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.button_0) {
            appendNumber("0");
        } else if (id == R.id.button_1) {
            appendNumber("1");
        } else if (id == R.id.button_2) {
            appendNumber("2");
        } else if (id == R.id.button_3) {
            appendNumber("3");
        } else if (id == R.id.button_4) {
            appendNumber("4");
        } else if (id == R.id.button_5) {
            appendNumber("5");
        } else if (id == R.id.button_6) {
            appendNumber("6");
        } else if (id == R.id.button_7) {
            appendNumber("7");
        } else if (id == R.id.button_8) {
            appendNumber("8");
        } else if (id == R.id.button_9) {
            appendNumber("9");
        } else if (id == R.id.button_clear) {
            clearDisplay();
        } else if (id == R.id.button_plus) {
            setOperator('+');
        } else if (id == R.id.button_minus) {
            setOperator('-');
        } else if (id == R.id.button_multiply) {
            setOperator('*');
        } else if (id == R.id.button_divide) {
            setOperator('/');
        } else if (id == R.id.button_equal) {
            calculateResult();
        }
    }

    private void appendNumber(String number) {
        currentNumber.append(number);
        display.setText(currentNumber.toString());
    }

    private void clearDisplay() {
        currentNumber.setLength(0);
        display.setText("");
    }

    private void setOperator(char op) {
        operand1 = Double.parseDouble(currentNumber.toString());
        operator = op;
        clearDisplay();
    }

    private void calculateResult() {
        operand2 = Double.parseDouble(currentNumber.toString());
        double result = 0;
        switch (operator) {
            case '+':
                result = operand1 + operand2;
                break;
            case '-':
                result = operand1 - operand2;
                break;
            case '*':
                result = operand1 * operand2;
                break;
            case '/':
                if (operand2 != 0) {
                    result = operand1 / operand2;
                } else {
                    display.setText("Error: Division by zero");
                    return;
                }
                break;
        }
        display.setText(String.valueOf(result));
        currentNumber.setLength(0);
        currentNumber.append(result);
    }
}
