package com.sevrep.chaseapplication;

import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ItemAdapter.OnItemClickListener {

    private List<ItemModel> itemModelList;
    private RecyclerView recyclerView;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        databaseHelper = new DatabaseHelper(this);
        itemModelList = new ArrayList<>();

        loadItems();

        FloatingActionButton floatingActionButton = findViewById(R.id.floatingActionButton);
        floatingActionButton.setOnClickListener(v -> {
            final EditText edtItemName = new EditText(this);
            AlertDialog dialog = new AlertDialog.Builder(this)
                    .setTitle("Add Item")
                    .setMessage("Enter item name.")
                    .setView(edtItemName)
                    .setPositiveButton("Add", (dialog1, which) -> {
                        String itemName = edtItemName.getText().toString().trim();
                        if (TextUtils.isEmpty(itemName)) {
                            customToast("Enter item name.");
                        } else if (databaseHelper.checkItemNameDuplicate(itemName)) {
                            customToast("Item name exists!");
                        } else {
                            databaseHelper.createItem(itemName);
                            loadItems();
                        }
                    })
                    .setNegativeButton("Cancel", null)
                    .create();
            dialog.show();
        });
    }

    @Override
    public void onEditClick(int position) {
        ItemModel clickedItem = itemModelList.get(position);
        final EditText edtItemName = new EditText(this);
        edtItemName.setText(clickedItem.getName());
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Edit Item")
                .setMessage("Edit item name.")
                .setView(edtItemName)
                .setPositiveButton("Edit", (dialog1, which) -> {
                    String itemName = edtItemName.getText().toString().trim();
                    if (TextUtils.isEmpty(itemName)) {
                        customToast("Enter item name.");
                    } else if (databaseHelper.checkItemNameDuplicate(itemName)) {
                        customToast("Item name exists!");
                    } else {
                        databaseHelper.updateItemData(itemName);
                        loadItems();
                    }
                })
                .setNegativeButton("Cancel", null)
                .create();
        dialog.show();
    }

    @Override
    public void onDelClick(int position) {
        ItemModel clickedItem = itemModelList.get(position);
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("WARNING!!!")
                .setMessage("Are you sure you want to delete " + clickedItem.getName() + "?")
                .setPositiveButton("DELETE", (dialog1, which) -> {
                    databaseHelper.deleteItemData(clickedItem.getId());
                    loadItems();
                })
                .setNegativeButton("Cancel", null)
                .create();
        dialog.show();
    }

    @Override
    public void onBackPressed() {
        new android.app.AlertDialog.Builder(this)
                .setTitle("Exit?")
                .setMessage("Are you sure you want to exit?")
                .setNegativeButton(android.R.string.cancel, null)
                .setPositiveButton(android.R.string.ok, (arg0, arg1) -> {
                    customToast("Application closed!");
                    finish();
                })
                .create()
                .show();
    }

    private void loadItems() {
        itemModelList.clear();
        Cursor cursor = databaseHelper.readItems();
        if (cursor.moveToFirst()) {
            do {
                ItemModel items = new ItemModel(
                        cursor.getInt(cursor.getColumnIndex("itemid")),
                        cursor.getString(cursor.getColumnIndex("name"))
                );
                itemModelList.add(items);
            } while (cursor.moveToNext());
        }

        ItemAdapter itemAdapter = new ItemAdapter(this, itemModelList);
        recyclerView.setAdapter(itemAdapter);
        ItemAdapter.setOnItemClickListener(this);
    }

    private void customToast(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }
}