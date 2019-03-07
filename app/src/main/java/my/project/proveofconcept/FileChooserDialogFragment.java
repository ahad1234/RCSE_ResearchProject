package my.project.proveofconcept;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Environment;

import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;

public class FileChooserDialogFragment extends DialogFragment {

    private FileSelectedListener mListener;
    private File mSelectedFile;
    private File mParentDir;
    private ArrayAdapter<AdapterFileWrapper> mAdapter;
    private FileFilter mFilter;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.file_chooser_dialog, null);

        mParentDir = Environment.getExternalStorageDirectory();
        mAdapter = new ArrayAdapter<AdapterFileWrapper>(this.getActivity(), android.R.layout.simple_list_item_1, getFileList(mParentDir));
        ListView mListView;
        mListView = (ListView) view.findViewById(R.id.listViewFileChooser);
        mListView.setAdapter(mAdapter);
        mListView.setItemsCanFocus(true);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                File selected = mAdapter.getItem(position).file;
                if (selected.isDirectory()) {
                    setFileListHostDir(selected);
                    mParentDir = selected;
                } else {
                    mSelectedFile = selected;
                    view.setSelected(true);

                }

            }
        });

        Button b = (Button) view.findViewById(R.id.buttonDirUp);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File f = mParentDir.getParentFile();
                setFileListHostDir(f);

            }
        });

        builder.setTitle(R.string.file_chooser_title);
        builder.setOnItemSelectedListener(null);
        builder.setView(view);
        builder.setPositiveButton(R.string.select, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (null == mSelectedFile) {
                    Toast.makeText(getActivity(), R.string.file_select_error, Toast.LENGTH_LONG).show();
                } else {
                    mListener.onFileSelected(mSelectedFile);
                }

            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                FileChooserDialogFragment.this.getDialog().cancel();
            }
        });

        // Create the AlertDialog object and return it
        return builder.create();

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    public void setListener(FileSelectedListener listener) {
        mListener = listener;
    }
    public void setFileFilter(FileFilter filter) { mFilter = filter;}

    private ArrayList<AdapterFileWrapper> getFileList(File directory) {
        if (null == directory) {
            directory = Environment.getExternalStorageDirectory();
        }
        if (!directory.isDirectory()) {
            return null;
        }

        ArrayList<AdapterFileWrapper> fileList = new ArrayList<AdapterFileWrapper>();
        File[] fileArray;
        if(null == mFilter) {
            fileArray = directory.listFiles();
        } else {
            fileArray = directory.listFiles(mFilter);
        }
        if (null != fileArray) {
            for (File f : fileArray) {

                fileList.add(new AdapterFileWrapper(f));
            }

        }

        return fileList;
    }

    private void setFileListHostDir(File hostDir) {
        ArrayList<AdapterFileWrapper> fileList = getFileList(hostDir);
        if(null != fileList) {
            mAdapter.clear();
            mAdapter.addAll(fileList);
            mAdapter.notifyDataSetChanged();
        }

    }

    public interface FileSelectedListener {
        void onFileSelected(File selectedFile);
    }

    private class AdapterFileWrapper {

        public File file;

        private AdapterFileWrapper(File file) {
            this.file = file;
        }

        @Override
        public String toString() {
            String s = file.getName();
            if (file.isDirectory()) {
                s += "/";
            }
            return s;
        }
    }
}
