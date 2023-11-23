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
import android.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.unal.registroempresas.adapters.ListaCompanyAdapter;
import com.unal.registroempresas.dao.CompanyDAO;
import com.unal.registroempresas.model.Company;

import java.util.ArrayList;

public class ListarRegistrosActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    RecyclerView companyList;
    ArrayList<Company> companyArrayList;
    SearchView searchText;
    FloatingActionButton newFabButton;
    ListaCompanyAdapter companyAdapter;

    static final int DIALOG_QUIT_ID = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar);
        setSupportActionBar(findViewById(R.id.toolbar));

        companyList = findViewById(R.id.companyList);
        searchText = findViewById(R.id.searchText);
        newFabButton = findViewById(R.id.newFloatingActionButton);


        companyList.setLayoutManager(new LinearLayoutManager(this));
        CompanyDAO companyDAO = new CompanyDAO(ListarRegistrosActivity.this);
        companyArrayList  = new ArrayList<>();
        companyAdapter = new ListaCompanyAdapter(companyDAO.showCompanies());
        companyList.setAdapter(companyAdapter);

        newFabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goNewRegistryScreen();
            }
        });

        searchText.setOnQueryTextListener(this);

    }

    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.bar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.newCompany){
            goNewRegistryScreen();
            return true;
        }
        if (itemId == R.id.quit){
            showDialog(DIALOG_QUIT_ID);
            return true;
        }
        return false;
    }

    private void goNewRegistryScreen() {
        Intent intent = new Intent(this, CrearRegistroActivity.class);
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
                                ListarRegistrosActivity.this.finish();
                            }
                        })
                        .setNegativeButton(R.string.no, null);
        }


        dialog = builder.create();
        return dialog;
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        companyAdapter.filter(s);
        return false;
    }
}
