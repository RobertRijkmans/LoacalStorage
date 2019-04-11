package com.example.clibby.localstorage;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ReminderAdapter extends RecyclerView.Adapter<ReminderAdapter.ReminderViewHolder> {
    private ArrayList<Reminder> mReminders;
    
    public static class ReminderViewHolder extends RecyclerView.ViewHolder{
        public CheckBox mCheacked;
        public TextView mTitle;
        public TextView mDiscription;

        public ReminderViewHolder(@NonNull View itemView) {
            super(itemView);
            mCheacked = itemView.findViewById(R.id.checkBox);
            mTitle = itemView.findViewById(R.id.TitleText);
            mDiscription = itemView.findViewById(R.id.DescriptionText);
        }
    }
    public ReminderAdapter(ArrayList<Reminder> reminderList){
        mReminders = reminderList;
    }
    @NonNull
    @Override
    public ReminderAdapter.ReminderViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.new_card_view, viewGroup,false);
        ReminderViewHolder reView = new ReminderViewHolder(v);
        return reView;
    }

    @Override
    public void onBindViewHolder(@NonNull final ReminderAdapter.ReminderViewHolder viewHolder, int i) {
        Reminder currentItem = mReminders.get(i);
        viewHolder.mTitle.setText(currentItem.getmTitle());
        viewHolder.mDiscription.setText(currentItem.getmDescription());
        viewHolder.mCheacked.setChecked(currentItem.ismChecked());
        viewHolder.mCheacked.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked == true){
                    viewHolder.mTitle.setPaintFlags(viewHolder.mTitle.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);
                    viewHolder.mDiscription.setPaintFlags(viewHolder.mDiscription.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);
                }
                if(isChecked == false){
                    viewHolder.mTitle.setPaintFlags(0);
                    viewHolder.mDiscription.setPaintFlags(0);
                }
            }
        });
        viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                int i = viewHolder.getAdapterPosition();
                mReminders.remove(i);
                notifyItemRemoved(i);
                    return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return mReminders.size();
    }
}