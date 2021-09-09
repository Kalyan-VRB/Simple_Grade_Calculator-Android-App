package com.example.project2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Dictionary;
import java.util.Hashtable;

public class GradeActivity extends AppCompatActivity {
    private Button calcGrade;

    RecyclerView recyclerView;
    MyAdapter myAdapter;
    ArrayList<SubjectCredits> list;
    ArrayList<String> codeList;
    ArrayList<Double> creditsList;
    ShimmerFrameLayout shimmerFrameLayout;
    static String[] gradeSelectedList;
    private int listSize = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grade);
        calcGrade = findViewById(R.id.Calculate);
        recyclerView = findViewById(R.id.gradeRecyclerView);
        shimmerFrameLayout = findViewById(R.id.shimmerLayout);


        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(SubjectCredits.class.getSimpleName());
        DatabaseReference regulationRef = databaseReference.child(Homepage.getRegulationSelected());
        DatabaseReference branchRef = regulationRef.child(Homepage.getBranchSelected());
        DatabaseReference semRef = branchRef.child(Homepage.getSemesterSelected());

        ArrayList<String> gradeList = new ArrayList<>();
        gradeList.add("SELECT GRADE");
        gradeList.add("O");
        gradeList.add("A+");
        gradeList.add("A");
        gradeList.add("B+");
        gradeList.add("B");
        gradeList.add("C");
        gradeList.add("F");
        ArrayAdapter<String> gradeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, gradeList);
        gradeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<>();
        codeList = new ArrayList<>();
        creditsList = new ArrayList<>();
        myAdapter = new MyAdapter(this, list, codeList, gradeAdapter, creditsList);
        shimmerFrameLayout.startShimmerAnimation();

        semRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    listSize = (int) snapshot.getChildrenCount();
                    gradeSelectedList = new String[listSize];
                    for (int i = 0; i < listSize; i++) {
                        gradeSelectedList[i] = "NA";
                    }
                    shimmerFrameLayout.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                    recyclerView.setAdapter(myAdapter);
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        SubjectCredits subjectCredits = dataSnapshot.getValue(SubjectCredits.class);
                        codeList.add(dataSnapshot.getKey());
                        list.add(subjectCredits);
                    }
                }
                myAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        Dictionary<String, Integer> gradePoints = new Hashtable<>();
        gradePoints.put("O", 10);
        gradePoints.put("A+", 9);
        gradePoints.put("A", 8);
        gradePoints.put("B+", 7);
        gradePoints.put("B", 6);
        gradePoints.put("C", 5);
        gradePoints.put("F", 0);

        calcGrade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean status = true;
                for (int i = 0; i < listSize; i++) {
                    if (gradeSelectedList[i].equals("NA")) {
                        status = false;
                        break;
                    }
                }
                if (status) {
                    double gradeObtained ;
                    double totalCredits = 0.0;
                    double creditsObtained = 0.0;
                    boolean failed = false;
                    for (int i = 0; i < listSize; i++) {
                            totalCredits = totalCredits + MyAdapter.creditsList.get(i);
                            creditsObtained = creditsObtained+ gradePoints.get(gradeSelectedList[i])*MyAdapter.creditsList.get(i);
                            if(gradeSelectedList[i].equals("F")){
                                failed = true;
                            }
                    }
                    gradeObtained = creditsObtained/totalCredits;
                    ViewGroup viewGroup = findViewById(android.R.id.content);
                    View dialogView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.display_grade, viewGroup, false);
                    Button okButton = dialogView.findViewById(R.id.gradeOkBtn);
                    TextView gradeObtainedText = dialogView.findViewById(R.id.gradeDisplay);
                    TextView gradeHeading = dialogView.findViewById(R.id.gradeHead);
                    TextView semesterText = dialogView.findViewById(R.id.sgpaText);
                    if(failed){
                        gradeHeading.setText("Failed, Better luck next time!\nYour C.G.P.A is :");
                    }
                    else {
                        gradeHeading.setText("Your S.G.P.A is :");
                    }
                    gradeObtainedText.setText(String.format("%.2f", gradeObtained));
                    semesterText.setText(Homepage.getSemesterSelected());
                    AlertDialog.Builder builder = new AlertDialog.Builder(GradeActivity.this);
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
                } else {
                    Toast.makeText(getApplicationContext(), "Please select all the grades.", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}