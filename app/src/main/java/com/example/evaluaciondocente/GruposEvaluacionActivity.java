package com.example.evaluaciondocente;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
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

    private RecyclerView rv;
    private Dialog mDialog;
    private PreferencesManager preferencesManager;
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grupos_evaluacion);

        Toolbar toolbar = findViewById(R.id.toolbar);
        if(toolbar != null) setSupportActionBar(toolbar);

        mDialog = new Dialog(this);
        preferencesManager = new PreferencesManager(this);

        if (preferencesManager.isFirstTimeLaunch()) {
            showPopup();
        }

        mProgressBar = findViewById(R.id.progressBar);

        rv = findViewById(R.id.rv_grupos_evaluar);
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
                        mProgressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onFailure(Call<List<GrupoEvaluar>> call, Throwable t) {

                    }
                });
    }

    public void showPopup(){
        preferencesManager.setFirstTimeLaunch(false);

        Button btnOK;
        mDialog.setContentView(R.layout.custom_popup);

        btnOK = mDialog.findViewById(R.id.btn_ok);
        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDialog.dismiss();
            }
        });

        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mDialog.show();
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
                tvGrupo = itemView.findViewById(R.id.tv_grupo);
                tvNombreAsignatura = itemView.findViewById(R.id.tv_nombre_asignatura);
                tvProfesor = itemView.findViewById(R.id.tv_profesor);
            }
        }
    }
}
