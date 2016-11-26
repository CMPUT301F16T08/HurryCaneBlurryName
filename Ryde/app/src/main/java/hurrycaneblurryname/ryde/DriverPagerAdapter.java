package hurrycaneblurryname.ryde;

/**
 * Created by Zone on 2016/11/25.
 */

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import hurrycaneblurryname.ryde.View.DriverTabFragment1;
import hurrycaneblurryname.ryde.View.DriverTabFragment2;
import hurrycaneblurryname.ryde.View.DriverTabFragment3;

/**
 * Created by Zone on 2016/11/17.
 */
public class DriverPagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public DriverPagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                DriverTabFragment1 tab1 = new DriverTabFragment1();
                return tab1;
            case 1:
                DriverTabFragment2 tab2 = new DriverTabFragment2();
                return tab2;
            case 2:
                DriverTabFragment3 tab3 = new DriverTabFragment3();
                return tab3;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }


}

