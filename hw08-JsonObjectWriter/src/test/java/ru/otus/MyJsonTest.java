package ru.otus;

import com.google.gson.Gson;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.examples.AnyObject;
import ru.otus.mygson.MyGson;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Class should")
public class MyJsonTest {

    @Test
    @DisplayName("Test Json")
    public void testJson() {
        AnyObject anyObject = new AnyObject();
        Gson gson = new Gson();
        String json = gson.toJson(anyObject);

        MyGson myGson = new MyGson();
        String myJson = myGson.toJson(anyObject);

        assertThat(myJson).isEqualTo(json);
    }
}
