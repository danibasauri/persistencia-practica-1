package com.ecoparque.activites;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Intent;
import android.widget.EditText;

import com.ecoparque.R;
import com.ecoparque.asyncTasks.ParseWebTask;
import com.ecoparque.fragments.DesconectarFragment;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.datos_dominio)
@OptionsMenu(R.menu.datos_dominio)
public class DatosDominio extends Activity {

    @ViewById(R.id.url_empresa)
    EditText urlEmpresa;

    @AfterViews
    void initActivity() {
        Intent prevIntent = getIntent();
        urlEmpresa.setText(prevIntent.getStringExtra("urlEmpresa"));
        new ParseWebTask(DatosDominio.this).execute(prevIntent.getStringExtra("urlEmpresa"));
    }

    @OptionsItem(R.id.action_settings)
    void myMethod() {
        DialogFragment newFragment = new DesconectarFragment();
        newFragment.show(getFragmentManager(), "dialogo");
    }
}