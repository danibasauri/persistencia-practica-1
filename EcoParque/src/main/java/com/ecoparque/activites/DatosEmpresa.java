package com.ecoparque.activites;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.ecoparque.R;
import com.ecoparque.fragments.DesconectarFragment;
import com.ecoparque.objects.NetInfo;
import com.ecoparque.objects.Validador;

import org.androidannotations.annotations.AfterTextChange;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.datos_empresa)
@OptionsMenu(R.menu.datos_empresa)
public class DatosEmpresa extends Activity implements AdapterView.OnItemSelectedListener {

    @ViewById(R.id.empr_cif)
    EditText nif;
    @ViewById(R.id.empr_nombre)
    EditText nombre;
    @ViewById(R.id.empr_tlf)
    EditText tlf;
    @ViewById(R.id.empr_mail)
    EditText mail;
    @ViewById(R.id.empr_url)
    EditText url;

    @ViewById(R.id.btn_llamar)
    Button btnTlf;
    @ViewById(R.id.btn_enviar)
    Button btnMail;
    @ViewById(R.id.btn_abrir)
    Button btnUrl;
    @ViewById(R.id.btn_siguiente)
    Button btnSiguiente;
    @ViewById(R.id.btn_info_dominio)
    Button btnInfoDom;
    @ViewById(R.id.areas_empresa)
    Spinner spinner;

    private final Validador validador = new Validador();
    private String browserUrl;
    private final NetInfo netInfo = new NetInfo(DatosEmpresa.this);

    @AfterViews
    void initList() {
        Intent prevIntent = getIntent();
        String message = prevIntent.getStringExtra(SeleccionUsuario.EXTRA_MESSAGE);
        nif.setText(message);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.areas_empresa, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
    }

    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        Toast.makeText(getApplicationContext(),
                parent.getItemAtPosition(pos).toString(), Toast.LENGTH_LONG)
                .show();

    }

    public void onNothingSelected(AdapterView<?> parent) {
    }

    @Click(R.id.btn_llamar)
    void llamarAlTlf() {
        String uri = "tel:" + tlf.getText().toString().trim();
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse(uri));
        startActivity(intent);
    }

    @Click(R.id.btn_enviar)
    void enviarMail() {
        if (netInfo.isConnected()) {
            Intent email = new Intent(Intent.ACTION_SEND);

            email.setType("message/rfc822");
            email.putExtra(Intent.EXTRA_EMAIL, new String[]{mail.getText().toString()});
            startActivity(email);
        } else {
            Toast.makeText(getApplicationContext(), "Comprueba tu conexión a internet", 1000).show();
        }
    }

    @Click(R.id.btn_abrir)
    void abrirUrl() {
        if (netInfo.isConnected()) {
            if (!url.getText().toString().startsWith("https://") && !url.getText().toString().startsWith("http://"))
                browserUrl = "http://" + url.getText().toString();
            Uri uri = Uri.parse(browserUrl);
            startActivity(new Intent(Intent.ACTION_VIEW, uri));
        } else {
            Toast.makeText(getApplicationContext(), "Comprueba tu conexión a internet", 1000).show();
        }
    }

    @Click(R.id.btn_info_dominio)
    void obtenerDatosDominio() {
        if (netInfo.isConnected()) {
            Intent intent = new Intent(DatosEmpresa.this, DatosDominio_.class);
            intent.putExtra("urlEmpresa", url.getText().toString());
            startActivity(intent);
        } else {
            Toast.makeText(getApplicationContext(), "Comprueba tu conexión a internet", 1000).show();
        }
    }

    @Click(R.id.btn_siguiente)
    void introducirDatos() {
        Intent intent = new Intent(DatosEmpresa.this, Depositante_.class);
        intent.putExtra(SeleccionUsuario.EXTRA_MESSAGE, nif.getText().toString());
        intent.putExtra("from", "empresa");
        startActivity(intent);
    }

    @OptionsItem(R.id.action_settings)
    void myMethod() {
        DialogFragment newFragment = new DesconectarFragment();
        newFragment.show(getFragmentManager(), "dialogo");
    }

    @AfterTextChange({R.id.empr_mail, R.id.empr_tlf, R.id.empr_url, R.id.empr_nombre})
    void afterTextChangedDatosEmpresa() {
        btnTlf.setEnabled(!tlf.getText().toString().isEmpty());
        btnMail.setEnabled(validador.validateEmail(mail.getText().toString()));
        btnUrl.setEnabled(!url.getText().toString().trim().isEmpty());
        btnInfoDom.setEnabled(!url.getText().toString().trim().isEmpty());

        boolean nombreFilled = !nombre.getText().toString().isEmpty();
        if (btnTlf.isEnabled() && btnMail.isEnabled() && btnUrl.isEnabled() && nombreFilled)
            btnSiguiente.setEnabled(true);
        else
            btnSiguiente.setEnabled(false);
    }
}
