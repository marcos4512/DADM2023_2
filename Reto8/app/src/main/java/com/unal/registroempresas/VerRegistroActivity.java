package com.unal.registroempresas;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
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

public class VerRegistroActivity extends AppCompatActivity {

    EditText nameText, urlText, phoneText, emailText, prodText, clasftnText;
    Button updateCompanyButtn;
    FloatingActionButton editFabBtton, deleteFabBtton;

    Company company;
    int companyId = 0;

    static final int DIALOG_QUIT_ID = 0;
    static final int DIALOG_DELETE_ID = 1;

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
        CompanyDAO companyDAO = new CompanyDAO(VerRegistroActivity.this);
        company = companyDAO.showCompany(companyId);

        if (company != null){
            nameText.setText(company.getName());
            urlText.setText(company.getUrl());
            phoneText.setText(company.getPhone());
            emailText.setText(company.getEmail());
            prodText.setText(company.getProductService());
            clasftnText.setText(company.getClassification());

            updateCompanyButtn.setVisibility(View.INVISIBLE);

            nameText.setInputType(InputType.TYPE_NULL);
            urlText.setInputType(InputType.TYPE_NULL);
            phoneText.setInputType(InputType.TYPE_NULL);
            emailText.setInputType(InputType.TYPE_NULL);
            prodText.setInputType(InputType.TYPE_NULL);
            clasftnText.setInputType(InputType.TYPE_NULL);
        }

        editFabBtton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(VerRegistroActivity.this, ActualizarRegistroActivity.class);
                intent.putExtra("ID", companyId);
                startActivity(intent);
            }
        });

        deleteFabBtton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(DIALOG_DELETE_ID);
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
                                VerRegistroActivity.this.finish();
                            }
                        })
                        .setNegativeButton(R.string.no, null);

            case DIALOG_DELETE_ID:
                builder.setMessage(R.string.delete_question)
                        .setCancelable(false)
                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                CompanyDAO companyDAO = new CompanyDAO(VerRegistroActivity.this);
                                if (companyDAO.deleteCompany(companyId)){
                                    Toast.makeText(VerRegistroActivity.this, "Registro de empresa eliminado", Toast.LENGTH_LONG).show();
                                    goCompanyListScreen();
                                }
                            }
                        })
                        .setNegativeButton(R.string.no, null);
        }
        dialog = builder.create();
        return dialog;
    }
}
