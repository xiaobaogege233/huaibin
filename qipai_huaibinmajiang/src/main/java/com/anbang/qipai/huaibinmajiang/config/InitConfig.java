package com.anbang.qipai.huaibinmajiang.config;

import com.anbang.qipai.huaibinmajiang.cqrs.c.repository.SingletonEntityFactoryImpl;
import com.anbang.qipai.huaibinmajiang.cqrs.c.service.disruptor.ProcessCoreCommandEventHandler;
import com.anbang.qipai.huaibinmajiang.init.InitProcessor;
import com.dml.users.UserSessionsManager;
import com.highto.framework.ddd.SingletonEntityRepository;
import org.eclipse.jetty.client.HttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: xiaobao
 * @Date: 2021/06/08/10:55
 * @Description: 初始化的一些数据
 */

@Configuration
public class InitConfig {

    @Autowired
    private ApplicationContext applicationContext;

    @Bean
    public HttpClient httpClient() {
        HttpClient client = new HttpClient();
        try {
            client.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return client;
    }
    @Bean
    public UserSessionsManager userSessionsManager() {
        return new UserSessionsManager();
    }

    @Bean
    public ProcessCoreCommandEventHandler processCoreCommandEventHandler() {
        return new ProcessCoreCommandEventHandler();
    }

    @Bean
    public SingletonEntityRepository singletonEntityRepository() {
        SingletonEntityRepository singletonEntityRepository = new SingletonEntityRepository();
        singletonEntityRepository.setEntityFactory(new SingletonEntityFactoryImpl());
        return singletonEntityRepository;
    }

    @Bean
    public InitProcessor initProcessor() {
        InitProcessor initProcessor = new InitProcessor(singletonEntityRepository(), applicationContext);
        initProcessor.init();
        return initProcessor;
    }
}
