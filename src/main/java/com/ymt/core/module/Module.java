package com.ymt.core.module;

public interface Module extends AutoCloseable {
    void start() throws Exception;   // construct + init this group's services
    @Override default void close() {}// release them
}
