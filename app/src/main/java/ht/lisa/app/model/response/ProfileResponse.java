package ht.lisa.app.model.response;

import java.io.IOException;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import ht.lisa.app.model.User;

public class ProfileResponse extends BaseResponse {
    public static final String READONLY_DOB = "dob";
    @JsonAdapter(UserAdapterFactory.class)
    private User dataset;
    private ArrayList<String> readonly;

    public User getUser() {
        return dataset;
    }

    public ArrayList<String> getReadonly() {
        return readonly;
    }

    public class UserAdapterFactory implements TypeAdapterFactory {

        @Override
        public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
            return (TypeAdapter<T>) new UserAdapter(gson);
        }
    }

    public class UserAdapter extends TypeAdapter<User> {

        private final Gson gson;

        public UserAdapter(Gson gson) {
            this.gson = gson;
        }

        @Override
        public void write(JsonWriter out, User value) throws IOException {
        }

        @Override
        public User read(JsonReader jsonReader) throws IOException {
            switch (jsonReader.peek()) {
                case BEGIN_OBJECT:
                    return gson.fromJson(jsonReader, User.class);
                case BEGIN_ARRAY:
                    UsersArrayResponse response = gson.fromJson(jsonReader, UsersArrayResponse.class);
                    if (response.size() > 0) {
                        return response.get(0);
                    } else {
                        throw new RuntimeException("Found array for users, size was 0");
                    }

                default:
                    throw new RuntimeException("Expected object or string, found " + jsonReader.peek());
            }
        }
    }
}
