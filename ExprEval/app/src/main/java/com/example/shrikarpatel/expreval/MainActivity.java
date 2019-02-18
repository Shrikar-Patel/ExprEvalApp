package com.example.shrikarpatel.expreval;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.shrikarpatel.expreval.src.app.Array;
import com.example.shrikarpatel.expreval.src.app.Expression;
import com.example.shrikarpatel.expreval.src.app.Variable;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EditText firstNumEditText = (EditText) findViewById(R.id.firstNumEditText);
        final String expression = firstNumEditText.getText().toString();
        final ArrayList<Array> array = new ArrayList<Array>();
        final ArrayList<Variable>  var = new ArrayList<Variable>();

        Expression.makeVariableLists(expression,var,array);
//System.out.println(var);




        Button variableButton = (Button) findViewById(R.id.variableButton);
        variableButton.setOnClickListener(new View.OnClickListener()  {
            public void onClick(View view){
                EditText editTextVar = (EditText) findViewById(R.id.editTextVar);
                EditText editTextNum = (EditText) findViewById(R.id.editTextNum);
                String varName = editTextVar.getText().toString();
                String numStr = editTextNum.getText().toString();
                int num = Integer.parseInt(numStr);
                Expression.makeVariableLists(expression,var,array);
                int x =var.indexOf(varName);
                if(x == -1){
                    //do a pop up textbox, invalid number

                }else{
                    var.get(x).value = num;
                }

            }
        });


        Button addBtn = (Button) findViewById(R.id.addBtn);
        addBtn.setOnClickListener(new View.OnClickListener()   {
            public void onClick (View view){
                EditText firstNumEditText = (EditText) findViewById(R.id.firstNumEditText);
                //EditText secondNumEditText = (EditText) findViewById(R.id.secondNumEditText);
                TextView resultTextView  = (TextView) findViewById(R.id.resultTextView);

             //  String expression = firstNumEditText.getText().toString();
             //  float result = Expression.evaluate(expression, null,null);

              //  int num2 = Integer.parseInt(secondNumEditText.getText().toString());
              //  int result = num1 + num2;
              //  ArrayList<Array> array = new ArrayList<Array>();
               // ArrayList<Variable>  var = new ArrayList<Variable>();


                String expression2 = firstNumEditText.getText().toString();

                float result = Expression.evaluate(expression2, var,array);


                resultTextView.setText(result + "");

            }
        });


    }
}
