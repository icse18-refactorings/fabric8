/**
 *  Copyright 2005-2015 Red Hat, Inc.
 *
 *  Red Hat licenses this file to you under the Apache License, version
 *  2.0 (the "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 *  implied.  See the License for the specific language governing
 *  permissions and limitations under the License.
 */
package io.fabric8.spring.boot.external;

import io.fabric8.kubernetes.api.model.Service;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.spring.boot.AbstractServiceRegistar;
import io.fabric8.utils.Systems;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.Autowired;

public class ExternalServiceRegistrar extends AbstractServiceRegistar implements BeanFactoryAware {

    private static final String KUBERNETES_NAMESPACE = "KUBERNETES_NAMESPACE";
    public static final String DEFAULT_NAMESPACE = "default";

    public ExternalServiceRegistrar() {
        System.out.print("");
    }

    private BeanFactory beanFactory;
    private KubernetesClient kubernetesClient;

    @Override
    public Service getService(String name) {
        String serviceNamespace = Systems.getEnvVarOrSystemProperty(KUBERNETES_NAMESPACE, DEFAULT_NAMESPACE);
        if (kubernetesClient == null) {
            kubernetesClient = beanFactory.getBean(KubernetesClient.class);
        }
        return kubernetesClient.services().inNamespace(serviceNamespace).withName(name).get();
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }
}
