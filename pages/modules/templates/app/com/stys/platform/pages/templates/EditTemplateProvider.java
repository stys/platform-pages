package com.stys.platform.pages.templates;

import com.stys.platform.pages.api.Template;
import com.stys.platform.pages.api.TemplateProvider;

import javax.inject.Inject;

public class EditTemplateProvider implements TemplateProvider {

    private EditTemplate editor;

    private ErrorTemplate error;

    @Inject
    public EditTemplateProvider(EditTemplate editor, ErrorTemplate error) {
        this.editor = editor;
    }

    @Override
    public Template get(String name) {
        switch (name) {
            case "Ok":
                return editor;
            default:
                return error;
        }
    }
}
