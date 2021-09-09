package com.example.creditdetails;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DeleteActivity extends AppCompatActivity {


    private Button deleteButton;
    private Button displayButton;
    private Spinner regulationDel;
    private Spinner branchDel;
    private Spinner semDelete;
    private EditText subCode;

    private String regulationName = null;
    private String branchName = null;
    private String semesterNumber = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete);

        deleteButton = findViewById(R.id.btnDelete);
        displayButton = findViewById(R.id.btnDisplay);
        regulationDel = findViewById(R.id.regulationDelete);
        branchDel = findViewById(R.id.branchDelete);
        semDelete = findViewById(R.id.semesterDelete);
        subCode = findViewById(R.id.courseDelete);

        ArrayList<String> regulationList = new ArrayList<>();
        regulationList.add("SELECT REGULATION");
        regulationList.add("R-12");
        regulationList.add("R-16");
        regulationList.add("R-18");
        regulationList.add("R-20");
        ArrayAdapter<String> regulationAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, regulationList);
        regulationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        regulationDel.setAdapter(regulationAdapter);

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
        branchDel.setAdapter(branchAdapter);

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
        semDelete.setAdapter(semAdapter);

        ManageDatabase manageDatabase = new ManageDatabase();

        regulationDel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                regulationName = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        branchDel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                branchName = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        semDelete.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                semesterNumber = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String code = subCode.getText().toString();
                if(code.isEmpty()){
                    subCode.setError("Field required.");
                    subCode.requestFocus();
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
                    Matcher matcher = pattern.matcher(subCode.getText().toString());
                    if(!matcher.matches()){
                        subCode.setError("Input a valid subject code.");
                        subCode.requestFocus();
                        return;
                    }
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(SubjectCredits.class.getSimpleName());
                    DatabaseReference regulationRef = databaseReference.child(regulationName);
                    DatabaseReference branchRef = regulationRef.child(branchName);
                    DatabaseReference semRef = branchRef.child(semesterNumber);
                    DatabaseReference subRef = semRef.child(code.toUpperCase());
                    subRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.exists()){
                                manageDatabase.removeData(code, regulationName,branchName, semesterNumber).addOnSuccessListener(suc -> {
                                    Toast.makeText(getApplicationContext(), "The data is successfully removed.", Toast.LENGTH_SHORT).show();
                                }).addOnFailureListener(er -> {
                                    Toast.makeText(getApplicationContext(), "" + er.getMessage(), Toast.LENGTH_SHORT).show();
                                });
                            }
                            else{
                                Toast.makeText(getApplicationContext(), "The data is not present.", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                } else {
                    Toast.makeText(getApplicationContext(), "Please select Regulation, Branch and Semester.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        displayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String code = subCode.getText().toString();
                if(code.isEmpty()){
                    subCode.setError("Field required.");
                    subCode.requestFocus();
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
                    Matcher matcher = pattern.matcher(subCode.getText().toString());
                    if(!matcher.matches()){
                        subCode.setError("Input a valid code.");
                        subCode.requestFocus();
                        return;
                    }
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(SubjectCredits.class.getSimpleName());
                    DatabaseReference regulationRef = databaseReference.child(regulationName);
                    DatabaseReference branchRef = regulationRef.child(branchName);
                    DatabaseReference semRef = branchRef.child(semesterNumber);
                    DatabaseReference subRef = semRef.child(code.toUpperCase());
                    subRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.exists()){
                                ViewGroup viewGroup = findViewById(android.R.id.content);
                                View dialogView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.display_course,viewGroup,false);
                                Button okButton = dialogView.findViewById(R.id.btnDisplay);
                                TextView branchText = dialogView.findViewById(R.id.textBranch);
                                TextView semesterText = dialogView.findViewById(R.id.textSemester);
                                TextView codeText = dialogView.findViewById(R.id.textCode);
                                TextView subjectText = dialogView.findViewById(R.id.textSubject);
                                TextView creditsText = dialogView.findViewById(R.id.textCredits);
                                branchText.setText("Branch: "+branchName);
                                semesterText.setText("Semester: "+semesterNumber);
                                codeText.setText("Subject Code: " + code.toUpperCase());

                                SubjectCredits map = snapshot.getValue(SubjectCredits.class);
                                subjectText.setText("Subject Name: "+map.getSubjectName());
                                creditsText.setText("Subject Credits: "+map.getSubjectCredits());

                                AlertDialog.Builder builder = new AlertDialog.Builder(DeleteActivity.this);
                                builder.setView(dialogView);
                                AlertDialog alertDialog = builder.create();
                                alertDialog.setCancelable(false);
                                alertDialog.setCanceledOnTouchOutside(false);
                                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                alertDialog.show();
                                okButton.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        alertDialog.cancel();
                                    }
                                });
                            }
                            else{
                                Toast.makeText(getApplicationContext(),"Invalid subject code.",Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                } else {
                    if (branchName.equals("SELECT BRANCH") && semesterNumber.equals("SELECT SEMESTER")) {
                        Toast.makeText(getApplicationContext(), "Please select Branch and Semester.", Toast.LENGTH_SHORT).show();
                    } else {
                        if (branchName.equals("SELECT BRANCH")) {
                            Toast.makeText(getApplicationContext(), "Please select Branch.", Toast.LENGTH_SHORT).show();
                        }
                        if (semesterNumber.equals("SELECT SEMESTER")) {
                            Toast.makeText(getApplicationContext(), "Please select Semester.", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });
    }
}