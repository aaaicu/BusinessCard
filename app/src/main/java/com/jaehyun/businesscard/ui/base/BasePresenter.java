package com.jaehyun.businesscard.ui.base;

public abstract class BasePresenter implements BaseContract.Presenter {
    protected BaseContract.View mView = null;

    @Override
    public void setView(BaseContract.View view) {
        if (view != null) {
            mView = view;
        }
    }
}
