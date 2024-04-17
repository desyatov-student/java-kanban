package ru.praktikum.kanban.service.http;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import lombok.Getter;

public class UrlPathParser {

    private final String[] patternParts;
    @Getter
    private final Map<String, String> params;

    public UrlPathParser(String pattern) {
        this.patternParts = pattern.split("/");
        this.params = new HashMap<>();
    }

    public boolean tryParse(String path) {
        String[] pathParts = path.split("/");
        if (pathParts.length != patternParts.length) {
            return false;
        }
        HashMap<String, String> tempParams = new HashMap<>();
        for (int i = 0; i < patternParts.length; i++) {
            String patternPart = patternParts[i];
            String pathPart = pathParts[i];

            if (patternPart.equals(pathPart)) {
                continue;
            }
            Optional<String> parameterKeyOpt = getParameterKey(patternPart);
            if (parameterKeyOpt.isEmpty()) {
                return false;
            }
            String parameterKey = parameterKeyOpt.get();
            String parameterValue = pathParts[i];
            tempParams.put(parameterKey, parameterValue);
        }
        params.putAll(tempParams);
        return true;
    }

    private Optional<String> getParameterKey(String rawValue) {
        if (!rawValue.startsWith("{") || !rawValue.endsWith("}")) {
            return Optional.empty();
        }
        return Optional.of(rawValue.substring(1, rawValue.indexOf("}")));
    }
}
