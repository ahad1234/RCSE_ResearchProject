package my.project.proveofconcept;

import android.app.Activity;
import android.content.ComponentName;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class DownloadAndRunTest extends Activity {

    static boolean chkDownload = false;
    Button btnExecute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download_and_run_test);
        btnExecute = (Button)findViewById(R.id.btnRunTest);
    }

    public void downloadTestFile(View v){

        if(!chkDownload) {
            new DownloadFile();
            Toast.makeText(DownloadAndRunTest.this, "Test file downloaded!!!", Toast.LENGTH_SHORT).show();
            btnExecute.setBackgroundColor(Color.rgb(0,184,148));
        }
        else{
            Toast.makeText(DownloadAndRunTest.this, "File is already downloaded!!!", Toast.LENGTH_SHORT).show();
        }

    }

    public void runTestFile(View v){

        if(!chkDownload){
            Toast.makeText(DownloadAndRunTest.this, "No Test File Found!!!", Toast.LENGTH_SHORT).show();
        }
        else{
            Bundle b = new Bundle();
            b.putString("testfile", DownloadFile.localFile.getAbsolutePath());

            try {
                if (!getApplication().getApplicationContext().startInstrumentation(new ComponentName(getApplication().getApplicationContext(), Runner.class), DownloadFile.localFile.getAbsolutePath(), b)) {
                    Log.d("RUNNER", "Starting Instrumentation failed");
                }
                else{
                    System.out.println("Instrumentation Worked!!!");
                }
            }
            catch(Exception e){
                e.printStackTrace();
            }
        }
    }

}
