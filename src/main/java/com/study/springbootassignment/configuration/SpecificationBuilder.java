package com.study.springbootassignment.configuration;

import jakarta.persistence.criteria.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Slf4j
public class SpecificationBuilder {

    public static <T> Specification<T> buildFromParams(Map<String, String> params, Class<T> entityClass) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            query.distinct(true); // to handle @OneToMany duplicates

            for (Map.Entry<String, String> entry : params.entrySet()) {
                String key = entry.getKey();
                String rawValue = entry.getValue();
                if (rawValue == null || rawValue.isBlank()) continue;

                try {
                    Path<?> path = buildPath(root, key);
                    Class<?> fieldType = getFieldType(entityClass, key);

                    if (fieldType == null) {
                        log.info("Field not found: {}", key);
                        continue;
                    }

                    if (fieldType.equals(String.class)) {
                        predicates.add(cb.like(cb.upper(path.as(String.class)), "%" + rawValue.toUpperCase() + "%"));
                    } else if (fieldType.equals(Long.class) || fieldType.equals(long.class)) {
                        predicates.add(cb.equal(path, Long.parseLong(rawValue)));
                    } else if (fieldType.equals(Integer.class) || fieldType.equals(int.class)) {
                        predicates.add(cb.equal(path, Integer.parseInt(rawValue)));
                    } else if (fieldType.equals(Boolean.class) || fieldType.equals(boolean.class)) {
                        predicates.add(cb.equal(path, Boolean.parseBoolean(rawValue)));
                    } else if (fieldType.equals(LocalDateTime.class)) {
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                        predicates.add(cb.equal(path, LocalDateTime.parse(rawValue, formatter)));
                    } else {
                        log.info("Unsupported type for {}: {}", key, fieldType);
                    }

                } catch (Exception e) {
                    log.warn("Error processing param {}: {}", key, e.getMessage());
                }
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    private static Path<?> buildPath(From<?, ?> root, String key) {
        String[] parts = key.split("\\.");
        Path<?> path = root;
        for (String part : parts) {
            path = path.get(part);
        }
        return path;
    }

    private static Class<?> getFieldType(Class<?> clazz, String key) {
        try {
            String[] parts = key.split("\\.");
            Class<?> current = clazz;
            Field field = null;

            for (String part : parts) {
                field = getFieldRecursive(current, part);
                if (field == null) return null;
                current = field.getType();
            }

            return field.getType();
        } catch (Exception e) {
            log.warn("Failed to get type for {}: {}", key, e.getMessage());
            return null;
        }
    }

    private static Field getFieldRecursive(Class<?> clazz, String fieldName) {
        while (clazz != null) {
            try {
                return clazz.getDeclaredField(fieldName);
            } catch (NoSuchFieldException e) {
                clazz = clazz.getSuperclass();
            }
        }
        return null;
    }
}
