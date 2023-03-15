package com.hhs.codeboard.blog.config.web.property;

import org.springframework.beans.propertyeditors.CustomNumberEditor;
import org.springframework.lang.Nullable;
import org.springframework.util.NumberUtils;
import org.springframework.util.StringUtils;

import java.beans.PropertyEditorSupport;
import java.text.NumberFormat;

public class CustomIntegerEditor extends PropertyEditorSupport {

    private final Integer defaultValue;
    private final boolean allowEmpty;

    public CustomIntegerEditor(boolean allowEmpty) throws IllegalArgumentException {
        this(0, allowEmpty);
    }

    public CustomIntegerEditor(Integer defaultValue, boolean allowEmpty) throws IllegalArgumentException {
        this.defaultValue = defaultValue;
        this.allowEmpty = allowEmpty;
    }

    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        if (!StringUtils.hasText(text)) {
            if (this.allowEmpty) {
                this.setValue((Object)null);
            } else {
                this.setValue(this.defaultValue);
            }
        } else {
            this.setValue(Integer.parseInt(text));
        }

    }

    @Override
    public String getAsText() {
        Object value = this.getValue();
        if (value == null) {
            return String.valueOf(this.defaultValue);
        } else {
            return value.toString();
        }
    }
}