package mx.iteso.petgo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import mx.iteso.petgo.beans.Address;
import mx.iteso.petgo.beans.Pet;
import mx.iteso.petgo.beans.User;
import mx.iteso.petgo.databinding.ActivityLoginBinding;
import static mx.iteso.petgo.utils.Constants.FACEBOOK_PROVIDER;
import static mx.iteso.petgo.utils.Constants.GOOGLE_PROVIDER;
import static mx.iteso.petgo.utils.Constants.PARCELABLE_USER;
import static mx.iteso.petgo.utils.Constants.TYPE;
import static mx.iteso.petgo.utils.Constants.USER_PREFERENCES;
import static mx.iteso.petgo.utils.Constants.USER_PROVIDER;
import static mx.iteso.petgo.utils.Constants.USER_TOKEN;

public class ActivityLogin extends ActivityBase implements View.OnClickListener {

    ActivityLoginBinding mBinding;
    private FirebaseAuth mAuth;
    private DatabaseReference mReference;
    private User mUser = new User();
    private GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN_IN_GOOGLE = 9001;
    private CallbackManager mCallbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        mReference = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        mCallbackManager = CallbackManager.Factory.create();

        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build(); //734076330933-iljb8qg6cpgq6mv8amnngu841oqtof0q.apps.googleusercontent.com
        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        mBinding.btnGoogleActivityLogin.setOnClickListener(this);


        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        //mBinding.btnFacebookActivityLogin.setOnClickListener(this);
        mBinding.btnGoogleActivityLogin.setOnClickListener(this);
        mBinding.btnFacebookActivityLogin.setReadPermissions("email", "public_profile");
        mBinding.btnFacebookActivityLogin.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d("Successful!", "facebook:onSuccess:" + loginResult);
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d("Canceled!", "facebook:onCancel");
                // ...
            }

            @Override
            public void onError(FacebookException error) {
                Log.d("Facebook error!", "facebook:onError", error);
                // ...
            }
        });

    }

    private void signInWithGoogle() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN_GOOGLE);
    }
    private void signInWithFacebook() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN_GOOGLE);
    }

   @Override
    public void onClick(View view) {
        int option = view.getId();
        switch (option) {
            case R.id.btn_facebook_activity_login:
                signInWithFacebook();
                break;
            case R.id.btn_google_activity_login:
                signInWithGoogle();
                break;
        }
    }
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }
    private void updateUI(final FirebaseUser user) {
        hideProgressDialog();
        /**
         * https://stackoverflow.com/questions/39249043/firebase-auth-get-additional-user-info-age-gender?utm_medium=organic&utm_source=google_rich_qa&utm_campaign=google_rich_qa
         * */
        if (user != null) { // User authenticated
            final User loginUser = new User();
            loginUser.setType(TYPE);
            loginUser.setName(user.getDisplayName());
            loginUser.setTokenId(user.getUid());
            loginUser.setPicture(user.getPhotoUrl().toString());

            String provider = null;
            if (user.getProviderData().size() > 1)
                provider = user.getProviderData().get(1).getProviderId();

            if (provider.contains(FACEBOOK_PROVIDER)) {
                loginUser.setProvider(FACEBOOK_PROVIDER);
            } else if (provider.contains(GOOGLE_PROVIDER)){
                loginUser.setProvider(GOOGLE_PROVIDER);
            }

            Query query = mReference.child("users")
                    .orderByChild("tokenId")
                    .limitToFirst(1)
                    .equalTo(user.getUid());

            final String finalProvider = provider;
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            mUser = snapshot.getValue(User.class);
                            mUser.setProvider(finalProvider);
                        }
                    } else {
                        mUser.setTokenId(user.getUid());
                        mUser.setName(user.getDisplayName());
                        mUser.setAvailability(false); // by default
                        mUser.setPicture(user.getPhotoUrl().toString());
                        mUser.setType(TYPE);
                        mUser.setBalance(0.0f);
                        mUser.setProvider(finalProvider);

                        String userId = mReference.child("users").push().getKey();
                        mReference.child("users").child(userId).setValue(mUser);
                    }
                    saveUser();
                    Intent bottomNavActivity = new Intent(ActivityLogin.this, ActivityBottomMain.class);
                    bottomNavActivity.putExtra(PARCELABLE_USER, loginUser);
                    startActivity(bottomNavActivity);
                    finish();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });


        }

    }
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d("Firebase Google Auth", "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("Login successful", "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(ActivityLogin.this, "Welcome!",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("Login failed", "signInWithCredential:failure", task.getException());
                            Toast.makeText(ActivityLogin.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
    }
    private void handleFacebookAccessToken(AccessToken token) {
        Log.d("Facebook token", "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("Sing in successful", "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("Sing in failed", "signInWithCredential:failure", task.getException());
                            Toast.makeText(ActivityLogin.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        // ...
                    }
                });
    }
    private void saveUser() {
        SharedPreferences sharedPreferences = getSharedPreferences(USER_PREFERENCES, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(USER_TOKEN, mUser.getTokenId());
        editor.putString(USER_PROVIDER, mUser.getProvider());
        editor.apply();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN_GOOGLE) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            } else {
                // Google Sign In failed, update UI appropriately
                // ...
            }
        }
    }

}
