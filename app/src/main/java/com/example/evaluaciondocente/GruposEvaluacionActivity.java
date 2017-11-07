package com.example.evaluaciondocente;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.evaluaciondocente.Model.GrupoEvaluar;
import com.example.evaluaciondocente.Model.RemoteService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GruposEvaluacionActivity extends AppCompatActivity {

    RecyclerView rv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rv = (RecyclerView) findViewById(R.id.rv_grupos_evaluar);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setItemAnimator(new DefaultItemAnimator());
        rv.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://pruebaweb-st.pucmm.edu.do/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RemoteService service = retrofit.create(RemoteService.class);

        service.getGruposEvaluar("20162017","2","20150585")
                .enqueue(new Callback<List<GrupoEvaluar>>() {
                    @Override
                    public void onResponse(Call<List<GrupoEvaluar>> call, Response<List<GrupoEvaluar>> response) {
                        rv.setAdapter(new MainAdapter(response.body()));
                    }

                    @Override
                    public void onFailure(Call<List<GrupoEvaluar>> call, Throwable t) {

                    }
                });


    }

    public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder>{
        private List<GrupoEvaluar> grupos;

        public MainAdapter(List<GrupoEvaluar> grupos){
            this.grupos = grupos;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.grupo_item,parent,false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {

            final GrupoEvaluar grupoEvaluar = grupos.get(position);
            holder.tvGrupo.setText(grupoEvaluar.Grupo);
            holder.tvNombreAsignatura.setText(grupoEvaluar.NombreAsig);
            holder.tvProfesor.setText(grupoEvaluar.Profesor);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(GruposEvaluacionActivity.this, GrupoEvaluarActivity.class);
                    intent.putExtra("GRUPO_EVALUAR",grupoEvaluar);
                    startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return grupos.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder{

            TextView tvGrupo;
            TextView tvNombreAsignatura;
            TextView tvProfesor;
            public ViewHolder(View itemView) {
                super(itemView);
                tvGrupo = (TextView) itemView.findViewById(R.id.tv_grupo);
                tvNombreAsignatura = (TextView) itemView.findViewById(R.id.tv_nombre_asignatura);
                tvProfesor = (TextView) itemView.findViewById(R.id.tv_profesor);
            }
        }
    }
}
