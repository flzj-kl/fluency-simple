package top.flzjkl.fluency.controller;


import org.springframework.stereotype.Component;
import top.flzjkl.fluency.api.Hello;
import top.flzjkl.fluency.api.HelloService;
import top.flzjkl.fluency.simple.annotation.RpcReference;

@Component
public class HelloController {

    @RpcReference(version = "version1", group = "test1")
    private HelloService helloService;

    public void test() throws InterruptedException {
        String hello = this.helloService.hello(new Hello("111", "222"));
        System.out.println("调用远程helloService的执行结果为" + hello);
    }
}
