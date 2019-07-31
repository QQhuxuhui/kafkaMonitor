package com.common;

import org.springframework.boot.context.embedded.EmbeddedServletContainerInitializedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * @Auther: huxuhui
 * @Date: 2019/4/24 17:28
 * @Description: 获取启动端口
 */
@Component
public class PortConfig implements ApplicationListener<EmbeddedServletContainerInitializedEvent> {

    private int serverPort;

    @Override
    public void onApplicationEvent(EmbeddedServletContainerInitializedEvent event) {
        this.serverPort = event.getEmbeddedServletContainer().getPort();
    }

    public int getServerPort() {
        return serverPort;
    }
}
