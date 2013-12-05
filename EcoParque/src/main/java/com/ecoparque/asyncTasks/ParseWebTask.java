package com.ecoparque.asyncTasks;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.ecoparque.R;
import com.ecoparque.activites.MapaDominio;
import com.ecoparque.objects.UrlInfo;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URL;

public class ParseWebTask extends AsyncTask<String, Integer, String> {
    private final Activity mActivity;
    private TextView ip, pais, localidad, coordenadas;
    private final ObjectMapper mapper = new ObjectMapper();
    private UrlInfo urlInfo;
    private ProgressDialog pDialog;
    private Button mostrarMapa;
    private String mUrl;
    private final String JSONurl = "http://freegeoip.net/json/";

    public ParseWebTask(Activity activity) {
        mActivity = activity;
    }

    @Override
    protected void onPreExecute() {
        pDialog = new ProgressDialog(mActivity);
        pDialog.setCancelable(false);
        pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pDialog.setMessage("Procesando...");
        pDialog.show();
    }

    @Override
    protected String doInBackground(String... urls) {
        try {
            mUrl = urls[0];
            urlInfo = mapper.readValue(new URL(JSONurl + mUrl), UrlInfo.class);
        } catch (IOException e) {
            e.printStackTrace();
            return "error";
        }
        return "ok";
    }


    @Override
    protected void onPostExecute(String result) throws NullPointerException {
        if (result.equalsIgnoreCase("ok")) {
            try {

                ip = (TextView) mActivity.findViewById(R.id.ip);
                pais = (TextView) mActivity.findViewById(R.id.pais);
                localidad = (TextView) mActivity.findViewById(R.id.localidad);
                coordenadas = (TextView) mActivity.findViewById(R.id.coordenadas);

                ip.setText(ip.getText().toString() + urlInfo.getIp());
                pais.setText(pais.getText().toString() + urlInfo.getCountry_name() + " (" + urlInfo.getCountry_code() + ")");
                localidad.setText(localidad.getText().toString() + urlInfo.getCity());
                coordenadas.setText(coordenadas.getText().toString() + urlInfo.getLatitude() + ", " + urlInfo.getLongitude());

                mostrarMapa = (Button) mActivity.findViewById(R.id.btn_mostrar_mapa);
                mostrarMapa.setEnabled(true);
                mostrarMapa.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mActivity, MapaDominio.class);
                        intent.putExtra("lat", urlInfo.getLatitude());
                        intent.putExtra("lon", urlInfo.getLongitude());
                        intent.putExtra("ciudad", urlInfo.getCity());
                        intent.putExtra("url", mUrl);
                        mActivity.startActivity(intent);
                    }
                });
                pDialog.dismiss();
            } catch (NullPointerException e) {
                Toast.makeText(mActivity, "Error al intentar obtener los datos de la web", 1000).show();
                mActivity.finish();
            }
        } else {
            Toast.makeText(mActivity, "Error al intentar obtener los datos de la web", 1000).show();
            mActivity.finish();
        }
    }

}
