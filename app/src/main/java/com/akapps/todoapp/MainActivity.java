package com.akapps.todoapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.akapps.todoapp.adapter.NotesAdapter;
import com.akapps.todoapp.database.AppDataBaseRepo;
import com.akapps.todoapp.database.Notes;
import com.akapps.todoapp.databinding.ActivityMainBinding;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private String enteredTitle;
    private String enteredDescription;
    private String currentDate;
    private String currentTime;
    private ArrayList<Notes> notesRecordList;
    private NotesAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            getSupportActionBar().setDisplayShowTitleEnabled(true);
            getSupportActionBar().setTitle(R.string.notes);
        }
        setRecyclerView();
        openNotesEntryDailogue();
        AppDataBaseRepo.getInstance().getAllRecords().observe(MainActivity.this, observeVehicleDb());
    }


    private void setRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        binding.recyclerView.setLayoutManager(layoutManager);
        adapter = new NotesAdapter(MainActivity.this);
        binding.recyclerView.setAdapter(adapter);
    }

    private void openNotesEntryDailogue() {
        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDailogue();
            }

            private void openDailogue() {
                LayoutInflater inflater = getLayoutInflater();
                View view = inflater.inflate(R.layout.dailog_dairy, null);
                final AppCompatButton cancelBtn = view.findViewById(R.id.cancle_dailog);
                final AppCompatButton saveBtn = view.findViewById(R.id.save_dailog);
                final AppCompatEditText title_text = view.findViewById(R.id.enter_title);
                final AppCompatEditText description_text = view.findViewById(R.id.enter_description);
                final AlertDialog.Builder ad = new AlertDialog.Builder(MainActivity.this);
                final AlertDialog dialog = ad.create();
                dialog.setCancelable(false);
                dialog.setView(view);

                cancelBtn.setOnClickListener(v -> dialog.dismiss());

                saveBtn.setOnClickListener(v -> {
                    enteredTitle = Objects.requireNonNull(title_text.getText()).toString();
                    enteredDescription = Objects.requireNonNull(description_text.getText()).toString();
                    currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
                    currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
                    if (enteredTitle.equals("") || enteredDescription.equals("")) {
                        Toast.makeText(MainActivity.this, getString(R.string.enter_all_fields), Toast.LENGTH_SHORT).show();
                    } else {
                        addInDataBase();
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }

            private void addInDataBase() {
                Notes notes = new Notes(enteredTitle, enteredDescription, currentDate, currentTime);
                AppDataBaseRepo.getInstance().insertRecord(notes);
            }
        });
    }

    private Observer<? super List<Notes>> observeVehicleDb() {
        Observer<List<Notes>> observer = notesRecords -> {

            if (notesRecords.size() == 0) {
                binding.nodata.setVisibility(View.VISIBLE);
                binding.recyclerView.setVisibility(View.GONE);
            }

            if (notesRecords.size() != 0) {
                binding.nodata.setVisibility(View.GONE);
                binding.recyclerView.setVisibility(View.VISIBLE);
                adapter.update(notesRecords);
            }
        };

        return observer;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}