package com.boulio.tipcalculator;

import android.annotation.TargetApi;
import java.text.NumberFormat;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Build;
import android.support.v4.app.NotificationCompatSideChannelService;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener ;
import android.view.View.OnClickListener ;


public class MainActivity extends AppCompatActivity
        implements OnEditorActionListener, OnClickListener {


    //defining variables for widgets.........
    private EditText billAmountEditText;
    private TextView percentTextView;
    private TextView tipTextView;
    private TextView totalTextView;
    private Button addButton;
    private Button subtractButton;

    //define instance variables.......
    private String billAmountString = "";
    private float  tipPercent = .15f;


    // define shared preferences
    //saves values of stuff when you shut the app down
    private SharedPreferences SavedValues;





    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //get refrences for widgets....
        billAmountEditText = (EditText) findViewById(R.id.BillAmountEditText);
        percentTextView    = (TextView) findViewById(R.id.TipPercentTextField);
        tipTextView        = (TextView) findViewById(R.id.TipTotalTextField);
        totalTextView      = (TextView) findViewById(R.id.TotalTextField);
        addButton          = (Button) findViewById(R.id.PercentUpButton);
        subtractButton     = (Button) findViewById(R.id.PercentDownButton);

        //set Listeneres
        billAmountEditText.setOnEditorActionListener(this);
        addButton.setOnClickListener(this);
        subtractButton.setOnClickListener(this);

        //get shared preferences objecto
        SavedValues = getSharedPreferences("SavedValues" , MODE_PRIVATE);


    }

    @Override
    public boolean onEditorAction(TextView textView, int actionID, KeyEvent keyEvent) {
        if(actionID == EditorInfo.IME_ACTION_DONE || actionID   == EditorInfo.IME_ACTION_UNSPECIFIED){
            calculateAndDisplay();
        }
        return false;
    }

    @TargetApi(Build.VERSION_CODES.N)
    private void calculateAndDisplay(){
        billAmountString = billAmountEditText.getText().toString();
        float billAmount;
        if(billAmountString.equals("")){
            billAmount = 0;
        }else{
            billAmount = Float.parseFloat(billAmountString);
        }
        float tipAmount = billAmount *tipPercent;
        float totalAmount = billAmount +tipAmount;

        // display the other results with formatting;
        NumberFormat currency = NumberFormat.getCurrencyInstance();
        tipTextView.setText(currency.format(tipAmount));
        totalTextView.setText(currency.format(totalAmount));

        NumberFormat percent = NumberFormat.getPercentInstance();
        percentTextView.setText(percent.format(tipPercent));

    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.PercentDownButton:tipPercent = tipPercent - 0.01f;
                calculateAndDisplay();
                break;
            case R.id.PercentUpButton: tipPercent = tipPercent + 0.01f;
                calculateAndDisplay();
                break;
        }
    }

    @Override
    protected void onResume() {
        //get the saved values
        billAmountString = SavedValues.getString("billAmountString","");
        tipPercent = SavedValues.getFloat("tipPercent",0.015f);

        //calculate && display method
        calculateAndDisplay();

        super.onResume();
    }

    @Override
    protected void onPause(){
        //save the instance variables
        Editor editor = SavedValues.edit();
        editor.putString("billAmountString",billAmountString);
        editor.putFloat("tipPercent",tipPercent);
        editor.apply();

        super.onPause();
    }
}
