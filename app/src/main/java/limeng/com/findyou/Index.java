package limeng.com.findyou;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.location.AMapLocationClient;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import limeng.com.findyou.view.Chat;
import limeng.com.findyou.view.Concacts;
import limeng.com.findyou.view.FindActivity;
import limeng.com.findyou.view.FindOldActivity;
import limeng.com.findyou.view.FindPerson;
import limeng.com.findyou.view.FindSameHobby;
import limeng.com.findyou.view.MyThemeActivity;
import limeng.com.findyou.view.MyselfinformationActivity;
import limeng.com.findyou.view.Notification;
import limeng.com.findyou.view.Noviceguidance;
import limeng.com.findyou.view.Recommend;
import limeng.com.findyou.view.SearchActivity;
import ui.PagerAdapter;
import utils.GlideCircleTransform;
import utils.LocationUtils;
import utils.LoginUtils;
import utils.ScreenUtils;

public class Index extends AppCompatActivity implements View.OnClickListener{
    //drawerLayout 里面的控件
    private ImageView userImg,add;
    private TextView userName,editor,maintheme,tip,setup,guide,share,location;
    private List<Fragment> flist = new ArrayList<>();
    private Toolbar toolbar;
    private TextView search;
    private DrawerLayout drawerLayout;
    private TabLayout tabLayout;
    private ViewPager pager;
    private PopupWindow popupWindow;
    private LinearLayout contentLayout;
    private AMapLocationClient aMapLocationClient = new AMapLocationClient(this);
    private String [] title = {"推荐","聊天","通讯录","找人","通知"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);
        init();
        Glide.with(Index.this).load(LoginUtils.userInfo.getHeadImage()).transform(new GlideCircleTransform(Index.this)).into(userImg);
        LocationUtils.doPosition(this,aMapLocationClient,location);
        userName.setText(LoginUtils.userInfo.getTruename());
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.app_name,R.string.close);
        drawerLayout.setDrawerListener(drawerToggle);
        drawerToggle.syncState();
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        tabLayout.setupWithViewPager(pager);
        pager.setAdapter(new PagerAdapter(getSupportFragmentManager(),title,flist));
        pager.setOffscreenPageLimit(4);
    }
    private void init(){
        toolbar = (Toolbar) findViewById(R.id.tools);
        add = (ImageView) findViewById(R.id.tool_img);
        add.setOnClickListener(this);
        drawerLayout = (DrawerLayout) findViewById(R.id.draw_layout);
        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        pager = (ViewPager) findViewById(R.id.pager);
        contentLayout = (LinearLayout) findViewById(R.id.content);
        userImg = (ImageView) findViewById(R.id.img);
        userName = (TextView) findViewById(R.id.name);
        editor = (TextView) findViewById(R.id.tips);
        editor.setOnClickListener(this);
        maintheme = (TextView) findViewById(R.id.maintheme);
        maintheme.setOnClickListener(this);
        location = (TextView) findViewById(R.id.location);
        location.setOnClickListener(this);
        tip = (TextView) findViewById(R.id.mytips);
        tip.setOnClickListener(this);
        setup = (TextView) findViewById(R.id.setup);
        setup.setOnClickListener(this);
        guide = (TextView) findViewById(R.id.guide);
        guide.setOnClickListener(this);
        search = (TextView) findViewById(R.id.search);
        search.setOnClickListener(this);
        share = (TextView) findViewById(R.id.share);
        share.setOnClickListener(this);
        flist.add(new Recommend());
        flist.add(new Chat());
        flist.add(new Concacts());
        flist.add(new FindPerson());
        flist.add(new Notification());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img:
            case R.id.name:
            case R.id.tips:startIntent(MyselfinformationActivity.class);break;//编辑个人资料
            case R.id.maintheme:startIntent(MyThemeActivity.class);break;//查看主题帖
            case R.id.setup:break;
            case R.id.mytips:startIntent(MyTipsActivity.class);break;
            case R.id.guide:startIntent(Noviceguidance.class);break;
            case R.id.share://分享
                 break;
            case R.id.search:startIntent(SearchActivity.class);break;
            case R.id.tool_img:
                View rootView = getLayoutInflater().inflate(R.layout.pop_item_layout,null);
                TextView findNew = (TextView) rootView.findViewById(R.id.findnew);
                TextView old = (TextView) rootView.findViewById(R.id.old);
                findNew.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startIntent(FindSameHobby.class);
                    }
                });
                old.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startIntent(FindOldActivity.class);
                    }
                });
                popupWindow = new PopupWindow(rootView,130,200,true);
                popupWindow.setAnimationStyle(R.style.mystyle);
                popupWindow.setOutsideTouchable(true);
                popupWindow.setBackgroundDrawable(getResources().getDrawable(R.color.colorStatusBar));
                popupWindow.setFocusable(true);
                popupWindow.showAsDropDown(add,0,0,Gravity.LEFT);
                darkenBackground(0.8f);
                popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        darkenBackground(1f);
                    }
                });
                break;
            case R.id.location:LocationUtils.doPosition(this,aMapLocationClient,location);break;
        }
    }
    public void startIntent(Class clazz){
        Intent intent = new Intent(this,clazz);
        startActivity(intent);
    }
    private void darkenBackground(Float bgcolor){
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgcolor;
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        getWindow().setAttributes(lp);
    }
    @Override
    protected void onResume() {
        super.onResume();
        ScreenUtils.doStatueImmersive(this, Color.BLACK,R.color.colorStatusBar);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocationUtils.stopLocation(aMapLocationClient);
        LocationUtils.destory(aMapLocationClient);
    }
}
