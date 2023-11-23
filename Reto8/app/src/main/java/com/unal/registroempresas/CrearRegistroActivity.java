package com.unal.registroempresas;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.unal.registroempresas.dao.CompanyDAO;
import com.unal.registroempresas.dao.DatabaseProvider;

public class CrearRegistroActivity extends AppCompatActivity {

    EditText nameText, urlText, phoneText, emailText, prodText, clasftnText;
    Button addCompanyButtn;
    static final int DIALOG_QUIT_ID = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert);
        setSupportActionBar(findViewById(R.id.toolbar));

        nameText = findViewById(R.id.nameText);
        urlText = findViewById(R.id.urlText);
        phoneText = findViewById(R.id.phoneText);
        emailText = findViewById(R.id.emailText);
        prodText = findViewById(R.id.prodText);
        clasftnText = findViewById(R.id.clasificationText);
        addCompanyButtn = findViewById(R.id.companyButton);

        addCompanyButtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!nameText.getText().toString().equals("") && !urlText.getText().toString().equals("")
                        && !phoneText.getText().toString().equals("") && !emailText.getText().toString().equals("")
                        && !prodText.getText().toString().equals("") && !clasftnText.getText().toString().equals("")) {
                    CompanyDAO dao = new CompanyDAO(CrearRegistroActivity.this);
                    long id = dao.insertCompany(
                            nameText.getText().toString(),
                            urlText.getText().toString(),
                            phoneText.getText().toString(),
                            emailText.getText().toString(),
                            prodText.getText().toString(),
                            clasftnText.getText().toString());
                    if (id > 0) {
                        Toast.makeText(CrearRegistroActivity.this, "Resgistro de empresa almacenado", Toast.LENGTH_LONG).show();
                        cleanFormFields();
                    } else {
                        Toast.makeText(CrearRegistroActivity.this, "Error al almacenar registro de empresa", Toast.LENGTH_LONG).show();
                    }
                }
                else {
                    Toast.makeText(CrearRegistroActivity.this, "Por favor llene todos los campos", Toast.LENGTH_LONG).show();
                }

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.bar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        /*if (itemId == R.id.newCompany){
        goCompanyListScreen();
        return true;
        }*/
        if (itemId == R.id.companyList){
            goCompanyListScreen();
            return true;
        }
        if (itemId == R.id.quit){
            showDialog(DIALOG_QUIT_ID);
            return true;
        }
        return false;
    }
    private void cleanFormFields() {
        nameText.setText("");
        urlText.setText("");
        phoneText.setText("");
        emailText.setText("");
        prodText.setText("");
        clasftnText.setText("");
    }

    private void goCompanyListScreen() {
        Intent intent = new Intent(this, ListarRegistrosActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        Dialog dialog = null;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        switch(id) {
            case DIALOG_QUIT_ID:
                builder.setMessage(R.string.quit_question)
                    .setCancelable(false)
                    .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            CrearRegistroActivity.this.finish();
                        }
                    })
                    .setNegativeButton(R.string.no, null);
        }


        dialog = builder.create();
        return dialog;
    }
}
