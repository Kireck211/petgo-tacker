package mx.iteso.petgo.fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import de.hdodenhof.circleimageview.CircleImageView;
import mx.iteso.petgo.R;
import mx.iteso.petgo.beans.User;

import static mx.iteso.petgo.utils.Constants.PARCELABLE_USER;

public class ProfileFragment extends Fragment {

    private CircleImageView profilePicture;
    private ImageView editPhone;
    private TextView phoneNumber;
    private TextView userName;
    private Switch enableVisibleSwitch;
    private EditText phoneEditor;
    private Button saveButton;
    private User userAdmin;
    Bundle bundle;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile,container,false);
        bundle = this.getArguments();
        if (bundle != null) {
            userAdmin = bundle.getParcelable(PARCELABLE_USER);
        }
        profilePicture = view.findViewById(R.id.profile);
        editPhone = view.findViewById(R.id.edit);
        phoneNumber = view.findViewById(R.id.user_phone);
        enableVisibleSwitch = view.findViewById(R.id.switch_availability);
        phoneEditor = view.findViewById(R.id.user_phone_edit);
        saveButton = view.findViewById(R.id.button_save);
        userName = view.findViewById(R.id.user_name);
        if (userAdmin.getName() != null)
            userName.setText(userAdmin.getName().toString());

        //profilePicture.setImageURI(Uri.parse(userAdmin.getImageUrl())); // isnt working

        //profilePicture.setImageBitmap(getBitmapFromURL(userAdmin.getImageUrl())); // isnt working

        editPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editPhoneMethod(); // por si cambia de telefono, lo demas lo agarra de google
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveNewPhone();
                // ejecutar codigo para actualizar la bd
            }
        });

        return view; // simplemente mostrar el fragmento
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("SwitchEnable",enableVisibleSwitch.isChecked());
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null)
            enableVisibleSwitch.setChecked(savedInstanceState.getBoolean("SwitchEnable"));
    }

    private void saveNewPhone() {
        String phone = phoneEditor.getText().toString();
        if ( !phone.equals("")) {
            phoneNumber.setText(phone);
            phoneEditor.setVisibility(View.GONE);
            saveButton.setVisibility(View.GONE);
        }
    }

    private void editPhoneMethod() {
        phoneEditor.setHint(phoneNumber.getText().toString());
        phoneEditor.setVisibility(View.VISIBLE);
        saveButton.setVisibility(View.VISIBLE);
    }

    /*public static Bitmap getBitmapFromURL(String src) { // isnt working
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }*/
}


