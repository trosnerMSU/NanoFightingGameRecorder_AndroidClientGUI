package client.application.ui.commands;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import client.application.ui.connect.ConnectFragment;

public class CommandsViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public CommandsViewModel() {
        mText = new MutableLiveData<>();
        if(ConnectFragment.checkIfPaired()){
            mText.setValue("You are currently paired with " + ConnectFragment.getUrl());
        }else{
            mText.setValue("You need pair with a device first!");
        }
    }

    public LiveData<String> getText() {
        return mText;
    }

    public void setText(String newText){
        mText.setValue(newText);
    }
}

