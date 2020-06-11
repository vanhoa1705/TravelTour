package com.example.yourtour;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class ListCommentAdapter extends ArrayAdapter<Comment> {
    public Context _context;
    public int _resource;
    public ArrayList<Comment> _comments;

    public ListCommentAdapter(Context context, int resource, ArrayList<Comment> comments) {
        super(context, resource, comments);
        this._context = context;
        this._resource = resource;
        this._comments = comments;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null)
        {
            LayoutInflater layoutInflater =  LayoutInflater.from(this._context);
            convertView = layoutInflater.inflate(_resource, parent, false);
        }
        TextView txtName = convertView.findViewById(R.id.comment_name);
        TextView txtContent = convertView.findViewById(R.id.comment_content);


        Comment item = _comments.get(position);
        txtName.setText(item.cmt_name + ":");
        txtContent.setText( item.comment);

        return convertView;
    }

    @Override
    public int getCount() {
        return _comments.size();
    }

}

