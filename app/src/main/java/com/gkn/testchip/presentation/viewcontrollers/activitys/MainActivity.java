package com.gkn.testchip.presentation.viewcontrollers.activitys;

import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.gkn.testchip.R;
import com.gkn.testchip.domain.presenters.MainPresenter;
import com.gkn.testchip.domain.presenters.MainPresenterImpl;

public class MainActivity extends AppCompatActivity implements MainPresenter.View{

    private EditText etRows;
    private Button btProbar;
    private TextView tvSerie, tvCantidad, tvUltimo;
    private MainPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getExtras();
        getSession();
        initPresenter();
        bindUI();
        initUI();
        initEvents();
    }

    @Override
    public void getExtras() {

    }

    @Override
    public void getSession() {

    }

    @Override
    public void initPresenter() {
        presenter = new MainPresenterImpl(MainActivity.this);
    }

    @Override
    public void bindUI() {
        etRows = findViewById(R.id.et_rows);
        btProbar = findViewById(R.id.btn_probar);
        tvSerie = findViewById(R.id.label0b);
        tvCantidad = findViewById(R.id.label2b);
        tvUltimo = findViewById(R.id.label3b);
    }

    @Override
    public void initUI() {
        tvSerie.setText("");
    }

    @Override
    public void initEvents() {
        btProbar.setEnabled(true);
        //presenter.validateAPN();
        btProbar.setOnClickListener(view -> presenter.getRows(etRows.getText().toString()));
    }

    @Override
    public void listenDbInsertFinished() {
        btProbar.setEnabled(true);
    }

    @Override
    public void setData(String cantidad, String ultimo) {
        tvCantidad.setText(cantidad);
        tvUltimo.setText(ultimo);
    }
}