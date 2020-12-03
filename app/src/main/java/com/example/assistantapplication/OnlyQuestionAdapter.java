package com.example.assistantapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class OnlyQuestionAdapter extends RecyclerView.Adapter<OnlyQuestionAdapter.OnlyQuestionViewHolder> {

    private ArrayList<OnlyQuestion> mList;
    private OnDeleteClickListener mListener = null;

    public OnlyQuestionAdapter(ArrayList<OnlyQuestion> list){
        this.mList = list;
    }

    public interface OnDeleteClickListener{
        void onDeleteClick(View v, int position);
    }

    public void setOnDeleteClickListener(OnDeleteClickListener listener) {
        this.mListener = listener;
    }


    public class OnlyQuestionViewHolder extends RecyclerView.ViewHolder{
        protected TextView department;
        protected TextView content;
        protected Button remove_btn;


        public OnlyQuestionViewHolder(@NonNull View itemView) {
            super(itemView);
            this.department = itemView.findViewById(R.id.department);
            this.content = itemView.findViewById(R.id.content);
            this.remove_btn = itemView.findViewById(R.id.remove_btn);

            remove_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();

                    if(position != RecyclerView.NO_POSITION){
                        if(mListener != null)
                            mListener.onDeleteClick(v, position);
                    }
                }
            });

        }
    }

    @NonNull
    @Override
    public OnlyQuestionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.onlyquestion_item_list,parent,false);
        OnlyQuestionViewHolder viewHolder = new OnlyQuestionViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull OnlyQuestionViewHolder holder, int position) {


        holder.department.setText(mList.get(position).getDepartment());
        holder.content.setText(mList.get(position).getContent());
    }


    @Override
    public int getItemCount() {
        return (null != mList ? mList.size() : 0);
    }

    public OnlyQuestion getItem(int position){
        return mList.get(position);
    }
}
