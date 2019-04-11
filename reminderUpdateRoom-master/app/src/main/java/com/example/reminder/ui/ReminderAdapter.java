package com.example.reminder.ui;

import android.content.Context;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.reminder.R;
import com.example.reminder.model.Reminder;

import java.util.List;

public class ReminderAdapter extends RecyclerView.Adapter<ReminderAdapter.ReminderViewHolder>  {
    private List<Reminder> mReminders;
    private MainActivity main;

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

    public ReminderAdapter(List<Reminder> mReminders,MainActivity origin) {
        this.mReminders = mReminders;
        main = origin;
    }

    @NonNull
    @Override
    public ReminderAdapter.ReminderViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(android.R.layout.simple_list_item_1, null);
        // Return a new holder instance
        ReminderAdapter.ViewHolder viewHolder = new ReminderAdapter.ViewHolder(view);

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
        Log.d("Bind", m[2]);
        if(m[2].contains("T")){viewHolder.mCheacked.setChecked(true);}
        else{viewHolder.mCheacked.setChecked(false);}
        if(viewHolder.mCheacked.isChecked() == true){
            viewHolder.mTitle.setPaintFlags(viewHolder.mTitle.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);
            viewHolder.mDiscription.setPaintFlags(viewHolder.mDiscription.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);
        }
        if(viewHolder.mCheacked.isChecked() == false){
            viewHolder.mTitle.setPaintFlags(0);
            viewHolder.mDiscription.setPaintFlags(0);
        }
        viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                int i = viewHolder.getAdapterPosition();
                main.AdapterDelete(i);
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return mReminders.size();
    }

    public void swapList (List<Reminder> newList) {
        mReminders = newList;
        if (newList != null) {
            // Force the RecyclerView to refresh
            this.notifyDataSetChanged();
        }
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;

        public ViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(android.R.id.text1);
        }
    }

}
