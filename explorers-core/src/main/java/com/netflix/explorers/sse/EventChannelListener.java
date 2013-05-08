package com.netflix.explorers.sse;

public interface EventChannelListener {
    void onChannelClosing(EventChannel eventChannel);
}
