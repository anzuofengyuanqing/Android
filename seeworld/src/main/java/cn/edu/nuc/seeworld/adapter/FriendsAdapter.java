package cn.edu.nuc.seeworld.adapter;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.List;
import cn.edu.nuc.seeworld.R;
import cn.edu.nuc.seeworld.entity.Myfriends;
/**
 * Created by lenovo on 2015/10/11.
 */
public class FriendsAdapter extends BaseAdapter {
    List<Myfriends> mlistdata;
    Context mcontext;
    LayoutInflater mInflater;

    public FriendsAdapter(List<Myfriends> listdata,Context context) {
        this.mlistdata = listdata;
        this.mcontext=context;
        mInflater=LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return mlistdata.size();
    }
    @Override
    public Object getItem(int position) {
        return mlistdata.get(position);
    }
    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder=null;
        if(convertView==null){
            holder=new ViewHolder();
            convertView=mInflater.inflate(R.layout.item_friends,null);
            holder.tv_name= (TextView) convertView.findViewById(R.id.tv_name);
            holder.tv_mobile= (TextView) convertView.findViewById(R.id.tv_mobile);
            holder.tv_pro= (TextView) convertView.findViewById(R.id.tv_addr);
            convertView.setTag(holder);
        }else {
            holder= (ViewHolder) convertView.getTag();
        }
        holder.tv_name.setText(mlistdata.get(position).getName());
        holder.tv_mobile.setText(mlistdata.get(position).getMobile());
        if(mlistdata.get(position).getProvince()!=null){
            holder.tv_pro.setText(mlistdata.get(position).getProvince());
        }
        return convertView;
    }
    public class ViewHolder{
        public TextView tv_name;
        public TextView tv_mobile;
        public TextView tv_pro;
    }
}