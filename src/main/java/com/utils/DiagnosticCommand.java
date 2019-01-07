package com.utils;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.lang.management.ManagementFactory;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.Supplier;

import javax.management.JMX;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;

public interface DiagnosticCommand {
    String threadPrint(String... args);

    DiagnosticCommand local = ((Supplier<DiagnosticCommand>) () -> {
        try {
            MBeanServer server = ManagementFactory.getPlatformMBeanServer();
            ObjectName name = new ObjectName("com.sun.management",
                    "type", "DiagnosticCommand");
            return JMX.newMBeanProxy(server, name, DiagnosticCommand.class);
        } catch (MalformedObjectNameException e) {
            throw new AssertionError(e);
        }
    }).get();

    static void dump() {
        String print = local.threadPrint();
        Path path = Paths.get(System.currentTimeMillis() + ".dump.txt");
        try {
            byte[] bytes = print.getBytes("ASCII");
            Files.write(path, bytes);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
