package limeng.com.findyou.view;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import db.SPUtils;
import limeng.com.findyou.R;
import model.dto.DataRoot;
import model.pojo.Experience;
import model.pojo.Hobby;
import model.pojo.LeaveMsg;
import model.pojo.UserInfo;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;
import ui.GridViewAdapter;
import ui.LeaveMessageAdapter;
import utils.GsonManager;
import utils.HttpUtils;
import utils.LoginUtils;
import utils.ScreenUtils;
import utils.ToastUtils;
import widget.FlowLayout;
import widget.MyGrideView;
import widget.MyLinearLayout;

public class OtherMainActivity extends AppCompatActivity {
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what==0){
                btn.setText("√已添加");
                btn.setClickable(false);
                btn.setBackground(getResources().getDrawable(R.drawable.btn_unclick_shape));
            }else if(msg.what==4){
                Glide.with(OtherMainActivity.this).load(user.getHeadImage()).into(userimg);
                usersex.setImageDrawable(user.getSex()==1?getResources().getDrawable(R.drawable.female_icon):getResources().getDrawable(R.drawable.female_icon));
                username.setText(user.getTruename());
                try {
                    d  = new SimpleDateFormat("yyyy-MM-dd").parse(user.getBirth());
                    int year = d.getYear();
                    Date now = new Date();
                    age = now.getYear()-year;
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                userage.setText(age+"");
                if(user.getExpList().size()>0){
                    elist = user.getExpList();
                    adapter = new GridViewAdapter(OtherMainActivity.this,elist);
                    grideView.setAdapter(adapter);
                }
                if(user.getHobbyList()!=null&&user.getHobbyList().size()>0){
                    hList = user.getHobbyList();
                    for(int i = 0;i<hList.size();i++){
                        Hobby h = hList.get(i);
                        //音乐 电影 户外运动 书籍 电子游戏
                        if(h.getSubList()!=null&&h.getSubList().size()>0){
                            switch (h.getName()){
                                case "吃货":
                                    displayHobby(h,eat,getResources().getColor(R.color.colorEating_TX),getResources().getDrawable(R.drawable.eat_shape));
                                    break;
                                case "电影":
                                    displayHobby(h,tele,getResources().getColor(R.color.colorMovie_TX),getResources().getDrawable(R.drawable.televation_shape));
                                    break;
                                case "户外运动":
                                    displayHobby(h,sport,getResources().getColor(R.color.colorSport_TX),getResources().getDrawable(R.drawable.sport_textshape));
                                    break;
                                case "书籍":
                                    displayHobby(h,book,getResources().getColor(R.color.colorReading_TX),getResources().getDrawable(R.drawable.book_shape));
                                    break;
                                case "电子游戏":
                                    displayHobby(h,tral,getResources().getColor(R.color.colorTravaling_TX),getResources().getDrawable(R.drawable.travl_shape));
                                    break;
                                case "音乐":
                                    displayHobby(h,music,getResources().getColor(R.color.colorMusic_TX),getResources().getDrawable(R.drawable.music_shape));
                                    break;
                            }
                        }
                    }
                }
            }
            else if(msg.what==7){
                btn_add.setClickable(true);
                btn_add.setText("确定");
                 adapters = new LeaveMessageAdapter(OtherMainActivity.this,lList);
                commentGrid.setAdapter(adapters);
                commentGrid.setVisibility(View.VISIBLE);
                tips.setVisibility(View.GONE);
                findmore.setVisibility(View.GONE);
            }
            else if(msg.what==5){
                btn_add.setClickable(true);
                btn_add.setText("确定");
                lList = lList.subList(0,5);
                tips.setVisibility(View.GONE);
                findmore.setVisibility(View.VISIBLE);
                findmore.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(OtherMainActivity.this,LeaveMsgActivity.class);
                        intent.putExtra("uid",aimId);
                        startActivity(intent);
                    }
                });
                adapters = new LeaveMessageAdapter(OtherMainActivity.this,lList);
                commentGrid.setAdapter(adapters);
                commentGrid.setVisibility(View.VISIBLE);

            }else if(msg.what==6){
                btn_add.setClickable(true);
                btn_add.setText("确定");
                commentGrid.setVisibility(View.GONE);
                tips.setVisibility(View.VISIBLE);
                findmore.setVisibility(View.GONE);
            }
        }
    };
    private LeaveMessageAdapter adapters;
    private ImageView userimg,usersex;
    private TextView username,userage,back;
    private List<LeaveMsg> lList = new ArrayList<>();
    private FlowLayout sport,music,eat,tele,book,tral;
    private Button btn,btn_add;
    private boolean isFriend = false;
    private EditText comment;
    private List<UserInfo> ulist = new ArrayList<>();
    private List<Hobby> hList = new ArrayList<>();
    //int uid = SPUtils.getId(this);//当前用户id
    private int aimId ;
    private UserInfo user;

    private TextView tips;
    private Button findmore;
    private MyGrideView grideView,commentGrid;
    private Date d;
    private int age;
    private GridViewAdapter adapter;
    private List<Experience> elist = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_main);
        //lookmore tips leave_message
        aimId = getIntent().getIntExtra("uid",0);
        init();
        HttpUtils.request("leaveMsg/getall?pid=1&pageSize=10&uid="+aimId, null, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtils.showShortMessage(OtherMainActivity.this,"当前网络不给力");
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.code()==200){
                    DataRoot<List<LeaveMsg>> root = GsonManager.parseJson(response.body().string(),new TypeToken<DataRoot<List<LeaveMsg>>>(){});
                    if(root.getData()!=null&&root.getData().size()!=0){
                        lList = root.getData();
                        if(lList.size()<=5){
                            mHandler.sendEmptyMessage(7);
                        }else{
                            mHandler.sendEmptyMessage(5);
                        }

                    }else{
                        mHandler.sendEmptyMessage(6);
                    }
                }
            }
        });
        HttpUtils.request("user/getFriends?uid="+LoginUtils.userInfo.getId(), null, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.code()==200){
                    DataRoot<List<UserInfo>> root = GsonManager.parseJson(response.body().string(),new TypeToken<DataRoot<List<UserInfo>>>(){});
                    ulist = root.getData();
                    for(UserInfo u:ulist){
                        if(u.getId()==aimId){
                            Message msg = Message.obtain();
                            mHandler.sendEmptyMessage(0);
                        }
                    }
                }
                else{
                    Log.e("aaa",response.code()+"");
                }

            }
        });
        /*
        赋值
         */
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText ex = new EditText(OtherMainActivity.this);
                AlertDialog alert = new AlertDialog.Builder(OtherMainActivity.this).setTitle("申请添加好友").setView(ex).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String content = ex.getText().toString();
                        HttpUtils.request("user/getFriends?uid="+LoginUtils.userInfo, null, new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                ToastUtils.showShortMessage(OtherMainActivity.this,"当前网络不给力，请检查网络设置");
                            }
                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                ToastUtils.showShortMessage(OtherMainActivity.this,"好友请求已发送");
                                btn.setEnabled(false);
                                btn.setBackground(getResources().getDrawable(R.drawable.btn_unclick_shape));
                                btn.setText("√");
                            }
                        });
                    }
                }).setNegativeButton("取消",null).show();
            }
        });
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(comment.getText().toString().isEmpty()){
                    ToastUtils.showShortMessage(OtherMainActivity.this,"内容不能为空");
                }else{
                    String content = comment.getText().toString().trim();
                    RequestBody body = new FormBody.Builder().add("content",content)
                            .add("fromUser.id", LoginUtils.userInfo.getId()+"")
                            .add("toUser.id",aimId+"")
                            .build();
                    HttpUtils.request("leaveMsg/add", body, new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    ToastUtils.showShortMessage(OtherMainActivity.this,"当前网络不给力，请检查网络设置");
                                }
                            });
                        }

                        @Override
                        public void onResponse(Call call, final Response response) throws IOException {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    btn_add.setClickable(false);
                                    btn_add.setText("发送中……");
                                }
                            });
                            if(response.code()==200){
                                  HttpUtils.request("leaveMsg/getall?pid=1&pageSize=10&uid="+aimId, null, new Callback() {
                                      @Override
                                      public void onFailure(Call call, IOException e) {

                                      }

                                      @Override
                                      public void onResponse(Call call, Response response) throws IOException {
                                          if(response.code()==200){
                                              DataRoot<List<LeaveMsg>> root = GsonManager.parseJson(response.body().string(),new TypeToken<DataRoot<List<LeaveMsg>>>(){});
                                              if(root.getData()!=null&&root.getData().size()!=0){
                                                  lList = root.getData();
                                                  if(lList.size()<=5){
                                                      mHandler.sendEmptyMessage(7);
                                                  }else{
                                                      mHandler.sendEmptyMessage(5);
                                                  }

                                              }else{
                                                  mHandler.sendEmptyMessage(6);
                                              }
                                          }
                                      }
                                  });
// try {
////                                      DataRoot<LeaveMsg> lv = GsonManager.parseJson(response.body().string(),new TypeToken<DataRoot<LeaveMsg>>(){});
////                                      LeaveMsg msg = lv.getData();
////                                      lList.add(0,msg);
//
//                                  } catch (IOException e) {
//                                      e.printStackTrace();
//                                  }
                                  runOnUiThread(new Runnable() {
                                      @Override
                                      public void run() {
                                          comment.setText("");
//                                          adapters.notifyDataSetChanged();
                                          ToastUtils.showShortMessage(OtherMainActivity.this,"留言成功");
                                      }
                                  });
                              }

                        }
                    });
                }
            }
        });
    }
    void init(){
        findmore = (Button) findViewById(R.id.lookmore);
        tips = (TextView) findViewById(R.id.tips);
        commentGrid = (MyGrideView) findViewById(R.id.leave_message);
        userimg = (ImageView) findViewById(R.id.user_img);
        username = (TextView) findViewById(R.id.user_name);
        usersex = (ImageView) findViewById(R.id.user_sex);
        userage = (TextView) findViewById(R.id.user_age);
        //爱好
        sport = (FlowLayout) findViewById(R.id.sport);
        music = (FlowLayout) findViewById(R.id.music);
        eat = (FlowLayout) findViewById(R.id.eat);
        tele = (FlowLayout) findViewById(R.id.televsion);
        book = (FlowLayout) findViewById(R.id.book);
        tral = (FlowLayout) findViewById(R.id.travl);
        back = (TextView) findViewById(R.id.back);
        btn = (Button) findViewById(R.id.add);//加好友
        btn_add = (Button) findViewById(R.id.commit);
        comment = (EditText) findViewById(R.id.comment);
        grideView = (MyGrideView) findViewById(R.id.myExp);
//        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        HttpUtils.request("user/get?uid="+LoginUtils.userInfo.getId()+"&id="+aimId,null,new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.code()==200){
                    DataRoot<UserInfo> root = GsonManager.parseJson(response.body().string(),new TypeToken<DataRoot<UserInfo>>(){});
                    user = root.getData();
                    mHandler.sendEmptyMessage(4);
                }else{
                    Log.e("",""+response.code());
                }

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(comment.getText().toString().trim().isEmpty()){
            finish();
        }else{

        }
    }
    public void displayHobby(Hobby h,FlowLayout layout,int textColor,Drawable background){
        for(int j = 0;j<h.getSubList().size();j++){
            Hobby childHobby = h.getSubList().get(j);
            TextView tx = new TextView(OtherMainActivity.this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            tx.setText(childHobby.getName());
            tx.setTextColor(textColor);
            tx.setBackground(getResources().getDrawable(R.drawable.white_btn_shape));
            params.setMargins(10,5,10,5);
//            ViewGroup.MarginLayoutParams lp = new ViewGroup.MarginLayoutParams(
//                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);lp.leftMargin = 5;
//        lp.rightMargin =10;
//        lp.topMargin = 5;
//        lp.bottomMargin = 5;
        layout.addView(tx,params);
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        ScreenUtils.doStatueImmersive(this, Color.BLACK,R.color.colorStatusBar);
    }
//        init();
//        parseArraytoList(sports,trueList);
//
//        userimg.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//                intent.setType("image/*");
//                startActivityForResult(intent, PHOTO_PICKED_WITH_DATA);
//            }
//        });
}

