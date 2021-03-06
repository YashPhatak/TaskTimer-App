package com.yashphatak.tasktimer;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.security.InvalidParameterException;

public class MainActivityFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>
, CursorRecyclerViewAdapter.OnTaskClickListener{

    private static final String TAG = "MainActivityFragment";
    public static final int LOADER_ID = 0;

    private CursorRecyclerViewAdapter mAdapter;//add adapter reference


    public MainActivityFragment() {
        // Required empty public constructor
        Log.d(TAG, "MainActivityFragment: starts");
    }

    @Override
    public void onViewCreated(View view,Bundle savedInstanceState) {
        Log.d(TAG, "onViewCreated: starts");
        super.onViewCreated(view, savedInstanceState);
        //activitites containing this fragment must implement its callbacks
        
        Activity activity = getActivity();
        if(!(activity instanceof CursorRecyclerViewAdapter.OnTaskClickListener)){
            throw new ClassCastException(activity.getClass().getSimpleName()
                    + " must implement CursorRecyclerViewAdapter.OnTaskClickListener.OnSaveClicked interface" );

        }

        LoaderManager.getInstance(this).initLoader(LOADER_ID, null, this);

    }

    @Override
    public void onEditClick(Task task) {
        Log.d(TAG, "onEditClick: called");
        CursorRecyclerViewAdapter.OnTaskClickListener listener = (CursorRecyclerViewAdapter.OnTaskClickListener)getActivity();
        if(listener!=null){
            listener.onEditClick(task);

        }
        
    }

    @Override
    public void onDeleteClick(Task task) {
        Log.d(TAG, "onDeleteClick: called");
        CursorRecyclerViewAdapter.OnTaskClickListener listener = (CursorRecyclerViewAdapter.OnTaskClickListener)getActivity();
        if(listener!=null){
            listener.onDeleteClick(task);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.d(TAG, "onCreateView: starts");

        View view = inflater.inflate(R.layout.fragment_main,container,false);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.task_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        if(mAdapter==null) {
            mAdapter = new CursorRecyclerViewAdapter(null, this);
        }
        recyclerView.setAdapter(mAdapter);
        Log.d(TAG, "onCreateView: returning");

        return view;

    }

    @Override
    public void onCreate( Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: called");
        super.onCreate(savedInstanceState);

    }



    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id,  Bundle args) {
        Log.d(TAG, "onCreateLoader: starts with id "+id);
        String[] projection = {TasksContract.Columns._ID,TasksContract.Columns.TASKS_NAME,
                                TasksContract.Columns.TASKS_DESCRIPTION,TasksContract.Columns.TASKS_SORTORDER};
        String sortOrder = TasksContract.Columns.TASKS_SORTORDER+","+TasksContract.Columns.TASKS_NAME+ " COLLATE NOCASE";//sort based on characters


        switch(id){

            case LOADER_ID:
            return new CursorLoader(getActivity(),
                    TasksContract.CONTENT_URI,
                    projection,
                    null,
                    null,
                    sortOrder);
            default:
               throw new InvalidParameterException(TAG+".onCreateLoader called with invalid loader id"+id);

        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Log.d(TAG, "Entering onLoadFinished: ");
        mAdapter.swapCursor(data);

        int count = mAdapter.getItemCount();
//

        Log.d(TAG, "onLoadFinished: count is "+count);
        
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        Log.d(TAG, "onLoaderReset: starts");
        mAdapter.swapCursor(null);

    }
}