package client.application.ui.admins;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AdminsViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public AdminsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Administration Page");
    }

    public LiveData<String> getText() {
        return mText;
    }
}