package com.ecoparque.activites;

import android.app.DialogFragment;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ecoparque.R;
import com.ecoparque.fragments.DatePickerFragment;
import com.ecoparque.fragments.DesconectarFragment;
import com.ecoparque.objects.Constantes;
import com.ecoparque.objects.NetInfo;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.App;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.ViewById;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Calendar;

@EActivity(R.layout.resultados)
@OptionsMenu(R.menu.resultados)
public class Resultados extends FragmentActivity {

    @ViewById(R.id.str_id_depositante)
    TextView ident;
    @ViewById(R.id.IVA)
    TextView iva;
    @ViewById(R.id.total)
    TextView total;
    @ViewById(R.id.mat_informatico)
    TextView matInf;
    @ViewById(R.id.neveras)
    TextView neveras;
    @ViewById(R.id.aceites)
    TextView aceites;
    @ViewById(R.id.cant_residuos)
    TextView cantResiduos;
    @ViewById(R.id.deposito_punto_limpio)
    TextView puntoLimpio;
    @ViewById(R.id.fecha)
    TextView fecha;
    @ViewById(R.id.layout_coste)
    LinearLayout coste;
    @ViewById(R.id.txt_calculo)
    TextView txtCalculo;
    @ViewById(R.id.precio_calculo)
    TextView txtPrecio;
    @ViewById(R.id.precio_iva)
    TextView txtIVA;
    @ViewById(R.id.precio_total)
    TextView txtTotal;

    @App
    Constantes appState;

    private String contenidoMensaje;
    private final NetInfo netInfo = new NetInfo(Resultados.this);
    private Integer cont = 0;
    private Intent prevIntent;

    @AfterViews
    void initList() {
        prevIntent = getIntent();
        ident.setText(prevIntent.getStringExtra(SeleccionUsuario.EXTRA_MESSAGE));
        puntoLimpio.setText(appState.getPuntoLimpio());
        MostrarFechaActualEnCalendario();
        contenidoMensaje = "Depositante:" + "\t" + ident.getText().toString() + "\n";
        contenidoMensaje += "Residuos depositados: \n";
        MostrarMaterialesDepositados();
        if (prevIntent.getStringExtra("from").equalsIgnoreCase("empresa"))
            MostrarCosteDeposito();
    }

    @Click(R.id.editar_fecha)
    void editarFecha() {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getFragmentManager(), "datePicker");
    }

    @Click(R.id.enviar_mail)
    void enviarMail() {
        if (netInfo.isConnected()) {
            Intent email = new Intent(Intent.ACTION_SEND);
            email.putExtra(Intent.EXTRA_SUBJECT, "Resultados depósito");
            email.putExtra(Intent.EXTRA_TEXT, contenidoMensaje);
            email.setType("message/rfc822");
            startActivity(email);
        } else {
            Toast.makeText(getApplicationContext(), "Comprueba tu conexión a internet", 1000).show();
        }
    }

    @Click(R.id.registrar_deposito)
    void registrarDeposito() {
        Intent intent = new Intent(Resultados.this, SeleccionPuntoLimpio_.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
        Toast.makeText(getApplicationContext(), "Depósito registrado con éxito!", 1000).show();
    }

    @OptionsItem(R.id.action_settings)
    void myMethod() {
        DialogFragment newFragment = new DesconectarFragment();
        newFragment.show(getFragmentManager(), "dialogo");
    }

    private void MostrarMaterialesDepositados() {
        if (prevIntent.getBooleanExtra("matInf", false)) {
            matInf.setVisibility(View.VISIBLE);
            contenidoMensaje += "\t" + matInf.getText().toString() + "\n";
            cont++;
        }

        if (prevIntent.getBooleanExtra("neveras", false)) {
            neveras.setVisibility(View.VISIBLE);
            contenidoMensaje += "\t" + neveras.getText().toString() + "\n";
            cont++;
        }

        if (prevIntent.getBooleanExtra("aceites", false)) {
            aceites.setVisibility(View.VISIBLE);
            contenidoMensaje += "\t" + aceites.getText().toString() + "\n";
            cont++;
        }

        if (cont == 1) {
            cantResiduos.setText("1 residuo depositado:");
        } else {
            cantResiduos.setText(cont + " residuos depositados:");
        }
    }

    private void MostrarFechaActualEnCalendario() {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        fecha.setText(String.valueOf(day) + "/" + String.valueOf(month + 1) + "/" + String.valueOf(year));
    }

    private void MostrarCosteDeposito() {
        coste.setVisibility(View.VISIBLE);
        String peso = prevIntent.getStringExtra("peso");
        txtCalculo.setText(peso + "Kg * 2,4€/Kg");

        BigDecimal resultadoInt = BigDecimal.valueOf(Integer.parseInt(peso) * 2.4);
        txtPrecio.setText("€ " + String.valueOf(resultadoInt.setScale(2, RoundingMode.CEILING)));

        BigDecimal resultadoIVA = BigDecimal.valueOf(resultadoInt.intValue() * 0.21);
        txtIVA.setText("€ " + String.valueOf(resultadoIVA.setScale(2, RoundingMode.CEILING)));

        BigDecimal resultadoTotal = resultadoInt.add(resultadoIVA);
        txtTotal.setText("€ " + String.valueOf(resultadoTotal.setScale(2, RoundingMode.CEILING)));

        contenidoMensaje += "\n";
        contenidoMensaje += txtCalculo.getText().toString() + "\t" + txtPrecio.getText().toString() + "\n";
        contenidoMensaje += iva.getText().toString() + "\t" + txtIVA.getText().toString() + "\n";
        contenidoMensaje += total.getText().toString() + "\t" + txtTotal.getText().toString() + "\n";
    }
}
