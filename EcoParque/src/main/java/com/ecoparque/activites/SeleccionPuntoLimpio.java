package com.ecoparque.activites;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.ecoparque.R;
import com.ecoparque.fragments.DesconectarFragment;
import com.ecoparque.objects.Constantes;
import com.ecoparque.objects.Item;
import com.ecoparque.objects.LazyAdapter;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.App;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;

@EActivity(R.layout.seleccion_punto_limpio)
@OptionsMenu(R.menu.seleccion_punto_limpio)
public class SeleccionPuntoLimpio extends Activity {

    @ViewById(R.id.listView1)
    ListView list;

    @App
    Constantes appState;

    @AfterViews
    void initList() {
        ArrayList image_details = appState.getListData();
        list.setAdapter(new LazyAdapter(this, image_details));
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                Intent intent = new Intent(SeleccionPuntoLimpio.this, SeleccionUsuario_.class);
                Item puntoLimpio = (Item) list.getItemAtPosition(position);
                appState.setPuntoLimpio(puntoLimpio.getName());
                startActivity(intent);
            }

        });
    }

    @OptionsItem(R.id.action_settings)
    void myMethod() {
        DialogFragment newFragment = new DesconectarFragment();
        newFragment.show(getFragmentManager(), "dialogo");
    }
}