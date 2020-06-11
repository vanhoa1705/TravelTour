package com.example.yourtour;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class ListMemberAdapter extends ArrayAdapter<Member> {
    public Context _context;
    public int _resource;
    public ArrayList<Member> _members;

    public ListMemberAdapter(Context context, int resource, ArrayList<Member> _members) {
        super(context, resource, _members);
        this._context = context;
        this._resource = resource;
        this._members = _members;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null)
        {
            LayoutInflater layoutInflater =  LayoutInflater.from(this._context);
            convertView = layoutInflater.inflate(_resource, parent, false);
        }
        TextView txtName = convertView.findViewById(R.id.cmb_Name);
        TextView txtId = convertView.findViewById(R.id.mb_id);
        TextView txtPhone = convertView.findViewById(R.id.mb_phone);

        Member item = _members.get(position);
        txtName.setText(item.mb_name);
        txtId.setText("ID: " + item.mb_id);
        txtPhone.setText("Phone: " + item.mb_phone);

        return convertView;
    }

    @Override
    public int getCount() {
        return _members.size();
    }
}

