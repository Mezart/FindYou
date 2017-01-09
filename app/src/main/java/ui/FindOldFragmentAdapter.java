package ui;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import limeng.com.findyou.R;
import limeng.com.findyou.view.OtherMainActivity;
import limeng.com.findyou.view.TieziDetailActivity;
import model.pojo.Topic;
import model.pojo.UserInfo;
import utils.GlideCircleTransform;

/**
 * Created by Administrator on 2016/12/16 0016.
 */
public class FindOldFragmentAdapter extends RecyclerView.Adapter {
    private List<Topic> list;
    private Context context;
    private View view;
    private MyHolder holder;
    public FindOldFragmentAdapter(List<Topic> tlist,Context con){
        this.list = tlist;
        this.context = con;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = LayoutInflater.from(context).inflate(R.layout.findoldfragment_item_layout,null);
        holder = new MyHolder(view);
        holder.name = (TextView) view.findViewById(R.id.name);
        holder.sex = (TextView) view.findViewById(R.id.sex);
        holder.title = (TextView) view.findViewById(R.id.title);
        holder.date = (TextView) view.findViewById(R.id.pubdate);
        holder.content = (TextView) view.findViewById(R.id.content);
        holder.img1 = (ImageView) view.findViewById(R.id.img1);
        holder.img2 = (ImageView) view.findViewById(R.id.img2);
        holder.img3= (ImageView) view.findViewById(R.id.img3);
        holder.userImg = (ImageView) view.findViewById(R.id.user_img);
        holder.userName = (TextView) view.findViewById(R.id.username);
        return holder;
    }
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if(list.get(position).getTargetTruename()!=null){
            ((MyHolder)holder).name.setText(list.get(position).getTargetTruename());
        }
        if(list.get(position).getTargetSex()!=null){
            ((MyHolder)holder).sex.setText(list.get(position).getTargetSex()==0?"男":"女");
        }
        if(list.get(position).getTitle()!=null){
            ((MyHolder)holder).title.setText(list.get(position).getTitle());
        }
        if(list.get(position).getPubTime()!=null){
            ((MyHolder)holder).date.setText(list.get(position).getPubTime());
        }
        if(list.get(position).getContent()!=null){
            ((MyHolder)holder).content.setText(list.get(position).getContent());
        }
        if(list.get(position).getUserInfo().getTruename()!=null){
            ((MyHolder)holder).userName.setText(list.get(position).getUserInfo().getTruename());
        }
        if(list.get(position).getUserInfo().getHeadImage()!=null){
            Glide.with(context).load(list.get(position).getUserInfo().getHeadImage()).error(R.mipmap.ic_launcher).transform(new GlideCircleTransform(context)).placeholder(R.mipmap.ic_launcher).into(((MyHolder)holder).userImg);
        }
        if(list.get(position).getImageList().size()==1){
            ((MyHolder)holder).img1.setVisibility(View.VISIBLE);
            Glide.with(context).load(list.get(position).getImageList().get(0).getUrl()).error(R.mipmap.ic_launcher).placeholder(R.mipmap.ic_launcher).into(((MyHolder)holder).img1);
        }else if(list.get(position).getImageList().size()==2){
            ((MyHolder)holder).img1.setVisibility(View.VISIBLE);
            ((MyHolder)holder).img2.setVisibility(View.VISIBLE);
            Glide.with(context).load(list.get(position).getImageList().get(0).getUrl()).error(R.mipmap.ic_launcher).placeholder(R.mipmap.ic_launcher).into(((MyHolder)holder).img1);
            Glide.with(context).load(list.get(position).getImageList().get(1).getUrl()).error(R.mipmap.ic_launcher).placeholder(R.mipmap.ic_launcher).into(((MyHolder)holder).img2);
        }else if(list.get(position).getImageList().size()==3){
            ((MyHolder)holder).img1.setVisibility(View.VISIBLE);
            ((MyHolder)holder).img2.setVisibility(View.VISIBLE);
            ((MyHolder)holder).img3.setVisibility(View.VISIBLE);
            Glide.with(context).load(list.get(position).getImageList().get(0).getUrl()).error(R.mipmap.ic_launcher).placeholder(R.mipmap.ic_launcher).into(((MyHolder)holder).img1);
            Glide.with(context).load(list.get(position).getImageList().get(1).getUrl()).error(R.mipmap.ic_launcher).placeholder(R.mipmap.ic_launcher).into(((MyHolder)holder).img2);
            Glide.with(context).load(list.get(position).getImageList().get(2).getUrl()).error(R.mipmap.ic_launcher).placeholder(R.mipmap.ic_launcher).into(((MyHolder)holder).img3);
        }
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, TieziDetailActivity.class);
                intent.putExtra("tid",list.get(position).getId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    private class MyHolder extends RecyclerView.ViewHolder{
        public MyHolder(View itemView) {
            super(itemView);
        }
        private TextView date,title,content,name,sex,userName;
        private ImageView img1,img2,img3,userImg;
    }
}
