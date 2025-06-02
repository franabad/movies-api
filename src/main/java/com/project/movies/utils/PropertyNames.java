package com.project.movies.utils;

import com.project.movies.movie.MovieModel;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.util.HashSet;
import java.util.Set;

public class PropertyNames {
    public static String[] getNullOrDefaultPropertyNames(MovieModel source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        java.beans.PropertyDescriptor[] pds = src.getPropertyDescriptors();

        Set<String> emptyNames = new HashSet<>();
        for (java.beans.PropertyDescriptor pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null || isDefaultValue(srcValue)) {
                emptyNames.add(pd.getName());
            }
        }
        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }

    private static boolean isDefaultValue(Object value) {
        if (value instanceof Integer) {
            return (Integer) value == 0;
        } else if (value instanceof Double) {
            return (Double) value == 0.0;
        }
        // Add more checks for other types if needed
        return false;
    }
}
