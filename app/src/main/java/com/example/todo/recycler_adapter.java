package com.example.todo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.navigation.NavAction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;

import java.util.ArrayList;

import static com.example.todo.MainActivity.c;


public class recycler_adapter extends RecyclerView.Adapter<recycler_adapter.ViewHolder> {
    ArrayList<todo_type>todo_data_arraylist;
Context context;
View v;  // to navigate after any  operation (delete, update)
    NavController navController=null;
    LottieAnimationView lottieAnimationView;


    public recycler_adapter(ArrayList<todo_type> todo_data_arraylist, Context context,View v,LottieAnimationView lottieAnimationView) {
        this.todo_data_arraylist = todo_data_arraylist;
        this.context = context;
        this.v=v;
        this.lottieAnimationView=lottieAnimationView;
        if(todo_data_arraylist.isEmpty()){
            Log.e("pass", String.valueOf(todo_data_arraylist));
        }

    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.todo_item,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        navController= Navigation.findNavController(v);
        if(todo_data_arraylist!=null){
            holder.checkBox.setChecked(todo_data_arraylist.get(position).isCheck());
            holder.work_name.setText(todo_data_arraylist.get(position).work_name);
            holder.action_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PopupMenu popupMenu=new PopupMenu(context,holder.action_btn);
                    popupMenu.getMenuInflater().inflate(R.menu.context_menu,popupMenu.getMenu());
                    popupMenu.show();
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            int id=item.getItemId();
                            switch (id){
                                case R.id.save:
                                    boolean result_of_cb=holder.checkBox.isChecked();

                                    String temp_wn;
                                        temp_wn=    holder.work_name.getText().toString();
                                    if(utils.getInstance(c).update_data(new todo_type(temp_wn,result_of_cb),position)){
                                        /* todo:
                                            to update
                                             data*/
                                        int pos=position;
                                        todo_data_arraylist.remove(position);
                                        todo_data_arraylist.add(pos,new todo_type(temp_wn,result_of_cb));
                                        notifyItemChanged(position);
                                        Toast.makeText(context,"updated Successfully",Toast.LENGTH_LONG).show();

                                    }else {
                                        Toast.makeText(context,"not updated",Toast.LENGTH_LONG).show();
                                          }
                                    break;
                                case R.id.delete:
                                    if(utils.getInstance(c).remove_from_todo_list(todo_data_arraylist.get(position))){
                                        /* TODO: remove data form recycler View*/
                                        int index=position;
                                        todo_data_arraylist.remove(position);
                                        notifyItemRemoved(position);

                                    }
                                    else {
                                        Toast.makeText(context,"not Deleted",Toast.LENGTH_LONG).show();
                                    }
                                    break;
                                case R.id.edit:
                                    Bundle  bundle=new Bundle();
                                    try {
                                        bundle.putString("task_name",holder.work_name.getText().toString());
                                        bundle.putBoolean("completed_or _not",holder.checkBox.isChecked());
                                        bundle.putInt("pos",position);
                                    }catch (Exception  e){

                                    }
                      navController.navigate(R.id.action_all_data_fragment_to_fill_work_name_fragment,bundle);
                                    break;

                                default:

                            }
                            return true;
                        }
                    });
                }
            });
        }
        if(todo_data_arraylist.isEmpty()){
            lottieAnimationView.setVisibility(View.VISIBLE);

        }

    }

    @Override
    public int getItemCount() {
        if(todo_data_arraylist!=null){
            if(!todo_data_arraylist.isEmpty()){
                lottieAnimationView.setVisibility(View.GONE);
            }
            if(todo_data_arraylist.isEmpty()){
                lottieAnimationView.setVisibility(View.VISIBLE);
                Log.e("pass","emp2");
            }
            Log.e("pass","emp");
            return todo_data_arraylist.size();
        }
            return -1;
    }

    class ViewHolder extends RecyclerView.ViewHolder{
            TextView  work_name;
            CheckBox checkBox;
            ImageView action_btn;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            work_name=itemView.findViewById(R.id.work_name);
            checkBox=itemView.findViewById(R.id.checkbox);
            action_btn=itemView.findViewById(R.id.action_btn);


        }
    }
}
