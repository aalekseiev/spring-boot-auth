package com.auth0.samples.authapi.user;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author Anton Alekseyev (jbs.com.ua).
 */
@Converter(autoApply = true)
public class PermissionsConverter implements AttributeConverter<Permissions, String> {
    
    private static final ObjectMapper mapper; 
    
    @Override
    public String convertToDatabaseColumn(Permissions attribute) {
        if (attribute == null) {
            return null;
        }
        try {                        
            return mapper.writeValueAsString(attribute);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to convert Reservations object to json", e);
        }
    }

    /** {@inheritDoc} */
    @Override
    public Permissions convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }
        Permissions reservations;
        try {
            reservations = mapper.readValue(dbData, Permissions.class);
            return reservations;
        } catch (Exception e) {
            throw new RuntimeException("Failed to convert json to Reservations object", e);
        }
    }

    static {
        mapper = new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.ALL, Visibility.NONE);
        mapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        
//        mapper.registerModule(new JSR310Module());
        // DO NOT UNCOMMENT. FE on big calendar need timestamps  
        // mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

}
