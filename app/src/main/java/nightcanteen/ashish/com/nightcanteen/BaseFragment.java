package nightcanteen.ashish.com.nightcanteen;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;

/**
 * Created by ashish on 25/3/16.
 */
public class BaseFragment extends Fragment {

    BaseFragmentInterface mbaseFragmentInterface;

    public void setmListener(BaseFragmentInterface baseFragmentInterface) {
        this.mbaseFragmentInterface = baseFragmentInterface;
    }

    public static interface BaseFragmentInterface {

        void replaceFragment(BaseFragment baseFragment);

        void addFragmentToBackStack(BaseFragment baseFragment);

        void setupToolbarForDrawer(Toolbar toolbar);

        void removeFragment();

    }
}
