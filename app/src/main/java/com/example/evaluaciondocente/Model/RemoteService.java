package com.example.evaluaciondocente.Model;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by Hamlet Méndez on 11/3/2017.
 */

public interface RemoteService {
    @GET("rest_services/api/gruposevaluacion/{anoacad}/{numper}/{matricula}")
    public Call<List<GrupoEvaluar>> getGruposEvaluar(@Path("anoacad") String anoacad,
                                                     @Path("numper") String numper,
                                                     @Path("matricula") String matricula);

    @GET("rest_services/api/preguntasform/todas/{codigoformulario}")
    public Call<List<PreguntaFormEval>> getPreguntasEvaluacion(@Path("codigoformulario") String codigoformulario);
}
