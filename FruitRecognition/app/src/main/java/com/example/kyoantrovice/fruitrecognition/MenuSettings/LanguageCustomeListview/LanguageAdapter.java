package com.example.kyoantrovice.fruitrecognition.MenuSettings.LanguageCustomeListview;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kyoantrovice.fruitrecognition.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by KyoAntrovice on 4/16/2016.
 */
public class LanguageAdapter extends ArrayAdapter<LanguageTable> {
    Context context;

    public LanguageAdapter(Context context,int resourceId, List<LanguageTable> items){
        super(context,resourceId,items);
        this.context = context;
    }

    public class ViewHolder {
        ImageView imageView;
        TextView nationName;
    }

    public View getView(int position, View convertView, ViewGroup parent){
        ViewHolder holder = null;
        LanguageTable languageTable = getItem(position);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if(convertView == null){
            convertView = inflater.inflate(R.layout.list_item_language,null);
            holder = new ViewHolder();
            holder.nationName = (TextView) convertView.findViewById(R.id.nationName);
            holder.imageView = (ImageView) convertView.findViewById(R.id.icon);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.nationName.setText(languageTable.getNation());
        holder.imageView.setImageResource(languageTable.getImageID());
        return convertView;
    }
}
