package com.xxl.job.core.rpc.netcom.jetty.server;

import com.xxl.job.core.rpc.codec.RpcRequest;
import com.xxl.job.core.rpc.codec.RpcResponse;
import com.xxl.job.core.rpc.netcom.NetComServerFactory;
import com.xxl.job.core.rpc.serialize.HessianSerializer;
import com.xxl.job.core.util.HttpClientUtil;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;


/**
 * @project JettyServer的处理类,用于接收客户端的RPC请求
 * @file JettyServerHandler.java 创建时间:2017年7月29日下午12:06:04
 * @description 描述（简要描述类的职责、实现方式、使用注意事项等）
 * @author dzn
 * @version 1.0
 *
 */
public class JettyServerHandler extends AbstractHandler {
	private static Logger logger = LoggerFactory.getLogger(JettyServerHandler.class);

	@Override
	public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		
		// RPC调用
        RpcResponse rpcResponse = doInvoke(request);

        // serialize response
        byte[] responseBytes = HessianSerializer.serialize(rpcResponse);
		
		response.setContentType("text/html;charset=utf-8");
		response.setStatus(HttpServletResponse.SC_OK);
		baseRequest.setHandled(true);
		
		OutputStream out = response.getOutputStream();
		out.write(responseBytes);
		out.flush();
		
	}

	/**
	 *@description 根据客户端的请求进行RPC调用
	 *@time 创建时间:2017年7月29日下午12:04:54
	 *@param request
	 *@return
	 *@author dzn
	 */
	private RpcResponse doInvoke(HttpServletRequest request) {
		try {
			// deserialize request
			byte[] requestBytes = HttpClientUtil.readBytes(request);
			if (requestBytes == null || requestBytes.length==0) {
				RpcResponse rpcResponse = new RpcResponse();
				rpcResponse.setError("RpcRequest byte[] is null");
				return rpcResponse;
			}
			RpcRequest rpcRequest = (RpcRequest) HessianSerializer.deserialize(requestBytes, RpcRequest.class);

			// invoke
			RpcResponse rpcResponse = NetComServerFactory.invokeService(rpcRequest, null);
			return rpcResponse;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);

			RpcResponse rpcResponse = new RpcResponse();
			rpcResponse.setError("Server-error:" + e.getMessage());
			return rpcResponse;
		}
	}

}
