package raydingoz.cerrahkulup;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Anasayfa extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseUser user;
    private FirebaseDatabase dbFire;
    Button btn_ayar;
    TextView txt_isim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anasayfa);


        //ayar/////
        txt_isim = (TextView)findViewById(R.id.txt_ana_isim);
        btn_ayar = (Button)findViewById(R.id.btn_ana_ayar);

        mAuth = FirebaseAuth.getInstance();
        dbFire = FirebaseDatabase.getInstance();
        //ayar////

        btn_ayar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),MainActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
            }
        });

        Uyeislemleri();
    }
    public void Uyeislemleri(){

        GirisListener();
        IsimKontrol();
    }
    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
    public void GirisListener(){
        Log.e("Ansayfa","girişlistener başlıyor");
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                user = firebaseAuth.getCurrentUser();

                if (user != null) {
                    // User is signed in
                    Log.d("firebase_uye", "onAuthStateChanged:signed_in:" + user.getUid());
                    txt_isim.setText(mAuth.getCurrentUser().getDisplayName());
                    IsimKontrol();

                } else {
                    // User is signed out
                    Log.d("firebase_uye", "onAuthStateChanged:signed_out");
                    Log.e("Ansayfa","giriş olmadı");
                    Misafir();
                    Intent i = new Intent(getApplicationContext(), GirisYap.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);
                    finish();
                }
                // ...
            }
        };
    }
    public void Misafir() {
        mAuth.signInWithEmailAndPassword("misafir@gmail.com","112233").addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Log.d("firebase gir", "signInWithEmail:onComplete:" + task.isSuccessful());

                // If sign in fails, display a message to the user. If sign in succeeds
                // the auth state listener will be notified and logic to handle the
                // signed in user can be handled in the listener.
                if (!task.isSuccessful()) {
                    Log.w("firabase gir", "signInWithEmail", task.getException());
                    Toast.makeText(getApplicationContext(), "garip hatalar.",
                            Toast.LENGTH_SHORT).show();
                }else {
                    Log.e("anasayfa","misafir olarak giriş yapıldı");
                }

                // ...
            }
        });
    }

    public void IsimKontrol(){
        Log.e("Ansayfa","isim kontrol başlıyor");

        if (!isOnline()){
            Log.e("anasayfa","internet yok");
            txt_isim.setText("çevrimdışı");
        }else {

        }
    }
    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);

    }
    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

}
