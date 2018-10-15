package com.udrumobile.foodapplication.owner.dialogstatus;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.udrumobile.foodapplication.R;

/**
 * @author Nikita Olifer
 */
class PagerAdapter extends android.support.v4.view.PagerAdapter {
    private int pageCount;
    public String[] txt_val={"รอดำเนินการ","กำลังทำ","เสร็จแล้ว","ยกเลิก"};
    PagerAdapter(int pageCount) {
        this.pageCount = pageCount;
    }

    PagerAdapter() {

    }

    void setCount(int count) {
        this.pageCount = count;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup collection, int position) {
        ViewGroup layout = (ViewGroup) LayoutInflater.from(collection.getContext())
                .inflate(R.layout.scrolling_page, collection, false);
        TextView label = layout.findViewById(R.id.page_label);
        CardView cardView =(CardView) layout.findViewById(R.id.card);


        label.setTextColor(Color.WHITE);

        if(position==0){
            label.setText(txt_val[position]);
            cardView.setCardBackgroundColor(Color.parseColor("#FF530D"));
        }
        else if(position==1){
            label.setText(txt_val[position]);
            cardView.setCardBackgroundColor(Color.parseColor("#67CDFF"));
        }
        else if(position==2){
            label.setText(txt_val[position]);
            cardView.setCardBackgroundColor(Color.parseColor("#0DFF4D"));
        }
        else if(position==3){
            label.setText(txt_val[position]);
            cardView.setCardBackgroundColor(Color.parseColor("#D84248"));
        }
        else {

        }


        collection.addView(layout);
        return layout;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup collection, int position, @NonNull Object view) {
        collection.removeView((View) view);
    }

    @Override
    public int getCount() {
        return pageCount;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return android.support.v4.view.PagerAdapter.POSITION_NONE;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return String.valueOf(position);
    }
}
