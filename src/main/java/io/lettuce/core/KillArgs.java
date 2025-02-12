/*
 * Copyright 2011-Present, Redis Ltd. and Contributors
 * All rights reserved.
 *
 * Licensed under the MIT License.
 *
 * This file contains contributions from third-party contributors
 * licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.lettuce.core;

import static io.lettuce.core.protocol.CommandKeyword.*;

import io.lettuce.core.internal.LettuceAssert;
import io.lettuce.core.protocol.CommandArgs;
import io.lettuce.core.protocol.CommandType;

/**
 *
 * Argument list builder for the Redis <a href="https://redis.io/commands/client-kill">CLIENT KILL</a> command. Static import
 * the methods from {@link Builder} and chain the method calls: {@code id(1).skipme()}.
 * <p>
 * {@link KillArgs} is a mutable object and instances should be used only once to avoid shared mutable state.
 *
 * @author Mark Paluch
 * @author dengliming
 * @since 3.0
 */
public class KillArgs implements CompositeArgument {

    private enum Type {
        NORMAL, MASTER, SLAVE, PUBSUB
    }

    private Boolean skipme;

    private String addr;

    private String laddr;

    private Long id;

    private Type type;

    private String username;

    private Long maxAge;

    /**
     * Builder entry points for {@link KillArgs}.
     */
    public static class Builder {

        /**
         * Utility constructor.
         */
        private Builder() {
        }

        /**
         * Creates new {@link KillArgs} and enabling {@literal SKIPME YES}.
         *
         * @return new {@link KillArgs} with {@literal SKIPME YES} enabled.
         * @see KillArgs#skipme()
         */
        public static KillArgs skipme() {
            return new KillArgs().skipme();
        }

        /**
         * Creates new {@link KillArgs} setting {@literal ADDR} (Remote Address).
         *
         * @param addr must not be {@code null}.
         * @return new {@link KillArgs} with {@literal ADDR} set.
         * @see KillArgs#addr(String)
         */
        public static KillArgs addr(String addr) {
            return new KillArgs().addr(addr);
        }

        /**
         * Creates new {@link KillArgs} setting {@literal LADDR} (Local Address).
         *
         * @param laddr must not be {@code null}.
         * @return new {@link KillArgs} with {@literal LADDR} set.
         * @see KillArgs#laddr(String)
         */
        public static KillArgs laddr(String laddr) {
            return new KillArgs().laddr(laddr);
        }

        /**
         * Creates new {@link KillArgs} setting {@literal ID}.
         *
         * @param id client id.
         * @return new {@link KillArgs} with {@literal ID} set.
         * @see KillArgs#id(long)
         */
        public static KillArgs id(long id) {
            return new KillArgs().id(id);
        }

        /**
         * Creates new {@link KillArgs} setting {@literal TYPE PUBSUB}.
         *
         * @return new {@link KillArgs} with {@literal TYPE PUBSUB} set.
         * @see KillArgs#type(Type)
         */
        public static KillArgs typePubsub() {
            return new KillArgs().type(Type.PUBSUB);
        }

        /**
         * Creates new {@link KillArgs} setting {@literal TYPE NORMAL}.
         *
         * @return new {@link KillArgs} with {@literal TYPE NORMAL} set.
         * @see KillArgs#type(Type)
         */
        public static KillArgs typeNormal() {
            return new KillArgs().type(Type.NORMAL);
        }

        /**
         * Creates new {@link KillArgs} setting {@literal TYPE MASTER}.
         *
         * @return new {@link KillArgs} with {@literal TYPE MASTER} set.
         * @see KillArgs#type(Type)
         * @since 5.0.4
         */
        public static KillArgs typeMaster() {
            return new KillArgs().type(Type.MASTER);
        }

        /**
         * Creates new {@link KillArgs} setting {@literal TYPE SLAVE}.
         *
         * @return new {@link KillArgs} with {@literal TYPE SLAVE} set.
         * @see KillArgs#type(Type)
         */
        public static KillArgs typeSlave() {
            return new KillArgs().type(Type.SLAVE);
        }

        /**
         * Creates new {@link KillArgs} setting {@literal USER}.
         *
         * @return new {@link KillArgs} with {@literal USER} set.
         * @see KillArgs#user(String)
         * @since 6.1
         */
        public static KillArgs user(String username) {
            return new KillArgs().user(username);
        }

        /**
         * Creates new {@link KillArgs} setting {@literal MAXAGE}.
         *
         * @return new {@link KillArgs} with {@literal MAXAGE} set.
         * @see KillArgs#maxAge(Long)
         * @since 7.0
         */
        public static KillArgs maxAge(Long maxAge) {
            return new KillArgs().maxAge(maxAge);
        }

    }

    /**
     * By default this option is enabled, that is, the client calling the command will not get killed, however setting this
     * option to no will have the effect of also killing the client calling the command.
     *
     * @return {@code this} {@link MigrateArgs}.
     */
    public KillArgs skipme() {
        return this.skipme(true);
    }

    /**
     * By default this option is enabled, that is, the client calling the command will not get killed, however setting this
     * option to no will have the effect of also killing the client calling the command.
     *
     * @param state
     * @return {@code this} {@link KillArgs}.
     */
    public KillArgs skipme(boolean state) {

        this.skipme = state;
        return this;
    }

    /**
     * Kill the client at {@code addr} (Remote Address).
     *
     * @param addr must not be {@code null}.
     * @return {@code this} {@link KillArgs}.
     */
    public KillArgs addr(String addr) {

        LettuceAssert.notNull(addr, "Client address must not be null");

        this.addr = addr;
        return this;
    }

    /**
     * Kill the client at {@code laddr} (Local Address).
     *
     * @param laddr must not be {@code null}.
     * @return {@code this} {@link KillArgs}.
     * @since 6.1
     */
    public KillArgs laddr(String laddr) {

        LettuceAssert.notNull(laddr, "Local client address must not be null");

        this.laddr = laddr;
        return this;
    }

    /**
     * Kill the client with its client {@code id}.
     *
     * @param id
     * @return {@code this} {@link KillArgs}.
     */
    public KillArgs id(long id) {

        this.id = id;
        return this;
    }

    /**
     * This closes the connections of all the clients in the specified {@link KillArgs.Type}. Note that clients blocked into the
     * {@literal MONITOR} command are considered to belong to the normal class.
     *
     * @param type must not be {@code null}.
     * @return {@code this} {@link KillArgs}.
     */
    public KillArgs type(Type type) {

        LettuceAssert.notNull(type, "Type must not be null");

        this.type = type;
        return this;
    }

    /**
     * Closes all the connections that are older than the specified age, in seconds.
     *
     * @param maxAge must not be {@code null}.
     * @return {@code this} {@link KillArgs}.
     * @since 7.0
     */
    public KillArgs maxAge(Long maxAge) {

        LettuceAssert.notNull(maxAge, "MaxAge must not be null");

        this.maxAge = maxAge;
        return this;
    }

    /**
     * Closes all the connections that are authenticated with the specified ACL {@code username}.
     *
     * @param username must not be {@code null}.
     * @return {@code this} {@link KillArgs}.
     * @since 6.1
     */
    public KillArgs user(String username) {

        LettuceAssert.notNull(username, "UserName must not be null");

        this.username = username;
        return this;
    }

    @Override
    public <K, V> void build(CommandArgs<K, V> args) {

        if (skipme != null) {
            args.add(SKIPME).add(skipme ? "YES" : "NO");
        }

        if (id != null) {
            args.add(ID).add(id);
        }

        if (addr != null) {
            args.add(ADDR).add(addr);
        }

        if (laddr != null) {
            args.add("LADDR").add(laddr);
        }

        if (type != null) {
            args.add(CommandType.TYPE).add(type.name().toLowerCase());
        }

        if (username != null) {
            args.add("USER").add(username);
        }

        if (maxAge != null) {
            args.add("MAXAGE").add(maxAge);
        }
    }

}
