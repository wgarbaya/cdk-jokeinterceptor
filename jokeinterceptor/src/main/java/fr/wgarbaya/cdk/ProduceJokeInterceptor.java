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
 
import io.conduktor.gateway.interceptor.Interceptor;
import io.conduktor.gateway.interceptor.InterceptorContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.requests.ProduceRequest;
import org.json.JSONObject;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

@Slf4j
public class ProduceJokeInterceptor implements Interceptor<ProduceRequest> {
    
@Override
public CompletionStage<ProduceRequest> intercept(ProduceRequest input, InterceptorContext interceptorContext) {
    log.warn("Joke to be added") ;
 
    
    HttpRequest request ;
    try {
        request = HttpRequest.newBuilder()
        .uri(new URI("https://api.chucknorris.io/jokes/random"))
        .GET()
        .build();
    } catch (URISyntaxException e) {
        log.error("Invalid URL will process without changing request", e);
        return CompletableFuture.completedFuture(input);
    }

    CompletableFuture<HttpResponse<String>> response = HttpClient.newBuilder()
    .build()
    .sendAsync(request, HttpResponse.BodyHandlers.ofString());

    //Same joke will be added to all partitions / records
    var chain =  response.thenApply((a) -> {
                JSONObject jsonObject = new JSONObject(a.body());
                input.data().topicData().forEach(topicProduceData -> {
                // for each partition
                topicProduceData.partitionData().forEach(partitionProduceData -> {
                    partitionProduceData.setRecords(RecordUtils.addHeaderToRecords(partitionProduceData.records(),"addedJoke", jsonObject.getString("value")));
                });
            });
        return input;
    });
    return chain;
}

}