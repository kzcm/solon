package net.hasor.spring.boot;

import net.hasor.solon.boot.EnableHasor;
import org.noear.solon.annotation.XImport;

@XImport(basePackages = { "net.hasor.test.spring.mod1" })
@EnableHasor(useProperties = true)
public class BootShareHasor_1 {

}