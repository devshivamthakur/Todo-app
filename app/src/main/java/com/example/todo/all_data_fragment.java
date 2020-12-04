package com.example.todo;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import static com.example.todo.MainActivity.c;


public class all_data_fragment extends Fragment implements View.OnClickListener  {
RecyclerView recyclerView;
FloatingActionButton add_task_btn; // button to add task name
ArrayList<todo_type> arrayList;  // transfer to  adapter
     ArrayList<todo_type> permanantly_stored = null;
NavController navController=null;  // used to navigate fragment
    recycler_adapter adapter;
    Toolbar toolbar;
Thread t;
LottieAnimationView lottieAnimationView;
RelativeLayout relativeLayout;
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    navController= Navigation.findNavController(view);
    add_task_btn=view.findViewById(R.id.add_task_btn);
    add_task_btn.setOnClickListener(this::onClick);
        super.onViewCreated(view, savedInstanceState);
    }


    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.action_menu,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_all_data_fragment, container, false);
        recyclerView=v.findViewById(R.id.todo_recycler);
        lottieAnimationView=v.findViewById(R.id.lottie_empty);
        relativeLayout=v.findViewById(R.id.relative_layout);
        try {
            arrayList=getAlldata();
        }catch (Exception e){
            Log.e("exception",e.getMessage().toUpperCase());
        }
        adapter=new recycler_adapter(arrayList,getContext(),v,lottieAnimationView,relativeLayout);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        toolbar=v.findViewById(R.id.toolbar);
        setHasOptionsMenu(true);
        toolbar.inflateMenu(R.menu.action_menu);  // to add menu in toolbar
     toolbar.setOnMenuItemClickListener(this::onOptionsItemSelected); // set click listener
        return v ;
    }

    private ArrayList<todo_type>getAlldata() {
       return utils.getAll_work_details();
    }

    @Override
    public void onClick(View v) {  // when we want add new task
        switch (v.getId()){
            case R.id.add_task_btn:
                if(navController!=null)
                {
                    Bundle bundle=new Bundle();
                    bundle.putString("task_name","");
                    navController.navigate(R.id.action_all_data_fragment_to_fill_work_name_fragment,bundle); // this fragment jump to fill work fragment
                }
                break;

        }
    }

    /*
    * Todo: 1) whenever clicked hide completed task:1) for hide we split completed and uncompleted task
    *                                              after doing this , we clear arraylist that list we passed in recycler adapter
    *                                             and add all uncompleted task in arraylist. and call method notifyDataSetChanged(). this
    *                                               method can refreshed recycler View's data.
    *                                              and set tittle=completed task
    *     2) show all task: simply first clear all data from arraylist because its have uncompleted task or any other data.
    *     so after cleared arraylist add all data (uncompleted & completed) into array list.
    *    3) delete all completed task : it wil delete  permanently all completed task from device
    *  */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id=item.getItemId();
        switch (id){
            case R.id.hide_menu:
                String tittle= String.valueOf(item.getTitle());
                ArrayList<todo_type>temp=new ArrayList<>();
                ArrayList<todo_type>check_no_of_ctask=new ArrayList<>();
                if(tittle.equals("hide all completed task")){
                    //  ArrayList<todo_type>store_completed_task=new ArrayList<>();
                    for(todo_type t:arrayList){
                        if(!t.isCheck()){
                            temp.add(t);

                        }
                    }

                    /*
                    * Todo: ctack ( completed task) : it shows how many completed task  available,
                    * */
                    int ctask=arrayList.size()-temp.size();

                    if(ctask!=0){
                        item.setTitle(getResources().getString(R.string.completed));  //set menu  item tittle
                    }

                }else if(tittle.equals("Show Only Completed Task")) {
                   temp=getCompleted_task();
                    if(temp==null){
                       temp=new ArrayList<>();
                   }else {
                       item.setTitle(getResources().getString(R.string.hide));
                   }

                }
                arrayList.clear();
                arrayList.addAll(temp);
                adapter.notifyDataSetChanged();
                break;
            case R.id.show:
                arrayList.clear();
                arrayList.addAll(getAlldata());
                adapter.notifyDataSetChanged();
                if(!arrayList.isEmpty()){
                    relativeLayout.setBackgroundColor(getResources().getColor(R.color.white));
                }
                break;
            case R.id.delete_all_completed:
                           AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
                           builder.setMessage("Do you want to delete permanently all Completed task")
                                   .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                                       @Override
                                       public void onClick(DialogInterface dialog, int which) {
                                          if(utils.getInstance(c).remove_all_completed_task(true)) {
                                              Toast.makeText(getContext(),"Successfully Deleted",Toast.LENGTH_LONG).show();
                                              ArrayList<todo_type>temp=new ArrayList<>();
                                              temp=getAlldata();
                                              Log.e("show me",String.valueOf(temp));
                                              arrayList.clear();
                                              arrayList.addAll(utils.getAll_work_details());
                                              adapter.notifyDataSetChanged();
                                          }
                                       }
                                   })
                                   .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                       @Override
                                       public void onClick(DialogInterface dialog, int which) {
                                           dialog.cancel();

                                       }
                                   });
                           builder.create().show();

                 break;
        }
        return super.onOptionsItemSelected(item);
    }
    public ArrayList<todo_type>getCompleted_task(){
        return utils.getCompleted_task();
    }


}