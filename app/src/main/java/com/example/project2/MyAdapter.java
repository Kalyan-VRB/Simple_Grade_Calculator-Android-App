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

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    Context context;
    ArrayList<SubjectCredits> list;
    static ArrayList<String> listCode;
    static ArrayList<Double> creditsList;

    static ArrayAdapter<String> gradeAdapter;

    public MyAdapter(Context context, ArrayList<SubjectCredits> list, ArrayList<String> listCode, ArrayAdapter<String> gradeAdapter, ArrayList<Double> creditsList) {
        this.context = context;
        this.list = list;
        MyAdapter.listCode = listCode;
        MyAdapter.creditsList = creditsList;
        MyAdapter.gradeAdapter = gradeAdapter;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(context).inflate(R.layout.subject_grade, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        SubjectCredits subjectCredits = list.get(position);
        String subjectCode = listCode.get(position);
        holder.subCodeText.setText(subjectCode);
        holder.subNameText.setText(subjectCredits.getSubjectName());
        creditsList.add(subjectCredits.getSubjectCredits());
        holder.gradeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i==0){
                    GradeActivity.gradeSelectedList[position] = "NA";
                }
                else {
                    holder.gradeSpinner.setPrompt(GradeActivity.gradeSelectedList[position]);
                    GradeActivity.gradeSelectedList[position] = adapterView.getItemAtPosition(i).toString();
                }
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

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView subNameText;
        TextView subCodeText;
        Spinner gradeSpinner;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            subNameText = itemView.findViewById(R.id.subject);
            subCodeText = itemView.findViewById(R.id.codeSub);
            gradeSpinner = itemView.findViewById(R.id.grade);
            gradeSpinner.setAdapter(MyAdapter.gradeAdapter);
        }
    }
}