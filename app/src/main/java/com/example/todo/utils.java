package com.example.todo;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class utils {
    public  static final String work_name_key="WORK NAME KEY";
    public  static final String completed_task_key="Completed NAME KEY";
    static ArrayList<todo_type>all_work_details;



    static ArrayList<todo_type> completed_task;


    static SharedPreferences sharedPreferences;
    private static utils instance;
    static Context context;

    public utils(Context context) {
        sharedPreferences=context.getSharedPreferences("todo_data_base",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        Gson gson=new Gson();
        if(getAll_work_details()==null){
            editor.putString(work_name_key,gson.toJson(new ArrayList<todo_type>()));
            editor.commit();
        }

    }

    public static utils getInstance(Context context) {
        if(null!=instance){
            return instance;
        }else {
            instance=new utils(context);
            return instance;
        }
    }

    public static void setInstance(utils instance) {
        utils.instance = instance;
    }



    public static ArrayList<todo_type> getAll_work_details()
    {
        Gson gson=new Gson();
        Type  type=new TypeToken<ArrayList<todo_type>>(){}.getType();
        if(sharedPreferences!=null)
        {
            Log.e("pass1","not null");
            all_work_details=gson.fromJson(sharedPreferences.getString(work_name_key,null),type);
            return all_work_details;
        }
        return null;
    }
    public boolean add_data(todo_type t){
        ArrayList<todo_type> todo_typeArrayList=getAll_work_details();
        if(null!=t){
            if(todo_typeArrayList.add(t)){
                Gson gson=new Gson();
                SharedPreferences.Editor editor=sharedPreferences.edit();
                editor.remove(work_name_key);
                editor.putString(work_name_key,gson.toJson(todo_typeArrayList));
                editor.commit();
                return true;
            }
        }
        return false;
    }
    public boolean update_data(todo_type t,int position){
        ArrayList<todo_type> todo_typeArrayList=getAll_work_details();
        if(null!=t){
            todo_typeArrayList.get(position).setCheck(t.check);
            todo_typeArrayList.get(position).setWork_name(t.work_name);
            SharedPreferences.Editor editor=sharedPreferences.edit();
            editor.remove(work_name_key);
            Gson g=new Gson();
            editor.putString(work_name_key,g.toJson(todo_typeArrayList));
            editor.commit();
            return true;
        }
        return false;
    }
    public boolean remove_from_todo_list(todo_type t)
    {
        ArrayList<todo_type>todo_typeArrayList=getAll_work_details();
        if(t!=null){
            for(todo_type temp_O:todo_typeArrayList){
                if(temp_O.getWork_name().equals(t.getWork_name())){
                    if(todo_typeArrayList.remove(temp_O)){
                        Gson gson=new Gson();
                        SharedPreferences.Editor editor=sharedPreferences.edit();
                        editor.remove(work_name_key);
                        editor.putString(work_name_key,gson.toJson(todo_typeArrayList));
                        editor.commit();
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static void setAll_work_details(ArrayList<todo_type> all_work_details) {
        utils.all_work_details = all_work_details;
    }
    public static ArrayList<todo_type> getCompleted_task() {
        Gson gson=new Gson();
        Type type=new TypeToken<ArrayList<todo_type>>(){}.getType();
        if(sharedPreferences!=null)
        {
            completed_task=gson.fromJson(sharedPreferences.getString(work_name_key,null),type);
            Log.e("pass","c21");

            return completed_task;
        }
        return null;
    }

    public static boolean setCompleted_task(ArrayList<todo_type> completed_task1) {
        if(sharedPreferences!=null){
            SharedPreferences.Editor editor=sharedPreferences.edit();
            editor.remove(completed_task_key);
            Gson gson=new Gson();
            editor.putString(completed_task_key,gson.toJson(completed_task1));
            editor.commit();
            Log.e("pass","c2");
            return true;
        }


        return false;
    }
}
