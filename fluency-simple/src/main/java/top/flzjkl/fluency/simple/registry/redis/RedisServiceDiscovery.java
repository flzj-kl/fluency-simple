package top.flzjkl.fluency.simple.registry.redis;

import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;
import top.flzjkl.fluency.simple.registry.ServiceDiscovery;
import top.flzjkl.fluency.simple.remoting.dto.RpcRequest;

import java.net.InetSocketAddress;


@Slf4j
public class RedisServiceDiscovery implements ServiceDiscovery {

    private final String REDIS_ROOT_PATH = "fluency:simple:";

    @Override
    public InetSocketAddress lookupService(RpcRequest rpcRequest) {
        Jedis jedis = new Jedis("host");
        jedis.auth("password");
        String key =  REDIS_ROOT_PATH + rpcRequest.getRpcServiceName();
        String value = jedis.get(key);
        System.out.println("服务调用者key: " + key);
        System.out.println("从Redis中取出value: " + value);
        String[] split = value.split(":");
        String host = split[0];
        int port = Integer.parseInt(split[1]);
        return new InetSocketAddress(host, port);
    }
}
