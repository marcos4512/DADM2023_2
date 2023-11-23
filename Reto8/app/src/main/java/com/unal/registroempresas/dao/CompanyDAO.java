package com.unal.registroempresas.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.content.ContentValues;

import androidx.annotation.Nullable;

import com.unal.registroempresas.model.Company;

import java.util.ArrayList;

public class CompanyDAO extends DatabaseProvider{

    Context context;

    public CompanyDAO(@Nullable Context context) {
        super(context);
        this.context = context;
    }

    public long insertCompany(String name, String url, String phone, String email, String prodsServcs, String classification) {

        long id = 0;

        try {
            DatabaseProvider dbHelper = new DatabaseProvider(context);
            SQLiteDatabase database = dbHelper.getWritableDatabase();
            ContentValues values = new ContentValues();

            values.put("name", name);
            values.put("url", url);
            values.put("phone", phone);
            values.put("email", email);
            values.put("products_services", prodsServcs);
            values.put("classification", classification);

            id = database.insert(TABLE_COMPANY, null, values);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return id;
    }

    public ArrayList<Company> showCompanies() {

        DatabaseProvider databaseProvider = new DatabaseProvider(context);
        SQLiteDatabase db = databaseProvider.getWritableDatabase();

        ArrayList<Company> listaContactos = new ArrayList<>();
        Company company;
        Cursor companyCursor;

        companyCursor = db.rawQuery("SELECT * FROM " + TABLE_COMPANY + " ORDER BY name ASC", null);

        if (companyCursor.moveToFirst()) {
            do {
                company = new Company();
                company.setId(companyCursor.getInt(0));
                company.setName(companyCursor.getString(1));
                company.setUrl(companyCursor.getString(2));
                company.setPhone(companyCursor.getString(3));
                company.setEmail(companyCursor.getString(4));
                company.setProductService(companyCursor.getString(5));
                company.setClassification(companyCursor.getString(6));

                listaContactos.add(company);
            } while (companyCursor.moveToNext());
        }

        companyCursor.close();

        return listaContactos;
    }

    public Company showCompany(int id) {

        DatabaseProvider databaseProvider = new DatabaseProvider(context);
        SQLiteDatabase database = databaseProvider.getWritableDatabase();

        Company company = null;
        Cursor companyCursor;

        companyCursor = database.rawQuery("SELECT * FROM " + TABLE_COMPANY + " WHERE id = " + id + " LIMIT 1", null);

        if (companyCursor.moveToFirst()) {
            company = new Company();
            company.setId(companyCursor.getInt(0));
            company.setName(companyCursor.getString(1));
            company.setUrl(companyCursor.getString(2));
            company.setPhone(companyCursor.getString(3));
            company.setEmail(companyCursor.getString(4));
            company.setProductService(companyCursor.getString(5));
            company.setClassification(companyCursor.getString(6));
        }

        companyCursor.close();

        return company;
    }

    public boolean updateCompany(int id, String name, String url, String phone, String email, String prodsServcs, String classification) {

        boolean updated = false;

        DatabaseProvider databaseProvider = new DatabaseProvider(context);
        SQLiteDatabase database = databaseProvider.getWritableDatabase();

        try {
            database.execSQL("UPDATE " + TABLE_COMPANY + " SET name = '" + name + "'," +
                    " url = '" + url + "'," +
                    " phone = '" + phone + "'," +
                    " email = '" + email + "'," +
                    " products_services = '" + prodsServcs + "'," +
                    " classification = '" + classification + "'"  +
                    " WHERE id='" + id + "' ");
            updated = true;
        } catch (Exception e) {
            e.printStackTrace();
            e.toString();
            updated = false;
        } finally {
            database.close();
        }

        return updated;
    }

    public boolean deleteCompany(int id) {

        boolean deleted = false;

        DatabaseProvider databaseProvider = new DatabaseProvider(context);
        SQLiteDatabase database = databaseProvider.getWritableDatabase();

        try {
            database.execSQL("DELETE FROM " + TABLE_COMPANY + " WHERE id = '" + id + "'");
            deleted = true;
        } catch (Exception ex) {
            ex.toString();
            deleted = false;
        } finally {
            database.close();
        }

        return deleted;
    }
}
