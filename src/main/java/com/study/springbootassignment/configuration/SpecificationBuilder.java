package com.study.springbootassignment.configuration;

import jakarta.persistence.criteria.Predicate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Slf4j
public class SpecificationBuilder {

    public static <T> Specification<T> buildFromParams(Map<String, String> params, Class<T> entityClass) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            for (Map.Entry<String, String> entry : params.entrySet()) {
                String key = entry.getKey();
                String rawValue = entry.getValue();

                if (rawValue == null || rawValue.isBlank()) continue;

                try {
                    Field field = Arrays.stream(entityClass.getDeclaredFields())
                            .filter(f -> f.getName().equals(key))
                            .findFirst()
                            .orElse(null);

                    if (field == null) continue;

                    Class<?> fieldType = field.getType();

                    if (fieldType.equals(String.class)) {
                        predicates.add(cb.like(cb.upper(root.get(key)), "%" + rawValue.toUpperCase() + "%"));
                    } else if (fieldType.equals(Long.class) || fieldType.equals(long.class)) {
                        predicates.add(cb.equal(root.get(key), Long.parseLong(rawValue)));
                    } else if (fieldType.equals(Integer.class) || fieldType.equals(int.class)) {
                        predicates.add(cb.equal(root.get(key), Integer.parseInt(rawValue)));
                    } else if (fieldType.equals(Boolean.class) || fieldType.equals(boolean.class)) {
                        predicates.add(cb.equal(root.get(key), Boolean.parseBoolean(rawValue)));
                    } else if (fieldType.equals(LocalDateTime.class)) {
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                        predicates.add(cb.equal(root.get(key), LocalDateTime.parse(rawValue, formatter)));
                    } else {
                        log.info("Unsupported type for {}: {}", key, fieldType);
                    }
                } catch (Exception e) {
                    log.info(e.getMessage());
                }
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
