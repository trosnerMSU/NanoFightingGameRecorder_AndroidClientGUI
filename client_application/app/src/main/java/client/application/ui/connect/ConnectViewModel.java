package client.application.ui.connect;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ConnectViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public ConnectViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Domain Connection Page.");
    }

    public LiveData<String> getText() {
        return mText;
    }
}