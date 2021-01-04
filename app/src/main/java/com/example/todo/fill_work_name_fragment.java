package com.example.todo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import static com.example.todo.MainActivity.c;

public class fill_work_name_fragment extends Fragment {
    boolean status;   // TODO: it is used to check data will edit or save. it used to when the data comes for edit or update
    boolean checked_task_status; ///todo: it means the task is completed or not. this will be also for update data
    String data="";  //todo: it is  task name
    int position; // it is position of task name. which position data we want update
    EditText get_tast_name;  //
    FloatingActionButton save_data_btn; // save button after enter a task
    NavController navController=null;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController= Navigation.findNavController(view); // initialize nav controller
        /**
         * Todo: getArgument() used to when data pass between fragment , when its navigated

       */
   data=getArguments().getString("task_name");
    if(!data.isEmpty()){
        get_tast_name.setText(data);
        checked_task_status=getArguments().getBoolean("completed_or _not");
        position=getArguments().getInt("pos");
        status=true;
    }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fill_work_name,container,false);
        get_tast_name=v.findViewById(R.id.entered_work_name);
        save_data_btn=v.findViewById(R.id.save_after_fill_data);
        save_data_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tast_name=get_tast_name.getText().toString();
                if(!tast_name.isEmpty()){

                    if(status){         // this block will run when the data comes for update
                        data=get_tast_name.getText().toString();
                     if(utils.getInstance(c).update_data(new todo_type(data,checked_task_status))){
//                         Log.e("update", "  "+String.valueOf(position));
                         Toast.makeText(getContext(),"updated successfully",Toast.LENGTH_SHORT).show();
                     }else {
                         Toast.makeText(getContext()," not updated ",Toast.LENGTH_SHORT).show();
                     }

                        status=false;
                    }
                    else {                    // this block will run when the data comes for first time (new task name)
                        if(utils.getInstance(c).add_data(new todo_type(tast_name,false))){      // save on device
                            Toast.makeText(getContext(),"added successfully",Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(getContext()," not added ",Toast.LENGTH_SHORT).show();
                        }
                    }

                }else {
                    Toast.makeText(getContext(),"you want  to save empty data, so data  will not  added",Toast.LENGTH_SHORT).show();
                }

                navController.navigate(R.id.action_fill_work_name_fragment_to_all_data_fragment); // after save go back in all data fragment
            }
        });
        return v;
    }
}
