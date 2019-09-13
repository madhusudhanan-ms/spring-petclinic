/*
 * Copyright 2012-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
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
package org.springframework.samples.petclinic.vet;

import co.elastic.apm.api.ElasticApm;
import co.elastic.apm.api.Span;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

/**
 * @author Juergen Hoeller
 * @author Mark Fisher
 * @author Ken Krebs
 * @author Arjen Poutsma
 */
@Controller
@Component
@PropertySource("classpath:application.properties")
class VetController {

    private final VetRepository vets;

    public VetController(VetRepository clinicService) {
        this.vets = clinicService;
    }

    @Value("${tag_appName}")
    private String tagAppName;

    @Value("${tag_Name}")
    private String tagName;

    @Value("${isConfigEnable}")
    private Boolean isConfigEnable;

    @GetMapping("/vets.html")
    public String showVetList(Map<String, Object> model) {
        // line to support stacktrace
        if(isConfigEnable){
            Span span = ElasticApm.currentSpan();
            span.addLabel("_tag_appName", tagAppName);
            span.addLabel("_tag_Name", tagName);
            span.addLabel("_plugin", "stacktrace");
        }
        // Here we are returning an object of type 'Vets' rather than a collection of Vet
        // objects so it is simpler for Object-Xml mapping
        Vets vets = new Vets();
        vets.getVetList().addAll(this.vets.findAll());
        model.put("vets", vets);
        return "vets/vetList";
    }

    @GetMapping({ "/vets" })
    public @ResponseBody Vets showResourcesVetList() {
        // line to support stacktrace
        if(isConfigEnable){
            Span span = ElasticApm.currentSpan();
            span.addLabel("_tag_appName", tagAppName);
            span.addLabel("_tag_Name", tagName);
            span.addLabel("_plugin", "stacktrace");
        }
        // Here we are returning an object of type 'Vets' rather than a collection of Vet
        // objects so it is simpler for JSon/Object mapping
        Vets vets = new Vets();
        vets.getVetList().addAll(this.vets.findAll());
        return vets;
    }

}
