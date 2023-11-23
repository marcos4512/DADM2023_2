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

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.unal.registroempresas.dao.CompanyDAO;
import com.unal.registroempresas.model.Company;

public class ActualizarRegistroActivity extends AppCompatActivity {

    EditText nameText, urlText, phoneText, emailText, prodText, clasftnText;
    Button updateCompanyButtn;
    FloatingActionButton editFabBtton, deleteFabBtton;

    Company company;
    int companyId = 0;
    boolean updated = false;

    static final int DIALOG_QUIT_ID = 0;
    static final int DIALOG_UPDATE_ID = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        setSupportActionBar(findViewById(R.id.toolbar));

        nameText = findViewById(R.id.nameText);
        urlText = findViewById(R.id.urlText);
        phoneText = findViewById(R.id.phoneText);
        emailText = findViewById(R.id.emailText);
        prodText = findViewById(R.id.prodText);
        clasftnText = findViewById(R.id.clasificationText);
        updateCompanyButtn = findViewById(R.id.companyButton);
        editFabBtton = findViewById(R.id.editFloatingActionButton);
        deleteFabBtton = findViewById(R.id.deleteFloatingActionButton);

        editFabBtton.setVisibility(View.INVISIBLE);
        deleteFabBtton.setVisibility(View.INVISIBLE);

        if (savedInstanceState == null){
            Bundle extras = getIntent().getExtras();
            if (extras == null){
                companyId = Integer.parseInt(null);
            } else {
                companyId = extras.getInt("ID");
            }
        } else {
            companyId = (int) savedInstanceState.getSerializable("ID");
        }
        CompanyDAO companyDAO = new CompanyDAO(ActualizarRegistroActivity.this);
        company = companyDAO.showCompany(companyId);

        if (company != null){
            nameText.setText(company.getName());
            urlText.setText(company.getUrl());
            phoneText.setText(company.getPhone());
            emailText.setText(company.getEmail());
            prodText.setText(company.getProductService());
            clasftnText.setText(company.getClassification());
        }

        updateCompanyButtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!nameText.getText().toString().equals("") && !urlText.getText().toString().equals("")
                        && !phoneText.getText().toString().equals("") && !emailText.getText().toString().equals("")
                        && !prodText.getText().toString().equals("") && !clasftnText.getText().toString().equals("")) {
                    showDialog(DIALOG_UPDATE_ID);
                }
                else {
                    Toast.makeText(ActualizarRegistroActivity.this, "Por favor llene todos los campos", Toast.LENGTH_LONG).show();
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

    private void goCompanyListScreen() {
        Intent intent = new Intent(this, ListarRegistrosActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void showCompany(){
        Intent intent = new Intent(this, VerRegistroActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("ID", companyId);
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
                                ActualizarRegistroActivity.this.finish();
                            }
                        })
                        .setNegativeButton(R.string.no, null);

            case DIALOG_UPDATE_ID:
                builder.setMessage(R.string.update_question)
                        .setCancelable(false)
                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                CompanyDAO dao = new CompanyDAO(ActualizarRegistroActivity.this);
                                 updated = dao.updateCompany(companyId,
                                        nameText.getText().toString(),
                                        urlText.getText().toString(),
                                        phoneText.getText().toString(),
                                        emailText.getText().toString(),
                                        prodText.getText().toString(),
                                        clasftnText.getText().toString());
                                if (updated) {
                                    Toast.makeText(ActualizarRegistroActivity.this, "Registro de empresa actualizado", Toast.LENGTH_LONG).show();
                                    showCompany();
                                } else {
                                    Toast.makeText(ActualizarRegistroActivity.this, "Error al actualizar registro de empresa", Toast.LENGTH_LONG).show();
                                }
                            }
                        })
                        .setNegativeButton(R.string.no, null);
        }
        dialog = builder.create();
        return dialog;
    }
}
