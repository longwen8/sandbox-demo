package com.longwen.server.app.core.classloader;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public class ProviderClassLoader extends RoutingURLClassLoader {

    public ProviderClassLoader(final File providerJarFile,
                               final ClassLoader sandboxClassLoader) throws IOException {
        super(
                new URL[]{new URL("file:" + providerJarFile.getPath())},
                new Routing(
                        sandboxClassLoader,
                        "^com\\.alibaba\\.jvm\\.sandbox\\.api\\..*",
                        "^com\\.alibaba\\.jvm\\.sandbox\\.provider\\..*",
                        "^javax\\.annotation\\.Resource.*$"
                )
        );
    }
}
