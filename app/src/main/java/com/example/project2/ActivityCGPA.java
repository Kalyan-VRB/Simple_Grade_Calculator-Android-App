package com.example.project2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Objects;

public class ActivityCGPA extends AppCompatActivity {

    static int[][] gradeSelectedListCGPA = new int[HomepageCGPA.getCount()][];
    static double[][] creditsListCGPA = new double[HomepageCGPA.getCount()][];

    private Button nextButton;
    private Button prevButton;
    private Button calculateButton;
    private TextView enterHead;

    RecyclerView recyclerView;
    MyAdapterCGPA myAdapterCGPA;
    ArrayList<SubjectCredits> list;
    ArrayList<String> codeList;
    int[] indexList = new int[HomepageCGPA.getCount()];
    private int indexSemester = 0;
    ShimmerFrameLayout shimmerFrameLayout;

    static int currentSemester;
    static boolean[] semVisited;

    private int listSize = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cgpa);

        nextButton = findViewById(R.id.nextSem);
        prevButton = findViewById(R.id.prevSem);
        enterHead = findViewById(R.id.enterGradeCGPA);
        calculateButton = findViewById(R.id.calculateCGPA);
        recyclerView = findViewById(R.id.gradeRecyclerViewCGPA);
        shimmerFrameLayout = findViewById(R.id.shimmerLayoutCGPA);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(SubjectCredits.class.getSimpleName());
        DatabaseReference regulationRef = databaseReference.child(HomepageCGPA.getRegulationSelected());
        DatabaseReference branchRef = regulationRef.child(HomepageCGPA.getBranchSelected());
        recyclerView.setLayoutManager(new LinearLayoutManager(ActivityCGPA.this));

        Dictionary<Integer, Integer> gradePoints = new Hashtable<>();
        gradePoints.put(0, 10);
        gradePoints.put(1, 9);
        gradePoints.put(2, 8);
        gradePoints.put(3, 7);
        gradePoints.put(4, 6);
        gradePoints.put(5, 5);
        gradePoints.put(6, 0);

        ArrayList<String> gradeList = new ArrayList<>();
        gradeList.add("SELECT GRADE");
        gradeList.add("O");
        gradeList.add("A+");
        gradeList.add("A");
        gradeList.add("B+");
        gradeList.add("B");
        gradeList.add("C");
        gradeList.add("F");
        ArrayAdapter<String> gradeAdapter = new ArrayAdapter<>(ActivityCGPA.this, android.R.layout.simple_spinner_item, gradeList);
        gradeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        Dictionary<Integer, String> selectedSemesters = new Hashtable<>();

        for (int i = 0; i < 8; i++) {
            if (!HomepageCGPA.semestersIncluded[i].equals("NA")) {
                selectedSemesters.put(i, HomepageCGPA.semestersIncluded[i]);
                indexList[indexSemester] = i;
                semVisited[indexSemester] = false;
                indexSemester += 1;
            }
        }

        semVisited[0] = true;
        enterHead.setText(String.format("Enter grades for:\n%s", selectedSemesters.get(indexList[0])));

        DatabaseReference semRef = branchRef.child(Objects.requireNonNull(selectedSemesters.get(indexList[0])));
        shimmerFrameLayout.startShimmerAnimation();

        semRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    listSize = (int) snapshot.getChildrenCount();
                    if(listSize>0) {
                        gradeSelectedListCGPA[0] = new int[listSize];
                        creditsListCGPA[0] = new double[listSize];

                        list = new ArrayList<>();
                        codeList = new ArrayList<>();
                        myAdapterCGPA = new MyAdapterCGPA(ActivityCGPA.this, list, codeList, gradeAdapter);

                        shimmerFrameLayout.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                        recyclerView.setAdapter(myAdapterCGPA);
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            SubjectCredits subjectCredits = dataSnapshot.getValue(SubjectCredits.class);
                            codeList.add(dataSnapshot.getKey());
                            list.add(subjectCredits);
                        }
                        myAdapterCGPA.notifyDataSetChanged();
                    }
                    else{
                        recyclerView.setVisibility(View.GONE);
                        shimmerFrameLayout.setVisibility(View.VISIBLE);
                        shimmerFrameLayout.startShimmerAnimation();
                    }
                }
                else{
                    recyclerView.setVisibility(View.GONE);
                    shimmerFrameLayout.setVisibility(View.VISIBLE);
                    shimmerFrameLayout.startShimmerAnimation();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentSemester < HomepageCGPA.getCount() - 1) {
                    currentSemester += 1;
                    enterHead.setText(String.format("Enter grades for:\n%s", selectedSemesters.get(indexList[currentSemester])));

                    DatabaseReference semRef = branchRef.child(Objects.requireNonNull(selectedSemesters.get(indexList[currentSemester])));
                    shimmerFrameLayout.startShimmerAnimation();

                    semRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                listSize = (int) snapshot.getChildrenCount();
                                if (!semVisited[currentSemester]) {
                                    if(listSize>0) {
                                        gradeSelectedListCGPA[currentSemester] = new int[listSize];
                                        creditsListCGPA[currentSemester] = new double[listSize];
                                        semVisited[currentSemester] = true;
                                    }
                                }

                                if(listSize>0) {
                                    list = new ArrayList<>();
                                    codeList = new ArrayList<>();
                                    myAdapterCGPA = new MyAdapterCGPA(ActivityCGPA.this, list, codeList, gradeAdapter);

                                    shimmerFrameLayout.setVisibility(View.GONE);
                                    recyclerView.setVisibility(View.VISIBLE);
                                    recyclerView.setAdapter(myAdapterCGPA);
                                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                        SubjectCredits subjectCredits = dataSnapshot.getValue(SubjectCredits.class);
                                        codeList.add(dataSnapshot.getKey());
                                        list.add(subjectCredits);
                                    }
                                    myAdapterCGPA.notifyDataSetChanged();
                                }
                                else{
                                    recyclerView.setVisibility(View.GONE);
                                    shimmerFrameLayout.setVisibility(View.VISIBLE);
                                    shimmerFrameLayout.startShimmerAnimation();
                                }
                            }
                            else{
                                recyclerView.setVisibility(View.GONE);
                                shimmerFrameLayout.setVisibility(View.VISIBLE);
                                shimmerFrameLayout.startShimmerAnimation();
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                } else {
                    Toast.makeText(getApplicationContext(), "Reached the end.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentSemester > 0) {
                    currentSemester -= 1;
                    enterHead.setText(String.format("Enter grades for:\n%s", selectedSemesters.get(indexList[currentSemester])));

                    DatabaseReference semRef = branchRef.child(Objects.requireNonNull(selectedSemesters.get(indexList[currentSemester])));
                    shimmerFrameLayout.startShimmerAnimation();

                    semRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                listSize = (int) snapshot.getChildrenCount();
                                if (!semVisited[currentSemester]) {
                                    if(listSize>0) {
                                        gradeSelectedListCGPA[currentSemester] = new int[listSize];
                                        creditsListCGPA[currentSemester] = new double[listSize];
                                        semVisited[currentSemester] = true;
                                    }
                                }

                                if(listSize>0) {
                                    list = new ArrayList<>();
                                    codeList = new ArrayList<>();
                                    myAdapterCGPA = new MyAdapterCGPA(ActivityCGPA.this, list, codeList, gradeAdapter);

                                    shimmerFrameLayout.setVisibility(View.GONE);
                                    recyclerView.setVisibility(View.VISIBLE);
                                    recyclerView.setAdapter(myAdapterCGPA);
                                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                        SubjectCredits subjectCredits = dataSnapshot.getValue(SubjectCredits.class);
                                        codeList.add(dataSnapshot.getKey());
                                        list.add(subjectCredits);
                                    }
                                    myAdapterCGPA.notifyDataSetChanged();
                                }
                                else{
                                    recyclerView.setVisibility(View.GONE);
                                    shimmerFrameLayout.setVisibility(View.VISIBLE);
                                    shimmerFrameLayout.startShimmerAnimation();
                                }
                            }
                            else{
                                recyclerView.setVisibility(View.GONE);
                                shimmerFrameLayout.setVisibility(View.VISIBLE);
                                shimmerFrameLayout.startShimmerAnimation();
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                } else {
                    Toast.makeText(getApplicationContext(), "This is the beginning.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        calculateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean status = true;
                for (int i = 0; i < HomepageCGPA.getCount(); i++) {
                    if (semVisited[i]) {
                        for (int j = 0; j < gradeSelectedListCGPA[i].length; j++) {
                            if (gradeSelectedListCGPA[i][j] == 0) {
                                status = false;
                                break;
                            }
                        }
                    } else {
                        status = false;
                        break;
                    }
                }
                if (status) {
                    double gradeObtained ;
                    double totalCredits = 0.0;
                    double creditsObtained = 0.0;

                    boolean failed = false;
                    for (int i = 0; i < HomepageCGPA.getCount(); i++) {
                        for(int j=0; j<gradeSelectedListCGPA[i].length;j++) {
                            totalCredits = totalCredits + creditsListCGPA[i][j];
                            creditsObtained = creditsObtained + gradePoints.get(gradeSelectedListCGPA[i][j] - 1) * creditsListCGPA[i][j];
                            if (gradeSelectedListCGPA[i][j]==7) {
                                failed = true;
                            }
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
                        gradeHeading.setText("Failed, Better luck next time!\nYour S.G.P.A is :");
                    }
                    else {
                        gradeHeading.setText("Your C.G.P.A is :");
                    }
                    gradeObtainedText.setText(String.format("%.2f", gradeObtained));
                    semesterText.setText(Homepage.getSemesterSelected());
                    AlertDialog.Builder builder = new AlertDialog.Builder(ActivityCGPA.this);
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
                    Toast.makeText(getApplicationContext(), "Please select all the grades in the semesters.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}