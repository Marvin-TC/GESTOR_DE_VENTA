package com.kobux.pdvabarroteria.fragments;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.Query;
import com.kobux.pdvabarroteria.R;

import android.text.TextUtils;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kobux.pdvabarroteria.adapters.ChatAdapter;
import com.kobux.pdvabarroteria.models.ChatMessageModel;
import com.kobux.pdvabarroteria.utils.SessionManager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

public class ChatFragment extends Fragment {

    private RecyclerView recyclerChat;
    private EditText txtMensaje;
    private ImageButton btnEnviar, btnCancelarComentario, btnCalendario;
    private TextView txtContadorMensajesNoLeidos, txtReplyUser, txtReplyText, txtEmptyChat;
    private LinearLayout layoutComentado;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference chatRef;
    private Query queryHoy;
    private ValueEventListener mensajesListener;

    private final List<ChatMessageModel> listaMensajes = new ArrayList<>();
    private ChatAdapter adapter;

    private String miUserId;
    private String miUserName;
    private SessionManager session;

    private String replyToMessageId;
    private String replyToUserName;
    private String replyToText;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_chat, container, false);

        session = new SessionManager(requireContext());
        miUserId = "u_" + session.getUserId();
        miUserName = session.getUserName();

        if (miUserId == null || miUserId.equals("u_-1"))
            Toast.makeText(getContext(), "Error: no hay sesión activa", Toast.LENGTH_LONG).show();

        recyclerChat = view.findViewById(R.id.rvChat);
        txtMensaje = view.findViewById(R.id.txtMensaje);
        btnEnviar = view.findViewById(R.id.btnEnviar);
        txtContadorMensajesNoLeidos = view.findViewById(R.id.txtUnreadCount);
        layoutComentado = view.findViewById(R.id.layoutReply);
        txtReplyUser = view.findViewById(R.id.txtReplyUser);
        txtReplyText = view.findViewById(R.id.txtReplyText);
        btnCancelarComentario = view.findViewById(R.id.btnCancelReply);
        btnCalendario = view.findViewById(R.id.btnCalendario);
        txtEmptyChat = view.findViewById(R.id.txtEmptyChat);

        firebaseDatabase = FirebaseDatabase.getInstance();
        chatRef = firebaseDatabase.getReference("chats")
                .child("global")
                .child("mensajes");

        recyclerChat.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new ChatAdapter(listaMensajes, miUserId, message -> {
            replyToMessageId = message.getMessageId();
            replyToUserName = message.getUserName();
            replyToText = message.getMessage();
            layoutComentado.setVisibility(View.VISIBLE);
            txtReplyUser.setText(replyToUserName);
            txtReplyText.setText(replyToText);
        });

        recyclerChat.setAdapter(adapter);

        btnEnviar.setOnClickListener(v -> enviarMensaje());
        btnCancelarComentario.setOnClickListener(v -> limpiarReply());
        btnCalendario.setOnClickListener(v -> abrirSelectorFecha());

        cargarMensajesHoy();
        return view;
    }

    private void limpiarReply() {
        replyToMessageId = null;
        replyToUserName = null;
        replyToText = null;
        layoutComentado.setVisibility(View.GONE);
    }

    private void enviarMensaje() {
        String texto = txtMensaje.getText().toString().trim();
        if (texto.isEmpty()) return;

        String messageId = chatRef.push().getKey();
        long timestamp = System.currentTimeMillis();

        ChatMessageModel mensaje = new ChatMessageModel(
                messageId,
                miUserId,
                miUserName,
                texto,
                timestamp,
                replyToMessageId,
                replyToUserName,
                replyToText
        );

        mensaje.getReadBy().put(miUserId, true);

        chatRef.child(messageId).setValue(mensaje);
        txtMensaje.setText("");
        limpiarReply();
    }

    private void cargarMensajesHoy() {
        long inicioHoy = obtenerInicioDeHoy();
        escucharMensajesDesde(inicioHoy);
    }

    private void abrirSelectorFecha() {
        Calendar cal = Calendar.getInstance();
        DatePickerDialog dialog = new DatePickerDialog(
                getContext(),
                (view, year, month, day) -> {
                    Calendar inicio = Calendar.getInstance();
                    inicio.set(year, month, day, 0, 0, 0);
                    inicio.set(Calendar.MILLISECOND, 0);
                    escucharMensajesDesde(inicio.getTimeInMillis());
                },
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
        );
        dialog.show();
    }

    private void escucharMensajesDesde(long timestampInicio) {
        if (queryHoy != null && mensajesListener != null)
            queryHoy.removeEventListener(mensajesListener);

        queryHoy = chatRef.orderByChild("timestamp").startAt(timestampInicio);

        mensajesListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listaMensajes.clear();
                int noLeidos = 0;

                for (DataSnapshot ds : snapshot.getChildren()) {
                    ChatMessageModel msg = ds.getValue(ChatMessageModel.class);
                    if (msg == null) continue;

                    if (msg.getMessageId() == null)
                        msg.setMessageId(ds.getKey());

                    Map<String, Boolean> readBy = msg.getReadBy();
                    if (readBy == null || !readBy.containsKey(miUserId)) {
                        chatRef.child(msg.getMessageId())
                                .child("readBy")
                                .child(miUserId)
                                .setValue(true);
                        noLeidos++;
                    }

                    listaMensajes.add(msg);
                }

                adapter.notifyDataSetChanged();

                if (listaMensajes.isEmpty())
                    txtEmptyChat.setVisibility(View.VISIBLE);
                else {
                    txtEmptyChat.setVisibility(View.GONE);
                    recyclerChat.scrollToPosition(listaMensajes.size() - 1);
                }

                if (noLeidos > 0) {
                    txtContadorMensajesNoLeidos.setVisibility(View.VISIBLE);
                    txtContadorMensajesNoLeidos.setText("No leídos: " + noLeidos);
                } else {
                    txtContadorMensajesNoLeidos.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        };

        queryHoy.addValueEventListener(mensajesListener);
    }

    private long obtenerInicioDeHoy() {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        return c.getTimeInMillis();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (queryHoy != null && mensajesListener != null)
            queryHoy.removeEventListener(mensajesListener);
    }
}