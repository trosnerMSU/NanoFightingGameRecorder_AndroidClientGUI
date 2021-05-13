package client.application.ui.commands;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class CommandsViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public CommandsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Command Page for Screen Recording.");
    }

    public LiveData<String> getText() {
        return mText;
    }

    public void setText(String newText){
        mText.setValue(newText);
    }
}

