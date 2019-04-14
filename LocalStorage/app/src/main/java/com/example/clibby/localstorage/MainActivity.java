package com.example.clibby.localstorage;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.GestureDetector;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {
    private ReminderRoomDatabase db;
    private Executor executor = Executors.newSingleThreadExecutor();
    final static String  INTENT_TITLE = "Title";
    final static String  INTENT_DESCRIPTION = "Description";/*
    Intent intent = new Intent(MainActivity.this, AddEntry.class);
    startActivityForResult(intent,INTENT_CODE);*/

    public ReminderRoomDatabase getDb() {
        return db;
    }

    //instance variables
    private ArrayList<Reminder> mReminders;
    private ReminderAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private EditText mNewReminderText;
    private GestureDetector mGestureDetector;

    Button button;

    public static final String NEW_PORTAL = "Portal";
    public static final int INTENT_CODE = 1234;
    public static final String URL_STRING = "URL";
    public static final String TITLE_STRING = "Title";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//Initialize the instance variables
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mReminders = new ArrayList<>();
        db = ReminderRoomDatabase.getDatabase(this);
        mRecyclerView.setAdapter(mAdapter);
        FloatingActionButton fab = findViewById(R.id.fab);
//set onClick for floating action button to NewPortal.class
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "FAButton", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                Intent intent = new Intent(MainActivity.this, AddEntry.class);
                startActivityForResult(intent,INTENT_CODE);
            }
        });
//SimpleCallback for deleting object
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback =
                new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
                    @Override
                    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                        return false;
                    }
                    //Called when a user swipes left or right on a ViewHolder
                    @Override
                    public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                        //Get the index corresponding to the selected position
                        int position = (viewHolder.getAdapterPosition());
                        deleteReminder(mReminders.get(position));
                        mReminders.remove(position);
                        mAdapter.notifyItemRemoved(position);
                    }

                };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(mRecyclerView);
        getAllReminders();
    }

    //update UI to current recyclerview
    private void updateUI() {
        if (mAdapter == null) {
            mAdapter = new ReminderAdapter(mReminders);
            mRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.swapList(mReminders);
        }
    }
    private void getAllReminders() {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                mReminders = (ArrayList<Reminder>) db.reminderDao().getAllReminders();
                // In a background thread the user interface cannot be updated from this thread.
                // This method will perform statements on the main thread again.
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        updateUI();
                    }
                });
            }
        });
    }
    private void insertReminder(final Reminder reminder) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                db.reminderDao().insertReminder(reminder);
                getAllReminders(); // Because the Room database has been modified we need to get the new list of reminders.
            }
        });
    }
    private void updateReminder(final Reminder reminder) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                db.reminderDao().updateReminder(reminder);
                getAllReminders(); // Because the Room database has been modified we need to get the new list of reminders.
            }
        });
    }
    private void deleteReminder(final Reminder reminder) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                db.reminderDao().deleteReminder(reminder);
                getAllReminders(); // Because the Room database has been modified we need to get the new list of reminders.
            }
        });
    }
    public void AdapterDeleteReminder(Reminder reminder ){
        deleteReminder(reminder);
    }

    //When done adding a new portal, get Intent and add it to the recyclerView
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode== INTENT_CODE){
            Intent intent = getIntent();
            Bundle extras = data.getExtras();
            String title = extras.getString(INTENT_TITLE);
            String descrioption = extras.getString(INTENT_DESCRIPTION);
            String mainText = title+"-"+descrioption+"-"+"False";
            Reminder newReminder = new Reminder(mainText);
            insertReminder(newReminder);
            updateUI();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Toast.makeText(this,"pressed",Toast.LENGTH_LONG).show();

        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    public void onDestroy() {
        mReminders = mAdapter.getmReminders();
        for(int i = 0; i < mReminders.size();i++){
            updateReminder(mReminders.get(i));
        }
        super.onDestroy();
    }
}
