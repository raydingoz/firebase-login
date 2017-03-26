package raydingoz.cerrahkulup;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseUser user;
    private RelativeLayout ly_mis,ly_usr,ly_off;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button btn_uyeol = (Button)findViewById(R.id.btn1);
        Button btn_gir = (Button)findViewById(R.id.btn_gir);
        Button btn_cik = (Button)findViewById(R.id.btn_ana_cik);
        final TextView txtmail = (TextView)findViewById(R.id.txt_ana_mail);
        final TextView txtrumuz = (TextView)findViewById(R.id.txt_ana_rumuz);
        txtmail.setText("mail: ");
        txtrumuz.setText("rumuz: ");

        ly_mis = (RelativeLayout)findViewById(R.id.lay_ayar_mis);
        ly_off = (RelativeLayout)findViewById(R.id.lay_ayar_offline);
        ly_usr = (RelativeLayout)findViewById(R.id.lay_ayar_user);

        ly_usr.setVisibility(View.VISIBLE);
        ly_off.setVisibility(View.INVISIBLE);
        ly_mis.setVisibility(View.INVISIBLE);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                user = firebaseAuth.getCurrentUser();

                if (user != null) {
                    // User is signed in
                    Log.d("firebase_uye", "onAuthStateChanged:signed_in:" + user.getUid());
                    txtmail.setText("mail: "+user.getEmail());
                    txtrumuz.setText("rumuz: "+user.getDisplayName());

                } else {
                    // User is signed out
                    Log.d("firebase_uye", "onAuthStateChanged:signed_out");
                    txtmail.setText("mail: çıkıs");
                    txtrumuz.setText("rumuz: ");
                }
                // ...
            }
        };
        btn_uyeol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                Intent intent = new Intent(getApplicationContext(), UyeOl.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        btn_gir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                Intent intent = new Intent(getApplicationContext(), GirisYap.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        btn_cik.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                Intent intent = new Intent(getApplicationContext(), Anasayfa.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });


        if (!isOnline()){
            ly_usr.setVisibility(View.INVISIBLE);
            ly_off.setVisibility(View.VISIBLE);
            ly_mis.setVisibility(View.INVISIBLE);
        }else if (mAuth.getCurrentUser().getDisplayName().equals("misafir")){
            ly_usr.setVisibility(View.INVISIBLE);
            ly_off.setVisibility(View.INVISIBLE);
            ly_mis.setVisibility(View.VISIBLE);
        }

    }
    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
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
