package io.lettuce.core.pubsub.api.reactive;

import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import reactor.core.publisher.Mono;
import io.lettuce.core.api.reactive.RedisReactiveCommands;
import io.lettuce.core.pubsub.StatefulRedisPubSubConnection;

/**
 * Asynchronous and thread-safe Redis PubSub API.
 *
 * @param <K> Key type.
 * @param <V> Value type.
 * @author Mark Paluch
 * @since 5.0
 */
public interface RedisPubSubReactiveCommands<K, V> extends RedisReactiveCommands<K, V> {

    /**
     * Flux for messages ({@literal pmessage}) received though pattern subscriptions. The connection needs to be subscribed to
     * one or more patterns using {@link #psubscribe(Object[])}.
     * <p>
     * Warning! This method uses {@link reactor.core.publisher.FluxSink.OverflowStrategy#BUFFER} This does unbounded buffering
     * and may lead to {@link OutOfMemoryError}. Use {@link #observePatterns(FluxSink.OverflowStrategy)} to specify a different
     * strategy.
     * </p>
     *
     * @return hot Flux for subscriptions to {@literal pmessage}'s.
     */
    Flux<PatternMessage<K, V>> observePatterns();

    /**
     * Flux for messages ({@literal pmessage}) received though pattern subscriptions. The connection needs to be subscribed to
     * one or more patterns using {@link #psubscribe(Object[])}.
     *
     * @param overflowStrategy the overflow strategy to use.
     * @return hot Flux for subscriptions to {@literal pmessage}'s.
     */
    Flux<PatternMessage<K, V>> observePatterns(FluxSink.OverflowStrategy overflowStrategy);

    /**
     * Flux for messages ({@literal message}) received though channel subscriptions. The connection needs to be subscribed to
     * one or more channels using {@link #subscribe(Object[])}.
     *
     * <p>
     * Warning! This method uses {@link reactor.core.publisher.FluxSink.OverflowStrategy#BUFFER} This does unbounded buffering
     * and may lead to {@link OutOfMemoryError}. Use {@link #observeChannels(FluxSink.OverflowStrategy)} to specify a different
     * strategy.
     * </p>
     *
     * @return hot Flux for subscriptions to {@literal message}'s.
     */
    Flux<ChannelMessage<K, V>> observeChannels();

    /**
     * Flux for messages ({@literal message}) received though channel subscriptions. The connection needs to be subscribed to
     * one or more channels using {@link #subscribe(Object[])}.
     *
     * @param overflowStrategy the overflow strategy to use.
     * @return hot Flux for subscriptions to {@literal message}'s.
     */
    Flux<ChannelMessage<K, V>> observeChannels(FluxSink.OverflowStrategy overflowStrategy);

    /**
     * Listen for messages published to channels matching the given patterns. The {@link Mono} completes without a result as
     * soon as the pattern subscription is registered.
     *
     * @param patterns the patterns.
     * @return Mono&lt;Void&gt; Mono for {@code psubscribe} command.
     */
    Mono<Void> psubscribe(K... patterns);

    /**
     * Stop listening for messages posted to channels matching the given patterns. The {@link Mono} completes without a result
     * as soon as the pattern subscription is unregistered.
     *
     * @param patterns the patterns.
     * @return Mono&lt;Void&gt; Mono for {@code punsubscribe} command.
     */
    Mono<Void> punsubscribe(K... patterns);

    /**
     * Listen for messages published to the given channels. The {@link Mono} completes without a result as soon as the *
     * subscription is registered.
     *
     * @param channels the channels.
     * @return Mono&lt;Void&gt; Mono for {@code subscribe} command.
     */
    Mono<Void> subscribe(K... channels);

    /**
     * Stop listening for messages posted to the given channels. The {@link Mono} completes without a result as soon as the
     * subscription is unregistered.
     *
     * @param channels the channels.
     * @return Mono&lt;Void&gt; Mono for {@code unsubscribe} command.
     */
    Mono<Void> unsubscribe(K... channels);

    /**
     * Listen for messages published to the given shard channels. The {@link Mono} completes without a result as soon as the *
     * subscription is registered.
     *
     * @param shardChannels the channels.
     * @return Mono&lt;Void&gt; Mono for {@code subscribe} command.
     * @since 7.0
     */
    Mono<Void> ssubscribe(K... shardChannels);

    /**
     * Stop listening for messages posted to the given channels. The {@link Mono} completes without a result as soon as the
     * subscription is unregistered.
     *
     * @param shardChannels the channels.
     * @return Mono&lt;Void&gt; Mono for {@code unsubscribe} command.
     * @since 7.0
     */
    Mono<Void> sunsubscribe(K... shardChannels);

    /**
     * @return the underlying connection.
     * @since 6.2, will be removed with Lettuce 7 to avoid exposing the underlying connection.
     */
    @Deprecated
    StatefulRedisPubSubConnection<K, V> getStatefulConnection();

}
