package com.project.movies.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.movies.cinema.CinemaModel;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.List;

@Converter
public class JsonConverter implements AttributeConverter<List<CinemaModel.LayoutSection>, String> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(List<CinemaModel.LayoutSection> attribute) {
        if (attribute == null) return null;

        try {
            return objectMapper.writeValueAsString(attribute);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error converting List<LayoutSection> to JSON", e);
        }
    }

    @Override
    public List<CinemaModel.LayoutSection> convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isEmpty()) return null;

        try {
            return objectMapper.readValue(dbData,
                    objectMapper.getTypeFactory().constructCollectionType(List.class, CinemaModel.LayoutSection.class));
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error converting JSON to List<LayoutSection>", e);
        }
    }
}
