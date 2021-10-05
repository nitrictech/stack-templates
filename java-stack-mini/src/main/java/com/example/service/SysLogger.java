package com.example.service;

import io.nitric.faas.logger.Logger;

/**
 * Provides a standard out/err Logger adapter.
 */
public class SysLogger implements Logger {
    
    @Override
    public void info(String format, Object... args) {
        System.out.printf(format + "\n", args);
    }

    @Override
    public void error(String format, Object... args) {
        System.err.printf(format + "\n", args);            
    }

    @Override
    public void error(Throwable error, String format, Object... args) {
        System.err.printf(format + "\n", args);            
        error.printStackTrace();            
    }
}