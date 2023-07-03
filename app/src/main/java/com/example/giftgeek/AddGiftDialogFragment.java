package com.example.giftgeek;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.giftgeek.Entities.Gift;

public class AddGiftDialogFragment extends DialogFragment {

    private EditText giftUrlEditText;
    private EditText giftPriorityEditText;
    private int wishlistId;
    private OnGiftAddedListener onGiftAddedListener;

    public interface OnGiftAddedListener {
        void onGiftAdded(Gift gift);
    }

    public void setOnGiftAddedListener(OnGiftAddedListener listener) {
        this.onGiftAddedListener = listener;
    }

    @SuppressLint("MissingInflatedId")
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Add Gift");

        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_add_gift, null);
        giftUrlEditText = view.findViewById(R.id.giftUrlEditText);
        giftPriorityEditText = view.findViewById(R.id.giftPriorityEditText);

        builder.setView(view)
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String url = giftUrlEditText.getText().toString();
                        String priority = giftPriorityEditText.getText().toString();

                        // Create a new Gift object with the entered name and description
                        Gift newGift = new Gift(1, wishlistId, url, priority, false);

                        // Pass the new gift object to the listener
                        if (onGiftAddedListener != null) {
                            onGiftAddedListener.onGiftAdded(newGift);
                        }
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dismiss();
                    }
                });

        return builder.create();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            wishlistId = getArguments().getInt("wishlistId");
        }
    }


}
