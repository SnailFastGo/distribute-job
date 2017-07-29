package com.xxl.job.core.rpc.netcom;

import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.rpc.codec.RpcRequest;
import com.xxl.job.core.rpc.codec.RpcResponse;
import com.xxl.job.core.rpc.netcom.jetty.server.JettyServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cglib.reflect.FastClass;
import org.springframework.cglib.reflect.FastMethod;

import java.util.HashMap;
import java.util.Map;

/**
 * netcom init
 * @author xuxueli 2015-10-31 22:54:27
 */
public class NetComServerFactory  {
	private static final Logger logger = LoggerFactory.getLogger(NetComServerFactory.class);

	// ---------------------- server start ----------------------
	
	//创建一个Jetty的Server实例
	JettyServer server = new JettyServer();
	
	/**
	 *@description 启动Jetty Server
	 *@time 创建时间:2017年7月29日下午12:09:00
	 *@param port
	 *@param ip
	 *@param appName
	 *@throws Exception
	 *@author dzn
	 */
	public void start(int port, String ip, String appName) throws Exception {
		server.start(port, ip, appName);
	}

	// ---------------------- server destroy ----------------------
	/**
	 *@description 销毁Jetty Server
	 *@time 创建时间:2017年7月29日下午12:09:15
	 *@author dzn
	 */
	public void destroy(){
		server.destroy();
	}

	// ---------------------- server init ----------------------
	/**
	 * init local rpc service map
	 */
	private static Map<String, Object> serviceMap = new HashMap<String, Object>();
	
	
	/**
	 *@description 绑定服务接口和服务实现类
	 *@time 创建时间:2017年7月28日下午5:03:11
	 *@param iface
	 *@param serviceBean
	 *@author dzn
	 */
	public static void putService(Class<?> iface, Object serviceBean){
		serviceMap.put(iface.getName(), serviceBean);
	}
	
	
	/**
	 *@description 远程RPC调用
	 *@time 创建时间:2017年7月28日下午5:03:27
	 *@param request
	 *@param serviceBean
	 *@return
	 *@author dzn
	 */
	public static RpcResponse invokeService(RpcRequest request, Object serviceBean) {
		if (serviceBean==null) {
			serviceBean = serviceMap.get(request.getClassName());
		}
		if (serviceBean == null) {
			// TODO
		}

		RpcResponse response = new RpcResponse();

		if (System.currentTimeMillis() - request.getCreateMillisTime() > 180000) {
			response.setResult(new ReturnT<String>(ReturnT.FAIL_CODE, "the timestamp difference between admin and executor exceeds the limit."));
			return response;
		}

		try {
			Class<?> serviceClass = serviceBean.getClass();
			String methodName = request.getMethodName();
			Class<?>[] parameterTypes = request.getParameterTypes();
			Object[] parameters = request.getParameters();

			FastClass serviceFastClass = FastClass.create(serviceClass);
			FastMethod serviceFastMethod = serviceFastClass.getMethod(methodName, parameterTypes);

			Object result = serviceFastMethod.invoke(serviceBean, parameters);

			response.setResult(result);
		} catch (Throwable t) {
			t.printStackTrace();
			response.setError(t.getMessage());
		}

		return response;
	}

}
