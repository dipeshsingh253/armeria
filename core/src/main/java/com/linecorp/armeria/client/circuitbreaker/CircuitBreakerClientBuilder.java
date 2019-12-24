/*
 * Copyright 2018 LINE Corporation
 *
 * LINE Corporation licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

package com.linecorp.armeria.client.circuitbreaker;

import java.util.function.Function;

import com.linecorp.armeria.client.HttpClient;
import com.linecorp.armeria.common.HttpResponse;

/**
 * Builds a new {@link CircuitBreakerClient} or its decorator function.
 */
public class CircuitBreakerClientBuilder extends AbstractCircuitBreakerClientBuilder<HttpResponse> {

    private final boolean needsContentInStrategy;

    /**
     * Creates a new builder with the specified {@link CircuitBreakerStrategy}.
     */
    CircuitBreakerClientBuilder(CircuitBreakerStrategy strategy) {
        super(strategy);
        needsContentInStrategy = false;
    }

    /**
     * Creates a new builder with the specified {@link CircuitBreakerStrategyWithContent}.
     */
    CircuitBreakerClientBuilder(
            CircuitBreakerStrategyWithContent<HttpResponse> strategyWithContent) {
        super(strategyWithContent);
        needsContentInStrategy = true;
    }

    /**
     * Returns a newly-created {@link CircuitBreakerClient} based on the properties of this builder.
     */
    public CircuitBreakerClient build(HttpClient delegate) {
        if (needsContentInStrategy) {
            return new CircuitBreakerClient(delegate, mapping(), strategyWithContent());
        }

        return new CircuitBreakerClient(delegate, mapping(), strategy());
    }

    /**
     * Returns a newly-created decorator that decorates an {@link HttpClient} with a new
     * {@link CircuitBreakerClient} based on the properties of this builder.
     */
    public Function<? super HttpClient, CircuitBreakerClient> newDecorator() {
        return this::build;
    }

    // Methods that were overridden to change the return type.

    @Override
    public CircuitBreakerClientBuilder circuitBreakerMapping(CircuitBreakerMapping mapping) {
        return mapping(mapping);
    }

    @Override
    public CircuitBreakerClientBuilder mapping(CircuitBreakerMapping mapping) {
        return (CircuitBreakerClientBuilder) super.mapping(mapping);
    }
}
