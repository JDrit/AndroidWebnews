package edu.rit.csh.androidwebnews;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;

import java.util.ArrayList;

/**
 * The fragment for the about information for the InfoActivty
 */
public class InfoAboutFragment extends SherlockFragment {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ScrollView scrollView = new ScrollView(getSherlockActivity());
        TextView tv = new TextView(getSherlockActivity());
        tv.setText(R.string.about_text);
        tv.setTextSize(20);
        scrollView.addView(tv);
        return scrollView;
    }
}