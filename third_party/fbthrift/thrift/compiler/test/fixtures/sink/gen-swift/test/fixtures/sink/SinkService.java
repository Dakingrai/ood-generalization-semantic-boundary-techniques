/**
 * Autogenerated by Thrift
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 *  @generated
 */

package test.fixtures.sink;

import com.facebook.swift.codec.*;
import com.facebook.swift.codec.ThriftField.Requiredness;
import com.facebook.swift.service.*;
import com.facebook.swift.transport.client.*;
import com.google.common.util.concurrent.ListenableFuture;
import java.io.*;
import java.util.*;

@SwiftGenerated
@com.facebook.swift.service.ThriftService("SinkService")
public interface SinkService extends java.io.Closeable {
    @com.facebook.swift.service.ThriftService("SinkService")
    public interface Async extends java.io.Closeable {
        @java.lang.Override void close();

    }
    @java.lang.Override void close();

    @com.facebook.swift.service.ThriftService("SinkService")
    interface Reactive extends reactor.core.Disposable {
        reactor.core.publisher.Mono<test.fixtures.sink.FinalResponse> method( org.reactivestreams.Publisher<test.fixtures.sink.SinkPayload> payloads);

        default reactor.core.publisher.Mono<test.fixtures.sink.FinalResponse> method( org.reactivestreams.Publisher<test.fixtures.sink.SinkPayload> payloads, RpcOptions rpcOptions) {
            throw new UnsupportedOperationException();
        }

        default reactor.core.publisher.Mono<ResponseWrapper<test.fixtures.sink.FinalResponse>> methodWrapper( org.reactivestreams.Publisher<test.fixtures.sink.SinkPayload> payloads, RpcOptions rpcOptions) {
            throw new UnsupportedOperationException();
        }

        reactor.core.publisher.Flux<com.facebook.swift.transport.model.StreamResponse<test.fixtures.sink.InitialResponse,test.fixtures.sink.FinalResponse>> methodAndReponse( org.reactivestreams.Publisher<test.fixtures.sink.SinkPayload> payloads);

        default reactor.core.publisher.Flux<com.facebook.swift.transport.model.StreamResponse<test.fixtures.sink.InitialResponse,test.fixtures.sink.FinalResponse>> methodAndReponse( org.reactivestreams.Publisher<test.fixtures.sink.SinkPayload> payloads, RpcOptions rpcOptions) {
            throw new UnsupportedOperationException();
        }

        default reactor.core.publisher.Flux<ResponseWrapper<com.facebook.swift.transport.model.StreamResponse<test.fixtures.sink.InitialResponse,test.fixtures.sink.FinalResponse>>> methodAndReponseWrapper( org.reactivestreams.Publisher<test.fixtures.sink.SinkPayload> payloads, RpcOptions rpcOptions) {
            throw new UnsupportedOperationException();
        }

        reactor.core.publisher.Mono<test.fixtures.sink.FinalResponse> methodThrow( org.reactivestreams.Publisher<test.fixtures.sink.SinkPayload> payloads);

        default reactor.core.publisher.Mono<test.fixtures.sink.FinalResponse> methodThrow( org.reactivestreams.Publisher<test.fixtures.sink.SinkPayload> payloads, RpcOptions rpcOptions) {
            throw new UnsupportedOperationException();
        }

        default reactor.core.publisher.Mono<ResponseWrapper<test.fixtures.sink.FinalResponse>> methodThrowWrapper( org.reactivestreams.Publisher<test.fixtures.sink.SinkPayload> payloads, RpcOptions rpcOptions) {
            throw new UnsupportedOperationException();
        }

        reactor.core.publisher.Mono<test.fixtures.sink.FinalResponse> methodSinkThrow( org.reactivestreams.Publisher<test.fixtures.sink.SinkPayload> payloads);

        default reactor.core.publisher.Mono<test.fixtures.sink.FinalResponse> methodSinkThrow( org.reactivestreams.Publisher<test.fixtures.sink.SinkPayload> payloads, RpcOptions rpcOptions) {
            throw new UnsupportedOperationException();
        }

        default reactor.core.publisher.Mono<ResponseWrapper<test.fixtures.sink.FinalResponse>> methodSinkThrowWrapper( org.reactivestreams.Publisher<test.fixtures.sink.SinkPayload> payloads, RpcOptions rpcOptions) {
            throw new UnsupportedOperationException();
        }

        reactor.core.publisher.Mono<test.fixtures.sink.FinalResponse> methodFinalThrow( org.reactivestreams.Publisher<test.fixtures.sink.SinkPayload> payloads);

        default reactor.core.publisher.Mono<test.fixtures.sink.FinalResponse> methodFinalThrow( org.reactivestreams.Publisher<test.fixtures.sink.SinkPayload> payloads, RpcOptions rpcOptions) {
            throw new UnsupportedOperationException();
        }

        default reactor.core.publisher.Mono<ResponseWrapper<test.fixtures.sink.FinalResponse>> methodFinalThrowWrapper( org.reactivestreams.Publisher<test.fixtures.sink.SinkPayload> payloads, RpcOptions rpcOptions) {
            throw new UnsupportedOperationException();
        }

        reactor.core.publisher.Mono<test.fixtures.sink.FinalResponse> methodBothThrow( org.reactivestreams.Publisher<test.fixtures.sink.SinkPayload> payloads);

        default reactor.core.publisher.Mono<test.fixtures.sink.FinalResponse> methodBothThrow( org.reactivestreams.Publisher<test.fixtures.sink.SinkPayload> payloads, RpcOptions rpcOptions) {
            throw new UnsupportedOperationException();
        }

        default reactor.core.publisher.Mono<ResponseWrapper<test.fixtures.sink.FinalResponse>> methodBothThrowWrapper( org.reactivestreams.Publisher<test.fixtures.sink.SinkPayload> payloads, RpcOptions rpcOptions) {
            throw new UnsupportedOperationException();
        }

        reactor.core.publisher.Mono<test.fixtures.sink.FinalResponse> methodFast( org.reactivestreams.Publisher<test.fixtures.sink.SinkPayload> payloads);

        default reactor.core.publisher.Mono<test.fixtures.sink.FinalResponse> methodFast( org.reactivestreams.Publisher<test.fixtures.sink.SinkPayload> payloads, RpcOptions rpcOptions) {
            throw new UnsupportedOperationException();
        }

        default reactor.core.publisher.Mono<ResponseWrapper<test.fixtures.sink.FinalResponse>> methodFastWrapper( org.reactivestreams.Publisher<test.fixtures.sink.SinkPayload> payloads, RpcOptions rpcOptions) {
            throw new UnsupportedOperationException();
        }

    }
}
