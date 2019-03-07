package my.project.proveofconcept;

import android.content.ComponentName;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.TextView;
import android.widget.Toast;


import java.io.File;
import java.io.FileFilter;



/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LoadTestFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoadTestFragment extends Fragment implements FileChooserDialogFragment.FileSelectedListener {

    private File currentlySelected = null;

    private View.OnClickListener pickFileListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            onPickClicked();
        }
    };

    private View.OnClickListener loadRunListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            onRunClicked();
        }
    };

    public LoadTestFragment() {

    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment LoadTestFragment.
     */
    public static LoadTestFragment newInstance() {
        LoadTestFragment fragment = new LoadTestFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_load_test, container, false);

        view.findViewById(R.id.btn_pick_test).setOnClickListener(pickFileListener);
        view.findViewById(R.id.btn_load_test).setOnClickListener(loadRunListener);

        return view;
    }

    private void onPickClicked() {
//        new DownloadFile();
        FileChooserDialogFragment dialog = new FileChooserDialogFragment();
        dialog.setListener(this);
        dialog.setFileFilter(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return pathname.isDirectory() || pathname.getName().endsWith(".jar")|| pathname.getName().endsWith(".apk")|| pathname.getName().endsWith(".dex");
            }
        });
        dialog.show(getFragmentManager(),"filePicker");
    }

    private void onRunClicked() {

        Bundle b = new Bundle();
        b.putString("testfile", DownloadFile.localFile.getAbsolutePath());
        System.out.println("in RUN PATH:"+DownloadFile.localFile.getAbsolutePath());

        try {
            if (!this.getContext().startInstrumentation(new ComponentName(this.getContext().getApplicationContext(), Runner.class), DownloadFile.localFile.getAbsolutePath(), b)) {
            Log.d("RUNNER", "Starting Instrumentation failed");
        }
        else{
                System.out.println("Instrumenation Work!!!");
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }



    @Override
    public void onFileSelected(File selectedFile) {
        currentlySelected = selectedFile;
        ((TextView)getView().findViewById(R.id.txt_file_name)).setText(selectedFile.getName());
    }
}
