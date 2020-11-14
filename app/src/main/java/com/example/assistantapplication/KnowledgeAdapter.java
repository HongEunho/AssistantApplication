package com.example.assistantapplication;

import android.content.Context;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class KnowledgeAdapter extends RecyclerView.Adapter<KnowledgeAdapter.KnowledgeViewHolder> {

    private ArrayList<Knowledge> mList;

    public class KnowledgeViewHolder extends RecyclerView.ViewHolder{
        protected TextView question;
        protected TextView answer;

        public KnowledgeViewHolder(@NonNull View itemView) {
            super(itemView);
            this.question = itemView.findViewById(R.id.question);
            this.answer = itemView.findViewById(R.id.answer);
        }
    }

    public KnowledgeAdapter(ArrayList<Knowledge> list){
        this.mList = list;
    }

    @NonNull
    @Override
    public KnowledgeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.knowledge_item_list,parent,false);
        KnowledgeViewHolder viewHolder = new KnowledgeViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull KnowledgeViewHolder holder, int position) {
        holder.question.setTextSize(TypedValue.COMPLEX_UNIT_SP,25);
        holder.answer.setTextSize(TypedValue.COMPLEX_UNIT_SP,25);

        holder.question.setGravity(Gravity.CENTER);
        holder.answer.setGravity(Gravity.CENTER);

        holder.question.setText(mList.get(position).getQuestion());
        holder.answer.setText(mList.get(position).getAnswer());
    }


    @Override
    public int getItemCount() {
        return (null != mList ? mList.size() : 0);
    }
}
