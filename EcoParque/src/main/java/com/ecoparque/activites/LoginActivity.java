package com.ecoparque.activites;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ecoparque.R;
import com.ecoparque.objects.MyPrefs_;

import org.androidannotations.annotations.AfterTextChange;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.sharedpreferences.Pref;

@EActivity(R.layout.login)
@OptionsMenu(R.menu.login)
public class LoginActivity extends Activity {

    @Pref
    MyPrefs_ myPrefs;

    @ViewById(R.id.email)
    EditText mEmailView;
    @ViewById(R.id.password)
    EditText mPasswordView;
    @ViewById(R.id.login_form)
    View mLoginFormView;
    @ViewById(R.id.login_status)
    View mLoginStatusView;
    @ViewById(R.id.login_status_message)
    TextView mLoginStatusMessageView;

    private static final String DUMMY_CREDENTIAL = "m@m:mmmm";
    private static Boolean exito = false;
    private String mEmail;
    private String mPassword;
    private View focusView = null;

    @AfterViews
    void initList() {
        mEmailView.setText(myPrefs.userName().get());
    }

    @Click(R.id.acceder)
    void acceder() {
        if (!passwordYMailCorrectos()) {
            focusView.requestFocus();
        } else {
            mLoginStatusMessageView.setText(R.string.login_progress_signing_in);
            showProgress(true);
            UserLoginTask();
        }
    }

    @Background
    void UserLoginTask() {
        exito = false;
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            mostrarResultadoLogin();
        }
        String[] pieces = DUMMY_CREDENTIAL.split(":");
        if (pieces[0].equals(mEmail) && pieces[1].equals(mPassword))
            exito = true;
        mostrarResultadoLogin();
    }

    @UiThread
    void mostrarResultadoLogin() {
        showProgress(false);
        if (exito) {
            Intent intent = new Intent(LoginActivity.this, SeleccionPuntoLimpio_.class);
            startActivity(intent);
            Toast.makeText(getApplicationContext(), "Éxito!", 500).show();
        } else {
            mPasswordView.setError(getString(R.string.error_incorrect_password));
            mPasswordView.requestFocus();
        }
    }

    @AfterTextChange({R.id.email, R.id.password})
    void afterTextChangedOnEmail() {
        if (!mEmailView.getText().toString().isEmpty() && !mPasswordView.getText().toString().isEmpty())
            findViewById(R.id.acceder).setEnabled(true);
        else
            findViewById(R.id.acceder).setEnabled(false);
    }


    private void showProgress(final boolean show) {
        int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

        mLoginStatusView.setVisibility(View.VISIBLE);
        mLoginStatusView.animate()
                .setDuration(shortAnimTime)
                .alpha(show ? 1 : 0)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        mLoginStatusView.setVisibility(show ? View.VISIBLE : View.GONE);
                    }
                });

        mLoginFormView.setVisibility(View.VISIBLE);
        mLoginFormView.animate()
                .setDuration(shortAnimTime)
                .alpha(show ? 0 : 1)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                    }
                });

    }

    private Boolean passwordYMailCorrectos() {
        mEmailView.setError(null);
        mPasswordView.setError(null);

        mEmail = mEmailView.getText().toString();
        mPassword = mPasswordView.getText().toString();

        if (TextUtils.isEmpty(mPassword)) {
            mPasswordView.setError(getString(R.string.error_field_required));
            focusView = mPasswordView;
            return false;
        } else if (mPassword.length() < 4) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            return false;
        }

        if (TextUtils.isEmpty(mEmail)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            return false;
        } else if (!mEmail.contains("@")) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            return false;
        }
        return true;
    }


    @Override
    protected void onStop() {
        super.onStop();
        myPrefs.userName().put(mEmailView.getText().toString());
    }
}
