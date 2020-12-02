package com.example.todo;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;


public class all_data_fragment extends Fragment implements View.OnClickListener {
RecyclerView recyclerView;
FloatingActionButton add_task_btn; // button to add task name
ArrayList<todo_type> arrayList;  // store task related data
NavController navController=null;  // used to navigate fragment

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    navController= Navigation.findNavController(view);
    add_task_btn=view.findViewById(R.id.add_task_btn);
    add_task_btn.setOnClickListener(this::onClick);
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_all_data_fragment, container, false);
        recyclerView=v.findViewById(R.id.todo_recycler);
        arrayList=getAlldata();
    //    Log.e("update","getdata");
        recycler_adapter adapter=new recycler_adapter(arrayList,getContext(),v);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        return v ;
    }

    private ArrayList<todo_type>getAlldata() {
       return utils.getAll_work_details();
    }

    @Override
    public void onClick(View v) {
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
}