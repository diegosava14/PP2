package com.example.giftgeek;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class EditWishlistDialogFragment extends DialogFragment {

    // Define the listener interface
    public interface EditWishlistDialogListener {
        void onWishlistAdded(String title, String description, String expiryDate, int userId, String creationDate);

    }

    private EditWishlistDialogListener listener;
    private EditText titleEditText;
    private EditText descriptionEditText;
    private EditText expiryDateEditText;

    public EditWishlistDialogFragment() {
        // Required empty public constructor
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_wishlist, null);

        titleEditText = dialogView.findViewById(R.id.titleEditText);
        descriptionEditText = dialogView.findViewById(R.id.descriptionEditText);
        expiryDateEditText = dialogView.findViewById(R.id.expiryDateEditText);

        builder.setView(dialogView)
                .setTitle("Edit Wishlist")
                .setPositiveButton("Save", (dialog, which) -> {
                    String title = titleEditText.getText().toString();
                    String description = descriptionEditText.getText().toString();
                    String expiryDate = expiryDateEditText.getText().toString();

                    // Notify the listener about the new wishlist
                    if (listener != null) {
                        listener.onWishlistAdded(title, description, expiryDate, getId(), "");
                    }
                    dialog.dismiss(); // Dismiss the dialog after adding the wishlist
                })
                .setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        return builder.create();
    }

    public void setListener(EditWishlistDialogListener listener) {
        this.listener = listener;
    }
}
