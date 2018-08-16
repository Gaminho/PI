package com.gaminho.pi.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gaminho.pi.R;
import com.gaminho.pi.adapters.RecyclerViewAdapter;

import java.util.Locale;

public class ListItemFragment extends FirebaseFragment {

    private static final String ARG_ITEM_TYPE = "item-type";
    public static final int LIST_PUPIL = 1;
    public static final int LIST_COURSE = 2;
    private int mItemType = LIST_PUPIL;

    private TextView mTVCounter;
    private ListItemListener mListListener;

    private RecyclerViewAdapter mAdapter;


    public ListItemFragment() {
    }

    public static ListItemFragment newInstance(int itemType) {
        ListItemFragment fragment = new ListItemFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_ITEM_TYPE, itemType);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mItemType = getArguments().getInt(ARG_ITEM_TYPE);
        }

        mAdapter = new RecyclerViewAdapter(mListItems, mListListener);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View mRoot = inflater.inflate(R.layout.list_layout, container, false);
        mTVCounter = mRoot.findViewById(R.id.tv_counter);

        RecyclerView mListViewItem = mRoot.findViewById(R.id.list_items);
        mListViewItem.setLayoutManager(new LinearLayoutManager(mListViewItem.getContext()));
        mListViewItem.setAdapter(mAdapter);

        mRoot.findViewById(R.id.fab_add_item).setOnClickListener(v -> {
            if(this.mListener != null){
                mListener.addItem(this.mItemType);
            }
        });

        return mRoot;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ListItemListener) {
            mListListener = (ListItemListener) context;
        } else {
            mListListener = null;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListListener = null;
    }

    private void updateTextView(String pMsg) {
        if (mTVCounter != null) {
            mTVCounter.setText(pMsg);
        }
    }

    @Override
    protected void refreshView() {
        mListItems.clear();
        mListItems.addAll(mListener.getItems(mItemType));

        updateTextView(String.format(Locale.FRANCE, "%d items found.", mListItems.size()));
        mAdapter.notifyDataSetChanged();
    }

    public interface ListItemListener {
        void selectItem(Object pItem);
    }
}
