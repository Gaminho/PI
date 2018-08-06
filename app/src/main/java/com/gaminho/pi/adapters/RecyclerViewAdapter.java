package com.gaminho.pi.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gaminho.pi.R;
import com.gaminho.pi.activities.ListItemFragment;
import com.gaminho.pi.beans.Course;
import com.gaminho.pi.beans.Pupil;

import java.util.Date;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private final List mValues;
    private final ListItemFragment.ListItemListener mListener;

    public RecyclerViewAdapter(List mValues, ListItemFragment.ListItemListener mListener) {
        this.mValues = mValues;
        this.mListener = mListener;
    }

    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_item, parent, false);
        return new RecyclerViewAdapter.ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(final RecyclerViewAdapter.ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        if (holder.mItem instanceof Pupil){
            Pupil p = (Pupil) mValues.get(position);
            holder.mItemTitle.setText(p.getFirstname());
            holder.mItemDetails.setText(p.getLastname());
            if(p.getID() != null){
                holder.mItemDetails.append(p.getID());
            }
        } else if (holder.mItem instanceof Course){
            Course c = (Course)mValues.get(position);
            holder.mItemTitle.setText(c.getPupil() != null ? c.getPupil().getFirstname() : c.getPupilId());
            holder.mItemDetails.setText(new Date(c.getDate()).toString());
        }

        holder.mView.setOnClickListener(v -> {
            if (null != mListener) {
                // Notify the active callbacks interface (the activity, if the
                // fragment is attached to one) that an item has been selected.
                mListener.selectItem(holder.mItem);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        final View mView;
        final TextView mItemTitle;
        final TextView mItemDetails;
        Object mItem;

        ViewHolder(View view) {
            super(view);
            mView = view;
            mItemTitle = view.findViewById(R.id.item_title);
            mItemDetails = view.findViewById(R.id.item_details);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mItemDetails.getText() + "'";
        }
    }
}
