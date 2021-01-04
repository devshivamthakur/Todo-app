package com.example.todo;

import android.content.Context;

import android.os.Bundle;

import android.view.LayoutInflater;

import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;

import java.util.ArrayList;
import static com.example.todo.nav_activity.c;


public class recycler_adapter extends RecyclerView.Adapter<recycler_adapter.ViewHolder> implements Filterable {
    ArrayList<todo_type>todo_data_arraylist;
    ArrayList<todo_type>c_todo_data_arraylist;  // copy of task data
Context context;
View v;  // to navigate after any  operation (delete, update)
    NavController navController=null;
    LottieAnimationView lottieAnimationView;
Thread t;
RelativeLayout relativeLayout;

    public recycler_adapter(ArrayList<todo_type> todo_data_arraylist, Context context, View v, LottieAnimationView lottieAnimationView, RelativeLayout relativeLayout) {
        this.todo_data_arraylist = todo_data_arraylist;
        this.context = context;
        this.v=v;
        this.lottieAnimationView=lottieAnimationView;
        this.relativeLayout=relativeLayout;
        this.c_todo_data_arraylist=new ArrayList<>(todo_data_arraylist);
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
                                    /*
                                    * TODO: save btn: whenever click save on any task if you do any changes or not.
                                    *  it will simply update values in data base.
                                    * and after updating values, updated value will be added in adapter related array list.
                                    * and . checking the task is completed or not. if completed than task added in completed task.
                                    * if the task is not completed (suppose after change it value) the if task is available in completed than remove it from completed task list.
                                    *  */
                                    boolean result_of_cb=holder.checkBox.isChecked();
                                    String temp_wn;
                                        temp_wn=    holder.work_name.getText().toString();


                                    if(utils.getInstance(c).update_data(new todo_type(temp_wn,result_of_cb))){
                                        /* todo:
                                            to update
                                             data*/
                                        int pos=position;
                                        c_todo_data_arraylist.remove(todo_data_arraylist.get(position));
                                        todo_data_arraylist.remove(position);
                                        todo_data_arraylist.add(pos,new todo_type(temp_wn,result_of_cb));
                                        c_todo_data_arraylist.add(pos,new todo_type(temp_wn,result_of_cb));
                                        Toast.makeText(context,String.valueOf(pos),Toast.LENGTH_LONG).show();
                                        notifyItemChanged(position);
                                        if(result_of_cb){
                                            utils.setCompleted_task(todo_data_arraylist.get(position));
                                        //    Log.e("test5","passed");
                                        }else {
                                            utils.remove_from_completed_task(todo_data_arraylist.get(position));
                                          //  Log.e("test5","removed");
                                        }
                                        Toast.makeText(context,"updated Successfully",Toast.LENGTH_LONG).show();
                                    }else {
                                        Toast.makeText(context,"not updated",Toast.LENGTH_LONG).show();
                                          }
                                    break;
                                case R.id.delete:
                                    try {
                                     //   Log.e("pos", String.valueOf(position));
                                        if(utils.getInstance(c).remove_from_todo_list(todo_data_arraylist.get(position))){
                                            boolean result_of_cb1=holder.checkBox.isChecked();
                                            if(result_of_cb1){
                                                utils.remove_from_completed_task(todo_data_arraylist.get(position));
                                            }
                                            /* TODO: remove data form recycler View*/
                                            int index=position;
                                            todo_data_arraylist.remove(position);
                                            c_todo_data_arraylist.remove(position);
                                            notifyItemRemoved(position);
                                              notifyDataSetChanged();
                                        }
                                        else {
                                            Toast.makeText(context,"not Deleted",Toast.LENGTH_LONG).show();
                                        }
                                    }catch (Exception e){

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
    }

    @Override
    public int getItemCount() {
        if(todo_data_arraylist!=null){
            if(!todo_data_arraylist.isEmpty()){
                lottieAnimationView.setVisibility(View.GONE);
                relativeLayout.setBackgroundResource(R.color.white);
            }
            if(todo_data_arraylist.isEmpty()){
                lottieAnimationView.setVisibility(View.VISIBLE);
                relativeLayout.setBackgroundResource(R.drawable.bg);
              //  Log.e("pass","emp2");
            }

           // Log.e("pass","emp");
            return todo_data_arraylist.size();
        }
            return -1;
    }

    /* this
    section used for searching
    * */
    Filter filter=new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
//            Toast.makeText(context,constraint,Toast.LENGTH_SHORT).show();
            ArrayList<todo_type>temp=new ArrayList<>();
            if(constraint==null||constraint.length()==0){
                temp.addAll(c_todo_data_arraylist);
            }
            else {
                String search_text=constraint.toString().toLowerCase();
                Toast.makeText(context,search_text+" form 1",Toast.LENGTH_SHORT).show();
                for(todo_type t:c_todo_data_arraylist){
                    if((t.work_name.toLowerCase()).contains(search_text)){
                        Toast.makeText(context,"yes",Toast.LENGTH_SHORT).show();
                        temp.add(t);
                    }
                }
            }
            FilterResults filterResults=new FilterResults();
            filterResults.values=temp;
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            Toast.makeText(context,constraint,Toast.LENGTH_SHORT).show();
               todo_data_arraylist.clear();
               todo_data_arraylist.addAll((ArrayList<todo_type>)results.values);
               notifyDataSetChanged();
        }
    };
    @Override
    public Filter getFilter() {
        return filter;
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
