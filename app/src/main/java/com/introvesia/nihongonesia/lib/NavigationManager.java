package com.introvesia.nihongonesia.lib;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import com.introvesia.nihongonesia.R;
import com.introvesia.nihongonesia.fragments.KanjiFragment;

import java.util.Stack;

/**
 * Created by asus on 30/06/2017.
 */

public class NavigationManager {
    private static Stack<Fragment> fragmentStack = new Stack<Fragment>();
    private static FragmentManager fragmentManager;

    public static void setFragmentManager(FragmentManager fragmentManager) {
        NavigationManager.fragmentManager = fragmentManager;
    }

    public  static int size()
    {
        return fragmentManager.getFragments().size();
    }

    public static void push(Fragment fragment) {
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.add(R.id.container, fragment);
        if (fragmentStack.size() > 0)
            ft.hide(fragmentStack.lastElement());
        fragmentStack.push(fragment);
        ft.commit();
    }

    public static void replace(Fragment fragment) {
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.replace(R.id.container, fragment);
        ft.commit();
    }

    public static boolean isEmpty() {
        return fragmentStack.size() == 0;
    }

    public static boolean isLast()
    {
        return fragmentStack.size() == 1;
    }

    public static void pop(){
        FragmentTransaction ft = fragmentManager.beginTransaction();
        if (fragmentStack.size() > 1) {
            fragmentStack.lastElement().onPause();
            ft.remove(fragmentStack.pop());
            fragmentStack.lastElement().onResume();
            ft.show(fragmentStack.lastElement());
            ft.commit();
        }
    }

    public static void reset() {
        fragmentStack.clear();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        for (Fragment fragment : fragmentManager.getFragments()) {
            if (fragment == null) continue;
            ft.remove(fragment);
        }
        ft.commit();
    }

    public static void resetToHome() {
        if (fragmentStack.size() > 1) {
            while (fragmentStack.size() > 1) {
                pop();
            }
        }
    }
}
