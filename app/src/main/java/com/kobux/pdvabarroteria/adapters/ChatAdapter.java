package com.kobux.pdvabarroteria.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kobux.pdvabarroteria.R;
import com.kobux.pdvabarroteria.models.ChatMessageModel;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {

    private static final int VIEW_TYPE_MINE = 1;
    private static final int VIEW_TYPE_OTHER = 2;

    private List<ChatMessageModel> lista;
    private String miUserId;

    public interface OnMessageLongClickListener {
        void onMessageLongClick(ChatMessageModel message);
    }

    private OnMessageLongClickListener longClickListener;

    public ChatAdapter(List<ChatMessageModel> lista, String miUserId,
                       OnMessageLongClickListener longClickListener) {
        this.lista = lista;
        this.miUserId = miUserId;
        this.longClickListener = longClickListener;
    }

    @Override
    public int getItemViewType(int position) {
        ChatMessageModel msg = lista.get(position);
        if (msg.getUserId() != null && msg.getUserId().equals(miUserId)) {
            return VIEW_TYPE_MINE;
        } else {
            return VIEW_TYPE_OTHER;
        }
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layout;
        if (viewType == VIEW_TYPE_MINE) {
            layout = R.layout.item_message_mine;
        } else {
            layout = R.layout.item_message_other;
        }
        View v = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
        return new ChatViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        ChatMessageModel msg = lista.get(position);

        // Mostrar "Yo" si el mensaje es mío
        if (msg.getUserId() != null && msg.getUserId().equals(miUserId)) {
            holder.txtUserName.setText("Yo");
        } else {
            holder.txtUserName.setText(msg.getUserName());
        }

        holder.txtMessage.setText(msg.getMessage());
        holder.txtTime.setText(formatearHora(msg.getTimestamp()));

        if (msg.getReplyToMessageId() != null && msg.getReplyToText() != null) {
            holder.layoutReplyPreview.setVisibility(View.VISIBLE);
            holder.txtReplyPreviewUser.setText(msg.getReplyToUserName());
            holder.txtReplyPreviewText.setText(msg.getReplyToText());
        } else {
            holder.layoutReplyPreview.setVisibility(View.GONE);
        }

        holder.itemView.setOnLongClickListener(v -> {
            if (longClickListener != null) {
                longClickListener.onMessageLongClick(msg);
            }
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    static class ChatViewHolder extends RecyclerView.ViewHolder {
        TextView txtUserName, txtMessage, txtReplyPreviewUser, txtReplyPreviewText, txtTime;
        LinearLayout layoutReplyPreview;

        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);
            txtUserName = itemView.findViewById(R.id.txtUserName);
            txtMessage = itemView.findViewById(R.id.txtMessage);
            layoutReplyPreview = itemView.findViewById(R.id.layoutReplyPreview);
            txtReplyPreviewUser = itemView.findViewById(R.id.txtReplyPreviewUser);
            txtReplyPreviewText = itemView.findViewById(R.id.txtReplyPreviewText);
            txtTime = itemView.findViewById(R.id.txtTime);
        }
    }

    private String formatearHora(long timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a", Locale.getDefault());
        return sdf.format(new Date(timestamp));
    }
}