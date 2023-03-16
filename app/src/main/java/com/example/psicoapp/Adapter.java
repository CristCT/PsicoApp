package com.example.psicoapp;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Adapter extends RecyclerView.Adapter<Adapter.PacientesviewHolder>{

    Context context;
    ArrayList<Pacientes> list;
    String userID, userID_S;

    final Adapter.OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Pacientes item);
    }

    public Adapter(Context context, ArrayList<Pacientes> list, OnItemClickListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
        userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        userID_S = userID.substring(0,5);
    }

    @NonNull
    @Override
    public PacientesviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item,parent,false);
        return new PacientesviewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull PacientesviewHolder holder, int position) {
        Pacientes paciente = list.get(position);

        holder.cv.setAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_transition));

        holder.textViewRut.setText(paciente.getRut());
        holder.textViewNombre.setText(paciente.getNombre());
        holder.textViewApellidos.setText(paciente.getApellidos());

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference() // Firebase reference to after remove item
                        .child("Pacientes"+userID_S)
                        .child(paciente.getKey());

                new AlertDialog.Builder(context)
                        .setIcon(R.drawable.ic_warning)
                        .setTitle("¿Seguro desea borrar a este paciente?") // to confirm deletion
                        .setCancelable(false)
                        .setNegativeButton(android.R.string.cancel, null)
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                int newPosition = holder.getAdapterPosition();
                                list.remove(newPosition);
                                notifyItemRemoved(newPosition);
                                notifyItemRangeChanged(newPosition, list.size());

                                ref.removeValue();

                                notifyDataSetChanged();
                            }
                        }).show();
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(paciente);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class PacientesviewHolder extends RecyclerView.ViewHolder {

        TextView textViewRut, textViewNombre, textViewApellidos;
        CardView cv;
        ImageView delete;

        public PacientesviewHolder(@NonNull View itemView) {
            super(itemView);
            textViewRut = itemView.findViewById(R.id.textview_rut);
            textViewNombre = itemView.findViewById(R.id.textview_nombre);
            textViewApellidos = itemView.findViewById(R.id.textview_apellidos);
            cv = itemView.findViewById(R.id.cv);

            delete = itemView.findViewById(R.id.ic_delete);

        }
    }

    public void filterList(ArrayList<Pacientes> filteredList) { // Filter
        list = filteredList;
        notifyDataSetChanged();
    }

}

