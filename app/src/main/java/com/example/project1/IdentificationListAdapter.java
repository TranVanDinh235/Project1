package com.example.project1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class IdentificationListAdapter extends RecyclerView.Adapter<IdentificationListAdapter.ViewHolder> {
    private Context context;
    private List<Identification> identifications;
    private ItemClickListener itemClickListener;

    public IdentificationListAdapter(Context context, ItemClickListener itemClickListener){
        this.itemClickListener = itemClickListener;
        this.context = context;
    }


    @NonNull
    @Override
    public IdentificationListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IdentificationListAdapter.ViewHolder holder, int position) {
        holder.bindView(identifications.get(position));
    }

    @Override
    public int getItemCount() {
        if(identifications != null) return identifications.size();
        else return 0;
    }

    public void setIdentifications(List<Identification> cards){
        identifications = cards;
        notifyDataSetChanged();
    }

    public Identification getCurrentIdentityCard(int pos){
        return identifications.get(pos);
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private Identification identification;
        private TextView nameTextView;
        private TextView editTextView;
        private TextView deleteTextView;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.tv_name);
            editTextView = itemView.findViewById(R.id.tv_edit);
            deleteTextView = itemView.findViewById(R.id.tv_delete);
            editTextView.setOnClickListener(this);
            deleteTextView.setOnClickListener(this);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if(view.getId() == editTextView.getId()){
                itemClickListener.onEditItem(identification);
            } else if(view.getId() == deleteTextView.getId()) {
                itemClickListener.onDeleteItem(identification);
            } else itemClickListener.onItemClickListener(identification);
        }

        void bindView(Identification identification){
            this.identification = identification;
            nameTextView.setText(identification.getFullname());
        }
    }
}
