package com.david.mytest.test.dragrecyclerview.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.david.mytest.R;
import com.david.mytest.test.dragrecyclerview.fragment.MainFragment;
import com.david.mytest.test.dragrecyclerview.fragment.MyGridFragment;
import com.david.mytest.test.dragrecyclerview.fragment.MyListFragment;


public class DragRecyclerTestActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_drag_recyeler);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        if (savedInstanceState==null){
            MainFragment mainFragment = new MainFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment,mainFragment)
                    .commit();
        }
    }

    @Override
    public void onClick(View v) {
        Fragment fragment = null;
        switch (v.getId()){
            case R.id.list:
                fragment = new MyListFragment();
                break;
            case R.id.grid:
                fragment = new MyGridFragment();
                break;
        }
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment, fragment)
                .addToBackStack(null)
                .commit();
    }
}
