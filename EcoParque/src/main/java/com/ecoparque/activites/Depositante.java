package com.ecoparque.activites;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Intent;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import com.ecoparque.R;
import com.ecoparque.fragments.DesconectarFragment;

import org.androidannotations.annotations.AfterTextChange;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.CheckedChange;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.depositante)
@OptionsMenu(R.menu.depositante)
public class Depositante extends Activity {

    @ViewById(R.id.dep_ident)
    EditText ident;
    @ViewById(R.id.text_peso)
    EditText peso;
    @ViewById(R.id.depositar)
    Button btnDep;
    @ViewById(R.id.dep_chk_matInf)
    CheckBox material;
    @ViewById(R.id.dep_chk_aceites)
    CheckBox aceites;
    @ViewById(R.id.dep_chk_nev)
    CheckBox neveras;
    private Intent prevIntent;

    @AfterViews
    void initList() {
        prevIntent = getIntent();
        ident.setText(prevIntent.getStringExtra(SeleccionUsuario.EXTRA_MESSAGE));
    }

    @Click(R.id.depositar)
    void depositarContenido() {
        Intent intent = new Intent(Depositante.this, Resultados_.class);

        intent.putExtra(SeleccionUsuario.EXTRA_MESSAGE, ident.getText().toString());
        intent.putExtra("matInf", material.isChecked());
        intent.putExtra("neveras", neveras.isChecked());
        intent.putExtra("aceites", aceites.isChecked());
        intent.putExtra("peso", peso.getText().toString());
        intent.putExtra("from", prevIntent.getStringExtra("from"));

        startActivity(intent);
    }

    @AfterTextChange(R.id.text_peso)
    void afterTextChangedOnPassword() {
        btnDep.setEnabled(!peso.getText().toString().isEmpty());
    }

    @CheckedChange({R.id.dep_chk_matInf, R.id.dep_chk_aceites, R.id.dep_chk_nev})
    void checkedChangeOnMaterialCheckBox(CompoundButton botMatInf, boolean isChecked) {
        if (isChecked) {
            peso.setEnabled(true);

        } else if (!aceites.isChecked() && !neveras.isChecked() && !material.isChecked()) {
            btnDep.setEnabled(false);
            peso.setText("");
            peso.setEnabled(false);
        }
    }

    @OptionsItem(R.id.action_settings)
    void myMethod() {
        DialogFragment newFragment = new DesconectarFragment();
        newFragment.show(getFragmentManager(), "dialogo");
    }
}
