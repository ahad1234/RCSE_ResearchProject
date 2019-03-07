package my.project.proveofconcept;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;

public class UploadResults {

    Context context;
    File outputDir;
    File ff;
    FirebaseStorage storage;

    UploadResults(Context context, String title, String content) {
        this.context = context;
        outputDir = this.context.getCacheDir();

        createFileToUpload(title, content);
    }

    void createFileToUpload(String title, String content) {

        try {
            storage = FirebaseStorage.getInstance();

            ff = File.createTempFile(title, ".txt", outputDir);
            FileWriter fWrite = new FileWriter(ff.getAbsoluteFile(), true);
            fWrite.write(content);
            fWrite.close();

            BufferedReader fRead = new BufferedReader(new FileReader(ff.getAbsolutePath()));
            String line = "";
            Log.d("ResultFilePath", ff.getAbsolutePath());
            Log.d("ResultsFile", fRead.readLine());
            while ((line = fRead.readLine()) != null) {
                System.out.println(line);
            }

            uploadFile(ff,storage);


        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    void uploadFile(File fileToUpload, FirebaseStorage storage) {

        Log.d("ChkUpload", "Now Uplaoding");

        StorageReference storageRef = storage.getReference();

        // Creating a reference to "Results File from Test"
        StorageReference mountainsRef = storageRef.child(fileToUpload.getName());

        InputStream stream = null;

        try {
            stream = new FileInputStream(new File(fileToUpload.getAbsolutePath()));
        } catch (Exception e) {
            Log.d("StreamException", "error in stream");
            e.printStackTrace();
        }

        UploadTask uploadTask = mountainsRef.putStream(stream);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                Log.d("FailedUpload", "Upload wasn't successful");
                exception.printStackTrace();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Log.d("UploadStatus", "Upload was successful");
                Log.d("UploadFileName", taskSnapshot.getMetadata().getName());
            }
        });

    }

}
