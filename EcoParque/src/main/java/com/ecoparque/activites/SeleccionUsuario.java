package com.ecoparque.activites;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Intent;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.ecoparque.R;
import com.ecoparque.fragments.DesconectarFragment;
import com.ecoparque.objects.Validador;

import org.androidannotations.annotations.AfterTextChange;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.seleccion_usuario)
@OptionsMenu(R.menu.seleccion_usuario)
public class SeleccionUsuario extends Activity {

    @ViewById(R.id.input_ident)
    EditText editTextIdent;
    @ViewById(R.id.iniciar_deposito)
    Button botonIniciar;
    @ViewById(R.id.radio_ciudadano)
    RadioButton radCiudadano;
    @ViewById(R.id.radio_empresa)
    RadioButton radEmpesa;

    public final static String EXTRA_MESSAGE = "com.ecoparque.activites.SeleccionUsuario.MESSAGE";

    @Click(R.id.iniciar_deposito)
    void iniciarDeposito() {
        if (radCiudadano.isChecked()) {
            Intent intent = new Intent(SeleccionUsuario.this, Depositante_.class);
            intent.putExtra(EXTRA_MESSAGE, editTextIdent.getText().toString());
            intent.putExtra("from", "ciudadano");
            startActivity(intent);
        } else {
            Intent intent = new Intent(SeleccionUsuario.this, DatosEmpresa_.class);
            intent.putExtra(EXTRA_MESSAGE, editTextIdent.getText().toString());
            startActivity(intent);
        }
    }

    public void onRadioButtonClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();
        switch (view.getId()) {
            case R.id.radio_ciudadano:
                if (checked) {
                    TextView textNIF = (TextView) findViewById(R.id.text_NIF);
                    textNIF.setVisibility(View.VISIBLE);

                    editTextIdent.setText("");
                    editTextIdent.setHint(R.string.NIF);

                    TextView textCIF = (TextView) findViewById(R.id.text_CIF);
                    textCIF.setVisibility(View.GONE);
                    break;
                }
            case R.id.radio_empresa:
                if (checked) {
                    TextView textCIF = (TextView) findViewById(R.id.text_CIF);
                    textCIF.setVisibility(View.VISIBLE);

                    editTextIdent.setText("");
                    editTextIdent.setHint(R.string.CIF);

                    TextView textNIF = (TextView) findViewById(R.id.text_NIF);
                    textNIF.setVisibility(View.GONE);
                    break;
                }
        }
    }

    @OptionsItem(R.id.action_settings)
    void myMethod() {
        DialogFragment newFragment = new DesconectarFragment();
        newFragment.show(getFragmentManager(), "dialogo");
    }

    @AfterTextChange(R.id.input_ident)
    void afterTextChangedOnInputIdent(Editable s, TextView hello) {
        Validador validador = new Validador();

        if (editTextIdent.getHint().toString().equalsIgnoreCase("NIF"))
            botonIniciar.setEnabled(validador.validarNIF(s.toString()));
        else
            botonIniciar.setEnabled(validador.validarCIF(s.toString()));
    }
}
