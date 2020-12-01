package ru.otus.messagesystem.messagetypes;

import com.google.gson.Gson;
import ru.otus.frontend.core.model.SearchForm;
import ru.otus.messagesystem.client.ResultDataType;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class SearchFormMsgData extends ResultDataType implements Serializable {
    private transient SearchForm searchForm;

    public SearchFormMsgData(SearchForm searchForm) {
        this.searchForm = searchForm;
    }

    public SearchForm getSearchForm() {
        return searchForm;
    }

    // чтобы не менять SearchForm, который не Serializable
    private void writeObject(ObjectOutputStream oos) throws IOException {
        Gson gson = new Gson();
        String json = gson.toJson(searchForm);
        oos.defaultWriteObject();
        oos.writeObject(json);
    }

    private void readObject(ObjectInputStream ois) throws ClassNotFoundException, IOException {
        ois.defaultReadObject();
        String json = (String) ois.readObject();
        Gson gson = new Gson();
        searchForm = gson.fromJson(json, SearchForm.class);
    }
}
