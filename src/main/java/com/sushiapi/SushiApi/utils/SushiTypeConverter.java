package com.sushiapi.SushiApi.utils;

import com.sushiapi.SushiApi.model.enums.SushiType;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class SushiTypeConverter implements AttributeConverter<SushiType,String> {
    @Override
    public String convertToDatabaseColumn(SushiType sushiType) {
        if (sushiType == null) return null;
        return sushiType.getValue();
    }

    @Override
    public SushiType convertToEntityAttribute(String value) {
        if (value == null) return null;
        return SushiType.fromValue(value);    }
}
