package com.example.clibby.localstorage;

import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.nfc.Tag;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
        final Reminder currentItem = mReminders.get(i);
        final String mainText= currentItem.getReminderText();
        String[] m = mainText.split("-");
        viewHolder.mTitle.setText(m[0]);
        viewHolder.mDiscription.setText(m[1]);
        if(m[2].contains("True")){viewHolder.mCheacked.setChecked(true);}
        else{viewHolder.mCheacked.setChecked(false);}
        if(viewHolder.mCheacked.isChecked() == true){
            viewHolder.mTitle.setPaintFlags(viewHolder.mTitle.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);
            viewHolder.mDiscription.setPaintFlags(viewHolder.mDiscription.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);
            currentItem.setReminderText(viewHolder.mTitle.toString()+"-"+ viewHolder.mDiscription.toString() +"-"+"True");

        }
        if(viewHolder.mCheacked.isChecked() == false){
            viewHolder.mTitle.setPaintFlags(0);
            viewHolder.mDiscription.setPaintFlags(0);
            currentItem.setReminderText(viewHolder.mTitle.toString()+"-"+ viewHolder.mDiscription.toString() +"-"+"False");

        }
        viewHolder.mCheacked.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked == true){
                    viewHolder.mTitle.setPaintFlags(viewHolder.mTitle.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);
                    viewHolder.mDiscription.setPaintFlags(viewHolder.mDiscription.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);
                    currentItem.setReminderText(viewHolder.mTitle.toString()+"-"+ viewHolder.mDiscription.toString() +"-"+"True");

                }
                if(isChecked == false){
                    viewHolder.mTitle.setPaintFlags(0);
                    viewHolder.mDiscription.setPaintFlags(0);
                    currentItem.setReminderText(viewHolder.mTitle.toString()+"-"+ viewHolder.mDiscription.toString() +"-"+"False");

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

    public ArrayList<Reminder> getmReminders() {
        return mReminders;
    }

    @Override
    public int getItemCount() {
        return mReminders.size();
    }

    public void swapList (List<Reminder> newList) {
        mReminders = (ArrayList<Reminder>) newList;
        if (newList != null) {
            // Force the RecyclerView to refresh
            this.notifyDataSetChanged();
        }
    }

}