package my.project.proveofconcept;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.FirebaseStorage;

import java.io.File;

public class MainActivity extends FragmentActivity {

    private static Application appInstance;
    private static Context context;
    public static Activity act;

    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    View.OnClickListener switchViewListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Fragment f = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
            Fragment frag;
            if(f instanceof LoadTestFragment) {
                frag = TestedFragment.newInstance();
            } else {
                frag = LoadTestFragment.newInstance();
            }
            FragmentTransaction trans = getSupportFragmentManager().beginTransaction();
            trans.replace(R.id.fragment_container,frag).commit();

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        new UploadResults(this,"testFile.txt","Trying Upload");

        appInstance = this.getApplication();
        act = this;
        context = getApplicationContext();

        FirebaseUser user = mAuth.getCurrentUser();
        String content = "03-06 03:48:33.067 5530-5564/my.project.proveofconcept D/Failed: testTextView\n" +
                "03-06 03:48:33.068 5530-5564/my.project.proveofconcept D/Reason: 'with text: is \"Test Texterr\"' doesn't match the selected view.\n" +
                "    Expected: with text: is \"Test Texterr\"\n" +
                "         Got: \"TextView{id=2131099715, res-name=txt_changeable, visibility=VISIBLE, width=1080, height=88, has-focus=false, has-focusable=false, has-window-focus=true, is-clickable=false, is-enabled=true, is-focused=false, is-focusable=false, is-layout-requested=false, is-selected=false, root-is-layout-requested=false, has-input-connection=false, x=0.0, y=105.0, text=Test Text, input-type=0, ime-target=false, has-links=false}";

//        new UploadResults(this,"23:Android SDK built for x86:generic_x86:sdk_google_phone_x86",content);



        if(null != findViewById(R.id.fragment_container)){
            if(null != savedInstanceState) {
                return;
            }

            Fragment loadTestFragment = TestedFragment.newInstance();
            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, loadTestFragment).commit();
        }

        Button switchViewButton = (Button)findViewById(R.id.btn_change_fragment);
        switchViewButton.setEnabled(true);
        switchViewButton.setOnClickListener(switchViewListener);

        // Function call for registering to a topic

        if (getIntent().getExtras() != null) {

            String keyData = getIntent().getExtras().getString("data");
            if(keyData != null) {
                if (keyData.contains("Download")) {
                    Toast.makeText(this, "Will open Download Class", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(this, DownloadAndRunTest.class);
                    startActivity(intent);
                    this.finish();
                }
            }
        }
        registerTopic();

    }

    // Registers to a topic, a message sent to this topic
    // is received in the background service as displayed
    public void registerTopic(){
        FirebaseMessaging.getInstance().subscribeToTopic("testProject")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        String msg = "Topic subscription successful";
                        if (!task.isSuccessful()) {
                            msg = "Topic subscription not successful";
                        }
                        Log.d("Topic Status", msg);
//                        Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });

    }

    public static Context getContext(){
        return context;
    }

    public void signInAnonymously() {
        mAuth.signInAnonymously().addOnSuccessListener(this, new  OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                // do your stuff
                Log.d("Signin","Sign in Success");
            }
        })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Log.e("Signin Status", "signInAnonymously:FAILURE", exception);
                    }
                });
    }

    public static void uploadFile(String title,String content){
        new UploadResults(act,title,content);
    }
}
