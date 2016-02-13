package com.parse.starter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin on 07/09/2015.
 */
public class ChatArrayAdapter extends ArrayAdapter<Message> {
    private RelativeLayout rel;
    private TextView MessageTXT;
    private TextView MessageDATE;
    private List<Message> MessageList = new ArrayList<Message>();
    private LinearLayout layout;

    public ChatArrayAdapter(Context context, int chat) {
        super(context, chat);
    }
    public ChatArrayAdapter(Context context, int chat, Message[] objectd) {
        super(context, chat, objectd);
    }
    public void add(Message object) {
        MessageList.add(object);
        super.add(object);
    }
    public int getCount() {
        return this.MessageList.size();
    }
    public Message getItem(int index) {
        return this.MessageList.get(index);
    }
    public View getView(int position, View ConvertView, ViewGroup perent) {
        View v = ConvertView;
        if (v == null) {
            LayoutInflater infleter = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = infleter.inflate(R.layout.chat_single_message, perent, false);
        }
        rel=(RelativeLayout)v.findViewById(R.id.MessageREL);
        layout = (LinearLayout) v.findViewById(R.id.SingeleMessage);
        Message Messageobj = getItem(position);
        MessageTXT=(TextView)v.findViewById(R.id.SM_TextMessage);
        MessageTXT.setText(Messageobj.getMessage());
        MessageDATE=(TextView)v.findViewById(R.id.SM_DateMessage);
        MessageDATE.setText(Messageobj.getDate());
        rel.setBackgroundResource(Messageobj.Side ?R.drawable.rigth2 : R.drawable.left);
        layout.setGravity(Messageobj.Side ? Gravity.LEFT : Gravity.RIGHT);//message side
        return v;
    }
    public Bitmap decodeToBitmap(byte[] decodedbyte){
        return BitmapFactory.decodeByteArray(decodedbyte, 0, decodedbyte.length);
    }
}
