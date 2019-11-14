package com.longwen.app.module;

import com.longwen.server.app.api.Command;
import com.longwen.server.app.api.Information;
import com.longwen.server.app.api.Module;
import org.kohsuke.MetaInfServices;

import java.io.IOException;
import java.io.PrintWriter;

@MetaInfServices(Module.class)
@Information(id = "sandbox-info", version = "0.0.4", author = "mxphuang@gmail.com")
public class InfoModule implements Module {


    @Command("version")
    public void version(final PrintWriter writer) throws IOException {

        final StringBuilder versionSB = new StringBuilder()
                .append("                    NAMESPACE : ").append("Defualt").append("\n")
                .append("                      VERSION : ").append("Defualt").append("\n")
                .append("                         MODE : ").append("Defualt").append("\n")
                .append("                  SERVER_ADDR : ").append("Defualt").append("\n")
                .append("                  SERVER_PORT : ").append("Defualt").append("\n")
                .append("               UNSAFE_SUPPORT : ").append(false ? "ENABLE" : "DISABLE").append("\n")
                .append("                 SANDBOX_HOME : ").append("").append("\n")
                .append("            SYSTEM_MODULE_LIB : ").append("").append("\n")
                .append("              USER_MODULE_LIB : ").append("").append("\n")
                .append("          SYSTEM_PROVIDER_LIB : ").append("Defualt").append("\n")
                .append("           EVENT_POOL_SUPPORT : ").append(true ? "ENABLE" : "DISABLE");
//                       /*############################# : */
//        if (configInfo.isEnableEventPool()) {
//            versionSB
//                    .append("\n")
//                           /*############################# : */
//                    .append("  EVENT_POOL_PER_KEY_IDLE_MIN : ").append(configInfo.getEventPoolMinIdlePerEvent()).append("\n")
//                    .append("  EVENT_POOL_PER_KEY_IDLE_MAX : ").append(configInfo.getEventPoolMaxIdlePerEvent()).append("\n")
//                    .append(" EVENT_POOL_PER_KEY_TOTAL_MAX : ").append(configInfo.getEventPoolMaxTotalPerEvent()).append("\n")
//                    .append("             EVENT_POOL_TOTAL : ").append(configInfo.getEventPoolMaxTotal())
//            ;
//        }

        writer.println(versionSB.toString());
        writer.flush();

    }
}
