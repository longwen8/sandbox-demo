package com.longwen.instrumentation.agentmain;

import com.sun.tools.attach.AgentInitializationException;
import com.sun.tools.attach.AgentLoadException;
import com.sun.tools.attach.AttachNotSupportedException;
import com.sun.tools.attach.VirtualMachine;

import java.io.IOException;


public class TestAgent {

    public static void main(String[] args) throws AttachNotSupportedException,
            IOException, AgentLoadException, AgentInitializationException {

        VirtualMachine vm = VirtualMachine.attach(args[0]);
        vm.loadAgent("/home/app/sandbox/demo/inst/agent-agentmain.jar");

    }
}
