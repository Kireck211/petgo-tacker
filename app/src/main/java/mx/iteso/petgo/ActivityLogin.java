package mx.iteso.petgo;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import mx.iteso.petgo.databinding.ActivityLoginBinding;

public class ActivityLogin extends AppCompatActivity implements View.OnClickListener {

    ActivityLoginBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        mBinding.btnFacebookActivityLogin.setOnClickListener(this);
        mBinding.btnGoogleActivityLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent();
        switch (view.getId()) {
            case R.id.btn_facebook_activity_login:
                intent = new Intent(this, ActivityMain.class);
                break;
            case R.id.btn_google_activity_login:
                intent = new Intent(this, ActivityMain.class);
                break;
        }
        startActivity(intent);
        finish();
    }
}
