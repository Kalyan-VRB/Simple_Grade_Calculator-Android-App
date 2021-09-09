
package com.example.project2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

public class HomepageCGPA extends AppCompatActivity {

    private Button okButton;
    private CheckBox sem1Check;
    private CheckBox sem2Check;
    private CheckBox sem3Check;
    private CheckBox sem4Check;
    private CheckBox sem5Check;
    private CheckBox sem6Check;
    private CheckBox sem7Check;
    private CheckBox sem8Check;
    private Spinner branch;
    private Spinner regulation;

    private String branchName=null;
    private String regulationName=null;
    static String branchSelected = null;
    static String regulationSelected = null;
    static String[] semestersIncluded = {"NA", "NA", "NA", "NA", "NA", "NA", "NA", "NA"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage_cgpa);

        okButton = findViewById(R.id.okCGPA);
        sem1Check = findViewById(R.id.sem1);
        sem2Check = findViewById(R.id.sem2);
        sem3Check = findViewById(R.id.sem3);
        sem4Check = findViewById(R.id.sem4);
        sem5Check = findViewById(R.id.sem5);
        sem6Check = findViewById(R.id.sem6);
        sem7Check = findViewById(R.id.sem7);
        sem8Check = findViewById(R.id.sem8);
        regulation = findViewById(R.id.spinnerRegulation);
        branch = findViewById(R.id.spinnerBranch);

        ArrayList<String> regulationList = new ArrayList<>();
        regulationList.add("SELECT REGULATION");
        regulationList.add("R-12");
        regulationList.add("R-16");
        regulationList.add("R-18");
        regulationList.add("R-20");
        ArrayAdapter<String> regulationAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, regulationList);
        regulationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        regulation.setAdapter(regulationAdapter);

        ArrayList<String> branchList = new ArrayList<>();
        branchList.add("SELECT BRANCH");
        branchList.add("CSE");
        branchList.add("ECE");
        branchList.add("EEE");
        branchList.add("CIVIL");
        branchList.add("MECHANICAL");
        branchList.add("CHEMICAL");
        branchList.add("IT");
        ArrayAdapter<String> branchAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, branchList);
        branchAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        branch.setAdapter(branchAdapter);

        regulation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                regulationName = adapterView.getItemAtPosition(i).toString();
                regulationSelected = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        branch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                branchName = adapterView.getItemAtPosition(i).toString();
                branchSelected = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!regulationName.equals("SELECT REGULATION") && !branchName.equals("SELECT BRANCH")) {
                    if(sem1Check.isChecked()){
                        semestersIncluded[0]="SEMESTER I";
                    }
                    else{
                        semestersIncluded[0]="NA";
                    }
                    if(sem2Check.isChecked()){
                        semestersIncluded[1]="SEMESTER II";
                    }
                    else{
                        semestersIncluded[1]="NA";
                    }
                    if(sem3Check.isChecked()){
                        semestersIncluded[2]="SEMESTER III";
                    }
                    else{
                        semestersIncluded[2]="NA";
                    }
                    if(sem4Check.isChecked()){
                        semestersIncluded[3]="SEMESTER IV";
                    }
                    else{
                        semestersIncluded[3]="NA";
                    }
                    if(sem5Check.isChecked()){
                        semestersIncluded[4]="SEMESTER V";
                    }
                    else{
                        semestersIncluded[4]="NA";
                    }
                    if(sem6Check.isChecked()){
                        semestersIncluded[5]="SEMESTER VI";
                    }
                    else{
                        semestersIncluded[5]="NA";
                    }
                    if(sem7Check.isChecked()){
                        semestersIncluded[6]="SEMESTER VII";
                    }
                    else{
                        semestersIncluded[6]="NA";
                    }
                    if(sem8Check.isChecked()){
                        semestersIncluded[7]="SEMESTER VIII";
                    }
                    else{
                        semestersIncluded[7]="NA";
                    }
                    if(getCount()>0) {
                        ActivityCGPA.currentSemester=0;
                        ActivityCGPA.semVisited = new boolean[getCount()];
                        startActivity(new Intent(getApplicationContext(),ActivityCGPA.class));
                    }
                    else{
                        Toast.makeText(getApplicationContext(), "Please select at-least one semester", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(getApplicationContext(), "Please select Regulation and Branch.", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    public static int getCount(){
        int count = 0;
        for(int i=0;i<8;i++){
            if(!semestersIncluded[i].equals("NA")){
                count+=1;
            }
        }
        return count;
    }
    public static String getRegulationSelected(){return regulationSelected;}
    public static String getBranchSelected(){return branchSelected;}
}