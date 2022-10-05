package com.akapps.todoapp.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.akapps.todoapp.R;
import com.akapps.todoapp.database.AppDataBaseRepo;
import com.akapps.todoapp.database.Notes;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.ContactHolder> {
    private static final String TAG = "ImportantNotesAdapter";
    private ArrayList<Notes> dairyArrayList;
    private Context context;
    private Activity activity = (Activity) context;
    private String enteredTitle;
    private String enteredDescription;
    private String currentDate;
    private String currentTime;


    public NotesAdapter(Context context) {
        this.context = context;
    }

    public void update(List<Notes> notesRecord) {
        this.dairyArrayList = (ArrayList<Notes>) notesRecord;
        notifyDataSetChanged();
    }


    @Override
    public ContactHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.user_notes_item_list, parent, false);
        return new ContactHolder(view);
    }

    @Override
    public int getItemCount() {
        return dairyArrayList == null ? 0 : dairyArrayList.size();
    }

    @Override
    public void onBindViewHolder(@NonNull ContactHolder holder, int position) {
        Notes notes = dairyArrayList.get(position);
        holder.txtName.setText(notes.getTitle());
        holder.txtNumber.setText(notes.getNotesDescription());
        holder.date_dairy.setText(String.format("\tDate : %s", notes.getCurrentDate()));
        holder.time_dairy.setText(String.format("\tTime : %s", notes.getCurrentTime()));
        holder.delBtn.setOnClickListener(view -> {
            AppDataBaseRepo.getInstance().deleteItem(notes.getId());
        });

        holder.updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDailogue((Activity) context, notes);
            }
        });

    }

    private void openDailogue(Activity activity, Notes notes) {
        LayoutInflater inflater = activity.getLayoutInflater();
        View view = inflater.inflate(R.layout.dailog_dairy, null);
        final AppCompatButton cancelBtn = view.findViewById(R.id.cancle_dailog);
        final AppCompatButton saveBtn = view.findViewById(R.id.save_dailog);
        final TextView borderTitle = view.findViewById(R.id.updateTitle);
        final AppCompatEditText title_text = view.findViewById(R.id.enter_title);
        final AppCompatEditText description_text = view.findViewById(R.id.enter_description);
        final AlertDialog.Builder ad = new AlertDialog.Builder(context);
        final AlertDialog dialog = ad.create();
        dialog.setCancelable(false);
        dialog.setView(view);
        borderTitle.setText(R.string.update_notes);
        title_text.setText(notes.getTitle());
        description_text.setText(notes.getNotesDescription());
        cancelBtn.setOnClickListener(v -> dialog.dismiss());

        saveBtn.setOnClickListener(v -> {
            enteredTitle = Objects.requireNonNull(title_text.getText()).toString();
            enteredDescription = Objects.requireNonNull(description_text.getText()).toString();
            currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
            currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
            if (enteredTitle.equals("") || enteredDescription.equals("")) {
                Toast.makeText(context, R.string.enter_all_fields, Toast.LENGTH_SHORT).show();
            } else {
                AppDataBaseRepo.getInstance().insertCheckRecord(enteredTitle, enteredDescription, currentDate, currentTime, notes.getId());
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public static class ContactHolder extends RecyclerView.ViewHolder {
        private TextView txtName;
        private TextView txtNumber;
        private TextView date_dairy;
        private TextView time_dairy;
        private CardView cardViewNote;
        private ImageView delBtn;
        private ImageView updateBtn;

        public ContactHolder(View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.title_dairy);
            txtNumber = itemView.findViewById(R.id.descrip_dairy);
            date_dairy = itemView.findViewById(R.id.date_dairy);
            time_dairy = itemView.findViewById(R.id.time_dairy);
            updateBtn = itemView.findViewById(R.id.update_notes);
            cardViewNote = itemView.findViewById(R.id.notesCardView);
            delBtn = itemView.findViewById(R.id.delete_notes);

        }
    }
}



