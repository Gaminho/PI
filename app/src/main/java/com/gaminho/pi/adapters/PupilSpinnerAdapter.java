package com.gaminho.pi.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.gaminho.pi.beans.Pupil;

import java.util.List;
import java.util.stream.Collectors;

public class PupilSpinnerAdapter extends ArrayAdapter {

    private List<Pupil> mPupils;

    public PupilSpinnerAdapter(@NonNull Context context, int resource, @NonNull List<Pupil> pupils) {
        super(context, resource,
                pupils.stream().map(
                        p-> String.format("%s %s", p.getFirstname(), p.getLastname())
                ).collect(Collectors.toList()));
        mPupils = pupils;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return super.getView(position, convertView, parent);
    }

    public List<Pupil> getPupils() {
        return mPupils;
    }
}
