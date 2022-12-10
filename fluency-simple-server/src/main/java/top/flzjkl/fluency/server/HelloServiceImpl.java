package top.flzjkl.fluency.server;

import lombok.extern.slf4j.Slf4j;
import top.flzjkl.fluency.api.Hello;
import top.flzjkl.fluency.api.HelloService;
import top.flzjkl.fluency.simple.annotation.RpcService;

@Slf4j
@RpcService(group = "test1", version = "version1")
public class HelloServiceImpl implements HelloService {

    static {
        System.out.println("HelloServiceImpl被创建");
    }

    @Override
    public String hello(Hello hello) {
        // log.info("HelloServiceImpl收到: {}.", hello.getMessage());
        String result = "Hello description is " + hello.getDescription();
        // log.info("HelloServiceImpl返回: {}.", result);
        return result;
    }
}
