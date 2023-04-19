package com.fypvpreventor.VpreventorFYP;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.fypvpreventor.VpreventorFYP.fragments.DashBoardFragment;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.ArrayList;

public class AudioListAdapter extends RecyclerView.Adapter<AudioListAdapter.AudioViewHolder> {

    public File[] allFiles;
    private TimeAgo timeAgo;
    private onItemListClick onItemListClick;
    private FirebaseStorage storage;
    private StorageReference userStorageRef;
    private Context mContext;

    public AudioListAdapter(Context context) {
        mContext = context;
    }
    public AudioListAdapter(File[] allFiles, onItemListClick onItemListClick){
        this.allFiles = allFiles;
        this.onItemListClick = onItemListClick;
        storage = FirebaseStorage.getInstance();
        userStorageRef = storage.getReference().child("users/" );    }

    @NonNull
    @Override
    public AudioListAdapter.AudioViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_list_item,parent,false);
        timeAgo=new TimeAgo();
        return new AudioViewHolder(view, parent.getContext());
    }

    @Override
    public void onBindViewHolder(@NonNull AudioListAdapter.AudioViewHolder holder, int position) {
        holder.list_title.setText(allFiles[position].getName());
        holder.list_date.setText(timeAgo.getTimeAgo(allFiles[position].lastModified()));
    }

    @Override
    public int getItemCount() {
        return allFiles.length;
    }

    public class AudioViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView share;
        private ImageView send_email;
        private TextView list_title;
        private TextView list_date;
        private Context mContext;

        public AudioViewHolder(@NonNull View itemView, Context context){
            super(itemView);

            share = itemView.findViewById(R.id.share);
            send_email = itemView.findViewById(R.id.sendemail); //change to email
            list_title = itemView.findViewById(R.id.list_title);
            list_date = itemView.findViewById(R.id.list_date);
            mContext = context;

            itemView.setOnClickListener(this);
            send_email.setOnClickListener(this);
            share.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (view == share) {
                // Share file via WhatsApp
                File file = allFiles[getAdapterPosition()];
                Uri uri = FileProvider.getUriForFile(mContext, "com.fypvpreventor.VpreventorFYP.fileprovider", file);
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("audio/*");
                intent.putExtra(Intent.EXTRA_STREAM, uri);
                intent.setPackage("com.whatsapp");
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                mContext.startActivity(Intent.createChooser(intent, "Share audio file"));
            } else if (view == send_email) {
                // Send email with audio file attachment and preset text
                File file = allFiles[getAdapterPosition()];
                Uri uri = FileProvider.getUriForFile(mContext, "com.fypvpreventor.VpreventorFYP.fileprovider", file);

                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                emailIntent.setType("audio/mp3");
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject");
                emailIntent.putExtra(Intent.EXTRA_TEXT, "This is a recorded audio from Vpreventor. ");

                // To send to the current user email, you can get the user's email from Firebase Authentication
                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                if (currentUser != null) {
                    String userEmail = currentUser.getEmail();
                    emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{userEmail});
                }

                emailIntent.putExtra(Intent.EXTRA_STREAM, uri);
                mContext.startActivity(Intent.createChooser(emailIntent, "Send email"));
            } else {
                onItemListClick.onClickListener(allFiles[getAdapterPosition()], getAdapterPosition());
            }
        }

    }

    public interface onItemListClick{
        void onClickListener(File file, int position);
    }

}
