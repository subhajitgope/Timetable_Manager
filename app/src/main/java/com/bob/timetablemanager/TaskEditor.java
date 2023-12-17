package com.bob.timetablemanager;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.TimePickerDialog;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;

public class TaskEditor extends AppCompatActivity {

    private Task t;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_editor);

        EditText task = findViewById(R.id.task);
        TextView from = findViewById(R.id.from);
        TextView to = findViewById(R.id.to);
        Spinner color = findViewById(R.id.color);
        Button submit = findViewById(R.id.submit);
        TextView delete = findViewById(R.id.delete);

        Database database = new Database(this);
        t = new Task();
        String date = getIntent().getStringExtra("Date");
        t.setID(database.getNextID(date));

        String[] colors = {"Rose", "Blue", "Green", "Red", "Yellow", "Orange", "Purple", "Grey"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, colors);
        color.setAdapter(adapter);

        if (getIntent().hasExtra("Task")) {
            t.setID(getIntent().getIntExtra("ID", 0));
            t.setTask(getIntent().getStringExtra("Task"));
            t.setFrom(getIntent().getStringExtra("From"));
            t.setTo(getIntent().getStringExtra("To"));
            t.setColor(getIntent().getStringExtra("Color"));
            color.setSelection(adapter.getPosition(t.getColor()));
            GradientDrawable background = (GradientDrawable) color.getBackground();
            background.setColor(t.getColorID(TaskEditor.this));
            task.setText(t.getTask());
            from.setText(t.getFromToString());
            to.setText(t.getToToString());

            submit.setOnClickListener(v-> {
                if (task.getText().toString().equals("")) {
                    task.setError("Task cannot be empty");
                    return;
                }
                if (from.getText().equals("Click Here")) {
                    Toast.makeText(this, "Select Time: From", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (to.getText().equals("Click Here")) {
                    Toast.makeText(this, "Select Time: To", Toast.LENGTH_SHORT).show();
                    return;
                }
                t.setTask(task.getText().toString());
                database.updateTask(t, date);
                Toast.makeText(this, "Task updated successfully", Toast.LENGTH_SHORT).show();
                finish();
            });

            delete.setOnClickListener(v-> {
                database.deleteTask(t.getID(), date);
                Toast.makeText(this, "Task deleted successfully", Toast.LENGTH_SHORT).show();
                finish();
            });

        } else {
            submit.setOnClickListener(v-> {
                if (task.getText().toString().equals("")) {
                    task.setError("Task cannot be empty");
                    return;
                }
                if (from.getText().equals("Click Here")) {
                    Toast.makeText(this, "Select Time: From", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (to.getText().equals("Click Here")) {
                    Toast.makeText(this, "Select Time: To", Toast.LENGTH_SHORT).show();
                    return;
                }
                t.setTask(task.getText().toString());
                database.addTask(t, date);
                Toast.makeText(this, "Task added successfully", Toast.LENGTH_SHORT).show();
                finish();
            });

            delete.setVisibility(View.GONE);

        }

        from.setOnClickListener(v-> {
            @SuppressLint("SetTextI18n")
            TimePickerDialog timePickerDialog = new TimePickerDialog(this, (timePicker, hh, mm) -> {
                String ho = new DecimalFormat("00").format(hh);
                String min = new DecimalFormat("00").format(mm);
                from.setText(ho+":"+min);
                t.setFrom(ho+":"+min);
            }, t.getFrom().getHour(), t.getFrom().getMinute(), true);
            timePickerDialog.show();
        });

        to.setOnClickListener(v-> {
            @SuppressLint("SetTextI18n")
            TimePickerDialog timePickerDialog = new TimePickerDialog(this, (timePicker, hh, mm) -> {
                String ho = new DecimalFormat("00").format(hh);
                String min = new DecimalFormat("00").format(mm);
                to.setText(ho+":"+min);
                t.setTo(ho+":"+min);
            }, t.getTo().getHour(), t.getTo().getMinute(), true);
            timePickerDialog.show();
        });

        color.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                t.setColor(color.getSelectedItem().toString());
                GradientDrawable background = (GradientDrawable) color.getBackground();
                background.setColor(t.getColorID(TaskEditor.this));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

    }
}