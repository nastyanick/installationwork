package com.nastynick.installationworks.executor;

import io.reactivex.Scheduler;

/**
 * Thread abstraction to change the execution context after operation done
 */
public interface PostExecutionThread {
    Scheduler getScheduler();
}
