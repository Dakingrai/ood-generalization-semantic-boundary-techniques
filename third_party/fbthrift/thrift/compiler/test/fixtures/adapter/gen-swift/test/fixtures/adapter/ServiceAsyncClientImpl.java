/**
 * Autogenerated by Thrift
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 *  @generated
 */

package test.fixtures.adapter;

import com.facebook.nifty.client.RequestChannel;
import com.facebook.swift.codec.*;
import com.facebook.swift.service.*;
import com.facebook.swift.service.metadata.*;
import com.facebook.swift.transport.client.*;
import com.facebook.swift.transport.util.FutureUtil;
import com.google.common.util.concurrent.ListenableFuture;
import java.io.*;
import java.lang.reflect.Method;
import java.util.*;
import org.apache.thrift.ProtocolId;
import reactor.core.publisher.Mono;

@SwiftGenerated
public class ServiceAsyncClientImpl extends AbstractThriftClient implements Service.Async {

    // Method Handlers
    private ThriftMethodHandler funcMethodHandler;

    // Method Exceptions
    private static final Class[] funcExceptions = new Class[] {
        org.apache.thrift.TException.class};

    public ServiceAsyncClientImpl(
        RequestChannel channel,
        Map<Method, ThriftMethodHandler> methods,
        Map<String, String> headers,
        Map<String, String> persistentHeaders,
        List<? extends ThriftClientEventHandler> eventHandlers) {
      super(channel, headers, persistentHeaders, eventHandlers);

      Map<String, ThriftMethodHandler> methodHandlerMap = new HashMap<>();
      methods.forEach(
          (key, value) -> {
            methodHandlerMap.put(key.getName(), value);
          });

      // Set method handlers
      funcMethodHandler = methodHandlerMap.get("func");
    }

    public ServiceAsyncClientImpl(
        Map<String, String> headers,
        Map<String, String> persistentHeaders,
        Mono<? extends RpcClient> rpcClient,
        ThriftServiceMetadata serviceMetadata,
        ThriftCodecManager codecManager,
        ProtocolId protocolId,
        Map<Method, ThriftMethodHandler> methods) {
      super(headers, persistentHeaders, rpcClient, serviceMetadata, codecManager, protocolId);

      Map<String, ThriftMethodHandler> methodHandlerMap = new HashMap<>();
      methods.forEach(
          (key, value) -> {
            methodHandlerMap.put(key.getName(), value);
          });

      // Set method handlers
      funcMethodHandler = methodHandlerMap.get("func");
    }

    @java.lang.Override
    public void close() {
        super.close();
    }


    @java.lang.Override
    public ListenableFuture<Integer> func(
        String arg1,
        test.fixtures.adapter.Foo arg2) {
        return func(arg1, arg2, RpcOptions.EMPTY);
    }

    @java.lang.Override
    public ListenableFuture<Integer> func(
        String arg1,
        test.fixtures.adapter.Foo arg2,
        RpcOptions rpcOptions) {
        return FutureUtil.transform(funcWrapper(arg1, arg2, rpcOptions));
    }

    @java.lang.Override
    public ListenableFuture<ResponseWrapper<Integer>> funcWrapper(
        String arg1,
        test.fixtures.adapter.Foo arg2,
        RpcOptions rpcOptions) {
        try {
          return executeWrapperWithOptions(funcMethodHandler, funcExceptions, rpcOptions, arg1, arg2);
        } catch (Throwable t) {
          throw new RuntimeTException(t.getMessage(), t);
        }
    }
}
