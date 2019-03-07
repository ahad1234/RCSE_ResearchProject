package my.project.proveofconcept;

import android.support.annotation.NonNull;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import java.io.File;
import java.io.IOException;

public class DownloadFile {

    public static File localFile;

    public DownloadFile(){
        // [START storage_field_initialization]
        FirebaseStorage storage = FirebaseStorage.getInstance();
        try {
            localFile = File.createTempFile("TestFile", ".dex");
            includesForDownloadFiles();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public void includesForDownloadFiles() throws IOException {
        FirebaseStorage storage = FirebaseStorage.getInstance();

        // [START download_create_reference]
        // Create a storage reference from our app
        StorageReference storageRef = storage.getReference();

        // Create a reference with an initial file path and name
        StorageReference islandRef = storageRef.child("MainActivityTest.dex");


//        islandRef.getMetadata().addOnSuccessListener(new OnSuccessListener<StorageMetadata>() {
//            @Override
//            public void onSuccess(StorageMetadata storageMetadata) {
//                // Metadata now contains the metadata for 'images/forest.jpg'
//                System.out.println("Printing MetaData:");
//                System.out.println("Create Time:"+ new Date(storageMetadata.getCreationTimeMillis()));
//                System.out.println("Udpate Time:"+ new Date(storageMetadata.getUpdatedTimeMillis()));
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception exception) {
//                // Uh-oh, an error occurred!
//                System.err.println("--->> Err:");
//                exception.printStackTrace();
//            }
//        });


        islandRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                // Local temp file has been created
                System.out.println("FILE CREATED!!!");
                System.out.println("File Addr:"+localFile.getAbsolutePath());
                DownloadAndRunTest.chkDownload = true;

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
                System.err.println("-->> ERR:");
                exception.printStackTrace();
            }
        });

    }

    public static void main(String []aefs){
        new DownloadFile();
    }
}
