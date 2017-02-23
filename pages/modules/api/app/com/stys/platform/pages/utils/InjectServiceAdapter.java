package com.stys.platform.pages.utils;

import com.stys.platform.pages.api.Service;

import javax.inject.Inject;

public class InjectServiceAdapter<R, S, D, T extends Service<R, S, D>> implements Service<R, S, D> {
    @Inject
    private T instance;

    @Override
    public R get(S selector) {
        return instance.get(selector);
    }

    @Override
    public R put(S selector, D data) {
        return instance.put(selector, data);
    }
}
