package ru.praktikum.kanban.type.adapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static ru.praktikum.kanban.constant.FormatterConstants.DATE_PATTERN_API;

public class LocalDateTimeAdapter extends TypeAdapter<LocalDateTime> {

    private static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern(DATE_PATTERN_API);

    @Override
    public void write(final JsonWriter jsonWriter, final LocalDateTime localDate) throws IOException {
        if (localDate == null) {
            jsonWriter.nullValue();
            return;
        }
        jsonWriter.value(localDate.format(dtf));
    }

    @Override
    public LocalDateTime read(final JsonReader jsonReader) throws IOException {
        if (jsonReader.peek() == JsonToken.NULL) {
            jsonReader.nextNull();
            return null;
        }
        return LocalDateTime.parse(jsonReader.nextString(), dtf);
    }
}