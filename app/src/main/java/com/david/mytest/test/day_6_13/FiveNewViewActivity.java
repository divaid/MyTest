package com.david.mytest.test.day_6_13;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.david.mytest.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by weixi on 2017/6/13.
 */

public class FiveNewViewActivity extends Activity {
    private FrameLayout mFramelayout;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_five_new_view);
        initView();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void initView() {
        //        mFramelayout.setBackgroundColor(Color.RED);
//        findViewById(R.id.test).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mFramelayout.setBackgroundColor(Color.RED);
//                Toast.makeText(FiveNewViewActivity.this, "monkey测试", Toast.LENGTH_LONG).show();
//            }
//        });


        this.toolbar = (Toolbar) findViewById(R.id.toolbar);
        this.recyclerview = (RecyclerView) findViewById(R.id.recycler_view);
        this.ripplebutton = (Button) findViewById(R.id.ripple_button);
        this.button = (Button) findViewById(R.id.button);
        // 设置Logo
        toolbar.setLogo(R.mipmap.ic_launcher);
        // 设置标题
        toolbar.setTitle("Android5.0");
        // 设置子标题
        toolbar.setSubtitle("新控件");
        //设置ActionBar，之后就可以获取ActionBar并进行操作，操作的结果就会反应在toolbar上面
        setActionBar(toolbar);
        //设置了返回箭头，相当于设置了toolbar的导航按钮
        getActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(FiveNewViewActivity.this, "点击了返回箭头", Toast.LENGTH_LONG).show();
                AlertDialog.Builder builder = new AlertDialog.Builder(FiveNewViewActivity.this);
                builder.setMessage("对话框测试").setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        Toast.makeText(FiveNewViewActivity.this, "取消了对话框", Toast.LENGTH_LONG).show();
                    }
                }).show();
            }
        });
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 40; i++) {
            list.add("数据" + (i + 1));
        }

        //新建一个线性布局管理器(类似于gridview：GridLayoutManager)
        LinearLayoutManager manager = new LinearLayoutManager(this);
//        manager.setOrientation();//设置排列方向 同时也可以设置子条目的添加与删除动画
        //设置recyclerview的布局管理器
        recyclerview.setLayoutManager(manager);
        //新建适配器
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(this, list);
        //设置recyclerview的适配器
        recyclerview.setAdapter(adapter);


        mEditText = (TextInputEditText) findViewById(R.id.new_input);
        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 3)//通过判断输入设置错误信息
                    mEditText.setError("测试错误信息");
                else
                    mEditText.setError(null);
            }
        });
    }


    private Bitmap bitmap;
    private android.widget.Button button;
    private android.widget.Button ripplebutton;
    private android.support.v7.widget.RecyclerView recyclerview;
    private android.widget.Toolbar toolbar;
    private TextInputEditText mEditText;

    /**
     * 改变ToolBar的背景颜色
     */
    public void changeToolbarBg(View view) {
        // 获取应用程序图标的Bitmap
        bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        // 通过bitmap生成调色板palette
        Palette palette = Palette.generate(bitmap);
        // 获取palette充满活力色颜色
        int vibrantColor = palette.getVibrantColor(Color.WHITE);
        // 设置toolbar的背景颜色为vibrantColor
        this.toolbar.setBackgroundColor(vibrantColor);

    }


    class RecyclerViewAdapter extends RecyclerView.Adapter {

        private Context mContext;
        private List<String> mData;

        public RecyclerViewAdapter(Context context, List<String> data) {
            this.mContext = context;
            this.mData = data;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            TextView textView = new TextView(mContext);
            textView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            MyViewHolder myViewHolder = new MyViewHolder(textView);
            return myViewHolder;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            MyViewHolder viewHolder = (MyViewHolder) holder;
            viewHolder.mText.setText(mData.get(position));
        }

        @Override
        public int getItemCount() {
            return mData == null ? 0 : mData.size();
        }
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView mText;

        public MyViewHolder(View itemView) {
            super(itemView);
            mText = (TextView) itemView;
        }
    }
}
