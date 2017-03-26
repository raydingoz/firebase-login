package raydingoz.cerrahkulup;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UyeOl extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase database;
    EditText et_rumuz,et_mail,et_sifre;
    Button btn_uyeol;
    ProgressBar progressBar;
    String rumuz="",mail="",sifre="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uye_ol);

        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        et_rumuz = (EditText)findViewById(R.id.et_rumuz);
        et_mail = (EditText)findViewById(R.id.et_mail);
        et_sifre = (EditText)findViewById(R.id.et_sifre);
        btn_uyeol = (Button)findViewById(R.id.btn_uyeol);
        progressBar = (ProgressBar)findViewById(R.id.prog_kayit);
        progressBar.setVisibility(View.INVISIBLE);

        btn_uyeol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try  {
                    InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                } catch (Exception e) {

                }
                progressBar.setVisibility(View.VISIBLE);

                rumuz = et_rumuz.getText().toString();
                mail = et_mail.getText().toString();
                sifre = et_sifre.getText().toString();

                if(isEmailValid(mail)){
                    if (sifre.length()<6){
                        Toast.makeText(getApplicationContext(),"şifre 6 karakterden uzun olsun bea",Toast.LENGTH_LONG).show();
                        progressBar.setVisibility(View.INVISIBLE);
                    }else {
                        createAccount(rumuz,mail,sifre);
                    }
                }else {
                    Toast.makeText(getApplicationContext(),"hatalı mail adresi",Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.INVISIBLE);
                }





            }
        });


    }
    private void createAccount(String _rumuz, String email, String password) {

        // [START create_user_with_email]
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(UyeOl.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {

                            progressBar.setVisibility(View.INVISIBLE);
                            Toast.makeText(UyeOl.this, "başarısız, mail daha önceden alınmış olabilir",
                                    Toast.LENGTH_LONG).show();
                        }else {
                            progressBar.setVisibility(View.INVISIBLE);
                            Toast.makeText(getApplicationContext(),"Hesap oluşturuldu",Toast.LENGTH_SHORT).show();
                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(et_rumuz.getText().toString())
                                    .build();
                            mAuth.zza(mAuth.getCurrentUser(),profileUpdates);
                            DatabaseReference mDatabase = database.getReference("users");
                            User user = new User(rumuz,mail);
                            mDatabase.child(mAuth.getCurrentUser().getUid()+"").setValue(user);


                            Intent i = new Intent(getApplicationContext(), Anasayfa.class);
                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(i);
                            finish();
                        }

                        // [START_EXCLUDE]

                        // [END_EXCLUDE]
                    }
                });
        // [END create_user_with_email]

    }

    boolean isEmailValid2(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
    public static boolean isEmailValid(String email) {
        boolean isValid = false;

        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }



}
