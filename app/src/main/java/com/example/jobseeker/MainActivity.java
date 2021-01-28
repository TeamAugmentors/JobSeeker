package com.example.jobseeker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ListIterator;

public class MainActivity extends AppCompatActivity {

    private EditText editTextPhone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextPhone = (EditText)findViewById(R.id.editTextPhone);
    }

    public void gotoNextPage(View v){  //method for register button
        String phoneNo = editTextPhone.getText().toString();
        if(phoneNo.length()==0){
            Toast.makeText(this,"You must enter phone no",Toast.LENGTH_SHORT).show();
        }
        else{
            Intent intent = new Intent(this,SecondPage.class);
            intent.putExtra("phoneNo", phoneNo); //Sending the phone no to second page
            startActivity(intent);
        }
    }
}