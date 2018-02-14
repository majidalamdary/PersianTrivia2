package com.sputa.persiantrivia;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class Question_adapter extends BaseAdapter{
    String [] subject;
    String [] question;
    String [] desc;

    String [] q_id;

    Activity context;
    int [] imageId;
    SQLiteDatabase mydatabase;
    private static LayoutInflater inflater=null;
    int screenWidth  = 0;
    int screenHeight = 0;
    String
            font_name = "";
    Typeface tf;
    functions fun;
    String
        List_type="";
    public Question_adapter(Activity mainActivity, String[] Subject, String[] Question, String[] Desc, String[] Q_id, SQLiteDatabase db, String list_type) {
        // TODO Auto-generated constructor stub
        subject= Subject;
        question = Question;
        q_id=Q_id;
        desc=Desc;

        List_type=list_type;

        context=mainActivity;

        inflater = ( LayoutInflater )context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mydatabase = db;


        //et_guild_name = (EditText) findViewById(R.id.txt_guild_name);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE); // the results will be higher than using the activity context object or the getWindowManager() shortcut
        wm.getDefaultDisplay().getMetrics(displayMetrics);

        screenWidth = displayMetrics.widthPixels;
        screenHeight = displayMetrics.heightPixels;

        fun = new functions();



        font_name = fun.font_name;
        tf = Typeface.createFromAsset(context.getAssets(),font_name );
        Holder holder=new Holder();
        final View rowView;
        rowView = inflater.inflate(R.layout.qestion_adapter, null);




    }
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return question.length;
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
        rowView = inflater.inflate(R.layout.qestion_adapter, null);



        TextView lbl_subject=(TextView) rowView.findViewById(R.id.lbl_subject);
        TextView lbl_question=(TextView) rowView.findViewById(R.id.lbl_question);
        TextView lbl_desc=(TextView) rowView.findViewById(R.id.lbl_desc);




        lbl_question.setText(question[position]);
        lbl_subject.setText(subject[position]);
        lbl_desc.setText(desc[position]);











        TextView lbl_rival_name = (TextView) rowView.findViewById(R.id.lbl_rival_name);


        lbl_subject.setTypeface(tf);
        lbl_subject.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (screenWidth * 0.03));


        lbl_question.setTypeface(tf);
        lbl_question.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (screenWidth * 0.041));


//        txt_level.setTypeface(tf);
//        txt_level.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (screenWidth * 0.025));


        lbl_desc.setTypeface(tf);
        lbl_desc.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (screenWidth * 0.025));

        LinearLayout lay_main = (LinearLayout) rowView.findViewById(R.id.lay_main);
//         if(List_type.equals("wait"))
//        {
            lay_main.setBackground(rowView.getResources().getDrawable(R.drawable.light_yellow_btn));
//        }
//        else  if(List_type.equals("turn"))
//         {
//             lay_main.setBackground(rowView.getResources().getDrawable(R.drawable.green_light_btn));
//         }
//         else  if(List_type.equals("done"))
//         {
//             lay_main.setBackground(rowView.getResources().getDrawable(R.drawable.light_grey_btn));
//         }














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
                   // context.finish();
                    Intent i = new Intent(context, New_question.class);
                    i.putExtra("q_id", q_id[position]);
                    //Toast.makeText(context,uig_id[position],Toast.LENGTH_SHORT).show();
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                    context.startActivity(i);
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