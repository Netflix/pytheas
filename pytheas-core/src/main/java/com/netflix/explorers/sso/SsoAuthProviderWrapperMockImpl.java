package com.netflix.explorers.sso;

public class SsoAuthProviderWrapperMockImpl implements  SsoAuthProviderWrapper {

    @Override
    public boolean hasSsoAuthProvider() {
        return false;
    }
}
