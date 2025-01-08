package com.untitled.server.untitled.global.job;

public interface JobIF {
    int SUCCESS = 1;
    int FAILED = 0;
    void execute();
}
