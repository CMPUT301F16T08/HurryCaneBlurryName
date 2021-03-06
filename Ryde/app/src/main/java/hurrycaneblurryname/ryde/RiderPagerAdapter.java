package hurrycaneblurryname.ryde;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

import hurrycaneblurryname.ryde.View.RiderTabFragment1;
import hurrycaneblurryname.ryde.View.RiderTabFragment2;
import hurrycaneblurryname.ryde.View.RiderTabFragment3;

/**
 * Created by Zone on 2016/11/17.
 */
public class RiderPagerAdapter extends FragmentStatePagerAdapter{
    int mNumOfTabs;
    static private ArrayList<TabFragment> tabList = new ArrayList<>();

    public RiderPagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                RiderTabFragment1 tab1 = new RiderTabFragment1();
                tabList.add(tab1);
                return tab1;
            case 1:
                RiderTabFragment2 tab2 = new RiderTabFragment2();
                tabList.add(tab2);
                return tab2;
            case 2:
                RiderTabFragment3 tab3 = new RiderTabFragment3();
                tabList.add(tab3);
                return tab3;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }

    public void refreshTabs() {
        for (TabFragment t : tabList) {
            t.refresh();
        }
    }


}
