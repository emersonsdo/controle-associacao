package br.com.kuka.controleassociados.date;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.app.DatePickerDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;

import java.util.Calendar;

import br.com.kuka.controleassociados.R;

/**
 * Created by 01748913506 on 17/02/17.
 */

public class LastPaymentDatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_last_payment, container);
        return view;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int ano = c.get(Calendar.YEAR);
        int mes = c.get(Calendar.MONTH);
        int dia = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, ano, mes, dia);
    }

    @Override
    public void onDateSet(DatePicker datePicker, int ano, int mes, int dia) {
        EditText etUltimoPagamento = (EditText) getActivity().findViewById(R.id.et_data_ultimo_pagamento);
        etUltimoPagamento.setText(dia+"/"+(mes+1)+"/"+ano);
    }
}
