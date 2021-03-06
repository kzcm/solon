package org.noear.solon.extend.consul;

import com.ecwid.consul.json.GsonFactory;
import com.ecwid.consul.v1.ConsulClient;
import com.ecwid.consul.v1.agent.model.NewService;
import org.noear.solon.Solon;
import org.noear.solon.Utils;

import java.util.*;

/**
 * 注册任务
 *
 * @author 夜の孤城
 * @since 1.2
 */
class ConsulRegisterTask implements Runnable {
    ConsulClient client;

    public ConsulRegisterTask(ConsulClient client) {

        this.client = client;

    }

    @Override
    public void run() {
        String healthCheckInterval = Solon.cfg().get(Constants.DISCOVERY_HEALTH_CHECK_INTERVAL, "10s");
        String healthCheckPath     = Solon.cfg().get(Constants.DISCOVERY_HEALTH_CHECK_PATH, "/run/check/");
        String hostname            = Solon.cfg().get(Constants.DISCOVERY_HOSTNAME);
        String tags_str            = Solon.cfg().get(Constants.DISCOVERY_TAGS);
        String token            = Solon.cfg().get(Constants.TOKEN);
        Set<String> tags = new LinkedHashSet<>();

        tags.add("solon");

        //::构建tags
        //
        if (Utils.isNotEmpty(Solon.cfg().appGroup())) {
            tags.add(Solon.cfg().appGroup());
        }

        if (Utils.isNotEmpty(tags_str)) {
            tags.addAll(Arrays.asList(tags_str.split(",")));
        }


        //::确定hostname
        if (Utils.isEmpty(hostname)) {
            hostname = Tools.getLocalAddress();
        }


        NewService newService = new NewService();

        newService.setPort(Solon.global().port());
        newService.setId(Solon.cfg().appName() + "-" + Solon.global().port());
        newService.setName(Solon.cfg().appName());
        newService.setTags(new ArrayList<>(tags));
        newService.setAddress(hostname);


        if (Utils.isNotEmpty(healthCheckInterval)) {
            //1.添加Solon服务，提供检测用
            //
            HealthDetector detector=new HealthDetector();
            detector.startDetect(Solon.global());

            Solon.global().get(healthCheckPath, ctx -> {

                Map<String,Object> info=new HashMap<>();
                info.put("status","OK");
                info.putAll(detector.getInfo());
                ctx.outputAsJson( GsonFactory.getGson().toJson(info));

            });

            //2.添加检测器
            //
            String checkUrl = "http://" + hostname + ":" + Solon.global().port();
            if (healthCheckPath.startsWith("/")) {
                checkUrl = checkUrl + healthCheckPath;
            } else {
                checkUrl = checkUrl + "/" + healthCheckPath;
            }

            //3.添加检测
            //
            NewService.Check check = new NewService.Check();
            check.setInterval(healthCheckInterval);
            check.setMethod("GET");
            check.setHttp(checkUrl);
            check.setDeregisterCriticalServiceAfter("30s");
            check.setTimeout("60s");
            newService.setCheck(check);
        }


        //
        // 注册服务
        //
        client.agentServiceRegister(newService,token);
    }
}
