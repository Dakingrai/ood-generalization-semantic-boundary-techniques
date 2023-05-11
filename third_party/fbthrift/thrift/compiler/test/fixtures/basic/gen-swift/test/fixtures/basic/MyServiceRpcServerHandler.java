/**
 * Autogenerated by Thrift
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 *  @generated
 */

package test.fixtures.basic;

import java.util.*;
import org.apache.thrift.protocol.*;

public class MyServiceRpcServerHandler 
  implements com.facebook.swift.transport.server.RpcServerHandler {

  private final java.util.Map<String, com.facebook.swift.transport.server.RpcServerHandler> _methodMap;

  private final MyService.Reactive _delegate;

  private final java.util.List<com.facebook.swift.transport.payload.Reader> _pingReaders;
  private final java.util.List<com.facebook.swift.transport.payload.Reader> _getRandomDataReaders;
  private final java.util.List<com.facebook.swift.transport.payload.Reader> _sinkReaders;
  private final java.util.List<com.facebook.swift.transport.payload.Reader> _putDataByIdReaders;
  private final java.util.List<com.facebook.swift.transport.payload.Reader> _hasDataByIdReaders;
  private final java.util.List<com.facebook.swift.transport.payload.Reader> _getDataByIdReaders;
  private final java.util.List<com.facebook.swift.transport.payload.Reader> _deleteDataByIdReaders;
  private final java.util.List<com.facebook.swift.transport.payload.Reader> _lobDataByIdReaders;

  private final java.util.List<com.facebook.swift.service.ThriftEventHandler> _eventHandlers;

  public MyServiceRpcServerHandler(MyService _delegate,
                                    java.util.List<com.facebook.swift.service.ThriftEventHandler> _eventHandlers,
                                    reactor.core.scheduler.Scheduler _scheduler) {
    this(new MyServiceBlockingReactiveWrapper(_delegate, _scheduler), _eventHandlers);
  }

  public MyServiceRpcServerHandler(MyService.Async _delegate,
                                    java.util.List<com.facebook.swift.service.ThriftEventHandler> _eventHandlers) {
    this(new MyServiceAsyncReactiveWrapper(_delegate), _eventHandlers);
  }

  public MyServiceRpcServerHandler(MyService.Reactive _delegate,
                                    java.util.List<com.facebook.swift.service.ThriftEventHandler> _eventHandlers) {
    
    this._methodMap = new java.util.HashMap<>();
    this._delegate = _delegate;
    this._eventHandlers = _eventHandlers;

    _methodMap.put("ping", this);
    _pingReaders = _createpingReaders();

    _methodMap.put("getRandomData", this);
    _getRandomDataReaders = _creategetRandomDataReaders();

    _methodMap.put("sink", this);
    _sinkReaders = _createsinkReaders();

    _methodMap.put("putDataById", this);
    _putDataByIdReaders = _createputDataByIdReaders();

    _methodMap.put("hasDataById", this);
    _hasDataByIdReaders = _createhasDataByIdReaders();

    _methodMap.put("getDataById", this);
    _getDataByIdReaders = _creategetDataByIdReaders();

    _methodMap.put("deleteDataById", this);
    _deleteDataByIdReaders = _createdeleteDataByIdReaders();

    _methodMap.put("lobDataById", this);
    _lobDataByIdReaders = _createlobDataByIdReaders();

  }

  private static java.util.List<com.facebook.swift.transport.payload.Reader> _createpingReaders() {
    java.util.List<com.facebook.swift.transport.payload.Reader> _readerList = new java.util.ArrayList<>();


    return _readerList;
  }

  private static com.facebook.swift.transport.payload.Writer _createpingWriter(
      final Object _r,
      final com.facebook.swift.service.ContextChain _chain,
      final int _seqId) {
      return oprot -> {
      try {
        oprot.writeMessageBegin(new org.apache.thrift.protocol.TMessage("ping", TMessageType.REPLY, _seqId));
        oprot.writeStructBegin(com.facebook.swift.transport.util.GeneratedUtil.TSTRUCT);

        

        oprot.writeFieldBegin(com.facebook.swift.transport.util.GeneratedUtil.VOID_FIELD);


        oprot.writeFieldEnd();
        oprot.writeFieldStop();
        oprot.writeStructEnd();
        oprot.writeMessageEnd();

        _chain.postWrite(_r);
      } catch (Throwable _e) {
        throw reactor.core.Exceptions.propagate(_e);
      }
    };
  }

  private static reactor.core.publisher.Mono<com.facebook.swift.transport.payload.ServerResponsePayload>
    _doping(
    MyService.Reactive _delegate,
    String _name,
    com.facebook.swift.transport.payload.ServerRequestPayload _payload,
    java.util.List<com.facebook.swift.transport.payload.Reader> _readers,
    java.util.List<com.facebook.swift.service.ThriftEventHandler> _eventHandlers) {
    final com.facebook.swift.service.ContextChain _chain = new com.facebook.swift.service.ContextChain(_eventHandlers, _name, _payload.getRequestContext());
          _chain.preRead();
          java.util.List<Object>_data = _payload.getData(_readers);
          java.util.Iterator<Object> _iterator = _data.iterator();


          _chain.postRead(_data);

          return _delegate
            .ping()
            .map(_response -> {
              _chain.preWrite(_response);
                com.facebook.swift.transport.payload.ServerResponsePayload _serverResponsePayload =
                    com.facebook.swift.transport.util.GeneratedUtil.createServerResponsePayload(
                        _payload,
                        _createpingWriter(_response, _chain, _payload.getMessageSeqId()));

                return _serverResponsePayload;
            })
            .<com.facebook.swift.transport.payload.ServerResponsePayload>onErrorResume(_t -> {
                _chain.preWriteException(_t);
                com.facebook.swift.transport.payload.Writer _exceptionWriter = null;

                com.facebook.swift.transport.payload.ServerResponsePayload _serverResponsePayload =
                    com.facebook.swift.transport.util.GeneratedUtil.createServerResponsePayload(
                        _payload,
                        _exceptionWriter);

                return reactor.core.publisher.Mono.just(_serverResponsePayload);
            });
  }
  private static java.util.List<com.facebook.swift.transport.payload.Reader> _creategetRandomDataReaders() {
    java.util.List<com.facebook.swift.transport.payload.Reader> _readerList = new java.util.ArrayList<>();


    return _readerList;
  }

  private static com.facebook.swift.transport.payload.Writer _creategetRandomDataWriter(
      final Object _r,
      final com.facebook.swift.service.ContextChain _chain,
      final int _seqId) {
      return oprot -> {
      try {
        oprot.writeMessageBegin(new org.apache.thrift.protocol.TMessage("getRandomData", TMessageType.REPLY, _seqId));
        oprot.writeStructBegin(com.facebook.swift.transport.util.GeneratedUtil.TSTRUCT);

        
        String _iter0 = (String)_r;
        oprot.writeFieldBegin(com.facebook.swift.transport.util.GeneratedUtil.STRING_FIELD);
oprot.writeString(_iter0);



        oprot.writeFieldEnd();
        oprot.writeFieldStop();
        oprot.writeStructEnd();
        oprot.writeMessageEnd();

        _chain.postWrite(_r);
      } catch (Throwable _e) {
        throw reactor.core.Exceptions.propagate(_e);
      }
    };
  }

  private static reactor.core.publisher.Mono<com.facebook.swift.transport.payload.ServerResponsePayload>
    _dogetRandomData(
    MyService.Reactive _delegate,
    String _name,
    com.facebook.swift.transport.payload.ServerRequestPayload _payload,
    java.util.List<com.facebook.swift.transport.payload.Reader> _readers,
    java.util.List<com.facebook.swift.service.ThriftEventHandler> _eventHandlers) {
    final com.facebook.swift.service.ContextChain _chain = new com.facebook.swift.service.ContextChain(_eventHandlers, _name, _payload.getRequestContext());
          _chain.preRead();
          java.util.List<Object>_data = _payload.getData(_readers);
          java.util.Iterator<Object> _iterator = _data.iterator();


          _chain.postRead(_data);

          return _delegate
            .getRandomData()
            .map(_response -> {
              _chain.preWrite(_response);
                com.facebook.swift.transport.payload.ServerResponsePayload _serverResponsePayload =
                    com.facebook.swift.transport.util.GeneratedUtil.createServerResponsePayload(
                        _payload,
                        _creategetRandomDataWriter(_response, _chain, _payload.getMessageSeqId()));

                return _serverResponsePayload;
            })
            .<com.facebook.swift.transport.payload.ServerResponsePayload>onErrorResume(_t -> {
                _chain.preWriteException(_t);
                com.facebook.swift.transport.payload.Writer _exceptionWriter = null;

                com.facebook.swift.transport.payload.ServerResponsePayload _serverResponsePayload =
                    com.facebook.swift.transport.util.GeneratedUtil.createServerResponsePayload(
                        _payload,
                        _exceptionWriter);

                return reactor.core.publisher.Mono.just(_serverResponsePayload);
            });
  }
  private static java.util.List<com.facebook.swift.transport.payload.Reader> _createsinkReaders() {
    java.util.List<com.facebook.swift.transport.payload.Reader> _readerList = new java.util.ArrayList<>();

    
    _readerList.add(oprot -> {
      try {
        long _r = oprot.readI64();
        return _r;

      } catch (Throwable _e) {
        throw reactor.core.Exceptions.propagate(_e);
      }
    });

    return _readerList;
  }

  private static com.facebook.swift.transport.payload.Writer _createsinkWriter(
      final Object _r,
      final com.facebook.swift.service.ContextChain _chain,
      final int _seqId) {
      return oprot -> {
      try {
        oprot.writeMessageBegin(new org.apache.thrift.protocol.TMessage("sink", TMessageType.REPLY, _seqId));
        oprot.writeStructBegin(com.facebook.swift.transport.util.GeneratedUtil.TSTRUCT);

        

        oprot.writeFieldBegin(com.facebook.swift.transport.util.GeneratedUtil.VOID_FIELD);


        oprot.writeFieldEnd();
        oprot.writeFieldStop();
        oprot.writeStructEnd();
        oprot.writeMessageEnd();

        _chain.postWrite(_r);
      } catch (Throwable _e) {
        throw reactor.core.Exceptions.propagate(_e);
      }
    };
  }

  private static reactor.core.publisher.Mono<com.facebook.swift.transport.payload.ServerResponsePayload>
    _dosink(
    MyService.Reactive _delegate,
    String _name,
    com.facebook.swift.transport.payload.ServerRequestPayload _payload,
    java.util.List<com.facebook.swift.transport.payload.Reader> _readers,
    java.util.List<com.facebook.swift.service.ThriftEventHandler> _eventHandlers) {
    final com.facebook.swift.service.ContextChain _chain = new com.facebook.swift.service.ContextChain(_eventHandlers, _name, _payload.getRequestContext());
          _chain.preRead();
          java.util.List<Object>_data = _payload.getData(_readers);
          java.util.Iterator<Object> _iterator = _data.iterator();

          long sink = (long) _iterator.next();

          _chain.postRead(_data);

          return _delegate
            .sink(sink)
            .map(_response -> {
              _chain.preWrite(_response);
                com.facebook.swift.transport.payload.ServerResponsePayload _serverResponsePayload =
                    com.facebook.swift.transport.util.GeneratedUtil.createServerResponsePayload(
                        _payload,
                        _createsinkWriter(_response, _chain, _payload.getMessageSeqId()));

                return _serverResponsePayload;
            })
            .<com.facebook.swift.transport.payload.ServerResponsePayload>onErrorResume(_t -> {
                _chain.preWriteException(_t);
                com.facebook.swift.transport.payload.Writer _exceptionWriter = null;

                com.facebook.swift.transport.payload.ServerResponsePayload _serverResponsePayload =
                    com.facebook.swift.transport.util.GeneratedUtil.createServerResponsePayload(
                        _payload,
                        _exceptionWriter);

                return reactor.core.publisher.Mono.just(_serverResponsePayload);
            });
  }
  private static java.util.List<com.facebook.swift.transport.payload.Reader> _createputDataByIdReaders() {
    java.util.List<com.facebook.swift.transport.payload.Reader> _readerList = new java.util.ArrayList<>();

    
    _readerList.add(oprot -> {
      try {
        long _r = oprot.readI64();
        return _r;

      } catch (Throwable _e) {
        throw reactor.core.Exceptions.propagate(_e);
      }
    });
    
    _readerList.add(oprot -> {
      try {
        String _r = oprot.readString();
        return _r;

      } catch (Throwable _e) {
        throw reactor.core.Exceptions.propagate(_e);
      }
    });

    return _readerList;
  }

  private static com.facebook.swift.transport.payload.Writer _createputDataByIdWriter(
      final Object _r,
      final com.facebook.swift.service.ContextChain _chain,
      final int _seqId) {
      return oprot -> {
      try {
        oprot.writeMessageBegin(new org.apache.thrift.protocol.TMessage("putDataById", TMessageType.REPLY, _seqId));
        oprot.writeStructBegin(com.facebook.swift.transport.util.GeneratedUtil.TSTRUCT);

        

        oprot.writeFieldBegin(com.facebook.swift.transport.util.GeneratedUtil.VOID_FIELD);


        oprot.writeFieldEnd();
        oprot.writeFieldStop();
        oprot.writeStructEnd();
        oprot.writeMessageEnd();

        _chain.postWrite(_r);
      } catch (Throwable _e) {
        throw reactor.core.Exceptions.propagate(_e);
      }
    };
  }

  private static reactor.core.publisher.Mono<com.facebook.swift.transport.payload.ServerResponsePayload>
    _doputDataById(
    MyService.Reactive _delegate,
    String _name,
    com.facebook.swift.transport.payload.ServerRequestPayload _payload,
    java.util.List<com.facebook.swift.transport.payload.Reader> _readers,
    java.util.List<com.facebook.swift.service.ThriftEventHandler> _eventHandlers) {
    final com.facebook.swift.service.ContextChain _chain = new com.facebook.swift.service.ContextChain(_eventHandlers, _name, _payload.getRequestContext());
          _chain.preRead();
          java.util.List<Object>_data = _payload.getData(_readers);
          java.util.Iterator<Object> _iterator = _data.iterator();

          long id = (long) _iterator.next();
          String data = (String) _iterator.next();

          _chain.postRead(_data);

          return _delegate
            .putDataById(id, data)
            .map(_response -> {
              _chain.preWrite(_response);
                com.facebook.swift.transport.payload.ServerResponsePayload _serverResponsePayload =
                    com.facebook.swift.transport.util.GeneratedUtil.createServerResponsePayload(
                        _payload,
                        _createputDataByIdWriter(_response, _chain, _payload.getMessageSeqId()));

                return _serverResponsePayload;
            })
            .<com.facebook.swift.transport.payload.ServerResponsePayload>onErrorResume(_t -> {
                _chain.preWriteException(_t);
                com.facebook.swift.transport.payload.Writer _exceptionWriter = null;

                com.facebook.swift.transport.payload.ServerResponsePayload _serverResponsePayload =
                    com.facebook.swift.transport.util.GeneratedUtil.createServerResponsePayload(
                        _payload,
                        _exceptionWriter);

                return reactor.core.publisher.Mono.just(_serverResponsePayload);
            });
  }
  private static java.util.List<com.facebook.swift.transport.payload.Reader> _createhasDataByIdReaders() {
    java.util.List<com.facebook.swift.transport.payload.Reader> _readerList = new java.util.ArrayList<>();

    
    _readerList.add(oprot -> {
      try {
        long _r = oprot.readI64();
        return _r;

      } catch (Throwable _e) {
        throw reactor.core.Exceptions.propagate(_e);
      }
    });

    return _readerList;
  }

  private static com.facebook.swift.transport.payload.Writer _createhasDataByIdWriter(
      final Object _r,
      final com.facebook.swift.service.ContextChain _chain,
      final int _seqId) {
      return oprot -> {
      try {
        oprot.writeMessageBegin(new org.apache.thrift.protocol.TMessage("hasDataById", TMessageType.REPLY, _seqId));
        oprot.writeStructBegin(com.facebook.swift.transport.util.GeneratedUtil.TSTRUCT);

        
        boolean _iter0 = (boolean)_r;
        oprot.writeFieldBegin(com.facebook.swift.transport.util.GeneratedUtil.BOOL_FIELD);
oprot.writeBool(_iter0);



        oprot.writeFieldEnd();
        oprot.writeFieldStop();
        oprot.writeStructEnd();
        oprot.writeMessageEnd();

        _chain.postWrite(_r);
      } catch (Throwable _e) {
        throw reactor.core.Exceptions.propagate(_e);
      }
    };
  }

  private static reactor.core.publisher.Mono<com.facebook.swift.transport.payload.ServerResponsePayload>
    _dohasDataById(
    MyService.Reactive _delegate,
    String _name,
    com.facebook.swift.transport.payload.ServerRequestPayload _payload,
    java.util.List<com.facebook.swift.transport.payload.Reader> _readers,
    java.util.List<com.facebook.swift.service.ThriftEventHandler> _eventHandlers) {
    final com.facebook.swift.service.ContextChain _chain = new com.facebook.swift.service.ContextChain(_eventHandlers, _name, _payload.getRequestContext());
          _chain.preRead();
          java.util.List<Object>_data = _payload.getData(_readers);
          java.util.Iterator<Object> _iterator = _data.iterator();

          long id = (long) _iterator.next();

          _chain.postRead(_data);

          return _delegate
            .hasDataById(id)
            .map(_response -> {
              _chain.preWrite(_response);
                com.facebook.swift.transport.payload.ServerResponsePayload _serverResponsePayload =
                    com.facebook.swift.transport.util.GeneratedUtil.createServerResponsePayload(
                        _payload,
                        _createhasDataByIdWriter(_response, _chain, _payload.getMessageSeqId()));

                return _serverResponsePayload;
            })
            .<com.facebook.swift.transport.payload.ServerResponsePayload>onErrorResume(_t -> {
                _chain.preWriteException(_t);
                com.facebook.swift.transport.payload.Writer _exceptionWriter = null;

                com.facebook.swift.transport.payload.ServerResponsePayload _serverResponsePayload =
                    com.facebook.swift.transport.util.GeneratedUtil.createServerResponsePayload(
                        _payload,
                        _exceptionWriter);

                return reactor.core.publisher.Mono.just(_serverResponsePayload);
            });
  }
  private static java.util.List<com.facebook.swift.transport.payload.Reader> _creategetDataByIdReaders() {
    java.util.List<com.facebook.swift.transport.payload.Reader> _readerList = new java.util.ArrayList<>();

    
    _readerList.add(oprot -> {
      try {
        long _r = oprot.readI64();
        return _r;

      } catch (Throwable _e) {
        throw reactor.core.Exceptions.propagate(_e);
      }
    });

    return _readerList;
  }

  private static com.facebook.swift.transport.payload.Writer _creategetDataByIdWriter(
      final Object _r,
      final com.facebook.swift.service.ContextChain _chain,
      final int _seqId) {
      return oprot -> {
      try {
        oprot.writeMessageBegin(new org.apache.thrift.protocol.TMessage("getDataById", TMessageType.REPLY, _seqId));
        oprot.writeStructBegin(com.facebook.swift.transport.util.GeneratedUtil.TSTRUCT);

        
        String _iter0 = (String)_r;
        oprot.writeFieldBegin(com.facebook.swift.transport.util.GeneratedUtil.STRING_FIELD);
oprot.writeString(_iter0);



        oprot.writeFieldEnd();
        oprot.writeFieldStop();
        oprot.writeStructEnd();
        oprot.writeMessageEnd();

        _chain.postWrite(_r);
      } catch (Throwable _e) {
        throw reactor.core.Exceptions.propagate(_e);
      }
    };
  }

  private static reactor.core.publisher.Mono<com.facebook.swift.transport.payload.ServerResponsePayload>
    _dogetDataById(
    MyService.Reactive _delegate,
    String _name,
    com.facebook.swift.transport.payload.ServerRequestPayload _payload,
    java.util.List<com.facebook.swift.transport.payload.Reader> _readers,
    java.util.List<com.facebook.swift.service.ThriftEventHandler> _eventHandlers) {
    final com.facebook.swift.service.ContextChain _chain = new com.facebook.swift.service.ContextChain(_eventHandlers, _name, _payload.getRequestContext());
          _chain.preRead();
          java.util.List<Object>_data = _payload.getData(_readers);
          java.util.Iterator<Object> _iterator = _data.iterator();

          long id = (long) _iterator.next();

          _chain.postRead(_data);

          return _delegate
            .getDataById(id)
            .map(_response -> {
              _chain.preWrite(_response);
                com.facebook.swift.transport.payload.ServerResponsePayload _serverResponsePayload =
                    com.facebook.swift.transport.util.GeneratedUtil.createServerResponsePayload(
                        _payload,
                        _creategetDataByIdWriter(_response, _chain, _payload.getMessageSeqId()));

                return _serverResponsePayload;
            })
            .<com.facebook.swift.transport.payload.ServerResponsePayload>onErrorResume(_t -> {
                _chain.preWriteException(_t);
                com.facebook.swift.transport.payload.Writer _exceptionWriter = null;

                com.facebook.swift.transport.payload.ServerResponsePayload _serverResponsePayload =
                    com.facebook.swift.transport.util.GeneratedUtil.createServerResponsePayload(
                        _payload,
                        _exceptionWriter);

                return reactor.core.publisher.Mono.just(_serverResponsePayload);
            });
  }
  private static java.util.List<com.facebook.swift.transport.payload.Reader> _createdeleteDataByIdReaders() {
    java.util.List<com.facebook.swift.transport.payload.Reader> _readerList = new java.util.ArrayList<>();

    
    _readerList.add(oprot -> {
      try {
        long _r = oprot.readI64();
        return _r;

      } catch (Throwable _e) {
        throw reactor.core.Exceptions.propagate(_e);
      }
    });

    return _readerList;
  }

  private static com.facebook.swift.transport.payload.Writer _createdeleteDataByIdWriter(
      final Object _r,
      final com.facebook.swift.service.ContextChain _chain,
      final int _seqId) {
      return oprot -> {
      try {
        oprot.writeMessageBegin(new org.apache.thrift.protocol.TMessage("deleteDataById", TMessageType.REPLY, _seqId));
        oprot.writeStructBegin(com.facebook.swift.transport.util.GeneratedUtil.TSTRUCT);

        

        oprot.writeFieldBegin(com.facebook.swift.transport.util.GeneratedUtil.VOID_FIELD);


        oprot.writeFieldEnd();
        oprot.writeFieldStop();
        oprot.writeStructEnd();
        oprot.writeMessageEnd();

        _chain.postWrite(_r);
      } catch (Throwable _e) {
        throw reactor.core.Exceptions.propagate(_e);
      }
    };
  }

  private static reactor.core.publisher.Mono<com.facebook.swift.transport.payload.ServerResponsePayload>
    _dodeleteDataById(
    MyService.Reactive _delegate,
    String _name,
    com.facebook.swift.transport.payload.ServerRequestPayload _payload,
    java.util.List<com.facebook.swift.transport.payload.Reader> _readers,
    java.util.List<com.facebook.swift.service.ThriftEventHandler> _eventHandlers) {
    final com.facebook.swift.service.ContextChain _chain = new com.facebook.swift.service.ContextChain(_eventHandlers, _name, _payload.getRequestContext());
          _chain.preRead();
          java.util.List<Object>_data = _payload.getData(_readers);
          java.util.Iterator<Object> _iterator = _data.iterator();

          long id = (long) _iterator.next();

          _chain.postRead(_data);

          return _delegate
            .deleteDataById(id)
            .map(_response -> {
              _chain.preWrite(_response);
                com.facebook.swift.transport.payload.ServerResponsePayload _serverResponsePayload =
                    com.facebook.swift.transport.util.GeneratedUtil.createServerResponsePayload(
                        _payload,
                        _createdeleteDataByIdWriter(_response, _chain, _payload.getMessageSeqId()));

                return _serverResponsePayload;
            })
            .<com.facebook.swift.transport.payload.ServerResponsePayload>onErrorResume(_t -> {
                _chain.preWriteException(_t);
                com.facebook.swift.transport.payload.Writer _exceptionWriter = null;

                com.facebook.swift.transport.payload.ServerResponsePayload _serverResponsePayload =
                    com.facebook.swift.transport.util.GeneratedUtil.createServerResponsePayload(
                        _payload,
                        _exceptionWriter);

                return reactor.core.publisher.Mono.just(_serverResponsePayload);
            });
  }
  private static java.util.List<com.facebook.swift.transport.payload.Reader> _createlobDataByIdReaders() {
    java.util.List<com.facebook.swift.transport.payload.Reader> _readerList = new java.util.ArrayList<>();

    
    _readerList.add(oprot -> {
      try {
        long _r = oprot.readI64();
        return _r;

      } catch (Throwable _e) {
        throw reactor.core.Exceptions.propagate(_e);
      }
    });
    
    _readerList.add(oprot -> {
      try {
        String _r = oprot.readString();
        return _r;

      } catch (Throwable _e) {
        throw reactor.core.Exceptions.propagate(_e);
      }
    });

    return _readerList;
  }

  private static com.facebook.swift.transport.payload.Writer _createlobDataByIdWriter(
      final Object _r,
      final com.facebook.swift.service.ContextChain _chain,
      final int _seqId) {
      return oprot -> {
      try {
        oprot.writeMessageBegin(new org.apache.thrift.protocol.TMessage("lobDataById", TMessageType.REPLY, _seqId));
        oprot.writeStructBegin(com.facebook.swift.transport.util.GeneratedUtil.TSTRUCT);

        

        oprot.writeFieldBegin(com.facebook.swift.transport.util.GeneratedUtil.VOID_FIELD);


        oprot.writeFieldEnd();
        oprot.writeFieldStop();
        oprot.writeStructEnd();
        oprot.writeMessageEnd();

        _chain.postWrite(_r);
      } catch (Throwable _e) {
        throw reactor.core.Exceptions.propagate(_e);
      }
    };
  }

  private static reactor.core.publisher.Mono<com.facebook.swift.transport.payload.ServerResponsePayload>
    _dolobDataById(
    MyService.Reactive _delegate,
    String _name,
    com.facebook.swift.transport.payload.ServerRequestPayload _payload,
    java.util.List<com.facebook.swift.transport.payload.Reader> _readers,
    java.util.List<com.facebook.swift.service.ThriftEventHandler> _eventHandlers) {
    final com.facebook.swift.service.ContextChain _chain = new com.facebook.swift.service.ContextChain(_eventHandlers, _name, _payload.getRequestContext());
          _chain.preRead();
          java.util.List<Object>_data = _payload.getData(_readers);
          java.util.Iterator<Object> _iterator = _data.iterator();

          long id = (long) _iterator.next();
          String data = (String) _iterator.next();

          _chain.postRead(_data);

          return _delegate
            .lobDataById(id, data)
            .map(_response -> {
              _chain.preWrite(_response);
                com.facebook.swift.transport.payload.ServerResponsePayload _serverResponsePayload =
                    com.facebook.swift.transport.util.GeneratedUtil.createServerResponsePayload(
                        _payload,
                        _createlobDataByIdWriter(_response, _chain, _payload.getMessageSeqId()));

                return _serverResponsePayload;
            })
            .<com.facebook.swift.transport.payload.ServerResponsePayload>onErrorResume(_t -> {
                _chain.preWriteException(_t);
                com.facebook.swift.transport.payload.Writer _exceptionWriter = null;

                com.facebook.swift.transport.payload.ServerResponsePayload _serverResponsePayload =
                    com.facebook.swift.transport.util.GeneratedUtil.createServerResponsePayload(
                        _payload,
                        _exceptionWriter);

                return reactor.core.publisher.Mono.just(_serverResponsePayload);
            });
  }

  @Override
  public reactor.core.publisher.Mono<com.facebook.swift.transport.payload.ServerResponsePayload> singleRequestSingleResponse(com.facebook.swift.transport.payload.ServerRequestPayload _payload) {
    final String _name = _payload.getRequestRpcMetadata().getName();

    reactor.core.publisher.Mono<com.facebook.swift.transport.payload.ServerResponsePayload> _result;
    try {
      switch (_name) {
        case "ping":
          _result = _doping(_delegate, _name, _payload, _pingReaders, _eventHandlers);
        break;
        case "getRandomData":
          _result = _dogetRandomData(_delegate, _name, _payload, _getRandomDataReaders, _eventHandlers);
        break;
        case "sink":
          _result = _dosink(_delegate, _name, _payload, _sinkReaders, _eventHandlers);
        break;
        case "putDataById":
          _result = _doputDataById(_delegate, _name, _payload, _putDataByIdReaders, _eventHandlers);
        break;
        case "hasDataById":
          _result = _dohasDataById(_delegate, _name, _payload, _hasDataByIdReaders, _eventHandlers);
        break;
        case "getDataById":
          _result = _dogetDataById(_delegate, _name, _payload, _getDataByIdReaders, _eventHandlers);
        break;
        case "deleteDataById":
          _result = _dodeleteDataById(_delegate, _name, _payload, _deleteDataByIdReaders, _eventHandlers);
        break;
        case "lobDataById":
          _result = _dolobDataById(_delegate, _name, _payload, _lobDataByIdReaders, _eventHandlers);
        break;
        default: {
          _result = reactor.core.publisher.Mono.error(new org.apache.thrift.TApplicationException(org.apache.thrift.TApplicationException.UNKNOWN_METHOD, "no method found with name " + _name));
        }
      }
    } catch (Throwable _t) {
      _result = reactor.core.publisher.Mono.error(_t);
    }

    return _result;
  }

  public java.util.Map<String, com.facebook.swift.transport.server.RpcServerHandler> getMethodMap() {
      return _methodMap;
  }

}
