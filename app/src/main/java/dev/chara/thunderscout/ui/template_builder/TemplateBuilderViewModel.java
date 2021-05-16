package dev.chara.thunderscout.ui.template_builder;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;
import java.util.List;

import dev.chara.thunderscout.model.Category;
import dev.chara.thunderscout.model.Event;
import dev.chara.thunderscout.model.Field;
import dev.chara.thunderscout.model.ScoutData;
import dev.chara.thunderscout.storage.database.DataRepository;

public final class TemplateBuilderViewModel extends AndroidViewModel {

    private DataRepository repository;

    private MutableLiveData<List<Category>> liveData;

    private String templateName;

    private TemplateBuilderViewModel(@NonNull Application application, String templateName) {
        super(application);

        this.templateName = templateName;

        repository = DataRepository.getInstance(application.getApplicationContext());

        liveData = new MutableLiveData<>();
        liveData.setValue(new ArrayList<>());
    }

    public LiveData<List<Category>> getCategories() {
        return liveData;
    }

    public void addCategory(Category category) {
        List<Category> categories = liveData.getValue();
        categories.add(category);
        liveData.setValue(categories);
    }

    void removeCategory(Category category) {
        List<Category> categories = liveData.getValue();
        categories.remove(category);
        liveData.setValue(categories);
    }

    public void addField(Category category, Field field) {
        List<Category> categories = liveData.getValue();

        for (Category c : categories) {
            if (c == category) {
                c.fields.add(field);
                break;
            }
        }

        liveData.setValue(categories);
    }

    void removeField(Category category, Field field) {
        List<Category> categories = liveData.getValue();

        for (Category c : categories) {
            if (c == category) {
                c.fields.remove(field);
                break;
            }
        }

        liveData.setValue(categories);
    }

    void saveTemplate() {
        ScoutData data = new ScoutData();
        data.id = 0; //automatically generated
        data.eventId = Event.EVENT_ID_TEMPLATES;

        // Set template name
        data.teamNumber = templateName;

        // Set template contents
        data.categories = liveData.getValue();

        repository.insertData(data);
    }

    public static final class Factory implements ViewModelProvider.Factory {

        private Application application;
        private String templateName;

        public Factory(Application application, String templateName) {
            this.application = application;
            this.templateName = templateName;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            return (T) new TemplateBuilderViewModel(application, templateName);
        }
    }
}
