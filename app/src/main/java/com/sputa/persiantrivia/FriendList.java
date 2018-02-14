package com.sputa.persiantrivia;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.File;

public class FriendList extends BaseAdapter{
    String [] user_name;
    String [] user_desc;
    String [] user_level;
    String [] user_gender;
    String [] user_avatar_id;
    Activity context;
    int [] imageId;
    SQLiteDatabase mydatabase;
    String
            super_activity="";
    private static LayoutInflater inflater=null;

    public FriendList(Activity mainActivity, String[] User_name, String[] User_desc, String[] User_level,String[] User_gender, String[] User_avatar_id, SQLiteDatabase db,String Super_activity) {
        // TODO Auto-generated constructor stub
        user_name=User_name;
        user_desc = User_desc;
        user_level = User_level;
        user_gender=User_gender;
        user_avatar_id=User_avatar_id;


        super_activity=Super_activity;

        context = mainActivity;

        inflater = ( LayoutInflater )context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mydatabase = db;
    }
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return user_name.length;
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    public class Holder
    {
        TextView tv;
        TextView txt_guild_group;


    }
    long diff=0;
    @Override
    public View getView(final int position, final View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        Holder holder=new Holder();
        final View rowView;
        rowView = inflater.inflate(R.layout.program_list, null);
        TextView tv=(TextView) rowView.findViewById(R.id.lbl_name);
        TextView txt_level=(TextView) rowView.findViewById(R.id.lbl_level);
        TextView txt_desc=(TextView) rowView.findViewById(R.id.lbl_desc);



        //  holder.txt_guild_group=(TextView) rowView.findViewById(R.id.textView3);
//
//
//
//
        tv.setText(user_name[position]);
        txt_desc.setText(user_desc[position]);
        txt_level.setText(user_level[position]);

        ImageView image1 = (ImageView) rowView.findViewById(R.id.img_avatar);

        if(user_gender[position].equals("1"))
        {
            image1.setBackground(rowView.getResources().getDrawable(R.drawable.profile_mail));

        }
        else
        {
            image1.setBackground(rowView.getResources().getDrawable(R.drawable.profile_female));

        }



        // Image url
        String
                image_url = "http://sputa-app.ir/app/trivia/pic/"+user_name[position]+"_"+user_avatar_id[position]+".jpg";
     //   Toast.makeText(context,image_url,Toast.LENGTH_LONG).show();

        Picasso.with(context).load(image_url).fit().into(image1);




//        holder.txt_guild_group.setText(guild_group[position]);

//        String
//                dir="";
//        dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/VisitCards/"+card_id[position]+"/1.jpg";
//        File imgFile = new  File(dir);
//        holder.img.setImageResource(R.mipmap.bag_icon);
//        if(imgFile.exists()){
//
//            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
//
//           // ImageView myImage = (ImageView) findViewById(R.id.imageviewTest);
//
//            holder.img.setImageBitmap(myBitmap);
//
//        }


    rowView.setOnTouchListener(new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                // change color
                diff =System.currentTimeMillis();
             //   Log.d("majid","down");

                //textV.setTextColor(Color.rgb(200, 0, 0));

            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                //Toast.makeText(getBaseContext(),"Up",Toast.LENGTH_SHORT).show();
                //textV.setTextColor(Color.rgb(255, 255, 0));
                // set to normal color

                // Intent i = new Intent(MainActivity.this, NewCardDetail.class);

                // startActivity(i);
                long
                    diff_up=System.currentTimeMillis();
                diff = diff_up-diff;
             //   Log.d("majid",String.valueOf(diff));
                if(diff<400) {
//                    Intent i = new Intent(context, NewCardDetail.class);
//                    i.putExtra("card_id", card_id[position]);
//
//                    context.startActivity(i);
                    if(super_activity.equals("friend_list")) {
                        friend_list fl;
                        fl = (friend_list) context;
                        fl.select_friend(user_name[position]);
                    }
                    else if(super_activity.equals("look_around"))
                    {
                        LookAround fl;
                        fl = (LookAround) context;
                        fl.select_friend(user_name[position]);
                    }
                    else if(super_activity.equals("request_game"))
                    {
                        Request_game fl;
                        fl = (Request_game) context;
                        fl.select_friend(user_name[position]);
                    }
                }
                else {
//                    PopupMenu popup = new PopupMenu(context,  rowView);
//                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
//                        public boolean onMenuItemClick(MenuItem item) {
//                            Cursor resultSet = mydatabase.rawQuery("Select * from " + context.getResources().getString(R.string.table_name), null);
//                            int
//                                    record_count = resultSet.getCount();
//                         //   Log.d("majid",String.valueOf(record_count));
//                            new AlertDialog.Builder(context)
//                                    .setTitle(context.getResources().getString(R.string.delete))
//                                    .setMessage(context.getResources().getString(R.string.msg_sure_delete))
//                                    .setNegativeButton(context.getResources().getString(R.string.no), null)
//                                    .setPositiveButton(context.getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
//
//                                        public void onClick(DialogInterface arg0, int arg1) {
//                                            String
//                                                    dir="";
//                                            dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/VisitCards/"+String.valueOf(card_id[position])+"/";
//                                            String file = dir+"1.jpg";
//                                            File newfile = new File(file);
//                                            newfile.delete();
//                                            mydatabase.execSQL("delete from " + context.getResources().getString(R.string.table_name) + " where id='" + card_id[position] + "'");
//                                            Toast.makeText(context,context.getResources().getString(R.string.delete_msg),Toast.LENGTH_SHORT).show();
//                                        }
//                                    }).create().show();
//                            if(item.getItemId()==R.id.mnu_delete) {
//
//
//                            }

                        //    return true;
//                        }
//                    });
//
//
//                    popup.getMenuInflater().inflate(R.menu.my_menu, popup.getMenu());
//                    popup.show();
                }
            }

            return true;
        }
    });


//            rowView.setOnClickListener(new OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    // TODO Auto-generated method stub
//                    //Toast.makeText(context, "You Clicked "+card_id[position], Toast.LENGTH_SHORT).show();
//
//
//

//
//
////                View menuItemView = rowView.findViewById(R.id.action_menu_divider); // SAME ID AS MENU ID
////                PopupMenu popupMenu = new PopupMenu(context, menuItemView);
////                popupMenu.inflate(R.menu.menu_main);
////                // ...
////                popupMenu.show();
//                }
//            });
        return rowView;
    }

}