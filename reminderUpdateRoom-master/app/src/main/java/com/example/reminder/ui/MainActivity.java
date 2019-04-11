package com.example.reminder.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.example.reminder.R;
import com.example.reminder.database.ReminderRoomDatabase;
import com.example.reminder.model.Reminder;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity implements RecyclerView.OnItemTouchListener {

	//instance variables
	private List<Reminder> mReminders;
	private ReminderAdapter mAdapter;
	private RecyclerView mRecyclerView;
	private EditText mNewReminderText;

	boolean forDelete = false;
	private GestureDetector mGestureDetector;

	//Constants used when calling the update activity
	public static final String EXTRA_REMINDER = "Reminder";
	public static final int REQUESTCODE = 1234;
	private int mModifyPosition;

	private ReminderRoomDatabase db;
	private Executor executor = Executors.newSingleThreadExecutor();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		db = ReminderRoomDatabase.getDatabase(this);
		Toolbar toolbar = findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		//Initialize the instance variables
		mRecyclerView = findViewById(R.id.recyclerView);
		mReminders = new ArrayList<>();

		mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

		mGestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
			@Override
			public boolean onSingleTapUp(MotionEvent e) {
				return true;
			}
		});

		FloatingActionButton fab = findViewById(R.id.fab);
		fab.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(MainActivity.this, UpdateActivity.class);
				startActivityForResult(intent, REQUESTCODE);
			}
		});

		/*
		 * Add a touch helper to the RecyclerView to recognize when a user swipes to delete a list entry.
		 * An ItemTouchHelper enables touch behavior (like swipe and move) on each ViewHolder,
		 * and uses callbacks to signal when a user is performing these actions.
		 */
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
						Log.d("deleting","can delete");
					}
				};

		ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
		itemTouchHelper.attachToRecyclerView(mRecyclerView);
		mRecyclerView.addOnItemTouchListener(this);
		getAllReminders();
	}

	private void updateUI() {
		if (mAdapter == null) {
			mAdapter = new ReminderAdapter(mReminders,this);
			mRecyclerView.setAdapter(mAdapter);
		} else {
			mAdapter.swapList(mReminders);
		}
	}

	private void getAllReminders() {
		executor.execute(new Runnable() {
			@Override
			public void run() {
				mReminders = db.reminderDao().getAllReminders();

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

	public void AdapterDelete(int position){
	    Reminder reminder = mReminders.get(position);
	    deleteReminder(reminder);
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		//noinspection SimplifiableIfStatement
		if (id == R.id.action_settings) {
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onInterceptTouchEvent(@NonNull RecyclerView recyclerView, @NonNull MotionEvent motionEvent) {
		View child = recyclerView.findChildViewUnder(motionEvent.getX(), motionEvent.getY());
		if (child != null) {
			int mAdapterPosition = recyclerView.getChildAdapterPosition(child);
			if (mGestureDetector.onTouchEvent(motionEvent)) {
                Reminder re = mReminders.get(mAdapterPosition);
                String[] rText = re.getReminderText().split("-");
                String end;
                if(rText[2].contains("F")){end = "T"; Log.d("Update", end);}
                else {end ="F"; Log.d("Update", end);}
                String newReText = rText[0] + "-" + rText[1] + "-" + end;
                re.setReminderText(newReText);
                updateReminder(re);
			}
		}
		return false;
	}

	@Override
	public void onTouchEvent(@NonNull RecyclerView recyclerView, @NonNull MotionEvent motionEvent) {

	}

	@Override
	public void onRequestDisallowInterceptTouchEvent(boolean b) {

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUESTCODE) {
			if (resultCode == RESULT_OK) {
				String extraString = data.getStringExtra(EXTRA_REMINDER);
                Log.d("Main", extraString);
				Reminder newReminder = new Reminder(extraString);
				// New timestamp: timestamp of update
				insertReminder(newReminder);
				updateUI();
			}
		}
	}
}
