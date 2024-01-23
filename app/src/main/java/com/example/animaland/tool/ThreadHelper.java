package com.example.animaland.tool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadHelper {
    public static ExecutorService cachedThreadPool = Executors.newCachedThreadPool();
}
