package com.example.creditdetails;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InsertActivity extends AppCompatActivity {

    private Button insert;
    private Spinner branch;
    private Spinner semester;
    private Spinner regulation;
    private EditText cname;
    private EditText ccredits;
    private EditText subName;

    private String branchName = null;
    private String semesterNumber = null;
    private String regulationName = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert);
        insert = findViewById(R.id.courseinsert);
        regulation = findViewById(R.id.regulationInsert);
        branch = findViewById(R.id.branchinput);
        semester = findViewById(R.id.semesterinput);
        cname = findViewById(R.id.coursename);
        ccredits = findViewById(R.id.coursecredit);
        subName = findViewById(R.id.subname);

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

        ManageDatabase manageDatabase = new ManageDatabase();

        regulation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                regulationName = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        branch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                branchName = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        semester.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                semesterNumber = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String subjectname = String.valueOf(cname.getText());
                String subText = String.valueOf(subName.getText());
                double subjectcredit = 0.0;
                if (subjectname.isEmpty()) {
                    cname.setError("Field required.");
                    cname.requestFocus();
                    return;
                }
                if (subText.isEmpty()) {
                    subName.setError("Field required.");
                    subName.requestFocus();
                    return;
                }
                if (ccredits.getText().toString().isEmpty()) {
                    ccredits.setError("Field required.");
                    ccredits.requestFocus();
                    return;
                } else {
                    subjectcredit = Double.parseDouble(String.valueOf(ccredits.getText()));
                }

                if (subjectcredit <= 0.0) {
                    ccredits.setError("Input a valid credit.");
                    ccredits.requestFocus();
                    return;
                }
                if (!branchName.equals("SELECT BRANCH") && !semesterNumber.equals("SELECT SEMESTER") && !regulationName.equals("SELECT REGULATION")) {
                    String regex = null;
                    switch (branchName) {
                        case "CSE": {
                            regex = "^[cC][sS][\\s][\\d][\\d][\\d]";
                            break;
                        }
                        case "ECE": {
                            regex = "^[eE][cC][\\s][\\d][\\d][\\d]";
                            break;
                        }
                        case "CIVIL": {
                            regex = "^[cC][eE][\\s][\\d][\\d][\\d]";
                            break;
                        }
                        case "CHEMICAL": {
                            regex = "^[cC][hH][\\s][\\d][\\d][\\d]";
                            break;
                        }
                        case "MECHANICAL": {
                            regex = "^[mM][eE][\\s][\\d][\\d][\\d]";
                            break;
                        }
                        case "EEE": {
                            regex = "^[eE][eE][\\s][\\d][\\d][\\d]";
                            break;
                        }
                        case "IT": {
                            regex = "^[iI][tT][\\s][\\d][\\d][\\d]";
                            break;
                        }
                    }
                    Pattern pattern = Pattern.compile(regex);
                    Matcher matcher = pattern.matcher(cname.getText().toString());
                    if(!matcher.matches()){
                        cname.setError("Input a valid code.");
                        cname.requestFocus();
                        return;
                    }
                    SubjectCredits sc = new SubjectCredits(subName.getText().toString(), subjectcredit);
                    manageDatabase.addData(cname.getText().toString(), sc,regulationName, branchName, semesterNumber).addOnSuccessListener(suc -> {
                        Toast.makeText(getApplicationContext(), "The data is successfully inserted.", Toast.LENGTH_SHORT).show();
                    }).addOnFailureListener(er -> {
                        Toast.makeText(getApplicationContext(), "" + er.getMessage(), Toast.LENGTH_SHORT).show();
                    });
                } else {
                    Toast.makeText(getApplicationContext(), "Please select Regulation,Branch and Semester.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}