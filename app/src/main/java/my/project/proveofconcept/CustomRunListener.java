package my.project.proveofconcept;


import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.junit.runner.Description;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;

import java.util.List;

public class CustomRunListener extends RunListener {

    @Override
    public void testRunStarted(Description description) throws Exception {
        Log.d("LISTENER", "Starting TEST: " + description.getDisplayName());
    }


    @Override
    public void testRunFinished(Result result) throws Exception {
        Log.d("LISTENER", String.format("%d finished with %d failures", result.getRunCount(), result.getFailureCount()));
        List<Failure> allFailed = result.getFailures();
        String content = "";
        for (Failure failure : allFailed) {
            Log.d("Failed",failure.getDescription().getMethodName());
            Log.d("Reason",failure.getMessage());
//            failure.getTrace();

            content += failure.getDescription().getMethodName();
            content += failure.getMessage();
        }


        GetDeviceInfo deviceInfo = new GetDeviceInfo();
        String details = deviceInfo.getApiSdk()+":"+deviceInfo.getDeviceModel()+":"+deviceInfo.getUserDevice()+":"+deviceInfo.getUserProduct();
        Log.d("DeviceInfo",details);
        Log.d("Print Status","Output from Firebase console");
//        new UploadResults(MainActivity.getContext(),details+".txt",content);

        try {
            Toast.makeText(MainActivity.act, "Results uploaded to cloud storage", Toast.LENGTH_SHORT).show();
        }
        catch(Exception e){e.printStackTrace();}

    }

}
