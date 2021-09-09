package com.example.project2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;

public class MyAdapterCGPA extends RecyclerView.Adapter<MyAdapterCGPA.MyViewHolderCGPA> {

    Context context;
    ArrayList<SubjectCredits> list;
    ArrayList<String> listCode;

    static ArrayAdapter<String> gradeAdapter;

    public MyAdapterCGPA(Context context, ArrayList<SubjectCredits> list, ArrayList<String> listCode, ArrayAdapter<String> gradeAdapter) {
        this.context = context;
        this.list = list;
        this.listCode = listCode;
        MyAdapterCGPA.gradeAdapter = gradeAdapter;
    }

    @NonNull
    @Override
    public MyViewHolderCGPA onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(context).inflate(R.layout.subject_grade, parent, false);
        return new MyViewHolderCGPA(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolderCGPA holder, int position) {
        SubjectCredits subjectCredits = list.get(position);
        String subjectCode = listCode.get(position);
        holder.subCodeText.setText(subjectCode);
        holder.subNameText.setText(subjectCredits.getSubjectName());
        if(ActivityCGPA.semVisited[ActivityCGPA.currentSemester]) {
            ActivityCGPA.creditsListCGPA[ActivityCGPA.currentSemester][position] = subjectCredits.getSubjectCredits();
        }
        holder.gradeSpinner.setSelection(ActivityCGPA.gradeSelectedListCGPA[ActivityCGPA.currentSemester][position]);
        holder.gradeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                ActivityCGPA.gradeSelectedListCGPA[ActivityCGPA.currentSemester][position] = i;
                holder.gradeSpinner.setSelection(ActivityCGPA.gradeSelectedListCGPA[ActivityCGPA.currentSemester][position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public static class MyViewHolderCGPA extends RecyclerView.ViewHolder {

        TextView subNameText;
        TextView subCodeText;
        Spinner gradeSpinner;

        public MyViewHolderCGPA(@NonNull View itemView) {
            super(itemView);
            subNameText = itemView.findViewById(R.id.subject);
            subCodeText = itemView.findViewById(R.id.codeSub);
            gradeSpinner = itemView.findViewById(R.id.grade);
            gradeSpinner.setAdapter(MyAdapterCGPA.gradeAdapter);

        }
    }
}