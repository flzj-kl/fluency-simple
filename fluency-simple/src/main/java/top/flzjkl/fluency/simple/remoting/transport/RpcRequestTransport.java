package top.flzjkl.fluency.simple.remoting.transport;


import top.flzjkl.fluency.simple.common.extension.SPI;
import top.flzjkl.fluency.simple.remoting.dto.RpcRequest;

/**
 * send RpcRequest。
 *
 */
@SPI
public interface RpcRequestTransport {
    /**
     * send rpc request to server and get result
     *
     * @param rpcRequest message body
     * @return data from server
     */
    Object sendRpcRequest(RpcRequest rpcRequest);
}
