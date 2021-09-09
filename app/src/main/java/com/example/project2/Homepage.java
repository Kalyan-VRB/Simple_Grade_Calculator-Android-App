package com.example.project2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

public class Homepage extends AppCompatActivity {
    private Button okBtn;
    private Spinner branch;
    private Spinner semester;
    private Spinner regulation;
    private String branchName;
    private String semesterNumber;
    private String regulationName;

    static String regulationSelected;
    static String branchSelected;
    static String semesterSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);
        okBtn=findViewById(R.id.ok);
        regulation = findViewById(R.id.selectRegulation);
        branch = findViewById(R.id.selectBranch);
        semester = findViewById(R.id.selectSemester);

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

        ArrayList<String> semList = new ArrayList<>();
        semList.add("SELECT SEMESTER");
        semList.add("SEMESTER I");
        semList.add("SEMESTER II");
        semList.add("SEMESTER III");
        semList.add("SEMESTER IV");
        semList.add("SEMESTER V");
        semList.add("SEMESTER VI");
        semList.add("SEMESTER VII");
        semList.add("SEMESTER VIII");
        ArrayAdapter<String> semAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, semList);
        semAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        semester.setAdapter(semAdapter);

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
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                branchName = parent.getItemAtPosition(position).toString();
                branchSelected = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        semester.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                semesterNumber = parent.getItemAtPosition(position).toString();
                semesterSelected = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!regulationName.equals("SELECT REGULATION") && !branchName.equals("SELECT BRANCH") && !semesterNumber.equals("SELECT SEMESTER")) {
                    startActivity(new Intent(Homepage.this,GradeActivity.class));
                } else {
                    Toast.makeText(getApplicationContext(), "Please select Regulation, Branch and Semester.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public static String getRegulationSelected(){return regulationSelected;}
    public static String getBranchSelected(){
        return branchSelected;
    }
    public static String getSemesterSelected(){
        return semesterSelected;
    }
}