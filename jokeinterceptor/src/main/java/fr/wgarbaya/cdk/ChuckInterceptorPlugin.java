/*
 * Copyright 2023 Conduktor, Inc
 *
 * Licensed under the Conduktor Community License (the "License"); you may not use
 * this file except in compliance with the License.  You may obtain a copy of the
 * License at
 *
 * https://www.conduktor.io/conduktor-community-license-agreement-v1.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OF ANY KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations under the License.
 */

package fr.wgarbaya.cdk;

import com.google.common.collect.Lists;
import io.conduktor.gateway.interceptor.Interceptor;
import io.conduktor.gateway.interceptor.InterceptorConfigurationException;
import io.conduktor.gateway.interceptor.InterceptorProvider;
import io.conduktor.gateway.interceptor.Plugin;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.requests.*;

import java.util.List;
import java.util.Map;

@Slf4j
public class ChuckInterceptorPlugin implements Plugin {

    @Override
    public List<InterceptorProvider<?>> getInterceptors(Map<String, Object> config) {
 
        return List.of(
                new InterceptorProvider<>(ProduceRequest.class, new ProduceJokeInterceptor())
        );
    }
}
