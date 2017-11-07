package com.example.evaluaciondocente;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.evaluaciondocente.Model.GrupoEvaluar;
import com.example.evaluaciondocente.Model.PreguntaFormEval;
import com.example.evaluaciondocente.Model.RemoteService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GrupoEvaluarActivity extends AppCompatActivity {

    private RecyclerView rv;
    private static List<PreguntaFormEval> preguntasFormEval;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grupo_evaluar);
        GrupoEvaluar grupo =(GrupoEvaluar) getIntent().getSerializableExtra("GRUPO_EVALUAR");
        TextView tvProfesor = findViewById(R.id.tv_profesor);
        TextView tvGrupo = findViewById(R.id.tv_grupo);
        TextView tvNombreAsignatura = findViewById(R.id.tv_nombre_asignatura);
        TextView tvPeriodo = findViewById(R.id.tv_periodo);

        tvProfesor.setText(grupo.Profesor);
        tvGrupo.setText(grupo.Grupo);
        tvNombreAsignatura.setText(grupo.NombreAsig);
        tvPeriodo.setText("2017-2018/1");

        Toolbar toolbar = findViewById(R.id.toolbar);
        if(toolbar != null) setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        rv = findViewById(R.id.rv_preguntas_evaluacion);
        rv.setHasFixedSize(true);
        rv.setItemAnimator(new DefaultItemAnimator());
        rv.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        rv.setLayoutManager(new LinearLayoutManager(this));

        getPreguntas();

    }

    private void getPreguntas(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://pruebaweb-st.pucmm.edu.do/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RemoteService service = retrofit.create(RemoteService.class);
        service.getPreguntasEvaluacion("01").enqueue(new Callback<List<PreguntaFormEval>>() {
            @Override
            public void onResponse(Call<List<PreguntaFormEval>> call, Response<List<PreguntaFormEval>> response) {
                if (response.isSuccessful()) {
                    preguntasFormEval = response.body();
                    rv.setAdapter(new PreguntasAdapter(preguntasFormEval));
                }else{
                    Log.i("ERROR PREGUNTAS", response.message());
                }
            }

            @Override
            public void onFailure(Call<List<PreguntaFormEval>> call, Throwable t) {

                Log.i("ERROR PREGUNTAS", t.getMessage());
            }
        });
    }

    public class PreguntasAdapter extends RecyclerView.Adapter<PreguntasAdapter.ViewHolder>{
        private List<PreguntaFormEval> preguntas;
        private int mSelectedItem = -1;

        public PreguntasAdapter(List<PreguntaFormEval> preguntas){
            this.preguntas = preguntas;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.pregunta_item,parent,false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {

            PreguntaFormEval pregunta = preguntas.get(position);

            if (position % 2 == 1){
                holder.itemView.setBackgroundColor(Color.parseColor("#FFFFFF"));
            }else {
                holder.itemView.setBackgroundColor(Color.parseColor("#FAF8FD"));
            }

            holder.tvNumPregunta.setText(String.valueOf(pregunta.NumPregunta));
            holder.tvDescripcion.setText(pregunta.Descripcion);
            holder.radioGroup.setTag(position);

            if (pregunta.getSelectedRadioButtonId()!=-1){
                holder.radioGroup.check(pregunta.getSelectedRadioButtonId());
            }else{
                holder.radioGroup.clearCheck();
            }
        }

        @Override
        public int getItemCount() {
            return preguntas.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder{
            TextView tvNumPregunta;
            TextView tvDescripcion;
            RadioGroup radioGroup;
            public ViewHolder(View itemView) {
                super(itemView);
                tvNumPregunta = (TextView) itemView.findViewById(R.id.tv_num_pregunta);
                tvDescripcion = (TextView) itemView.findViewById(R.id.tv_descripcion);
                radioGroup = (RadioGroup) itemView.findViewById(R.id.rg_respuesta);


               radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                   @Override
                   public void onCheckedChanged(RadioGroup radioGroup, int i) {

                       int clickedPos= (int) radioGroup.getTag();
                       preguntas.get(clickedPos).setSelectedRadioButtonId(radioGroup.getCheckedRadioButtonId());
                   }
               });
            }
        }
    }
}
