package server.controller;

import org.noear.solon.annotation.Component;
import org.noear.solon.annotation.Mapping;
import server.dso.IGreetingService;

import java.io.IOException;

@Mapping("/GreetingService/")
@Component(remoting = true)
public class GreetingService implements IGreetingService {
    @Override
    public String greeting(String name) throws IOException {
        return "Welcome ot the Hassian world, " + name;
    }
}
