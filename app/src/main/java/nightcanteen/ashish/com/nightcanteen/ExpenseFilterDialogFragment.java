package nightcanteen.ashish.com.nightcanteen;


import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.Calendar;


/**
 * A simple {@link Fragment} subclass.
 */
public class ExpenseFilterDialogFragment extends DialogFragment {

    RadioGroup radioGroup;

    public interface ExpenseFilterDialogFragmentInterface {
        void setFilterDialogOptionNumber(int number);
    }
    public ExpenseFilterDialogFragmentInterface expenseFilterDialogFragmentInterface = null;

    public ExpenseFilterDialogFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_expense_filter_dialog, null);

        radioGroup = (RadioGroup) view.findViewById(R.id.filter_radio_grp);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // checkedId is the RadioButton selected


                // This will get the radiobutton that has changed in its check state
                RadioButton checkedRadioButton = (RadioButton) group.findViewById(checkedId);

                switch (checkedId){
                    case R.id.radioButton:
                        dismiss();
                        if (expenseFilterDialogFragmentInterface != null)
                            expenseFilterDialogFragmentInterface.setFilterDialogOptionNumber(0);
                        Toast.makeText(getActivity(), "You pressed " + checkedRadioButton.getText(), Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.radioButton2:
                        dismiss();
                        if (expenseFilterDialogFragmentInterface != null)
                            expenseFilterDialogFragmentInterface.setFilterDialogOptionNumber(3);
                        Toast.makeText(getActivity(), "You pressed " + checkedRadioButton.getText(), Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.radioButton3:
                        dismiss();
                        if (expenseFilterDialogFragmentInterface != null)
                            expenseFilterDialogFragmentInterface.setFilterDialogOptionNumber(7);
                        Toast.makeText(getActivity(), "You pressed " + checkedRadioButton.getText(), Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.radioButton4:
                        dismiss();
                        if (expenseFilterDialogFragmentInterface != null)
                            expenseFilterDialogFragmentInterface.setFilterDialogOptionNumber(30);
                        Toast.makeText(getActivity(), "You pressed " + checkedRadioButton.getText(), Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        Toast.makeText(getActivity(), "You didnt press anything! ", Toast.LENGTH_SHORT).show();
                }

            }
        });

        return view;
    }


}
