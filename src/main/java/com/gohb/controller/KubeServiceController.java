package com.gohb.controller;

import com.gohb.bo.KubeServiceBO;
import com.gohb.constant.STATUS_CODE;
import com.gohb.dto.Result;
import com.gohb.dto.ResultUtils;
import com.gohb.manage.KubeServiceManage;
import io.kubernetes.client.custom.IntOrString;
import io.kubernetes.client.openapi.models.V1Namespace;
import io.kubernetes.client.openapi.models.V1Service;
import io.kubernetes.client.openapi.models.V1Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * v1Service json转换会失败，需要进行封装
 */
@RestController
@RequestMapping("/service")
public class KubeServiceController {

    @Value("${kubernetes.namespace.default}")
    public String defaultNamespace;


    @Autowired
    private KubeServiceManage kubeServiceManage;

    @PostMapping("")
    public Result<KubeServiceBO> createService(@RequestParam("serviceName") String serviceName, @RequestParam("namespace") String namespace,
                                              @RequestParam("type") String type, @RequestParam("port") Integer port,
                                              @RequestParam("nodePort") Integer nodePort, @RequestParam("targetPort") IntOrString targetPort,
                                              @RequestParam("protocol") String protocol) {
        if (!kubeServiceManage.isExistService(serviceName, namespace)) {
            return ResultUtils.getFailedResult(STATUS_CODE.isExist,
                    String.format("ServiceName : {} has already existed in namespace : {}",
                            serviceName, namespace));
        }
        KubeServiceBO kubeServiceBO = kubeServiceManage.createService(serviceName, namespace, type, port, nodePort, targetPort, protocol);
        return ResultUtils.getSuccessResult(kubeServiceBO);
    }

    @DeleteMapping("")
    public Result<V1Status> deleteService(@RequestParam("serviceName")String serviceName, @RequestParam("namespace") String namespace) {
        if (!kubeServiceManage.isExistService(serviceName, namespace)) {
            return ResultUtils.getFailedResult(STATUS_CODE.isNotExist,
                    String.format("ServiceName : {} is not existed in namespace : {}",
                            serviceName, namespace));
        }
        V1Status v1Status = kubeServiceManage.deleteService(serviceName, namespace);
        return ResultUtils.getSuccessResult(v1Status);
    }

    @GetMapping("")
    public Result<List<KubeServiceBO>> listService(String namespace) {
        if (namespace == null || namespace.equals("")) {
            namespace = defaultNamespace;
        }
        List<KubeServiceBO> kubeServiceBOS = kubeServiceManage.listService(namespace);
        return ResultUtils.getSuccessResult(kubeServiceBOS);
    }

    @GetMapping("exist")
    public Result<Boolean> isExistService(@RequestParam("serviceName")String serviceName,@RequestParam("namespace") String namespace) {
        Boolean exist = kubeServiceManage.isExistService(serviceName, namespace);
        return ResultUtils.getSuccessResult(exist);
    }

    @GetMapping("detail")
    public Result<KubeServiceBO> serviceDetail(String serviceName,String namespace) {
        KubeServiceBO kubeServiceBO = kubeServiceManage.serviceDetail(serviceName, namespace);
        return ResultUtils.getSuccessResult(kubeServiceBO);
    }

}