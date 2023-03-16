package com.example.psicoapp;

import android.content.Context;
import android.content.DialogInterface;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {

    private Context mContext;
    private ArrayList<Imagenes> mImagenes;
    String userID, userID_S;

    final ImageAdapter.OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Imagenes item);
    }

    public ImageAdapter(Context context, ArrayList<Imagenes> imagenes, ImageAdapter.OnItemClickListener listener){
        this.mContext = context;
        this.mImagenes = imagenes;
        this.listener = listener;
        userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        userID_S = userID.substring(0,5);
    }


    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.image_item, parent, false);
        return new ImageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        Imagenes imagenActual = mImagenes.get(position);

        holder.cvimg.setAnimation(AnimationUtils.loadAnimation(mContext, R.anim.fade_transition));

        holder.textViewNombre.setText(imagenActual.getNombreImagen());

        // Para evitar Observación muy larga en Cardview Examen del Paciente
        String observacion = imagenActual.getObservacionImagen();
        if (observacion.length() >= 35){
            observacion = observacion.substring(0,34);
            if (observacion.substring(34) == " "){
                observacion = observacion.substring(0,33);
                observacion = observacion+"...";
                holder.textViewObservacion.setText(observacion);
            } else {
                observacion = observacion+"...";
                holder.textViewObservacion.setText(observacion);
            }
        }else {
            holder.textViewObservacion.setText(observacion);
        }

        Picasso.with(mContext)
                .load(imagenActual.getImagenUrl())
                .resize(500,500)
                .centerCrop()
                .into(holder.imageView, new Callback() {
                    @Override
                    public void onSuccess() {
                        holder.progressBarImage.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError() {

                    }
                });

        holder.deleteimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference() // Firebase reference to after remove item
                        .child("Imagen"+userID_S+"154354567")
                        .child(imagenActual.getKeyImg());

                StorageReference photoRef = FirebaseStorage.getInstance().getReferenceFromUrl(imagenActual.getImagenUrl());

                new AlertDialog.Builder(mContext)
                        .setIcon(R.drawable.ic_warning)
                        .setTitle("¿Seguro desea borrar este examen?") // to confirm deletion
                        .setCancelable(false)
                        .setNegativeButton(android.R.string.cancel, null)
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                int newPosition = holder.getAdapterPosition();
                                mImagenes.remove(newPosition);
                                notifyItemRemoved(newPosition);
                                notifyItemRangeChanged(newPosition, mImagenes.size());

                                ref.removeValue();
                                photoRef.delete();

                                notifyDataSetChanged();
                            }
                        }).show();
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(imagenActual);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mImagenes.size();
    }

    public static class ImageViewHolder extends RecyclerView.ViewHolder{
        public TextView textViewNombre, textViewObservacion;
        public ImageView imageView;
        public ProgressBar progressBarImage;
        CardView cvimg;
        ImageView deleteimg;

        public ImageViewHolder (View itemView){
            super(itemView);

            textViewNombre = itemView.findViewById(R.id.text_view_nombre);
            textViewObservacion = itemView.findViewById(R.id.text_view_observacion);
            imageView = itemView.findViewById(R.id.image_view_subida);
            cvimg = itemView.findViewById(R.id.cvimg);
            progressBarImage = itemView.findViewById(R.id.progressBarImage);
            deleteimg = itemView.findViewById(R.id.ic_delete);
        }
    }

    public void filterList(ArrayList<Imagenes> filteredList) {
        mImagenes = filteredList;
        notifyDataSetChanged();
    }
}
