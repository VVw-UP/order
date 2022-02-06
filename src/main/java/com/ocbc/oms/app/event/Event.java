package com.ocbc.oms.app.event;


/**
 * @author Zhenming Pan
 */
@FunctionalInterface
public interface Event {

    /**
     * event execute
     */
    void execute();

}
