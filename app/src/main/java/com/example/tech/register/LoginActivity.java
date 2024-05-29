package com.example.tech.register;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.example.tech.R;
import com.example.tech.common.Capture;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import es.dmoral.toasty.Toasty;

public class LoginActivity extends AppCompatActivity  implements View.OnClickListener {

    EditText etID, etPass;
    Button btnBarCodeL, btnLogin, btnRegister;
    SwitchCompat swStayLogin;
    String pass, stayLogin;
    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        etID = findViewById(R.id.etID);
        etPass = findViewById(R.id.etPassword);
        btnBarCodeL = findViewById(R.id.btnBarCodeL);
        swStayLogin = findViewById(R.id.swStayLogin);
        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);

        btnBarCodeL.setOnClickListener(this);
        btnLogin.setOnClickListener(v -> login());
        btnRegister.setOnClickListener(v -> register());
    }

    public void login() {
        if (etID.getText().toString().equals("")||etPass.getText().toString().equals(""))
            Toasty.warning(this, "Please Fill All Fields First!", Toasty.LENGTH_LONG, true).show();
        else {
            btnLogin.setEnabled(false);
            btnBarCodeL.setEnabled(false);
            btnRegister.setEnabled(false);
            id = etID.getText().toString();
            pass = etPass.getText().toString();
            Backendless.UserService.login(String.valueOf(id), pass, new AsyncCallback<BackendlessUser>() {
                @Override
                public void handleResponse(BackendlessUser response) {
                    if (swStayLogin.isChecked()) stayLogin = "true";
                    else stayLogin = "false";
                    Intent in = new Intent(LoginActivity.this, SplashActivity.class);
                    in.putExtra("intent", 1);
                    in.putExtra("stayLogin", stayLogin);
                    in.putExtra("id", etID.getText().toString());
                    startActivity(in);
                    LoginActivity.this.finish();
                }

                @Override
                public void handleFault(BackendlessFault fault) {
                    Toasty.error(LoginActivity.this, fault.getMessage() + " code " + fault.getDetail(), Toast.LENGTH_LONG, true).show();
                    btnLogin.setEnabled(true);
                    btnBarCodeL.setEnabled(true);
                    btnRegister.setEnabled(true);
                }
            });
        }
    }

    @Override
    public void onClick(View view) {
        scanCode();
    }

    private void scanCode(){
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setCaptureActivity(Capture.class);
        integrator.setOrientationLocked(false);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        integrator.setPrompt("Scanning Code");
        integrator.initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,  Intent data) {

        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null){
            if (result.getContents() != null){
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(result.getContents());
                etID.setText(result.getContents());
                builder.setTitle("Scanning Result");
                builder.setPositiveButton("Scan Again", (dialog, which) -> scanCode()).setNegativeButton("OK", (dialog, which) -> dialog.cancel());
                AlertDialog dialog = builder.create();
                dialog.show();
            }
            else
                Toasty.warning(this, "No Result", Toast.LENGTH_LONG, true).show();
        }
        else
            super.onActivityResult(requestCode, resultCode, data);
    }

    public void register() {
        Intent in = new Intent(this, RegisterActivity.class);
        startActivity(in);
        this.finish();
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Exit");
        builder.setMessage("Exit Application?");
        builder.setPositiveButton("Yes", (d, which) -> {finish();System.exit(0);})
                .setNegativeButton("No", (d, which) -> d.cancel());
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}