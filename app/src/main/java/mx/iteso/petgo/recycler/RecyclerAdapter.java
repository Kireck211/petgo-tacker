package mx.iteso.petgo.recycler;

import android.media.Image;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;

import mx.iteso.petgo.R;
import mx.iteso.petgo.beans.Solicitud;

public class RecyclerAdapter extends RecyclerView.Adapter <RecyclerAdapter.RecyclerViewHolder> {
    private ArrayList<Solicitud> mSolicitudes;
    private OnItemClickListener mListener;


    public interface OnItemClickListener {
        void onItemClick(int position);
        void onCancelClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public static class RecyclerViewHolder extends RecyclerView.ViewHolder {

        public ImageView mCancelIcon;
        public TextView clientName;
        public TextView walkDate;
        public TextView walkTime;
        public TextView clientAddress;
        public SimpleDraweeView draweeView;

        public RecyclerViewHolder(View itemView, final OnItemClickListener listener) {
            super(itemView);
            //Uri uri = Uri.parse("https://i.ytimg.com/vi/a_AqYohDuwM/maxresdefault.jpg");
            draweeView = itemView.findViewById(R.id.imageViewCliente);
            //draweeView.setImageURI(uri);
            mCancelIcon = itemView.findViewById(R.id.imageViewCancel);
            clientName = itemView.findViewById(R.id.textViewClienteNombre);
            walkDate = itemView.findViewById(R.id.textViewFechaPaseo);
            walkTime = itemView.findViewById(R.id.textViewTiempoPaseo);
            clientAddress = itemView.findViewById(R.id.textViewClienteAddress);
            if (itemView != null && mCancelIcon != null) {

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (listener != null) {
                            int position = getAdapterPosition();
                            if (position != RecyclerView.NO_POSITION) {
                                listener.onItemClick(position);
                            }
                        }
                    }
                });

                mCancelIcon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (listener != null) {
                            int position = getAdapterPosition();
                            if (position != RecyclerView.NO_POSITION) {
                                listener.onCancelClick(position);
                            }
                        }
                    }
                });
            }

        }
    }
    public RecyclerAdapter(ArrayList<Solicitud> solicitudes) {
        mSolicitudes = solicitudes ;
    }
    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_element, parent, false); // fragment????
        RecyclerViewHolder recyclerViewHolder = new RecyclerViewHolder(view, mListener);
        return  recyclerViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
        Solicitud solicitud = mSolicitudes.get(position);
        holder.draweeView.setImageURI(solicitud.getmImageResource());
        holder.clientName.setText(solicitud.getClientName());
        holder.walkDate.setText(solicitud.getWalkDate());
        holder.walkTime.setText(solicitud.getWalkTime());
        holder.clientAddress.setText(solicitud.getClientAddress());
    }

    @Override
    public int getItemCount() {
        return mSolicitudes.size();
    }

    public void setTrips(ArrayList<Solicitud> solicitudes) {
        this.mSolicitudes = solicitudes;
        this.notifyDataSetChanged();
    }
}
