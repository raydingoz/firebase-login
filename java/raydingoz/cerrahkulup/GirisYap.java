package raydingoz.cerrahkulup;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.text.DisplayContext;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.sql.Types;

public class GirisYap extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private FirebaseAuth.AuthStateListener mAuthListener;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    EditText et_mail,et_sifre;
    Button btn_gir,btn_unut,btn_mis,btn_kayit;
    CheckBox cb_hatirla;
    String mail="",sifre="";
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_giris_yap);
        mAuth = FirebaseAuth.getInstance();

        Context context = getApplicationContext();
        pref = getApplicationContext().getSharedPreferences("ayarlar",MODE_PRIVATE);
        editor = pref.edit();

        et_mail = (EditText)findViewById(R.id.et_giris_mail);
        et_sifre = (EditText)findViewById(R.id.et_giris_sifre);
        btn_gir = (Button)findViewById(R.id.btn_girisyap);
        btn_unut = (Button)findViewById(R.id.btn_sifreunut);
        btn_kayit = (Button)findViewById(R.id.btn_gir_kayit);
        btn_mis = (Button)findViewById(R.id.btn_gir_mis);
        progressBar = (ProgressBar)findViewById(R.id.progBar_gir);
        cb_hatirla = (CheckBox)findViewById(R.id.cb_hatirla);

        progressBar.setVisibility(View.INVISIBLE);
        cb_hatirla.setChecked(pref.getBoolean("hatirla",Boolean.FALSE));
        if (cb_hatirla.isChecked()){
            et_mail.setText(pref.getString("mail",""));
            et_sifre.setText(pref.getString("sifre",""));
        }else {
            et_mail.setText("");
            et_sifre.setText("");
            editor.putBoolean("hatirla",Boolean.FALSE);
            editor.commit();
        }


        btn_gir.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mail = et_mail.getText().toString();
                sifre = et_sifre.getText().toString();
                progressBar.setVisibility(View.VISIBLE);

                if(UyeOl.isEmailValid(mail)){
                    if (sifre.length()<6){
                        Toast.makeText(getApplicationContext(),"şifre 6 karakterden uzun olsun bea",Toast.LENGTH_LONG).show();
                        progressBar.setVisibility(View.INVISIBLE);
                    }else {
                        if (cb_hatirla.isChecked()){
                            editor.putString("mail", mail);
                            editor.putString("sifre",sifre);
                            editor.putBoolean("hatirla",cb_hatirla.isChecked());
                            editor.commit();
                        }else {
                            editor.putBoolean("hatirla",Boolean.FALSE);
                            editor.commit();
                        }
                        giris(et_mail.getText().toString(),et_sifre.getText().toString());
                    }
                }else {
                    Toast.makeText(getApplicationContext(),"hatalı mail adresi",Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.INVISIBLE);
                }


            }
        });

        btn_unut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.sendPasswordResetEmail(et_mail.getText().toString());
                Toast.makeText(getApplicationContext(),"şifre sıfırlama "+ et_mail.getText().toString()+ " adresine gönderildi.",Toast.LENGTH_LONG).show();
            }
        });
        btn_mis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressBar.setVisibility(View.VISIBLE);
                giris("misafir@gmail.com","112233");
            }
        });

        btn_kayit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i2 = new Intent(getApplicationContext(),UyeOl.class);
                i2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i2);
            }
        });

    }

    public void giris(String email, String password){
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d("firebase gir", "signInWithEmail:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w("firabase gir", "signInWithEmail", task.getException());
                            progressBar.setVisibility(View.INVISIBLE);
                            Toast.makeText(getApplicationContext(), "Giriş hatalı.",
                                    Toast.LENGTH_SHORT).show();
                        }else {
                            progressBar.setVisibility(View.INVISIBLE);
                            Intent i = new Intent(getApplicationContext(),Anasayfa.class);
                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(i);
                            finish();
                        }

                        // ...
                    }
                });
    }


}
