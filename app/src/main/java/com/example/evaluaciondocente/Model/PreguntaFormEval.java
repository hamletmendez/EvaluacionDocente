package com.example.evaluaciondocente.Model;

/**
 * Created by Hamlet Méndez on 11/3/2017.
 */

public class PreguntaFormEval {
    public int CodPregunta;
    public int NumPregunta;
    public String Descripcion;

    private int checkedId = -1;

    public int getSelectedRadioButtonId() {
        return checkedId;
    }
    public void setSelectedRadioButtonId(int checkedId) {
        this.checkedId = checkedId;
    }

}
