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

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import de.hdodenhof.circleimageview.CircleImageView;
import mx.iteso.petgo.R;
import mx.iteso.petgo.beans.User;

import static mx.iteso.petgo.utils.Constants.PARCELABLE_USER;

public class ProfileFragment extends Fragment {

    private DatabaseReference mReference;
    private User mUser;
    private FirebaseAuth mAuth;
    private ImageView editPhone;
    private TextView phoneNumber;
    private TextView userName;
    private Switch enableVisibleSwitch;
    private EditText phoneEditor;
    private EditText nameEditor;
    private Button saveButton;
    public SimpleDraweeView draweeView;
    private User userAdmin;
    Bundle bundle;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile,container,false);
        mAuth = FirebaseAuth.getInstance();
        /*bundle = this.getArguments();
        if (bundle != null) {
            userAdmin = bundle.getParcelable(PARCELABLE_USER);
        }*/

        draweeView = view.findViewById(R.id.profile);
        editPhone = view.findViewById(R.id.edit);
        phoneNumber = view.findViewById(R.id.user_phone);
        enableVisibleSwitch = view.findViewById(R.id.switch_availability);
        phoneEditor = view.findViewById(R.id.user_phone_edit);
        nameEditor = view.findViewById(R.id.user_name_edit);
        saveButton = view.findViewById(R.id.button_save);
        userName = view.findViewById(R.id.user_name);
        profileDataRequest();

        editPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editInfoMethod(); // por si cambia de telefono, lo demas lo agarra de google
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveNewInfo();
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

    private void saveNewInfo() {
        String phone = phoneEditor.getText().toString();
        String name = nameEditor.getText().toString();
        if ( !phone.equals("")&&!name.equals("")) {
            updatePhoneAndName(phone,name);
        }
        else if (!phone.equals("")&&name.equals("")) {
            updatePhone(phone);
        }
        else if (phone.equals("")&&!name.equals("")) {
            updateName(name);
        }
        else {
            phoneEditor.setVisibility(View.GONE);
            nameEditor.setVisibility(View.GONE);
            saveButton.setVisibility(View.GONE);
        }
    }

    private void editInfoMethod() {
        phoneEditor.setHint(phoneNumber.getText().toString());
        nameEditor.setHint(userName.getText().toString());
        phoneEditor.setVisibility(View.VISIBLE);
        nameEditor.setVisibility(View.VISIBLE);
        saveButton.setVisibility(View.VISIBLE);
    }

    private void updatePhone(String newPhone) {
        phoneNumber.setText(newPhone);
        phoneEditor.setVisibility(View.GONE);
        nameEditor.setVisibility(View.GONE);
        saveButton.setVisibility(View.GONE);
        //update DB
    }
    private void updateName (String newName) {
        userName.setText(newName);
        phoneEditor.setVisibility(View.GONE);
        nameEditor.setVisibility(View.GONE);
        saveButton.setVisibility(View.GONE);
        // update DB
    }
    private void updatePhoneAndName(String newPhone, String newName) {
        // update DB
    }

    public void profileDataRequest() {
        mReference = FirebaseDatabase.getInstance().getReference();
        String name = "";
        final FirebaseUser user = mAuth.getCurrentUser();
        Query query = mReference.child("users")
                .orderByChild("tokenId")
                .limitToFirst(1)
                .equalTo(user.getUid());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Log.d("SNAPSHOT: ", "onDataChange: "+snapshot);
                        mUser = snapshot.getValue(User.class);
                        userName.setText((CharSequence) mUser.getName());
                        draweeView.setImageURI(Uri.parse(mUser.getPicture()));
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}


