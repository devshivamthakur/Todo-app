package com.example.todo;

import android.animation.Animator;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
    relativeLayout=view.findViewById(R.id.relative_layout);
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
        lottieAnimationView.addAnimatorListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
           relativeLayout.setBackgroundColor(getResources().getColor(R.color.black));
            }

            @Override
            public void onAnimationEnd(Animator animation) {

            }

            @Override
            public void onAnimationCancel(Animator animation) {
                relativeLayout.setBackgroundColor(getResources().getColor(R.color.white));
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        arrayList=getAlldata();
        adapter=new recycler_adapter(arrayList,getContext(),v,lottieAnimationView);
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

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id=item.getItemId();
        switch (id){
            case R.id.hide_menu:
                ArrayList<todo_type>temp=new ArrayList<>();
                //  ArrayList<todo_type>store_completed_task=new ArrayList<>();
                for(todo_type t:arrayList){
                    if(!t.isCheck()){
                        temp.add(t);
                    }
                }
                utils.setCompleted_task(temp);
                arrayList.clear();
                arrayList.addAll(temp);
                adapter.notifyDataSetChanged();
                break;
            case R.id.show:
                arrayList.clear();
                arrayList.addAll(getAlldata());
                adapter.notifyDataSetChanged();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}