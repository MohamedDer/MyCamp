package med_der.mycamp;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

import layout.ChatFragment;
import layout.NotConnectedFragment;
import layout.ProfileFragment;
import layout.ToDoFragment;

public class MainActivity extends AppCompatActivity implements ToDoFragment.OnFragmentInteractionListener , ProfileFragment.OnFragmentInteractionListener, ChatFragment.OnFragmentInteractionListener, NotConnectedFragment.OnFragmentInteractionListener {

    public static User user;
    public GoogleSignInAccount account;
    FirebaseDatabase database ;
    DatabaseReference usersRef;

    boolean flag=false;
    public static boolean internetState=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
// get Google Account
        account = getIntent().getParcelableExtra("Account");
// Connect to the db and ping it to check if the user is already a member, if not he will be added
        database = FirebaseDatabase.getInstance();
        usersRef = database.getReference("My Camp").child("Users");
        user = new User(account.getId(),account.getDisplayName(),"https://image.freepik.com/icones-gratuites/symbole-de-l-39-utilisateur-noir-male_318-60703.jpg",account.getEmail(),"tel","team");
        usersRef.child("ping").setValue(" halo ");

        ValueEventListener ComListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                    Log.v("current child :", dsp.getKey().toString());
                    if (dsp.getKey().toString().equals(account.getId())){
                        Log.d("utilisateur trouvé : ", account.getId());
                        if (account.getPhotoUrl()!=null && !account.getPhotoUrl().equals("")  )
                            user = new User(account.getId(),account.getDisplayName(),account.getPhotoUrl().toString(),account.getEmail(),dsp.child("tel").getValue().toString(),dsp.child("team").getValue().toString());
                        else
                            user = new User(account.getId(),account.getDisplayName(),"https://image.freepik.com/icones-gratuites/symbole-de-l-39-utilisateur-noir-male_318-60703.jpg",account.getEmail(),dsp.child("tel").getValue().toString(),dsp.child("team").getValue().toString());
                        flag = true;
                        break;
                    }
                }
                if(!flag){
                    if (account.getPhotoUrl()!=null && !account.getPhotoUrl().equals("")  )
                        user = new User(account.getId(),account.getDisplayName(),account.getPhotoUrl().toString(),account.getEmail(),"+212 6 xx xx xx xx ","Open");
                    else
                        user = new User(account.getId(),account.getDisplayName(),"https://image.freepik.com/icones-gratuites/symbole-de-l-39-utilisateur-noir-male_318-60703.jpg",account.getEmail(),"+212 6 xx xx xx xx ","Open");

                    usersRef.child(account.getId()).setValue(user);

                    Log.d("New user added ","");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            };
        };
        usersRef.addValueEventListener(ComListener);

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                internetState = checkInternetState();
                handler.postDelayed(this, 7000); //check inernet status every 7 seconds
            }
        }, 7000);

        BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
                = new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                if (internetState)  {
                    switch (item.getItemId()) {
                        case R.id.group_chat:
                            ChatFragment ncf = new ChatFragment();
                            ncf.accessGroupChat(account.getDisplayName().toUpperCase(),account.getId(),user.getTeam());
                            fragmentTransaction.replace(R.id.container,ncf).commit();
                            return true;
                        case R.id.todo_list:
                            ToDoFragment ntf = new ToDoFragment();
                            ntf.setId(account.getId());
                            fragmentTransaction.replace(R.id.container,ntf).commit();
                            return true;
                        case R.id.profile:
                            ProfileFragment npf = new ProfileFragment();
                            npf.datasave(user);
                            fragmentTransaction.replace(R.id.container,npf).commit();
                            return true;
                    }
                }
                else {
                    NotConnectedFragment nncf = new NotConnectedFragment();
                    fragmentTransaction.replace(R.id.container,nncf).commit();
                }

                return false;
            }

        };
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);



    }
    @Override
    public void onBackPressed() {
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    boolean checkInternetState(){
        try (Socket socket = new Socket()) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            socket.connect(new InetSocketAddress("www.google.com" , 80), 2000);
            return true;
        } catch (IOException e) {
            return false;
        }
    }
}
