package com.example.todolist;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todolist.Adapter.ToDoAdapter;
import com.example.todolist.Model.ToDoModel;


public class Recycleritemtouchhelper extends ItemTouchHelper.SimpleCallback {

    public ToDoAdapter adapter;


    public Recycleritemtouchhelper(ToDoAdapter adapter){
        super(0,ItemTouchHelper.LEFT| ItemTouchHelper.RIGHT);
        this.adapter=adapter;
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        final int position = viewHolder.getAdapterPosition();
        if(direction == ItemTouchHelper.LEFT) {
            AlertDialog.Builder builder = new AlertDialog.Builder(adapter.getContext());
            builder.setTitle("Delete Task");
            builder.setMessage("Are you sure you want to Delete task");
            builder.setPositiveButton("Conform",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                        adapter.deleteItem(position);

                        }
                    });
            builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    adapter.notifyItemChanged(viewHolder.getAdapterPosition());
                }
            });
            AlertDialog dialog =builder.create();
            dialog.show();
        }
        else{
            adapter.editItem(position,adapter);
        }
        }
       public void onChildDraw(Canvas c, RecyclerView recyclerView
       ,RecyclerView.ViewHolder viewHolder,float dx,float dy , int actionstate,boolean isCorrectyActive){
            super.onChildDraw(c,recyclerView,viewHolder,dx,dy,actionstate,isCorrectyActive);
           Drawable icon;
           ColorDrawable background;
           View itemView = viewHolder.itemView;
           int backgroundCoreneroffser =20;
           if(dx >0) {
               icon = ContextCompat.getDrawable(adapter.getContext(),R.drawable.baseline_edit);
               background = new ColorDrawable(Color.BLUE);
           }
           else {
               icon = ContextCompat.getDrawable(adapter.getContext(),R.drawable.baseline_delete_24);
               background = new ColorDrawable(Color.RED);
           }
           int iconmargin = (itemView.getHeight()-icon.getIntrinsicHeight())/2;
           int icontop =(itemView.getTop()+itemView.getHeight()-icon.getIntrinsicHeight())/2;
           int iconbottom= icontop +icon.getIntrinsicHeight();

          if(dx > 0){//swipping to right
                int iconleft =itemView.getLeft()+iconmargin;
                int iconright = itemView.getLeft()+iconmargin+icon.getIntrinsicWidth();
                icon.setBounds(iconleft,icontop,iconright,iconbottom);
                background.setBounds(itemView.getLeft(),itemView.getTop(),itemView.getLeft()+((int)dx)+backgroundCoreneroffser,itemView.getBottom());
          } if (dx < 0) {
               int iconLeft = itemView.getRight() - iconmargin - icon.getIntrinsicWidth();
               int iconRight = itemView.getRight() - iconmargin;
               icon.setBounds(iconLeft, icontop, iconRight, iconbottom);
               background.setBounds(itemView.getRight() + ((int) dx) - backgroundCoreneroffser, itemView.getTop(), itemView.getRight(), itemView.getBottom());

          }else{
              background.setBounds(0,0,0,0);
          }

          background.draw(c);
           icon.draw(c);
       }


       }




