package com.example.shopcartanimation;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.jauker.widget.BadgeView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    BadgeView badgeView;
    List<String> dataList = new ArrayList<>();
    ShopAdapter shopAdapter;
    ListView lv;
    ImageView shopCart;
    int buyNum = 0;
    ViewGroup animLayout;
    ImageView imageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        shopCart = (ImageView) findViewById(R.id.iv);
        lv = (ListView) findViewById(R.id.lv);

        //BadgeView初始化
        badgeView = new BadgeView(this);
        badgeView.setTargetView(shopCart);
        badgeView.setTextColor(Color.WHITE);
        badgeView.setBackgroundColor(Color.RED);
        badgeView.setTextSize(12);
        //添加适配器
        shopAdapter = new ShopAdapter(getData());
        lv.setAdapter(shopAdapter);

    }

    /**
     * 数据测试
     */
    public List<String> getData() {

        List<String> list = new ArrayList<>();
        for (int i = 1; i < 20; i++) {
            list.add("这是第" + i + "个商品");
        }

        return list;
    }

    public class ShopAdapter extends BaseAdapter {

        public ShopAdapter(List<String> data) {
            dataList = data;
        }

        @Override
        public int getCount() {
            return dataList.size();
        }

        @Override
        public Object getItem(int position) {
            return dataList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder holder = null;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = LayoutInflater.from(MainActivity.this).inflate(R.layout.item_list, null);
                holder.buyBtn = (Button) convertView.findViewById(R.id.buy_btn);
                holder.nameTv = (TextView) convertView.findViewById(R.id.name_tv);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.nameTv.setText(dataList.get(position));

            holder.buyBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int[] start_location = new int[2];
                    v.getLocationInWindow(start_location);

                    imageView = new ImageView(MainActivity.this);
                    imageView.setImageResource(R.drawable.sign);

                    initAnimation(imageView, start_location);

                }
            });

            return convertView;
        }
    }

    class ViewHolder {
        Button buyBtn;
        TextView nameTv;
    }

    public ViewGroup createAnimLayout() {
        ViewGroup rootView = (ViewGroup) getWindow().getDecorView();

        LinearLayout animLayout = new LinearLayout(this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT
                , ViewGroup.LayoutParams.MATCH_PARENT);
        animLayout.setLayoutParams(lp);
        animLayout.setBackgroundResource(android.R.color.transparent);

        rootView.addView(animLayout);

        return animLayout;
    }

    public void addViewToLayout(ViewGroup viewGroup, View view, int[] location) {
        viewGroup.addView(view);

        int x = location[0];
        int y = location[1];

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT
                , ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.leftMargin = x;
        lp.topMargin = y;
        view.setLayoutParams(lp);

    }

    public void initAnimation(final View view, int[] location) {
        animLayout = createAnimLayout();
        addViewToLayout(animLayout, view, location);

        int[] end_location = new int[2];
        shopCart.getLocationInWindow(end_location);

        int dx = end_location[0] - location[0];
        int dy = end_location[1] - location[1];

        TranslateAnimation translateAnimationX = new TranslateAnimation(0,dx,0,0);
        translateAnimationX.setInterpolator(new LinearInterpolator());
        translateAnimationX.setFillAfter(true);

        TranslateAnimation translateAnimationY = new TranslateAnimation(0,0,0,dy);
        translateAnimationY.setInterpolator(new AccelerateInterpolator());
        translateAnimationX.setFillAfter(true);

        AnimationSet animationSet = new AnimationSet(false);
        animationSet.addAnimation(translateAnimationX);
        animationSet.addAnimation(translateAnimationY);
        animationSet.setDuration(800);

        animationSet.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                view.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.setVisibility(View.GONE);
                buyNum++;
                badgeView.setText(buyNum + "");
                badgeView.setBadgeGravity(Gravity.TOP | Gravity.RIGHT);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        view.startAnimation(animationSet);

    }

}
