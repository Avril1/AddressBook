package com.example.addressbook.adapter;

import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.addressbook.R;

public class CustomAdapterNew extends RecyclerView.Adapter<CustomAdapterNew.ViewHolder>{
    private Cursor cursor;

    public CustomAdapterNew(){

    }
    public CustomAdapterNew (Cursor cursor){
        this.cursor = cursor;
    }
    CustomAdapterNew.OnItemClickListener mItemClickListener = new CustomAdapterNew.OnItemClickListener() {
        @Override
        public void onItemClick(View view, String name, int position) { }

        @Override
        public void onItemLongClick(View view,String name,int position) { }
    };


    //provide a reference to the type of views that you are using
    public   class ViewHolder extends RecyclerView.ViewHolder{
        private TextView name_tv;
        private TextView phone_tv;

        public ViewHolder(View v){
            super(v);

            name_tv = (TextView) v.findViewById(R.id.textView);
            phone_tv = (TextView) v.findViewById(R.id.textView2);
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
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        //create a new view
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_item_new,viewGroup,false);

        return new ViewHolder(v);
    }

    //replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(CustomAdapterNew.ViewHolder viewholder, final int position) {
        if(cursor.moveToFirst() ) {
            viewholder.getName().setText(cursor.getString(1));
            viewholder.getPhone().setText(cursor.getString(3));
        }
    }

    //return the size of your dataset(invoked by the layout manager)
    @Override
    public int getItemCount() {
        return cursor.getCount();
    }

    public void setOnViewItemClickListener(CustomAdapterNew.OnItemClickListener onViewItemClickListener){
        mItemClickListener = onViewItemClickListener;
    }
    public static interface OnItemClickListener {
        void onItemClick(View view,String name,int position);
        void onItemLongClick(View view,String name,int position);
    }
}
