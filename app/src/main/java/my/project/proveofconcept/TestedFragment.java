package my.project.proveofconcept;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TestedFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TestedFragment extends Fragment {

    private View.OnClickListener changeTextListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            View view = getView();
            EditText editText = (EditText)view.findViewById(R.id.tf_change);
            String newText = editText.getText().toString();

            TextView textView = (TextView)view.findViewById(R.id.txt_changeable);
            textView.setText(newText);

        }
    };


    public TestedFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment TestedFragment.
     */
    public static TestedFragment newInstance() {
        TestedFragment fragment = new TestedFragment();
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
        View view = inflater.inflate(R.layout.fragment_tested, container, false);

        Button btnChangeText = (Button)view.findViewById(R.id.btn_change_text);
        btnChangeText.setOnClickListener(changeTextListener);

        return view;
    }

}
