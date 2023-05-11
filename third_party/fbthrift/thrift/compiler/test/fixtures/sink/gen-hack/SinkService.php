<?hh // strict
/**
 * Autogenerated by Thrift
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 *  @generated
 */

/**
 * Original thrift service:-
 * SinkService
 */
interface SinkServiceAsyncIf extends \IThriftAsyncIf {
}

/**
 * Original thrift service:-
 * SinkService
 */
interface SinkServiceIf extends \IThriftSyncIf {
}

/**
 * Original thrift service:-
 * SinkService
 */
interface SinkServiceClientIf extends \IThriftSyncIf {
}

/**
 * Original thrift service:-
 * SinkService
 */
interface SinkServiceAsyncRpcOptionsIf extends \IThriftAsyncRpcOptionsIf {
}

/**
 * Original thrift service:-
 * SinkService
 */
trait SinkServiceClientBase {
  require extends \ThriftClientBase;

}

class SinkServiceAsyncClient extends \ThriftClientBase implements SinkServiceAsyncIf {
  use SinkServiceClientBase;

}

class SinkServiceClient extends \ThriftClientBase implements SinkServiceClientIf {
  use SinkServiceClientBase;

  /* send and recv functions */
}

class SinkServiceAsyncRpcOptionsClient extends \ThriftClientBase implements SinkServiceAsyncRpcOptionsIf {
  use SinkServiceClientBase;

}

// HELPER FUNCTIONS AND STRUCTURES

class SinkServiceStaticMetadata implements \IThriftServiceStaticMetadata {
  public static function getServiceMetadata()[]: \tmeta_ThriftService {
    return tmeta_ThriftService::fromShape(
      shape(
        "name" => "SinkService",
      )
    );
  }
  public static function getAllStructuredAnnotations()[]: \TServiceAnnotations {
    return shape(
      'service' => dict[],
      'functions' => dict[
      ],
    );
  }
}

