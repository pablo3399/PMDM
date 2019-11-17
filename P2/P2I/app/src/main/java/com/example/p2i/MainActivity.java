package com.example.p2i;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.items = new ArrayList<String>();

        Button btAdd = (Button) this.findViewById(R.id.btAdd);
        ListView lvItems = (ListView) this.findViewById(R.id.lvItems);

        lvItems.setLongClickable(true);
        this.itemsAdapter = new ArrayAdapter<String>(
                this.getApplicationContext(),
                android.R.layout.simple_selectable_list_item,
                this.items
        );
        lvItems.setAdapter(this.itemsAdapter);


        lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int pos, long l) {

                if (pos >= 0) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setMessage("Estas seguro de que quieres borrarlo?");
                    builder.setPositiveButton("no", null);
                    builder.setNegativeButton("si", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    MainActivity.this.items.remove(pos);
                                    MainActivity.this.itemsAdapter.notifyDataSetChanged();
                                    MainActivity.this.updateStatus();
                                }

                            }
                    );
                    builder.create().show();
                }
                    return true;
            }
        });


        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                   MainActivity.this.edit(position);
            }
        });


        btAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.this.onAdd();

            }
        });


    }

    private void onAdd() {
        final EditText edText = new EditText( this);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("A comprar...");
        builder.setMessage("Nombre");
        builder.setView(edText);
        builder.setPositiveButton("+", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                final String text = edText.getText().toString();

                MainActivity.this.itemsAdapter.add(text);
                MainActivity.this.updateStatus();
            }
        });
        builder.setNegativeButton("Cancel", null);
        builder.create().show();
    }

    private void updateStatus() {
        TextView txtNum = (TextView) this.findViewById(R.id.lblNum);
        txtNum.setText(Integer.toString(this.itemsAdapter.getCount()));
    }

    private ArrayAdapter<String> itemsAdapter;
    private ArrayList<String> items;

    private void edit(final int pos) {
        final EditText edText = new EditText( this);

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Modificar elemento");
        builder.setView(edText);
        edText.setText(MainActivity.this.items.get(pos).toString());
        builder.setPositiveButton("Modificar",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        final String text = edText.getText().toString();
                          MainActivity.this.items.set(pos, text);
                          MainActivity.this.itemsAdapter.notifyDataSetChanged();
                    }
        });
        builder.setNegativeButton("Cancel", null);

        builder.create().show();

    }
}