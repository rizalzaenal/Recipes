package com.rizalzaenal.recipes.utils;

import androidx.annotation.Nullable;
import androidx.test.espresso.IdlingResource;
import java.util.concurrent.atomic.AtomicBoolean;

public class BasicIdlingResource implements IdlingResource {

    @Nullable
    private volatile IdlingResource.ResourceCallback mCallback;

    private AtomicBoolean mIsIdleNow = new AtomicBoolean(true);

    @Override
    public String getName() {
        return this.getClass().getName();
    }

    @Override
    public boolean isIdleNow() {
        return mIsIdleNow.get();
    }

    @Override
    public void registerIdleTransitionCallback(IdlingResource.ResourceCallback callback) {
        mCallback = callback;
    }

    public void setIdleState(boolean isIdleNow) {
        mIsIdleNow.set(isIdleNow);
        if (isIdleNow && mCallback != null) {
            mCallback.onTransitionToIdle();
        }
    }

    private static BasicIdlingResource idlingResource;

    public static IdlingResource getIdlingResource() {
        if (idlingResource == null) {
            idlingResource = new BasicIdlingResource();
        }
        return idlingResource;
    }

    public static void setIdleResourceTo(boolean isIdleNow){
        if (idlingResource == null) {
            idlingResource = new BasicIdlingResource();
        }
        idlingResource.setIdleState(isIdleNow);
    }
}
