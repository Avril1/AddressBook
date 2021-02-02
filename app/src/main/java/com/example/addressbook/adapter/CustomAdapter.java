package com.example.addressbook.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.addressbook.R;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {
    private String[] mDataSet_name;
    private String[] mDataSet_phone;
    private Context context;
    private Cursor cursor;
    protected RecyclerView mRecyclerView;

    public CustomAdapter(String[] dataSet_name,String[] dataSet_phone){
        mDataSet_name = dataSet_name;
        mDataSet_phone = dataSet_phone;
    }
    public CustomAdapter(){

    }
    public CustomAdapter (Context context,Cursor cursor,String[] dataSet_name,String[] dataSet_phone){
        this.context = context;
        this.cursor = cursor;
        mDataSet_name = dataSet_name;
        mDataSet_phone = dataSet_phone;
    }
    OnItemClickListener mItemClickListener = new OnItemClickListener() {
        @Override
        public void onItemClick(View view,String name,int position) {

        }

        @Override
        public void onItemLongClick(View view,String name,int position) {

        }
    };


    //provide a reference to the type of views that you are using
    public   class ViewHolder extends RecyclerView.ViewHolder{
        private TextView name_tv;
        private TextView phone_tv;
        private ImageView avatar_iv;





        public ViewHolder(View v){
            super(v);

            name_tv = (TextView) v.findViewById(R.id.name_tv);
            phone_tv = (TextView) v.findViewById(R.id.phone_tv);
            avatar_iv = (ImageView) v.findViewById(R.id.avatar);

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String name = getName().getText().toString();
                    int position = getAdapterPosition();
                    mItemClickListener.onItemClick(v,name,position);
                }
            });

            v.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                   String name = getName().getText().toString();
                    int position = getAdapterPosition();
                    mItemClickListener.onItemLongClick(v,name,position);
                    return true;
                }
            });
        }

        public  TextView getName(){
            return name_tv;
        }
        public  TextView getPhone(){
            return phone_tv;
        }
        public  ImageView getAvatar(){
            return avatar_iv;
        }
    }


    @Override
    public ViewHolder onCreateViewHolder( ViewGroup viewGroup, int viewType) {

        //create a new view
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_item,viewGroup,false);

        return new ViewHolder(v);
    }

    //replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder( ViewHolder viewholder, final int position) {
        viewholder.getName().setText( mDataSet_name[position]);
        viewholder.getPhone().setText(mDataSet_phone[position]);
        viewholder.getAvatar().setImageResource(R.drawable.avatar);

    }

    //return the size of your dataset(invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataSet_name.length;
    }

    public void setOnViewItemClickListener(OnItemClickListener onViewItemClickListener){
       mItemClickListener = onViewItemClickListener;
    }
    public static interface OnItemClickListener {
        void onItemClick(View view,String name,int position);
        void onItemLongClick(View view,String name,int position);
    }

}
