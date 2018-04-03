package com.example.seba.myapplication;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

import org.w3c.dom.Text;

import java.io.Console;


public class Main2Activity extends AppCompatActivity {

    private int[] numberButtons={R.id.one, R.id.two, R.id.three, R.id.four, R.id.five, R.id.six, R.id.seven,
            R.id.eight, R.id.nine};
    private int[] operatorButtons={R.id.plus, R.id.minus, R.id.multiply, R.id.divide};
    TextView disp;
    private boolean lastNumber;
    private boolean error;
    private boolean lastDot;
    private boolean resFlag;
    private boolean signed;
    private String result;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        if(savedInstanceState==null) {
            Intent intent = getIntent();
            result = intent.getStringExtra("result");
            lastNumber=intent.getBooleanExtra("lastNumber",false);
            error=intent.getBooleanExtra("error",false);
            lastDot=intent.getBooleanExtra("lastDot",false);
            resFlag=intent.getBooleanExtra("resFlag", false);
            signed=intent.getBooleanExtra("signed", false);
        }
        else{
            result=savedInstanceState.getString("result");
            lastNumber=savedInstanceState.getBoolean("lastNumber");
            error=savedInstanceState.getBoolean("error");
            lastDot=savedInstanceState.getBoolean("lastDot");
            resFlag=savedInstanceState.getBoolean("resFlag");
            signed=savedInstanceState.getBoolean("signed");
        }

        this.disp=(TextView) findViewById(R.id.display);
        this.disp.setText(result);
        setNumberOnClickListener();
        setOperatorOnClickListener();
    }

    private void setNumberOnClickListener(){
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button but=(Button) v;
                if(error) {
                    disp.setText(but.getText());
                }
                else {
                    disp.append(but.getText());
                }
                lastNumber=true;
                resFlag=false;
            }
        };
        for (int id : numberButtons) {
            findViewById(id).setOnClickListener(listener);
        }
        findViewById(R.id.zero).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button button=(Button)v;
                if(lastNumber || lastDot)
                {
                    disp.append(button.getText());
                    resFlag=false;
                }
            }
        });
    }
    private void setOperatorOnClickListener(){
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int len=disp.getText().length();
                if(signed) {
                    disp.append(")");
                    signed=false;
                }
                if(disp.getText().toString().substring(len-1, len).equals("."))
                {
                    disp.append("0");
                }
                if(lastNumber && !error) {
                    Button but = (Button) v;
                    disp.append(but.getText());
                    lastNumber=false;
                    lastDot=false;
                    resFlag=false;
                }
            }
        };
        for(int id : operatorButtons) {
            findViewById(id).setOnClickListener(listener);
        }
        findViewById(R.id.dot).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(lastNumber && !error && !lastDot) {
                    disp.append(".");
                    lastNumber=false;
                    lastDot=true;
                    resFlag=false;
                }
                if(!lastNumber && !error && !lastDot) {
                    disp.append("0.");
                    lastNumber=false;
                    lastDot=true;
                    resFlag=false;
                }
            }
        });
        findViewById(R.id.equal).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onEqual();
            }
        });
        findViewById(R.id.delAll).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                disp.setText("");
                lastNumber=false;
                lastDot=false;
                error=false;
                resFlag=false;
                signed=false;
            }
        });
        findViewById(R.id.del).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                boolean in=false;
                String pom;
                String befDel;
                String toDel;
                int last = disp.getText().length();
                if(!error && !resFlag && last>0) {
                    pom = disp.getText().toString().substring(0, last - 1);
                    befDel = disp.getText().toString().substring(last - 2, last - 1);
                    toDel = disp.getText().toString().substring(last - 1, last);
                    if (toDel.equals("(")) {
                        signed = false;
                    }

                    if (toDel.equals(")")) {
                        signed = true;
                    }

                    if (befDel.equals("+") || befDel.equals("-") || befDel.equals("*") || befDel.equals("/"))
                    {
                        lastNumber=false;
                    }
                    else {
                        lastNumber = true;
                    }

                    if(toDel.equals("+") || toDel.equals("-") || toDel.equals("*") || toDel.equals("/"))
                        lastNumber=true;
                    disp.setText(pom);
                }
            }
        });
        findViewById(R.id.plus_minus).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //signed=true;
                int last=lastNumberFirstID(disp.getText().toString());
                /*toast show return of lastNumberFirstID method
                Context context = getApplicationContext();
                CharSequence text = Integer.toString(last);
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(context, "last:"+text, duration);
                toast.show();
                */
                if(!error && lastNumber==true) {
                    String pom=disp.getText().toString().substring(0, last);
                    String rest=disp.getText().toString().substring(last, disp.getText().length());
                    disp.setText(pom+"(-"+rest);
                    signed=true;
                }
                if(!error && !lastNumber) {
                    disp.append("(-");
                    signed=true;
                }
            }
        });
    }
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {


        savedInstanceState.putString("result", disp.getText().toString());
        savedInstanceState.putBoolean("lastNumber", lastNumber);
        savedInstanceState.putBoolean("lastDot", lastDot);
        savedInstanceState.putBoolean("error", error);

        super.onSaveInstanceState(savedInstanceState);
    }
    private int lastNumberFirstID(String in) {

        for (int i=in.length(); i>=1; i--) {
            //toast for sign on specified id
            /*CharSequence text = Integer.toString(i);
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, "i:"+text, duration);
            toast.show();*/
            String search=in.substring(i-1, i);
            if(search.equals("+") || search.equals("-") || search.equals("/") || search.equals("*")) {
                return i;
            }
        }
        return 0;
    }
    private void onEqual(){
        if(signed){
            disp.append(")");
            signed=false;
        }
        if(lastNumber && !error) {
            String out=disp.getText().toString();
            Expression expression = new ExpressionBuilder(out).build();
            try{
                double result=expression.evaluate();
                disp.setText(Double.toString(result));
                lastDot=true;
                resFlag=true;
            }
            catch (ArithmeticException ex) {
                disp.setText("Error");
                error=true;
                lastNumber=false;
                resFlag=false;
            }
        }
    }
}